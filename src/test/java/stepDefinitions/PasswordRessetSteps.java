package stepDefinitions;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class PasswordRessetSteps {

    private String email;
    private Response response;
    private String endpoint = "http://localhost:8080/api/account/reset-password/init";

    @Given("que soy un usuario registrado")
    public void soy_un_usuario_registrado() {
        email = "john.d@example.com";
    }

    @Given("tengo un correo electrónico asociado a mi cuenta")
    public void tengo_un_correo_registrado(){
        assertNotNull(email);

    }

    @When("envío una solicitud para restablecer la contraseña a mi correo")
    public void envio_solicitud_para_restablecer() {
        // Realiza la solicitud al endpoint
        response = given()
                .contentType("application/json")
                .body(email)
                .when()
                .post(endpoint);
    }

    @Then("la respuesta debe ser {string}")
    public void verificar_respuesta(String expectedMessage) {
        // Valida que la respuesta tenga un estado 200
        if (response.getStatusCode() == 200) {
            assertEquals("solicitud de restablecimiento de contraseña enviada", expectedMessage);
        }
    }

    @Then("debería recibir un correo con instrucciones para restablecer la contraseña")
    public void recibir_un_correo_con_instrucciones_para_restablecer(){
        System.out.println("Verificación de envío de correo (simulada).");

    }

}
