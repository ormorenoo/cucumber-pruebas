Feature: Eliminación de usuario
  Como administrador se requiere eliminar una cuenta de usuario

  Scenario: Eliminación exitosa de un usuario
    Given que soy un administrador autenticado
    And existe un usuario con un login dado
    When envío una solicitud para eliminar el usuario con login dado
    Then la respuesta debe ser: "Solicitud sin contenido exitosa"
    And debería ver un mensaje: "Usuario borrado con exito"
    And el usuario con login dado no debe existir en el sistema

  Scenario: Intentar eliminar un usuario que no existe
    Given que soy un administrador autenticado
    And no existe un usuario con un login dado
    When envío una solicitud para eliminar el usuario con login dado
    Then la respuuesta debe ser: "solicitud no encontrada"
    And debería ver un mensaje de error: "usuario no encontrado"

  Scenario: Intentar eliminar un usuario sin permisos suficientes
    Given que soy un usuario sin privilegios de administrador
    When envío una solicitud para eliminar el usuario con login dado
    Then la respuesta debe ser "solicitud rechazada"
    And debería ver un mensaje de error "No autorizado"