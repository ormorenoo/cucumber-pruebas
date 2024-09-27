Feature: Creación de usuario sin autenticación
  Se requiere registrar un usuario no existente

  Scenario: Creación exitosa de un nuevo usuario
    Given que soy un usuario sin registrar
    And proporciono todos los datos solicitados para el registro
    When envío una solicitud para crear un usuario
    Then la respuesta del servicio de creacion debe ser: "creado exitosamente"

  Scenario: Creación de un usuario con un email ya existente
    Given que soy un usuario sin registrar
    And Tengo un email ya registrado
    And proporciono todos los datos solicitados para el registro
    When envío una solicitud para crear un usuario
    Then la respuesta del servicio de creacion debe ser: "solicitud rechazada"
    And debería ver un mensaje de error "El correo ya está en uso"

  Scenario: Creación de un usuario con un login ya existente
    Given que soy un usuario sin registrar
    And Tengo un login ya registrado
    And proporciono todos los datos solicitados para el registro
    When envío una solicitud para crear un usuario
    Then la respuesta del servicio de creacion debe ser: "solicitud rechazada"
    And debería ver un mensaje de error "El nombre de usuario ya está en uso"

