Feature: Actualizacion de usuario
  Se requiere actualizar un usuario existente

  Scenario: Actualizacion exitosa de un usuario existente
    Given que soy un usuario con permisos de administrador autenticado
    And proporciono los datos correctos para modificar el perfil de un usuario
    When envío una solicitud para modificar los datos
    Then la respuesta del servicio de actualizacion debe ser: "solicitud exitosa"
    And los datos deben estar actualizados en el sistema


  Scenario: Actualizacion de un usuario con un email ya existente
    Given que soy un usuario con permisos de administrador autenticado
    And proporciono un email ya registrado en otro usuario
    When envío una solicitud para modificar los datos
    Then la respuesta del servicio de actualizacion debe ser: "solicitud rechazada"
    And debería ver un mensaje de error en el servicios de actualizacion: "El correo ya está en uso"

  Scenario: Actualizacion de un usuario con un login ya existente
    Given que soy un usuario con permisos de administrador autenticado
    And proporciono un login ya registrado en otro usuario
    When envío una solicitud para modificar los datos
    Then la respuesta del servicio de actualizacion debe ser: "solicitud rechazada"
    And debería ver un mensaje de error en el servicios de actualizacion: "El nombre de usuario ya está en uso"


  Scenario: Actualizacion de un usuario no existente
    Given que soy un usuario con permisos de administrador autenticado
    And proporciono los datos para actualizar un usuario ya eliminado
    When envío una solicitud para modificar los datos
    Then la respuesta del servicio de actualizacion debe ser: "solicitud no encontrada"
    And debería ver un mensaje de error en el servicios de actualizacion: "El usuario ya no existe en el sistema"

  Scenario: Actualizacion de un usuario sin permisos de administrador
    Given que soy un usuario no autenticado como administrador
    And proporciono los datos correctos para modificar el perfil de un usuario
    When envío una solicitud para modificar los datos
    Then la respuesta del servicio de actualizacion debe ser: "solicitud rechazada"
    And debería ver un mensaje de error en el servicios de actualizacion: "No autorizado"

