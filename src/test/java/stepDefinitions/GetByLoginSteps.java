package stepDefinitions;


import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.restassured.response.Response;
import org.junit.Assert;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class GetByLoginSteps {

    private String adminToken;
    private Response response;
    private String endpoint = "http://localhost:8080/api/admin/users/";
    private String loginUsuario;

    // Escenario 1 -- Usuario con rol ADMIN accede correctamente a los detalles de un usuario

    @Given("que soy un administrador autenticado")
    public void que_soy_un_usuario_con_permisos_de_administrador_autenticado() {

        String username = "admin";
        String password = "admin";

        Response authResponse = given()
                .contentType("application/json")
                .body("{\"username\": \"" + username + "\", \"password\": \"" + password + "\"}")
                .post("http://localhost:8080/api/authenticate");

        adminToken = authResponse.jsonPath().getString("id_token");
        Assert.assertNotNull("Token JWT no debe ser nulo", adminToken);
    }

    @Given("proporciono un login correcto de un usuario existente")
    public void proporciono_un_login_correcto_de_un_usuario_existente() {
        loginUsuario = "new_user";
    }

    @When("envio una solicitud para obtener el usuario")
    public void envio_una_solicitud_para_obtener_el_usuario() {
        response = given()
                .header("Authorization", "Bearer " + adminToken)
                .contentType("application/json")
                .get(endpoint + loginUsuario);
    }


    @Then("la respuesta para el servicio para obtener por id debe ser: {string}")
    public void la_respuesta_para_el_servicio_para_obtener_por_id_debe_ser(String mensajeEsperado) {
        if (response.getStatusCode() == 200) {
            assertEquals("solicitud exitosa", mensajeEsperado);
        }
        if (response.getStatusCode() == 400 || response.getStatusCode() == 403) {
            assertEquals("solicitud rechazada", mensajeEsperado);
        }
        if (response.getStatusCode() == 404) {
            assertEquals("solicitud no encontrada", mensajeEsperado);
        }
    }


    @Then("la respuesta contiene los detalles del usuario")
    public void la_respuesta_contiene_los_detalles_del_usuario() {
        String login = response.jsonPath().getString("login");
        String email = response.jsonPath().getString("email");

        assertEquals("new_user", login);
        assertEquals("otroemail@example", email);
    }

    //Escenario 2 -- Usuario sin rol admin intenta acceder a los detalles de un usuario
    @Given("que soy un usuario sin permisos de administrador autenticado")
    public void usuario_no_administrador_autenticado() {
        String username = "johnd";
        String password = "password123";

        // Enviar la solicitud de inicio de sesión
        Response loginResponse = given()
                .contentType("application/json")
                .body("{\"username\": \"" + username + "\", \"password\": \"" + password + "\"}")
                .post("http://localhost:8080/api/authenticate");

        // Obtener el token JWT de la respuesta
        adminToken = loginResponse.jsonPath().getString("id_token");
        assertNotNull("Se esperaba un token JWT, pero no se recibió", adminToken);
    }

    @Then("debería ver un mensaje de error en el servicio de obtener por id: {string}")
    public void mensaje_error(String mensajeEsperado) {
        if (response.statusCode() == 404) {
            assertEquals("Usuario no encontrado", mensajeEsperado);
        }
        if (response.statusCode() == 403) {
            assertEquals("No autorizado", mensajeEsperado);
        }
    }

    //Escenario 3 -- Busqueda de un login inexistente en el sistema
    @Given("proporciono un login incorrecto o de un usuario no existente")
    public void usuario_no_existente() {
        loginUsuario = "usuario_inexistente";
    }
}
