import org.json.JSONObject;
import org.json.JSONTokener;
import spark.Request;
import spark.Response;
import spark.Route;
import static spark.Spark.post;
import java.util.*;

public class UserController {

    static Queue<JSONObject> post_queue = new LinkedList<JSONObject>();

    public UserController(final UserService userService) {
//        get("/getall", new Route() {
//            @Override
//            public Object handle(Request request, Response response) {
//                return userService.getAllUsers();
//            }
//        });

        post("/post", new Route() {
            @Override
            public Object handle(Request request, Response response) {
                JSONTokener tokener = new JSONTokener(request.body());
                JSONObject root = new JSONObject(tokener);
                post_queue.add(root);
                return "ack";
            }
        });
    }
}