package util;

import io.restassured.response.Response;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class AuthUtil {
    public String obtenerTokenAdmin() {
        Map<String, String> adminCredentials = new HashMap<>();
        adminCredentials.put("username", "admin");
        adminCredentials.put("password", "admin");

        Response authResponse = given()
                .contentType("application/json")
                .body(adminCredentials)
                .when()
                .post("/api/authenticate");

        return authResponse.jsonPath().getString("id_token");
    }
}
