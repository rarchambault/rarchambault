package test.java.storyTests.steps;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.After;
import io.cucumber.java.AfterAll;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static java.lang.Thread.sleep;
import static org.junit.jupiter.api.Assertions.*;

public class TodosStepDefinitions {

    private static Process process;
    private Response response;
    private RequestSpecification request;

    @Given("the service is running")
    public void serviceRunning() {
        RestAssured.baseURI = "http://localhost:4567";
        if (isServiceRunning()) {
            shutdownServer();
            startService();
        } else {
            startService();
        }
    }

    /**
     * Test that the API is up and ready for testing
     * @return true if the service API is running, false otherwise.
     */
    private boolean isServiceRunning() {
        int serverResponse = 404; // This would indicate that the API is not running
        try{
            URL serverUrl = new URL("http://localhost:4567");
            HttpURLConnection serverConnection = (HttpURLConnection) serverUrl.openConnection();

            serverConnection.setRequestMethod("GET");
            serverResponse = serverConnection.getResponseCode();

            return serverResponse == 200;
        } catch(Exception ignored) {
            return false;
        }
    }

    private void startService() {
        try {
            process = Runtime.getRuntime().exec("java -jar runTodoManagerRestAPI-1.5.5.jar");
            sleep(500); // Allow some time to ensure the API is running
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @After
    public void deleteAllTodos() {
        // Get all existing todos
        Response response = given()
                .when()
                .get("/todos");

        List<Map<String, Object>> todos = response.jsonPath().getList("todos");

        if (todos == null || todos.isEmpty()) {
            return;
        }

        for (Map<String, Object> todo : todos) {
            if (!todo.containsKey("id") || todo.get("id") == null) {
                continue;
            }

            int todoID = Integer.parseInt(todo.get("id").toString());

            // Delete all existing todos
            Response deleteResponse = given()
                    .pathParam("id", todoID)
                    .when()
                    .delete("/todos/{id}");

            assertEquals(200, deleteResponse.getStatusCode());
        }
    }

    @AfterAll
    public static void shutdownServer() {
        try {
            given().when().get("/shutdown");
            // Stop the process that was running the API
            process.destroy();
            sleep(500);
        }
        catch (Exception ignored) {}
    }

    /* CreateTodo.feature */
    // Normal flow
    @When("a user sends a POST request to {string} containing a title {string}, a doneStatus {string} and a description {string}")
    public void createTodoWithAllFields(String endpoint, String title, String doneStatus, String description) {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("title", title);
        requestBody.put("doneStatus", Boolean.parseBoolean(doneStatus));
        requestBody.put("description", description);

        response = given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/" + endpoint);
    }

    @Then("the system shall return an HTTP response with status code {int}")
    public void verifyResponseStatus(int expectedStatusCode) {
        response.then().statusCode(expectedStatusCode);
    }

    @Then("the response shall display a todo task with title {string}, doneStatus {string} and description {string}")
    public void verifyResponseBody(String title, String doneStatus, String description) {
        String actualTitle = response.jsonPath().getString("title");
        Boolean actualDoneStatus = response.jsonPath().getBoolean("doneStatus");
        String actualDescription = response.jsonPath().getString("description");
        assertEquals(title, actualTitle);
        assertEquals(Boolean.parseBoolean(doneStatus), actualDoneStatus);
        assertEquals(description, actualDescription);
    }

    @Then("a todo with title {string}, doneStatus {string} and description {string} shall exist in the system")
    public void verifyTodoExists(String title, String doneStatus, String description) {
        response = RestAssured.given().get("/todos");

        List<Map<String, Object>> todos = response.jsonPath().getList("todos");

        assertNotNull(todos);  // Ensure todos list is not null
        assertFalse(todos.isEmpty()); // Ensure there are todos in the system

        // Convert doneStatus from String to Boolean for correct comparison
        boolean expectedDoneStatus = Boolean.parseBoolean(doneStatus);

        // Check if the expected todo exists
        boolean todoExists = todos.stream().anyMatch(todo ->
                title.equals(todo.get("title")) &&
                        expectedDoneStatus == Boolean.parseBoolean(todo.get("doneStatus").toString()) &&
                        description.equals(todo.get("description"))
        );

        assertTrue(todoExists, "Expected todo was not found in the system.");
    }

    // Alternate Flow
    @When("a user sends a POST request to {string} using title {string}")
    public void createTodoWithTitle(String endpoint, String title) {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("title", title);

        response = given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/" + endpoint);
    }

    // Error Flow
    @Then("the response shall contain the error message {string}")
    public void verifyErrorMessage(String expectedErrorMessage) {
        String actualErrorMessage = response.jsonPath().getString("errorMessages");
        assertEquals(expectedErrorMessage, actualErrorMessage);
    }

    /*
    GetTodosList.feature
     */
    // Normal Flow
    @Given("the following todos exist in the system:")
    public void ensureTodosExist(DataTable todosTable) {
        List<Map<String, String>> todos = todosTable.asMaps(String.class, String.class);

        for (Map<String, String> expectedTodo : todos) {
            String id = expectedTodo.get("id");
            String title = expectedTodo.get("title");
            boolean doneStatus = Boolean.parseBoolean(expectedTodo.get("doneStatus"));
            String description = expectedTodo.get("description");

            // Check if the todo already exists
            Response getResponse = given().when().get("/todos/" + id);

            Map<String, Object> updateBody = new HashMap<>();
            updateBody.put("title", title);
            updateBody.put("doneStatus", doneStatus);
            updateBody.put("description", description);

            if (getResponse.statusCode() == 200) {
                // Update todo to match expected values
                given()
                        .contentType(ContentType.JSON)
                        .body(updateBody)
                        .when()
                        .put("/todos/" + id);
            }
            else {
                // Create new todo
                given()
                        .contentType(ContentType.JSON)
                        .body(updateBody)
                        .when()
                        .post("/todos");
            }
        }
    }

    @When("a user sends a GET request to {string}")
    public void sendGetRequest(String endpoint) {
        response = given().when().get("/" + endpoint);
    }

    @Then("the response shall contain a list of todos")
    public void verifyTodosListExists() {
        List<Map<String, Object>> todos = response.jsonPath().getList("todos");
        assertNotNull(todos, "Todo list should not be null");
        assertFalse(todos.isEmpty(), "Todo list should not be empty");
    }

    @Then("the list shall include todos with the following information:")
    public void verifyTodosList(DataTable expectedTodosTable) {
        List<Map<String, String>> expectedTodos = expectedTodosTable.asMaps(String.class, String.class);
        List<Map<String, Object>> actualTodos = response.jsonPath().getList("todos");

        for (Map<String, String> expectedTodo : expectedTodos) {
            boolean exists = actualTodos.stream().anyMatch(todo ->
                    todo.get("title").equals(expectedTodo.get("title")) &&
                            Boolean.parseBoolean(todo.get("doneStatus").toString()) == Boolean.parseBoolean(expectedTodo.get("doneStatus")) &&
                            todo.get("description").equals(expectedTodo.get("description"))
            );
            assertTrue(exists, "Expected todo was not found: " + expectedTodo);
        }
    }

    // Alternate Flow
    @When("a user sends a GET request to {string} with filter {string}")
    public void sendGetRequestWithFilter(String endpoint, String filter) {
        response = given().when().get("/" + endpoint + filter);
    }

    // Error Flow
    // Reusing functions from CreateTodo.feature

    /*
    GetSpecificTodo.feature
     */
    // Normal Flow
    @When("a user sends a GET request to todos\\/{int}")
    public void getTodoById(int id) {
        response = given().when().get("/todos/" + id);
    }

    // Alternate Flow
    @When("a user sends a GET request to \"todos\" with parameter {string}")
    public void getTodoByTitle(String title) {
        response = given()
                .queryParam("title", title)
                .when()
                .get("/todos");
    }

    @Then("the response shall contain a todo task with title {string}, doneStatus {string} and description {string}")
    public void verifyTodosListOneElement(String title, String doneStatus, String description) {
        List<Map<String, Object>> actualTodos = response.jsonPath().getList("todos");

        assertTrue(actualTodos.size() == 1);

        boolean exists = actualTodos.stream().anyMatch(todo ->
                todo.get("title").equals(title) &&
                        Boolean.parseBoolean(todo.get("doneStatus").toString()) == Boolean.parseBoolean(doneStatus) &&
                        todo.get("description").equals(description)
        );
        assertTrue(exists);
    }

    // Error Flow
    // Reusing functions

    /*
    UpdateTodo.feature
     */
    // Normal Flow
    @When("a user sends a PUT request to todos\\/{int} containing a title {string}, a doneStatus {string} and a description {string}")
    public void updateTodoWithAllFields(int id, String title, String doneStatus, String description) {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("title", title);
        requestBody.put("doneStatus", Boolean.parseBoolean(doneStatus));
        requestBody.put("description", description);

        response = given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .put("/todos/" + id);
    }

    @Then("a todo with id {int}, title {string}, doneStatus {string} and description {string} shall exist in the system")
    public void verifyTodoExists(int id, String title, String doneStatus, String description) {
        response = RestAssured.given().get("/todos");

        List<Map<String, Object>> todos = response.jsonPath().getList("todos");

        assertNotNull(todos);  // Ensure todos list is not null
        assertFalse(todos.isEmpty()); // Ensure there are todos in the system

        // Convert doneStatus from String to Boolean for correct comparison
        boolean expectedDoneStatus = Boolean.parseBoolean(doneStatus);

        // Check if the expected todo exists
        boolean todoExists = todos.stream().anyMatch(todo ->
                id == Integer.parseInt(todo.get("id").toString()) &&
                        title.equals(todo.get("title")) &&
                        expectedDoneStatus == Boolean.parseBoolean(todo.get("doneStatus").toString()) &&
                        description.equals(todo.get("description"))
        );

        assertTrue(todoExists, "Expected todo was not found in the system.");
    }

    // Alternate Flow
    @When("a user sends a POST request to todos\\/{int} containing a title {string}, a doneStatus {string} and a description {string}")
    public void postTodoWithAllFields(int id, String title, String doneStatus, String description) {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("title", title);
        requestBody.put("doneStatus", Boolean.parseBoolean(doneStatus));
        requestBody.put("description", description);

        response = given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/todos/" + id);
    }

    // Error Flow
    @When("a user sends a PUT request to todos\\/{int} containing a doneStatus {string} and a description {string}")
    public void updateTodoWithAllFields(int id, String doneStatus, String description) {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("doneStatus", Boolean.parseBoolean(doneStatus));
        requestBody.put("description", description);

        response = given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .put("/todos/" + id);
    }

    /*
    DeleteTodo.feature
     */
    // Normal Flow
    @When("a user sends a DELETE request to todos\\/{int}")
    public void deleteTodoById(int id) {
        response = given()
                .when()
                .delete("/todos/" + id);
    }

    // Verify that the todo no longer exists
    @Then("a todo with {int} shall not exist in the system")
    public void verifyTodoDoesNotExist(int id) {
        response = given()
                .when()
                .get("/todos/" + id);

        assertEquals(404, response.getStatusCode(), "Todo with ID " + id + " still exists.");
    }

    // Alternate Flow
    // Reusing functions

    // Error Flow
    // Reusing functions
}

