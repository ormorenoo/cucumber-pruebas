package stepDefinitions;

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
    private String adminToken;
    private String loginUsuario;

    private String usuarioSinPermisosToken;

    private String endpoint = "http://localhost:8080/api";

    @Given("que soy un administrador autenticado")
    public void soyAdministradorAutenticado() {
        // Simulamos el login como administrador para obtener el token JWT
        Map<String, String> adminCredentials = new HashMap<>();
        adminCredentials.put("username", "admin");
        adminCredentials.put("password", "admin");

        // Autenticación para obtener el token de administrador
        Response authResponse = given()
                .contentType("application/json")
                .body(adminCredentials)
                .when()
                .post("/api/authenticate");

        // Extraemos el token de la respuesta
        adminToken = authResponse.jsonPath().getString("id_token");
    }

    @Given("existe un usuario con un login dado")
    public void existeUsuarioConLoginDado() {
        // Asignamos el login del usuario que será eliminado
        loginUsuario = "usuarioaeliminar";

        // Creamos un usuario para asegurarnos de que existe antes de eliminarlo
        Map<String, String> nuevoUsuario = new HashMap<>();
        nuevoUsuario.put("login", loginUsuario);
        nuevoUsuario.put("email", "usuario@ejemplo.com");
        nuevoUsuario.put("password", "Password123!");

        // Enviar solicitud para crear el usuario
        given()
                .header("Authorization", "Bearer " + adminToken)
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

    @Given("que soy un usuario sin privilegios de administrador")
    public void soyUsuarioSinPrivilegiosDeAdministrador() {
        // Simulamos el login de un usuario sin permisos
        Map<String, String> userCredentials = new HashMap<>();
        userCredentials.put("username", "user");
        userCredentials.put("password", "user");

        // Autenticación para obtener el token de usuario sin permisos
        Response authResponse = given()
                .contentType("application/json")
                .body(userCredentials)
                .when()
                .post(endpoint + "/authenticate");

        // Extraemos el token de la respuesta
        usuarioSinPermisosToken = authResponse.jsonPath().getString("id_token");
    }

    @When("envío una solicitud para eliminar el usuario con login dado")
    public void envioSolicitudParaEliminarUsuarioConLoginDado() {
        // Enviar la solicitud DELETE para eliminar el usuario
        response = given()
                .header("Authorization", "Bearer " + adminToken)  // Token de administrador
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
                .header("Authorization", "Bearer " + adminToken)
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
