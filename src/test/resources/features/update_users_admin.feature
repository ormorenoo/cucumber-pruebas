Feature: Actualizacion de usuario
  Se requiere actualizar un usuario existente

  Scenario: Actualizacion exitosa de un usuario existente
    Given que soy un usuario autenticado
    And proporciono los datos correctos para modificar mi perfil
    When envío una solicitud para modificar mis datos
    Then la respuesta debe ser: "solicitud exitosa"
    And mis datos deben estar actualizados en el sistema


  Scenario: Actualizacion de un usuario con un email ya existente
    Given que soy un usuario autenticado
    And proporciono un email ya registrado en otro usuario
    When envío una solicitud para actualizar mi perfil
    Then la respuesta debe ser: "solicitud rechazada"
    And debería ver un mensaje de error "El correo ya está en uso"

  Scenario: Actualizacion de un usuario con un login ya existente
    Given que soy un usuario autenticado
    And proporciono un login ya registrado en otro usuario
    When envío una solicitud para actualizar mi perfil
    Then la respuesta debe ser: "solicitud rechazada"
    And debería ver un mensaje de error "El nombre de usuario ya está en uso"


  Scenario: Actualizacion de un usuario no existente
    Given que soy un usuario con permisos de administrador autenticado
    And proporciono los datos para actualizar un usuario ya eliminado
    When envío una solicitud para actualizar el perfil
    Then la respuesta debe ser: "solicitud no encontrada"
    And debería ver un mensaje de error "El usuario ya no existe en el sistema"

