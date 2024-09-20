Feature: Creación de usuario sin autenticación
  Se requiere registrar un usuario no existente

  Scenario: Creación exitosa de un nuevo usuario
    Given que soy un usuario sin registrar
    When envío una solicitud para crear un usuario con todos los datos necesarios
    Then la respuesta debe tener el código de estado 201
    And el usuario debe existir en el sistema

  Scenario: Creación de un usuario con un email ya existente
    Given que soy un usuario sin registrar
    When envío una solicitud para crear un usuario con los datos necesarios y un email existente
    Then la respuesta debe tener el código de estado 400
    And debería ver un mensaje de error "El correo ya está en uso"

  Scenario: Creación de un usuario con un login ya existente
    Given que soy un usuario sin registrar
    When envío una solicitud para crear un usuario con los datos necesarios y un login existente
    Then la respuesta debe tener el código de estado 400
    And debería ver un mensaje de error "El correo ya está en uso"
    
