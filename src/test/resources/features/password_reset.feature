Feature: Restablecimiento de contraseña
Como usuario registrado quiero poder restablecer mi contraseña

  Scenario: Solicitud exitosa de restablecimiento de contraseña
    Given que soy un usuario registrado con el email "usuario@ejemplo.com"
    When envío una solicitud para restablecer mi contraseña
    Then debería recibir un correo electrónico con las instrucciones para restablecer la contraseña

  Scenario: Solicitud de restablecimiento de contraseña con un email no registrado
    Given que soy un usuario con el email "noexiste@ejemplo.com"
    When envío una solicitud para restablecer mi contraseña
    Then el sistema debería registrar la solicitud como exitosa
    And debería registrar un intento fallido sin informar al usuario

  Scenario: Finalización exitosa del restablecimiento de contraseña
    Given que recibí un correo con un link para recuperar la contraseña
    And quiero restablecer mi contraseña
    When envío la solicitud para finalizar el restablecimiento de mi contraseña con la nueva contraseña
    Then la contraseña debe actualizarse correctamente

  Scenario: Intento fallido de restablecimiento de contraseña con una contraseña inválida
    Given que recibí un correo con un link para recuperar la contraseña
    And quiero restablecer mi contraseña
    When envío la solicitud para finalizar el restablecimiento de mi contraseña con la nueva contraseña
    Then debería recibir un mensaje de error "La contraseña no es válida"