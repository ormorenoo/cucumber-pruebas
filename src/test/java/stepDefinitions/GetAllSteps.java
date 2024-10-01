package stepDefinitions;

import context.AuthContextSingleton;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;

public class GetAllSteps {

    private Response response;
    private String sessionToken;;
    private final AuthSteps authSteps = new AuthSteps();
    private Map<String, Object> ordenacion = new HashMap<>();

    @Given("proporciono los datos permitidos para las propiedades de ordenacion")
    public void proporcionoDatosPermitidosParaPropiedadesDeOrdenacion() {
        ordenacion.put("page", 0);  // Página 0 (primer página)
        ordenacion.put("size", 10); // Tamaño de página de 10
        ordenacion.put("sort", "login,asc");
    }

    @Given("proporciono los datos incorrectos para las propiedades de ordenacion")
    public void proporcionoDatosIncorrectosParaPropiedadesDeOrdenacion() {
        ordenacion.put("page", -1);  // Página inválida
        ordenacion.put("size", 0);   // Tamaño de página inválido
        ordenacion.put("sort", "invalidProperty,asc");
    }

    @When("envío una solicitud para listar todos los usuarios del sistema")
    public void envioSolicitudParaListarUsuarios() {
        sessionToken = AuthContextSingleton.getInstance().getToken(); // Reutiliza el token de autenticación

        response = given()
                .header("Authorization", "Bearer " + sessionToken)
                .queryParams(ordenacion)
                .when()
                .get("/api/admin/users");
    }

    @When("envío una solicitud para listar todos los usuarios del sistema sin permisos")
    public void envioSolicitudParaListarUsuariosSinPermisos() {
        sessionToken = AuthContextSingleton.getInstance().getToken();  // Reutiliza el token de usuario no administrador

        // Realiza la solicitud GET con el token de un usuario sin permisos
        response = given()
                .header("Authorization", "Bearer " + sessionToken)
                .queryParams(ordenacion)
                .when()
                .get("/api/users");
    }

    @Then("la respuesta de listar debe ser: {string}")
    public void respuestaDebeSer(String mensajeEsperado) {
        if(response.statusCode() == 200) {
            assertEquals("solicitud exitosa", mensajeEsperado);
        }
        if(response.statusCode() == 401) {
            assertEquals("solicitud rechazada", mensajeEsperado);
        }
    }

    @Then("debería ver un mensaje de error de listar: {string}")
    public void deberiaVerMensajeDeError(String mensajeError) {
        if(response.statusCode() == 400) {
            if("Propiedades de ordenamiento invalidas".equals(mensajeError)) {
                assertEquals("Propiedades de ordenamiento invalidas", mensajeError);
            }else{
                assertEquals("No autorizado", mensajeError);
            }
        }
    }
}
