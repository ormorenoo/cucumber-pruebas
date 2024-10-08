Feature: Obtener información de un usuario
  requiere obtener un usuario especifico

  Scenario: Usuario con rol ADMIN accede correctamente a los detalles de un usuario
    Given que soy un usuario con permisos de administrador autenticado
    And proporciono un login correcto de un usuario existente
    When envio una solicitud para obtener el usuario
    Then la respuesta para el servicio para obtener por id debe ser: "solicitud exitosa"
    And la respuesta contiene los detalles del usuario

  Scenario: Usuario sin rol admin intenta acceder a los detalles de un usuario
    Given que soy un usuario no autenticado como administrador
    And proporciono un login correcto de un usuario existente
    When envio una solicitud para obtener el usuario
    Then la respuesta para el servicio para obtener por id debe ser: "solicitud rechazada"
    And debería ver un mensaje de error en el servicio de obtener por id: "No autorizado"

  Scenario: Busqueda de un login inexistente en el sistema
    Given que soy un usuario con permisos de administrador autenticado
    And proporciono un login incorrecto o de un usuario no existente
    When envio una solicitud para obtener el usuario
    Then la respuesta para el servicio para obtener por id debe ser: "solicitud no encontrada"
    And debería ver un mensaje de error en el servicio de obtener por id: "Usuario no encontrado"

