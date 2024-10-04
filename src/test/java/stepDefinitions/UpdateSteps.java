package stepDefinitions;

import com.github.javafaker.Faker;
import context.AuthContextSingleton;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;


public class UpdateSteps {

    private Response response;
    private String endpoint = "http://localhost:8080/api/admin/users";
    private String authToken;
    private int usuarioId = 1152;
    private String nuevosDatos;
    private String username;
    private String password;
    private String email;
    private Faker faker;

    @Given("proporciono los datos correctos para modificar el perfil de un usuario")
    public void proporciono_los_datos_correctos_para_modificar_el_perfil_de_un_usuario() {
        faker = new Faker();
        username = faker.name().username();
        email = faker.internet().emailAddress();
        nuevosDatos = "{\"id\": \"" + usuarioId + "\", \"login\": \""+ username + "\", \"email\": \"" + email + "\", \"authorities\": [\"ROLE_USER\"]}";
    }

    @When("envío una solicitud para modificar los datos")
    public void envío_una_solicitud_para_modificar_los_datos() {
        authToken = AuthContextSingleton.getInstance().getToken();
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
                .get(endpoint + "/" + username);
        // Depuración: imprimir el cuerpo de la respuesta
        System.out.println("Respuesta del usuario actualizado: " + getResponse.getBody().asString());

        // Asegúrate de que la respuesta no es null
        assertNotNull("Se esperaba una respuesta del usuario actualizado, pero se recibió null", getResponse.getBody().asString());

        // Verificar que los datos actualizados sean correctos
        String nombreActualizado = getResponse.jsonPath().getString("login");
        String emailActualizado = getResponse.jsonPath().getString("email");

        assertEquals(username, nombreActualizado);
        assertEquals("ormoreno2000@gmail.com", emailActualizado);
    }

    //Escenario 2 -- Actualizacion de un usuario con un email ya existente
    @Given("proporciono un email ya registrado en otro usuario")
    public void email_existente() {
        nuevosDatos = "{\"id\": \"" + usuarioId + "\", \"login\": \"user2\", \"email\": \"" + "admin@localhost" + "\", \"authorities\": [\"ROLE_USER\"]}";
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
        nuevosDatos = "{\"id\": \"" + 3 + "\", \"login\": \"ejemplo\", \"email\": \"" + "ejemplo@localhost" + "\", \"authorities\": [\"ROLE_USER\"]}";
    }

}
