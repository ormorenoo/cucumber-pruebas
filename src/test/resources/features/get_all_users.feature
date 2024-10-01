Feature: Obtener todos los usuarios existentes
  Se requiere listar todos los usuarios del sistema

  Scenario: Listado exitoso de todos los usuarios
    Given que soy un usuario con permisos de administrador autenticado
    And proporciono los datos permitidos para las propiedades de ordenacion
    When envío una solicitud para listar todos los usuarios del sistema
    Then la respuesta de listar debe ser: "solicitud exitosa"

  Scenario: Listado con propiedades incorrectas de paginacion
    Given que soy un usuario con permisos de administrador autenticado
    And proporciono los datos incorrectos para las propiedades de ordenacion
    When envío una solicitud para listar todos los usuarios del sistema
    Then la respuesta de listar debe ser: "solicitud rechazada"
    And debería ver un mensaje de error de listar: "Propiedades de ordenamiento invalidas"

  Scenario: Solicitud de lista por un usuarios sin permisos de administrador
    Given que soy un usuario no autenticado como administrador
    When envío una solicitud para listar todos los usuarios del sistema
    Then la respuesta de listar debe ser: "solicitud rechazada"
    And debería ver un mensaje de error de listar: "No autorizado"
