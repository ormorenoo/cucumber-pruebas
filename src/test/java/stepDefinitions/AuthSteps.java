package stepDefinitions;

import context.AuthContextSingleton;
import io.cucumber.java.en.Given;
import io.restassured.response.Response;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertNotNull;

public class AuthSteps {
    private String adminToken;
    private String userToken;

    @Given("que soy un usuario con permisos de administrador autenticado")
    public void soyAdministradorAutenticado() {
        // Enviar la solicitud de inicio de sesión
        Response loginResponse = given()
                .contentType("application/json")
                .body("{\"username\": \"" + "admin" + "\", \"password\": \"" + "admin" + "\"}")
                .post("http://localhost:8080/api/authenticate");

        // Obtener el token JWT de la respuesta
        adminToken = loginResponse.jsonPath().getString("id_token");
        AuthContextSingleton.getInstance().setToken(adminToken);
        assertNotNull("Se esperaba un token JWT, pero no se recibió", adminToken);
    }

    @Given("que soy un usuario no autenticado como administrador")
    public void usuarioSinPermisos() {
        // Enviar la solicitud de inicio de sesión
        Response loginResponse = given()
                .contentType("application/json")
                .body("{\"username\": \"" + "johnd" + "\", \"password\": \"" + "password123" + "\"}")
                .post("http://localhost:8080/api/authenticate");

        // Obtener el token JWT de la respuesta
        userToken = loginResponse.jsonPath().getString("id_token");
        AuthContextSingleton.getInstance().setToken(userToken);
        assertNotNull("Se esperaba un token JWT, pero no se recibió", userToken);
    }
}
