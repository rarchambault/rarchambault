package test.java.unitTests;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.junit.jupiter.api.*;

import java.io.FileWriter;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static io.restassured.RestAssured.given;
import static java.lang.Thread.sleep;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

// Run unit tests in a random order
@TestMethodOrder(MethodOrderer.Random.class)
public class TodoUnitTests {
    private int todoID;
    private JSONObject todo;
    private int todo2ID;
    private JSONObject todo2;
    private static Process process;
    private JSONObject project;
    private int projectID;
    private JSONObject project2;
    private int project2ID;
    private JSONObject category;
    private int categoryID;
    private JSONObject category2;
    private int category2ID;

    @BeforeAll
    public static void setup() throws Exception {
        // Create a CSV file in which to store performance results
        CsvWriter.writeHeader("performance_results.csv");

        // Run the API under test
        try {
            process = Runtime.getRuntime().exec("java -jar runTodoManagerRestAPI-1.5.5.jar");
            sleep(500); // Allow some time to ensure the API is running
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Set the base URI for all HTTP requests
        RestAssured.baseURI = "http://localhost:4567";

        // Test that the API is up and ready for testing
        int serverResponse = 404; // This would indicate that the API is not running
        try{
            URL serverUrl = new URL("http://localhost:4567");
            HttpURLConnection serverConnection = (HttpURLConnection) serverUrl.openConnection();

            serverConnection.setRequestMethod("GET");
            serverResponse = serverConnection.getResponseCode();

            assertEquals(200, serverResponse);

        } catch(Exception e) {
            e.printStackTrace();
            assertEquals(200, serverResponse);
        }
    }

    // Create data before each test runs
    @BeforeEach
    public void createData() {
        // Create todo
        todo = RandomGenerator.generateTodo();
        Response response = given()
                .body(todo.toString())
                .when()
                .post("/todos");

        // Check that all input attributes are the same in the posted todo
        assertEquals(201, response.getStatusCode());
        assertEquals(todo.getString("title"), response.jsonPath().getString("title"));
        assertEquals(todo.getString("description"), response.jsonPath().getString("description"));
        assertEquals(String.valueOf(todo.getBoolean("doneStatus")), response.jsonPath().getString("doneStatus"));

        todoID = response.jsonPath().getInt("id");

        // Create todo 2
        todo2 = RandomGenerator.generateTodo();
        Response response2 = given()
                .body(todo2.toString())
                .when()
                .post("/todos");

        todo2ID = response2.jsonPath().getInt("id");

        // Create project
        project = new JSONObject();
        project.put("title", "project");
        project.put("completed", false);
        project.put("active", false);
        project.put("description", "project description");

        Response projectPostResponse = given()
                .body(project.toString())
                .when()
                .post("/projects");

        assertEquals(201, projectPostResponse.getStatusCode());

        projectID = projectPostResponse.jsonPath().getInt("id");
        assertEquals(project.getString("title"), projectPostResponse.jsonPath().getString("title"));
        assertEquals(String.valueOf(project.getBoolean("completed")), projectPostResponse.jsonPath().getString("completed"));
        assertEquals(String.valueOf(project.getBoolean("active")), projectPostResponse.jsonPath().getString("active"));
        assertEquals(project.getString("description"), projectPostResponse.jsonPath().getString("description"));

        // Add tasksof relationship with the created todo
        JSONObject tasksofRequestBody = new JSONObject();
        tasksofRequestBody.put("id", String.valueOf(projectID));

        Response tasksofPostResponse = given()
                .pathParam("id", todoID)
                .body(tasksofRequestBody.toString())
                .when()
                .post("/todos/{id}/tasksof");

        assertEquals(201, tasksofPostResponse.getStatusCode());

        // Create second project
        project2 = new JSONObject();
        project2.put("title", "project2");
        project2.put("completed", false);
        project2.put("active", false);
        project2.put("description", "project description 2");

        Response projectPostResponse2 = given()
                .body(project2.toString())
                .when()
                .post("/projects");

        assertEquals(201, projectPostResponse2.getStatusCode());
        project2ID = projectPostResponse2.jsonPath().getInt("id");

        // Create category
        category = new JSONObject();
        category.put("title", "category");
        category.put("description", "category description");

        Response categoryPostResponse = given()
                .body(category.toString())
                .when()
                .post("/categories");

        assertEquals(201, categoryPostResponse.getStatusCode());

        categoryID = categoryPostResponse.jsonPath().getInt("id");
        assertEquals(category.getString("title"), categoryPostResponse.jsonPath().getString("title"));
        assertEquals(category.getString("description"), categoryPostResponse.jsonPath().getString("description"));

        // Add category relationship with the created todo
        JSONObject categoriesRequestBody = new JSONObject();
        categoriesRequestBody.put("id", String.valueOf(categoryID));

        Response categoriesPostResponse = given()
                .pathParam("id", todoID)
                .body(categoriesRequestBody.toString())
                .when()
                .post("/todos/{id}/categories");

        assertEquals(201, categoriesPostResponse.getStatusCode());

        // Create second category
        category2 = new JSONObject();
        category2.put("title", "category2");
        category2.put("description", "category description 2");

        Response categoryPostResponse2 = given()
                .body(category2.toString())
                .when()
                .post("/categories");

        assertEquals(201, categoryPostResponse2.getStatusCode());
        category2ID = categoryPostResponse2.jsonPath().getInt("id");
    }

    // Delete the created data after each test terminates
    @AfterEach
    public void deleteData() {
        // Delete todo
        Response deleteTodoResponse = given()
                .pathParam("id", todoID)
                .when()
                .delete("/todos/{id}");

        assertEquals(200, deleteTodoResponse.getStatusCode());

        // Try to get the deleted todo to make sure it is deleted
        Response getTodoResponse = given()
                .pathParam("id", todoID)
                .when()
                .get("/todos/{id}");

        assertEquals(404, getTodoResponse.getStatusCode());

        // Delete todo 2
        Response deleteTodoResponse2 = given()
                .pathParam("id", todo2ID)
                .when()
                .delete("/todos/{id}");

        assertEquals(200, deleteTodoResponse2.getStatusCode());

        // Delete project
        Response deleteProjectResponse = given()
                .pathParam("id", projectID)
                .when()
                .delete("/projects/{id}");

        assertEquals(200, deleteProjectResponse.getStatusCode());

        // Try to get the deleted project to make sure it is deleted
        Response getProjectResponse = given()
                .pathParam("id", projectID)
                .when()
                .get("/projects/{id}");

        assertEquals(404, getProjectResponse.getStatusCode());

        // Delete project 2
        Response deleteProjectResponse2 = given()
                .pathParam("id", project2ID)
                .when()
                .delete("/projects/{id}");

        assertEquals(200, deleteProjectResponse2.getStatusCode());

        // Delete category
        Response deleteCategoryResponse = given()
                .pathParam("id", categoryID)
                .when()
                .delete("/categories/{id}");

        assertEquals(200, deleteCategoryResponse.getStatusCode());

        // Try to get the deleted category to make sure it is deleted
        Response getCategoryResponse = given()
                .pathParam("id", categoryID)
                .when()
                .get("/categories/{id}");

        assertEquals(404, getCategoryResponse.getStatusCode());

        // Delete category 2
        Response deleteCategoryResponse2 = given()
                .pathParam("id", category2ID)
                .when()
                .delete("/categories/{id}");

        assertEquals(200, deleteCategoryResponse2.getStatusCode());
    }

    // Shutdown the API after all tests run
    @AfterAll
    public static void shutdownServer() {
        try {
            // Stop the process that was running the API
            process.destroy();
            sleep(500);
        }
        catch (Exception ignored) {}
    }

    // Test that we can get the documentation page as specified
    @Test
    public void testGetDocs(){
        Response response = given()
                .when()
                .get("/docs");

        String title = response.htmlPath().getString("html.head.title");
        assertEquals(200, response.getStatusCode());
        assertTrue(title.contains("API Documentation"));
    }

    // Test that we can get all current todos in JSON format
    @Test
    public void testGetTodosJson() {
        Response response = given()
                .when()
                .get("/todos");

        List<Map<String, Object>> todos = response.jsonPath().getList("todos");

        // Check that the todo we just created is contained in the received list
        boolean found = false;
        for (Map<String, Object> newTodo : todos) {
            if (this.todo.getString("title").equals(newTodo.get("title"))) {
                found = true;
                break;
            }
        }
        assertEquals(200, response.getStatusCode());
        assertFalse(todos.isEmpty());
        assertTrue(found);
    }

    // Test that we can get all current todos in XML format
    @Test
    public void testGetTodosXml(){
        Response response = given()
                .header("Accept", ContentType.XML)
                .contentType(ContentType.XML)
                .when()
                .get("/todos");

        // Check that the list received contains the todo created before the test
        List<String> titles = response.xmlPath().getList("todos.todo.title");
        boolean found = titles.contains(todo.getString("title"));

        assertEquals(200, response.getStatusCode());
        assertNotNull(titles);
        assertFalse(titles.isEmpty());
        assertTrue(found);
    }

    @Test
    public void testHeadTodos() {
        Response response = given()
                .when()
                .head("/todos");

        assertEquals(200, response.getStatusCode());
        assertNotEquals(0, response.getHeaders().size());
        assertEquals("application/json", response.getHeaders().get("Content-Type").getValue());
        assertEquals("chunked", response.getHeaders().get("Transfer-Encoding").getValue());
    }

    @Test
    public void testOptionsTodos() {
        Response response = given()
                .when()
                .options("/todos");

        assertEquals(200, response.getStatusCode());
        assertEquals("OPTIONS, GET, HEAD, POST", response.getHeaders().get("Allow").getValue());
    }

    @Test
    public void testPostEmptyTodo() {
        JSONObject object = new JSONObject();
        Response response = given()
                .body(object.toString())
                .when()
                .post("/todos");

        assertEquals(400, response.getStatusCode());
        assertEquals("[title : field is mandatory]", response.jsonPath().getString("errorMessages"));
    }

    @Test
    public void testPostTodoNoTitle() {
        JSONObject object = new JSONObject();
        object.put("doneStatus", false);
        object.put("description", "test description");
        Response response = given()
                .body(object.toString())
                .when()
                .post("/todos");

        assertEquals(400, response.getStatusCode());
        assertEquals("[title : field is mandatory]", response.jsonPath().getString("errorMessages"));
    }

    @Test
    public void testPostTodoWrongType() {
        JSONObject object = new JSONObject();
        object.put("doneStatus", "false"); // Should have type boolean and not String
        Response response = given()
                .body(object.toString())
                .when()
                .post("/todos");

        assertEquals(400, response.getStatusCode());
        assertEquals("[Failed Validation: doneStatus should be BOOLEAN]", response.jsonPath().getString("errorMessages"));
    }

    @Test
    public void testPostTodoJson() {
        JSONObject object = new JSONObject();
        object.put("title", "test title");
        object.put("doneStatus", false);
        object.put("description", "test description");
        Response response = given()
                .body(object.toString())
                .when()
                .post("/todos");

        assertEquals(201, response.getStatusCode());
        assertEquals("test title", response.jsonPath().getString("title"));
        assertEquals("false", response.jsonPath().getString("doneStatus"));
        assertEquals("test description", response.jsonPath().getString("description"));

        // delete the newly created todo
        int testId = response.jsonPath().getInt("id");
        Response responsePost = given()
                .pathParam("id", testId)
                .when()
                .delete("/todos/{id}");

        assertEquals(200, responsePost.getStatusCode());
    }

    @Test
    public void testPostTodoXml() {
        String xmlBody = "<todo>" +
                "<title>test title</title>" +
                "<doneStatus>false</doneStatus>" +
                "<description>test description</description>" +
                "</todo>";

        Response response = given()
                .header("Accept", ContentType.XML)
                .contentType(ContentType.XML)
                .body(xmlBody)
                .when()
                .post("/todos");

        assertEquals(201, response.getStatusCode());
        assertEquals("test title", response.xmlPath().getString("todo.title"));
        assertEquals("false", response.xmlPath().getString("todo.doneStatus"));
        assertEquals("test description", response.xmlPath().getString("todo.description"));

        // Delete the newly created todo
        int testId = response.xmlPath().getInt("todo.id");
        Response responsePost = given()
                .pathParam("id", testId)
                .when()
                .delete("/todos/{id}");

        assertEquals(200, responsePost.getStatusCode());
    }

    @Test
    public void testPostTodoMalformedPayloadJson() {
        JSONObject object = new JSONObject();
        object.put("title", "test title");
        object.put("done", false);  // should be doneStatus, not done
        object.put("description", "test description");
        Response response = given()
                .body(object.toString())
                .when()
                .post("/todos");

        assertEquals(400, response.getStatusCode());
        assertEquals("[Could not find field: done]", response.jsonPath().getString("errorMessages"));
    }

    @Test
    public void testPostTodoMalformedPayloadXml() {
        String xmlBody = "<todo>" +
                "<title>test title</title>" +
                "<done>false</done>" +  // should be doneStatus, not done
                "<description>test description</description>" +
                "</todo>";

        Response response = given()
                .header("Accept", ContentType.XML)
                .contentType(ContentType.XML)
                .body(xmlBody)
                .when()
                .post("/todos");

        assertEquals(400, response.getStatusCode());
        assertEquals("Could not find field: done", response.xmlPath().getString("errorMessages"));
    }

    @Test
    public void testPostMultipleTodos() {
        int[] objectCounts = {1, 10, 100, 1000, 1500};
        String csvFile = "performance_results.csv";

        for (int numObjects : objectCounts) {
            PerformanceMonitor.PerformanceResult result = PerformanceMonitor.monitorDuring(() -> {
                for (int i = 0; i < numObjects; i++) {
                    JSONObject todo = RandomGenerator.generateTodo();
                    Response response = given()
                            .body(todo.toString())
                            .when()
                            .post("/todos");
                    assertEquals(201, response.getStatusCode());
                }
            });

            CsvWriter.writePerformanceResults(
                    csvFile,
                    "postMultipleTodos",
                    numObjects,
                    result.durationMs,
                    result.averageCpuPercent,
                    result.getJvmMemoryDelta(),           // JVM heap memory delta
                    result.peakJvmUsedMemory,             // Peak heap memory
                    result.getSystemMemoryDelta()         // Physical memory delta
            );
        }
    }

    @Test
    public void testGetTodoWithId() {
        Response response = given()
                .pathParam("id", todoID)
                .when()
                .get("/todos/{id}");

        assertEquals(200, response.getStatusCode());
        assertEquals(todo.getString("title"), response.jsonPath().getString("todos[0].title"));
        assertEquals(todo.getString("description"), response.jsonPath().getString("todos[0].description"));
        assertEquals(String.valueOf(todo.getBoolean("doneStatus")), response.jsonPath().getString("todos[0].doneStatus"));
    }

    @Test
    public void testGetTodoWithInvalidId() {
        int invalidId = -1;
        Response response = given()
                .pathParam("id", invalidId)
                .when()
                .get("/todos/{id}");

        assertEquals(404, response.getStatusCode());
        String expectedMessage = "[Could not find an instance with todos/" + invalidId + "]";
        assertEquals(expectedMessage, response.jsonPath().getString("errorMessages"));

    }

    @Test
    public void testHeadTodoWithId() {
        Response response = given()
                .pathParam("id", todoID)
                .when()
                .head("/todos/{id}");

        assertEquals(200, response.getStatusCode());
        assertNotEquals(0, response.getHeaders().size());
        assertEquals("application/json", response.getHeaders().get("Content-Type").getValue());
        assertEquals("chunked", response.getHeaders().get("Transfer-Encoding").getValue());

    }

    @Test
    public void testOptionsTodoWithId() {
        Response response = given()
                .pathParam("id", todoID)
                .when()
                .options("/todos/{id}");

        assertEquals(200, response.getStatusCode());
        assertEquals("OPTIONS, GET, HEAD, POST, PUT, DELETE", response.getHeaders().get("Allow").getValue());

    }

    @Test
    public void testUpdateTodoPost() {
        JSONObject updatedTodo = this.todo;
        updatedTodo.remove("title");
        updatedTodo.put("title", "updated title");

        Response responsePost = given()
                .body(updatedTodo.toString())
                .when()
                .post("/todos/" + todoID);

        assertEquals(200, responsePost.getStatusCode());
        assertEquals(updatedTodo.getString("title"), responsePost.jsonPath().getString("title"));
    }

    @Test
    public void testUpdateMultipleTodosPost() {
        int[] objectCounts = {1, 10, 100, 1000, 1500};
        String csvFile = "performance_results.csv";

        for (int numObjects : objectCounts) {
            PerformanceMonitor.PerformanceResult result = PerformanceMonitor.monitorDuring(() -> {
                for (int i = 0; i < numObjects; i++) {
                    JSONObject updatedTodo = RandomGenerator.generateTodo();
                    Response responsePost = given()
                            .body(updatedTodo.toString())
                            .when()
                            .post("/todos/" + todoID);
                    assertEquals(200, responsePost.getStatusCode());
                }
            });

            CsvWriter.writePerformanceResults(
                    csvFile,
                    "updateTodoPost",
                    numObjects,
                    result.durationMs,
                    result.averageCpuPercent,
                    result.getJvmMemoryDelta(),
                    result.peakJvmUsedMemory,
                    result.getSystemMemoryDelta()
            );
        }
    }

    @Test
    public void testUpdateTodoPostNoTitle() {
        JSONObject updatedObject = new JSONObject();
        updatedObject.put("doneStatus", true);

        Response responsePost = given()
                .body(updatedObject.toString())
                .when()
                .post("/todos/" + todoID);

        assertEquals(200, responsePost.getStatusCode());
        assertEquals("true", responsePost.jsonPath().getString("doneStatus"));
        assertEquals(todo.getString("title"), responsePost.jsonPath().getString("title"));
        assertEquals(todo.getString("description"), responsePost.jsonPath().getString("description"));
    }

    @Test
    public void testUpdateTodoPostInvalidId() {
        int invalidId = -1;
        JSONObject updatedObject = new JSONObject();
        updatedObject.put("title", "updated test title - post");

        Response responsePost = given()
                .body(updatedObject.toString())
                .when()
                .post("/todos/" + invalidId);

        assertEquals(404, responsePost.getStatusCode());
        String expectedMessage = "[No such todo entity instance with GUID or ID " + invalidId + " found]";
        assertEquals(expectedMessage, responsePost.jsonPath().getString("errorMessages"));
    }

    @Test
    public void testUpdateTodoPostWrongType() {
        JSONObject updatedObject = new JSONObject();
        updatedObject.put("doneStatus", "true"); // Should be boolean and not String

        Response response = given()
                .body(updatedObject.toString())
                .when()
                .post("/todos/" + todoID);

        assertEquals(400, response.getStatusCode());
        assertEquals("[Failed Validation: doneStatus should be BOOLEAN]", response.jsonPath().getString("errorMessages"));
    }

    @Test
    public void testUpdateTodoPut() {
        JSONObject updatedTodo = this.todo;
        updatedTodo.remove("title");
        updatedTodo.put("title", "updated title");

        Response responsePost = given()
                .body(updatedTodo.toString())
                .when()
                .put("/todos/" + todoID);

        assertEquals(200, responsePost.getStatusCode());
        assertEquals(updatedTodo.getString("title"), responsePost.jsonPath().getString("title"));
    }

    @Test
    public void testUpdateMultipleTodosPut() {
        int[] objectCounts = {1, 10, 100, 1000, 1500};
        String csvFile = "performance_results.csv";

        for (int numObjects : objectCounts) {
            PerformanceMonitor.PerformanceResult result = PerformanceMonitor.monitorDuring(() -> {
                for (int i = 0; i < numObjects; i++) {
                    JSONObject updatedTodo = RandomGenerator.generateTodo();

                    // Assuming you have a valid ID to update
                    Response responsePut = given()
                            .body(updatedTodo.toString())
                            .when()
                            .put("/todos/" + todoID);
                    assertEquals(200, responsePut.getStatusCode());
                }
            });

            CsvWriter.writePerformanceResults(
                    csvFile,
                    "updateTodoPut",
                    numObjects,
                    result.durationMs,
                    result.averageCpuPercent,
                    result.getJvmMemoryDelta(),
                    result.peakJvmUsedMemory,
                    result.getSystemMemoryDelta()
            );
        }
    }

    // Bug
    // Test shows expected behavior failing:
    // the todo should be updated (response code 200) and title other fields should remain unchanged
    @Test
    public void testUpdateTodoPutNoTitleFail() {
        JSONObject updatedObject = new JSONObject();

        Response responsePut = given()
                .body(updatedObject.toString())
                .when()
                .put("/todos/" + todoID);

        assertEquals(200, responsePut.getStatusCode());
        assertEquals(todo.getString("description"), responsePut.jsonPath().getString("description"));
        assertEquals(todo.getString("title"), responsePut.jsonPath().getString("title"));
        assertEquals(String.valueOf(todo.getBoolean("doneStatus")), responsePut.jsonPath().getString("doneStatus"));
    }

    // Bug
    // Test shows actual behavior passing: an error message is raised, stating that the title field is mandatory.
    @Test
    public void testUpdateTodoPutNoTitlePass() {
        JSONObject updatedObject = new JSONObject();

        Response responsePut = given()
                .body(updatedObject.toString())
                .when()
                .put("/todos/" + todoID);

        assertEquals(400, responsePut.getStatusCode());
        assertEquals("[title : field is mandatory]", responsePut.jsonPath().getString("errorMessages"));
    }

    @Test
    public void testUpdateTodoPutInvalidId() {
        int invalidId = -1;
        JSONObject updatedObject = new JSONObject();
        updatedObject.put("title", "updated test title - put");

        Response responsePut = given()
                .body(updatedObject.toString())
                .when()
                .put("/todos/" + invalidId);

        assertEquals(404, responsePut.getStatusCode());
        String expectedMessage = "[Invalid GUID for " + invalidId + " entity todo]";
        assertEquals(expectedMessage, responsePut.jsonPath().getString("errorMessages"));
    }

    @Test
    public void testDeleteTodoTwice() {
        JSONObject newTodo = RandomGenerator.generateTodo();
        Response response = given()
                .body(newTodo.toString())
                .when()
                .post("/todos");

        assertEquals(201, response.getStatusCode());
        assertEquals(newTodo.getString("title"), response.jsonPath().getString("title"));
        assertEquals(String.valueOf(newTodo.getBoolean("doneStatus")), response.jsonPath().getString("doneStatus"));
        assertEquals(newTodo.getString("description"), response.jsonPath().getString("description"));

        // Delete the new todo
        int id = response.jsonPath().getInt("id");
        Response responseDelete = given()
                .pathParam("id", id)
                .when()
                .delete("/todos/{id}");

        assertEquals(200, responseDelete.getStatusCode());

        // Delete the todo again
        Response responseDeleteAgain = given()
                .pathParam("id", id)
                .when()
                .delete("/todos/{id}");

        assertEquals(404, responseDeleteAgain.getStatusCode());
        String expectedMessage = "[Could not find any instances with todos/" + id + "]";
        assertEquals(expectedMessage, responseDeleteAgain.jsonPath().getString("errorMessages"));
    }

    @Test
    public void testDeleteTodoInvalidId() {
        int invalidId = -1;
        Response response = given()
                .pathParam("id", invalidId)
                .when()
                .delete("/todos/{id}");

        assertEquals(404, response.getStatusCode());
        String expectedMessage = "[Could not find any instances with todos/" + invalidId + "]";
        assertEquals(expectedMessage, response.jsonPath().getString("errorMessages"));
    }

    @Test
    public void testDeleteMultipleTodos() {
        int[] objectCounts = {1, 10, 100, 1000, 1500};
        String csvFile = "performance_results.csv";

        for (int numObjects : objectCounts) {
            // 1. Create TODOs and store their IDs
            int[] createdIds = new int[numObjects];
            for (int i = 0; i < numObjects; i++) {
                JSONObject todo = RandomGenerator.generateTodo();
                Response response = given()
                        .body(todo.toString())
                        .when()
                        .post("/todos");
                assertEquals(201, response.getStatusCode());
                createdIds[i] = response.jsonPath().getInt("id");
            }

            // 2. Measure performance while deleting the TODOs
            PerformanceMonitor.PerformanceResult result = PerformanceMonitor.monitorDuring(() -> {
                for (int id : createdIds) {
                    Response response = given()
                            .pathParam("id", id)
                            .when()
                            .delete("/todos/{id}");
                    assertEquals(200, response.getStatusCode());
                }
            });

            // 3. Write results to CSV
            CsvWriter.writePerformanceResults(
                    csvFile,
                    "deleteMultipleTodos",
                    numObjects,
                    result.durationMs,
                    result.averageCpuPercent,
                    result.getJvmMemoryDelta(),
                    result.peakJvmUsedMemory,
                    result.getSystemMemoryDelta()
            );
        }
    }

    // Undocumented API found after exploratory testing: /todos/tasksof (returns a list of projects that have tasks)
    @Test
    public void testGetTodoTasksof() {
        Response getTasksofResponse = given()
                .when()
                .get("/todos/tasksof");

        assertEquals(200, getTasksofResponse.getStatusCode());
        List<Map<String, Object>> tasksof = getTasksofResponse.jsonPath().getList("projects");

        Response getProjectsResponse = given()
                .when()
                .get("/projects");

        assertEquals(200, getProjectsResponse.getStatusCode());
        List<Map<String, Object>> projects = getProjectsResponse.jsonPath().getList("projects");

        // Check that tasksof is a subset of projects
        // Extract project IDs from the projects response
        Set<String> projectIds = projects.stream()
                .map(p -> String.valueOf(p.get("id"))) // Convert to String to handle mixed data types
                .collect(Collectors.toSet());

        // Ensure every tasksof element exists in projects
        for (Map<String, Object> task : tasksof) {
            String taskProjectId = String.valueOf(task.get("id")); // Convert to String
            assertTrue(projectIds.contains(taskProjectId));
        }

        // Ensure that project is contained in tasksof but not project 2 (since it does not have tasks)
        Set<String> tasksofIds = tasksof.stream()
                .map(p -> String.valueOf(p.get("id"))) // Convert to String to handle mixed data types
                .collect(Collectors.toSet());

        assertTrue(tasksofIds.contains(String.valueOf(projectID)));
        assertFalse(tasksofIds.contains(String.valueOf(project2ID)));
    }

    // Bug
    // When using the /todos/tasksof API, each project element in the output appears as many times as it has tasks.
    // This test fails in order to show this bug
    @Test
    public void testGetTodoTasksofFail() {
        // Add second todo to project to show bug
        JSONObject tasksofRequestBody = new JSONObject();
        tasksofRequestBody.put("id", String.valueOf(projectID));

        Response tasksofPostResponse = given()
                .pathParam("id", todo2ID)
                .body(tasksofRequestBody.toString())
                .when()
                .post("/todos/{id}/tasksof");

        assertEquals(201, tasksofPostResponse.getStatusCode());

        // Get project tasks
        Response getTasksofResponse = given()
                .when()
                .get("/todos/tasksof");

        assertEquals(200, getTasksofResponse.getStatusCode());
        List<Map<String, Object>> tasksof = getTasksofResponse.jsonPath().getList("projects");

        Response getProjectsResponse = given()
                .when()
                .get("/projects");

        assertEquals(200, getProjectsResponse.getStatusCode());
        List<Map<String, Object>> projects = getProjectsResponse.jsonPath().getList("projects");

        // Check that tasksof is a subset of projects
        // Extract project IDs from the projects response
        Set<String> projectIds = projects.stream()
                .map(p -> String.valueOf(p.get("id"))) // Convert to String to handle mixed data types
                .collect(Collectors.toSet());

        // Ensure every tasksof element exists in projects
        for (Map<String, Object> task : tasksof) {
            String taskProjectId = String.valueOf(task.get("id")); // Convert to String
            assertTrue(projectIds.contains(taskProjectId));
        }

        // Ensure that project is contained in tasksof but not project 2 (since it does not have tasks)
        Set<String> tasksofIds = tasksof.stream()
                .map(p -> String.valueOf(p.get("id"))) // Convert to String to handle mixed data types
                .collect(Collectors.toSet());

        assertTrue(tasksofIds.contains(String.valueOf(projectID)));
        assertFalse(tasksofIds.contains(String.valueOf(project2ID)));

        // Bug
        // Check that each element in tasksof appears only once
        Set<Map<String, Object>> uniqueElements = new HashSet<>(tasksof);
        assertEquals(tasksof.size(), uniqueElements.size(), "Duplicate elements found in tasksof.");
    }

    // Get list of projects for a todo
    @Test
    public void testGetTodoTasksofWithId() {
        Response response = given()
                .pathParam("id", todoID)
                .when()
                .get("/todos/{id}/tasksof");

        assertEquals(200, response.getStatusCode());
        assertEquals(
                "[[id:" + projectID + ", title:" + project.getString("title") +
                        ", completed:" + String.valueOf(project.getBoolean("completed")) +
                        ", active:" + String.valueOf(project.getBoolean("active")) +
                        ", description:" + project.getString("description") +
                        ", tasks:[[id:" + todoID + "]]]]",
                response.jsonPath().getString("projects"));
    }

    // Bug
    // Getting tasksof for a todo with invalid ID returns the list of projects that have tasks
    @Test
    public void testGetTodoTasksofWithInvalidIdFail() {
        Response response = given()
                .pathParam("id", -1)
                .when()
                .get("/todos/{id}/tasksof");

        assertEquals(404, response.getStatusCode());
    }

    // Bug
    // Getting tasksof for a todo with invalid ID returns the list of projects that have tasks
    // This test passes to show actual behaviour
    @Test
    public void testGetTodoTasksofWithInvalidIdPass() {
        Response response = given()
                .pathParam("id", -1)
                .when()
                .get("/todos/{id}/tasksof");

        assertEquals(200, response.getStatusCode());
        List<Map<String, Object>> projects = response.jsonPath().getList("projects");

        // Extract project IDs from the response
        Set<String> projectIds = projects.stream()
                .map(p -> String.valueOf(p.get("id"))) // Convert to String to handle mixed data types
                .collect(Collectors.toSet());

        // Ensure that project is contained in projects but not project 2 (since it does not have tasks)
        // This shows that the output list is the same as the one given by /todos/tasksof
        assertTrue(projectIds.contains(String.valueOf(projectID)));
        assertFalse(projectIds.contains(String.valueOf(project2ID)));
    }

    // Bug
    // When creating a tasksof relationship between a todo and a project, the project ID in the body
    // must be a string, which is misleading and not mentioned in the documentation
    // This test fails in order to show this bug
    @Test
    public void testPostTodoTasksofWithIdFail(){
        JSONObject tasksofRequestBody = new JSONObject();
        tasksofRequestBody.put("id", project2ID);

        Response tasksofPostResponse = given()
                .pathParam("id", todoID)
                .body(tasksofRequestBody.toString())
                .when()
                .post("/todos/{id}/tasksof");

        assertEquals(201, tasksofPostResponse.getStatusCode());

        // Note: we are deleting projects between tests, so we don't need to explicitly delete this newly
        // created relationship here
    }

    // Bug
    // When creating a tasksof relationship between a todo and a project, the project ID in the body
    // must be a string, which is misleading and not mentioned in the documentation
    // This test passes to show the unexpected behaviour functioning
    @Test
    public void testPostTodoTasksofWithIdPass(){
        JSONObject tasksofRequestBody = new JSONObject();
        tasksofRequestBody.put("id", String.valueOf(project2ID));

        Response tasksofPostResponse = given()
                .pathParam("id", todoID)
                .body(tasksofRequestBody.toString())
                .when()
                .post("/todos/{id}/tasksof");

        assertEquals(201, tasksofPostResponse.getStatusCode());

        // Note: we are deleting everything between tests, so we don't need to explicitly delete this newly
        // created relationship here
    }

    @Test
    public void testDeleteTodoTasksofWithId(){
        Response tasksofDeleteResponse = given()
                .pathParam("todoID", todoID)
                .pathParam("projectID", projectID)
                .when()
                .delete("/todos/{todoID}/tasksof/{projectID}");

        assertEquals(200, tasksofDeleteResponse.getStatusCode());
    }

    // Undocumented API found after exploratory testing: /todos/categories (returns a list of categories that have todos)
    @Test
    public void testGetTodoCategories() {
        Response getCategoriesResponse = given()
                .when()
                .get("/todos/categories");

        assertEquals(200, getCategoriesResponse.getStatusCode());
        List<Map<String, Object>> categories = getCategoriesResponse.jsonPath().getList("categories");

        Response getAllCategoriesResponse = given()
                .when()
                .get("/categories");

        assertEquals(200, getAllCategoriesResponse.getStatusCode());
        List<Map<String, Object>> allCategories = getAllCategoriesResponse.jsonPath().getList("categories");

        // Check that categories is a subset of allCategories
        // Extract category IDs from the allCategories response
        Set<String> allCategoryIds = allCategories.stream()
                .map(p -> String.valueOf(p.get("id"))) // Convert to String to handle mixed data types
                .collect(Collectors.toSet());

        // Ensure every category element exists in allCategories
        for (Map<String, Object> task : categories) {
            String todoCategoryId = String.valueOf(task.get("id")); // Convert to String
            assertTrue(allCategoryIds.contains(todoCategoryId));
        }

        // Ensure that category is contained in categories but not category 2 (since it does not have todos)
        Set<String> categoryIds = categories.stream()
                .map(p -> String.valueOf(p.get("id"))) // Convert to String to handle mixed data types
                .collect(Collectors.toSet());

        assertTrue(categoryIds.contains(String.valueOf(categoryID)));
        assertFalse(categoryIds.contains(String.valueOf(category2ID)));
    }

    // Bug
    // When using the /todos/categories API, each project element in the output appears as many times as it has todos.
    // This test fails in order to show this bug
    @Test
    public void testGetTodoCategoriesFail() {
        // Add a second todo to the first category to show bug
        JSONObject categoriesRequestBody = new JSONObject();
        categoriesRequestBody.put("id", String.valueOf(categoryID));

        Response categoriesPostResponse = given()
                .pathParam("id", todo2ID)
                .body(categoriesRequestBody.toString())
                .when()
                .post("/todos/{id}/categories");

        assertEquals(201, categoriesPostResponse.getStatusCode());

        // Get todo categories
        Response getCategoriesResponse = given()
                .when()
                .get("/todos/categories");

        assertEquals(200, getCategoriesResponse.getStatusCode());
        List<Map<String, Object>> categories = getCategoriesResponse.jsonPath().getList("categories");

        Response getAllCategoriesResponse = given()
                .when()
                .get("/categories");

        assertEquals(200, getAllCategoriesResponse.getStatusCode());
        List<Map<String, Object>> allCategories = getAllCategoriesResponse.jsonPath().getList("categories");

        // Check that categories is a subset of allCategories
        // Extract category IDs from the allCategories response
        Set<String> allCategoryIds = allCategories.stream()
                .map(p -> String.valueOf(p.get("id"))) // Convert to String to handle mixed data types
                .collect(Collectors.toSet());

        // Ensure every category element exists in allCategories
        for (Map<String, Object> task : categories) {
            String todoCategoryId = String.valueOf(task.get("id")); // Convert to String
            assertTrue(allCategoryIds.contains(todoCategoryId));
        }

        // Ensure that category is contained in categories but not category 2 (since it does not have todos)
        Set<String> categoryIds = categories.stream()
                .map(p -> String.valueOf(p.get("id"))) // Convert to String to handle mixed data types
                .collect(Collectors.toSet());

        assertTrue(categoryIds.contains(String.valueOf(categoryID)));
        assertFalse(categoryIds.contains(String.valueOf(category2ID)));

        // Bug
        // Check that each element in categories appears only once
        Set<Map<String, Object>> uniqueElements = new HashSet<>(categories);
        assertEquals(categories.size(), uniqueElements.size(), "Duplicate elements found in categories.");
    }

    // Get list of categories for a todo
    @Test
    public void testGetTodoCategoryWithId() {
        Response response = given()
                .pathParam("id", todoID)
                .when()
                .get("/todos/{id}/categories");

        assertEquals(200, response.getStatusCode());
        assertEquals(
                "[[id:" + categoryID + ", title:" + category.getString("title") +
                        ", description:" + category.getString("description") + "]]",
                response.jsonPath().getString("categories"));
    }

    // Bug
    // Getting categories for a todo with invalid ID returns the list of categories that have todos
    @Test
    public void testGetTodoCategoryWithInvalidIdFail() {
        Response response = given()
                .pathParam("id", -1)
                .when()
                .get("/todos/{id}/categories");

        assertEquals(404, response.getStatusCode());
    }

    // Bug
    // Getting categories for a todo with invalid ID returns the list of projects that have tasks
    // This test passes to show actual behaviour
    @Test
    public void testGetTodoCategoryWithInvalidIdPass() {
        Response response = given()
                .pathParam("id", -1)
                .when()
                .get("/todos/{id}/categories");

        assertEquals(200, response.getStatusCode());
        List<Map<String, Object>> categories = response.jsonPath().getList("categories");

        // Extract category IDs from the response
        Set<String> categoryIds = categories.stream()
                .map(p -> String.valueOf(p.get("id"))) // Convert to String to handle mixed data types
                .collect(Collectors.toSet());

        // Ensure that category is contained in categories but not category 2 (since it does not have todos)
        // This shows that the output list is the same as the one given by /todos/categories
        assertTrue(categoryIds.contains(String.valueOf(categoryID)));
        assertFalse(categoryIds.contains(String.valueOf(category2ID)));
    }

    // Bug
    // When creating a categories relationship between a todo and a category, the category ID in the body
    // must be a string, which is misleading and not mentioned in the documentation
    // This test fails in order to show this bug
    @Test
    public void testPostTodoCategoryWithIdFail(){
        JSONObject requestBody = new JSONObject();
        requestBody.put("id", category2ID);

        Response response = given()
                .pathParam("id", todoID)
                .body(requestBody.toString())
                .when()
                .post("/todos/{id}/categories");

        assertEquals(201, response.getStatusCode());

        // Note: we are deleting categories between tests, so we don't need to explicitly delete this newly
        // created relationship here
    }

    // Bug
    // When creating a categories relationship between a todo and a category, the category ID in the body
    // must be a string, which is misleading and not mentioned in the documentation
    // This test passes to show the unexpected behaviour functioning
    @Test
    public void testPostTodoCategoriesWithIdPass(){
        JSONObject requestBody = new JSONObject();
        requestBody.put("id", String.valueOf(category2ID));

        Response response = given()
                .pathParam("id", todoID)
                .body(requestBody.toString())
                .when()
                .post("/todos/{id}/categories");

        assertEquals(201, response.getStatusCode());

        // Note: we are deleting categories between tests, so we don't need to explicitly delete this newly
        // created relationship here
    }

    @Test
    public void testDeleteTodoCategoryWithId(){
        Response deleteResponse = given()
                .pathParam("todoID", todoID)
                .pathParam("categoryID", categoryID)
                .when()
                .delete("/todos/{todoID}/categories/{categoryID}");

        assertEquals(200, deleteResponse.getStatusCode());
    }
}
