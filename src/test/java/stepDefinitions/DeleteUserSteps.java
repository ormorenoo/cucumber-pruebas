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

public class DeleteUserSteps {
    private Response response;
    private String sessionToken;
    private String loginUsuario;

    private String usuarioSinPermisosToken;

    private String endpoint = "http://localhost:8080/api";

    @Given("existe un usuario con un login dado")
    public void existeUsuarioConLoginDado() {
        // Asignamos el login del usuario que será eliminado
        loginUsuario = "usuarioaeliminar";
        sessionToken = AuthContextSingleton.getInstance().getToken();

        // Creamos un usuario para asegurarnos de que existe antes de eliminarlo
        Map<String, String> nuevoUsuario = new HashMap<>();
        nuevoUsuario.put("login", loginUsuario);
        nuevoUsuario.put("email", "usuario@ejemplo.com");
        nuevoUsuario.put("password", "Password123!");

        // Enviar solicitud para crear el usuario
        given()
                .header("Authorization", "Bearer " + sessionToken)
                .contentType("application/json")
                .body(nuevoUsuario)
                .when()
                .post(endpoint + "/admin/users");
    }

    @Given("no existe un usuario con un login dado")
    public void noExisteUsuarioConLoginDado() {
        // Asignamos un login para un usuario que no existe
        loginUsuario = "usuarioNoExistente";
    }

    @When("envío una solicitud para eliminar el usuario con login dado")
    public void envioSolicitudParaEliminarUsuarioConLoginDado() {
        sessionToken = AuthContextSingleton.getInstance().getToken();
        // Enviar la solicitud DELETE para eliminar el usuario
        response = given()
                .header("Authorization", "Bearer " + sessionToken)  // Token de administrador
                .when()
                .delete(endpoint + "/admin/users/" + loginUsuario);
    }

    @When("envío una solicitud para eliminar el usuario con login dado sin permisos suficientes")
    public void envioSolicitudSinPermisos() {
        // Enviar la solicitud DELETE para eliminar el usuario, pero sin permisos suficientes
        response = given()
                .header("Authorization", "Bearer " + usuarioSinPermisosToken)  // Token sin permisos
                .when()
                .delete(endpoint + "/admin/users/" + loginUsuario);
    }

    @Then("la respuesta del servicio de eliminar debe ser: {string}")
    public void laRespuestaDebeSer(String mensajeEsperado) {
        // Verificamos el mensaje en la respuesta
        if(response.statusCode() == 204) {
            assertEquals("Solicitud sin contenido exitosa", mensajeEsperado);
        }
        if(response.statusCode() == 404) {
            assertEquals("solicitud no encontrada", mensajeEsperado);
        }
        if(response.statusCode() == 400) {
            assertEquals("solicitud rechazada", mensajeEsperado);
        }
    }

    @Then("debería ver un mensaje: {string}")
    public void deberiaVerUnMensaje(String mensajeEsperado) {
        if(response.statusCode() == 204) {
            assertEquals("Usuario borrado con exito", mensajeEsperado);
        }
    }

    @Then("el usuario con login dado no debe existir en el sistema")
    public void elUsuarioNoDebeExistir() {
        // Intentamos buscar el usuario eliminado
        given()
                .header("Authorization", "Bearer " + sessionToken)
                .when()
                .get(endpoint + "/admin/users/" + loginUsuario)
                .then()
                .statusCode(404);  // Si el usuario no existe, debería devolver 404
    }

    @Then("debería ver un mensaje de error de borrado: {string}")
    public void deberiaVerUnMensajeDeErrorBorrado(String mensajeError) {
        if(response.statusCode() == 404) {
            assertEquals("usuario no encontrado", mensajeError);
        }
        if(response.statusCode() == 401) {
            assertEquals("No autorizado", mensajeError);
        }
    }
}
