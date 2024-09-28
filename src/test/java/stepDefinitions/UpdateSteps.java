package stepDefinitions;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.restassured.response.Response;


import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;


public class UpdateSteps {

    private Response response;
    private String endpoint = "http://localhost:8080/api/admin/users";
    private String authToken;
    private int usuarioId = 1203;
    private String nuevosDatos;
    private String username;
    private String password;
    private String email= "otroemail@example";


    // Escenario 1 -- Actualizacion exitosa de un usuario existente
    @Given("que soy un usuario con permisos de administrador autenticado")
    public void que_soy_un_usuario_con_permisos_de_administrador_autenticado() {
        username = "admin";
        password = "admin";

        // Enviar la solicitud de inicio de sesión
        Response loginResponse = given()
                .contentType("application/json")
                .body("{\"username\": \"" + username + "\", \"password\": \"" + password + "\"}")
                .post("http://localhost:8080/api/authenticate");

        // Obtener el token JWT de la respuesta
        authToken = loginResponse.jsonPath().getString("id_token");
        assertNotNull("Se esperaba un token JWT, pero no se recibió", authToken);
    }


    @Given("proporciono los datos correctos para modificar el perfil de un usuario")
    public void proporciono_los_datos_correctos_para_modificar_el_perfil_de_un_usuario() {
        nuevosDatos = "{\"id\": \"" + usuarioId + "\", \"login\": \"new_user\", \"email\": \"" + email + "\", \"authorities\": [\"ROLE_USER\"]}";
    }


    @When("envío una solicitud para modificar los datos")
    public void envío_una_solicitud_para_modificar_los_datos() {
        response = given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + authToken)
                .body(nuevosDatos)
                .put(endpoint);
        // Validación del JSON Schema
        response.then().assertThat().body(matchesJsonSchemaInClasspath("schemas/update_schema.json"));

    }


    @Then("la respuesta del servicio de actualizacion debe ser: {string}")
    public void respuesta_update(String mensajeEsperado) {
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

    @Then("los datos deben estar actualizados en el sistema")
    public void los_datos_deben_estar_actualizados_en_el_sistema() {
        Response getResponse = given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + authToken)
                .get(endpoint + "/" + "new_user");
        // Depuración: imprimir el cuerpo de la respuesta
        System.out.println("Respuesta del usuario actualizado: " + getResponse.getBody().asString());

        // Asegúrate de que la respuesta no es null
        assertNotNull("Se esperaba una respuesta del usuario actualizado, pero se recibió null", getResponse.getBody().asString());

        // Verificar que los datos actualizados sean correctos
        String nombreActualizado = getResponse.jsonPath().getString("login");
        String emailActualizado = getResponse.jsonPath().getString("email");

        assertEquals("new_user", nombreActualizado);
        assertEquals("otroemail@example", emailActualizado);
    }

    //Escenario 2 -- Actualizacion de un usuario con un email ya existente
    @Given("proporciono un email ya registrado en otro usuario")
    public void email_existente() {
        email = "admin@localhost";
        nuevosDatos = "{\"id\": \"" + usuarioId + "\", \"login\": \"new_user\", \"email\": \"" + email + "\", \"authorities\": [\"ROLE_USER\"]}";
    }

    @Then("debería ver un mensaje de error en el servicios de actualizacion: {string}")
    public void mensaje_error(String mensajeEsperado) {
        if (response.statusCode() == 400) {
            if ("El nombre de usuario ya está en uso".equals(mensajeEsperado)) {
                assertEquals("El nombre de usuario ya está en uso", mensajeEsperado);
            } else {
                assertEquals("El correo ya está en uso", mensajeEsperado);
            }
        }
        if (response.statusCode() == 403) {
            assertEquals("No autorizado", mensajeEsperado);
        }
    }

    //Escenario 3 -- Actualizacion de un usuario con un login ya existente
    @Given("proporciono un login ya registrado en otro usuario")
    public void login_existente() {
        nuevosDatos = "{\"id\": \"" + usuarioId + "\", \"login\": \"admin\", \"email\": \"" + email + "\", \"authorities\": [\"ROLE_USER\"]}";
    }

    //Escenario 4 -- Actualizacion de un usuario no existente
    @Given("proporciono los datos para actualizar un usuario ya eliminado")
    public void usuario_inexistente() {
        email = "ejemplo@localhost";
        nuevosDatos = "{\"id\": \"" + 3 + "\", \"login\": \"ejemplo\", \"email\": \"" + email + "\", \"authorities\": [\"ROLE_USER\"]}";
    }

    //Escenario 5 -- Actualizacion de un usuario sin permisos de administrador
    @Given("que soy un usuario no autenticado como administrador")
    public void usuario_sin_permisos() {
        username = "johnd";
        password = "password123";

        // Enviar la solicitud de inicio de sesión
        Response loginResponse = given()
                .contentType("application/json")
                .body("{\"username\": \"" + username + "\", \"password\": \"" + password + "\"}")
                .post("http://localhost:8080/api/authenticate");

        // Obtener el token JWT de la respuesta
        authToken = loginResponse.jsonPath().getString("id_token");
        assertNotNull("Se esperaba un token JWT, pero no se recibió", authToken);
    }


}
