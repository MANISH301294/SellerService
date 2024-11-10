package reusable;

import io.restassured.path.json.JsonPath;

public class Reusable {

    public static JsonPath rowToJson(String response)
    {
        JsonPath js = new JsonPath(response);
        return js;
    }
}
