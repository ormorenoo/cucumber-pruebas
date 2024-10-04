package stepDefinitions;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.restassured.response.Response;
import org.junit.Assert;


import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.junit.Assert.assertEquals;


public class LoginSteps {

    private String username;
    private String password;
    private Response response;
    private String jwtToken;
    public String endpoint = "http://localhost:8080/api/authenticate";

    // Escenario 1 -- Inicio de sesión exitoso

    @Given("ingreso un nombre de usuario y contraseña")
    public void ingreso_un_nombre_de_usuario_y_contrasenia() {
        username = "johnd";
        password = "password123";
    }

    @When("envío las credenciales al sistema")
    public void envío_las_credenciales_al_sistema() {
        // Enviar las credenciales al endpoint de autenticación usando Rest Assured
        response = given()
                .contentType("application/json")
                .body("{\"username\": \"" + username + "\", \"password\": \"" + password + "\"}")
                .post(endpoint);
        
        // Validación del JSON Schema
        response.then().assertThat().body(matchesJsonSchemaInClasspath("schemas/login_schema.json"));
    }

    @Then("la respuesta debe ser: {string}")
    public void la_respuesta_debe_ser(String mensajeEsperado) {

        if (response.getStatusCode() == 200) {
            assertEquals("sesión iniciada correctamente", mensajeEsperado);
        }
        if (response.getStatusCode() == 401) {
            assertEquals("credenciales incorrectas", mensajeEsperado);
        }
    }

    @Then("debería obtener un token JWT")
    public void debería_obtener_un_token_JWT() {
        // Verifica que la respuesta contiene un token JWT
        jwtToken = response.jsonPath().getString("id_token");
        Assert.assertNotNull("Se esperaba un token JWT, pero no se recibió", jwtToken);
    }


    //Escenario 2 -- Inicio de sesión fallido con credenciales incorrectas

    @Given("la contraseña es incorrecta")
    public void contrasenia_incorrecta() {
        password = "contrasenia_incorrecta";
    }

    //Escenario 3 -- Inicio de sesión fallido con un usuario no registrado

    @Given("que soy un usuario no registrado")
    public void que_soy_un_usuario_no_registrado() {

    }

    @Given("el nombre de usuario no existe")
    public void el_nombre_de_usuario_no_existe() {
        username = "usuario_inexistente";
    }
}
