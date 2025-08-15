package test.java.unitTests;
import com.github.javafaker.Faker;
import org.json.JSONObject;

public class RandomGenerator {
    private static final Faker faker = new Faker();

    public static JSONObject generateTodo() {
        JSONObject todo = new JSONObject();
        todo.put("title", faker.lorem().sentence());
        todo.put("doneStatus", faker.bool().bool());
        todo.put("description", faker.lorem().paragraph());
        return todo;
    }
}
