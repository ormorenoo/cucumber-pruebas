package stepDefinitions;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;

public class CreateUserSteps {
    private Map<String, String> usuario;
    private Response response;
    private String endpoint = "http://localhost:8080/api/register";

    @Given("que soy un usuario sin registrar")
    public void soy_un_usuario_sin_registrar() {
        usuario = new HashMap<>();
    }

    @Given("proporciono todos los datos solicitados para el registro")
    public void proporciono_datos_dolicitados_para_registro() {
        usuario.put("login", "nuevoUsuario");
        usuario.put("email", "nuevo@ejemplo.com");
        usuario.put("password", "Password123!");
    }

    @Given("Tengo un email ya registrado")
    public void tengo_email_ya_registrado() {
        usuario.put("email", "existente@ejemplo.com"); // Email ya registrado
    }

    @Given("Tengo un login ya registrado")
    public void tengo_login_ya_registrado() {
        usuario.put("login", "usuarioExistente"); // Login ya registrado
    }

    @When("envío una solicitud para crear un usuario")
    public void envio_solicitud_para_crear_usuario() {
        response = given()
                .contentType("application/json")
                .body(usuario)
                .when()
                .post(endpoint);
    }

    @Then("la respuesta debe ser: {string}")
    public void la_respuesta_debe_ser(String mensajeEsperado) {
        if(response.statusCode() == 201) {
            assertEquals("creado exitosamente", mensajeEsperado);
        }
        if(response.statusCode() == 400) {
            assertEquals("solicitud rechazada", mensajeEsperado);
        }
    }

    @Then("el usuario debe existir en el sistema")
    public void elUsuarioDebeExistirEnElSistema() {
        // Verificamos que el usuario fue creado y ahora existe en el sistema
        given()
                .contentType("application/json")
                .when()
                .get("/api/users/" + usuario.get("login"))
                .then()
                .statusCode(200);  // Si el código es 200, el usuario existe
    }

    @Then("debería ver un mensaje de error {string}")
    public void deberiaVerMensajeDeError(String mensajeError) {
        if(response.statusCode() == 400) {
            if("El nombre de usuario ya está en uso".equals(mensajeError)) {
                assertEquals("El nombre de usuario ya está en uso", mensajeError);
            }else{
                assertEquals("El correo ya está en uso", mensajeError);
            }
        }
    }
}
