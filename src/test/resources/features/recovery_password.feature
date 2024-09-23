Feature: Restablecimiento de contraseña del usuario
  Se requiere restablecer la contraseña del usuario mediante un proceso de solicitud y finalización.

  Scenario: Solicitud de restablecimiento de contraseña exitosa
    Given que soy un usuario registrado
    And tengo un correo electrónico asociado a mi cuenta
    When envío una solicitud para restablecer la contraseña a mi correo
    Then la respuesta debe ser "solicitud de restablecimiento de contraseña enviada"
    And debería recibir un correo con instrucciones para restablecer la contraseña

  Scenario: Solicitud de restablecimiento de contraseña para un correo no registrado
    Given que soy un usuario no registrado
    And tengo un correo electrónico que no está asociado a ninguna cuenta
    When envío una solicitud para restablecer la contraseña a mi correo
    Then la respuesta debe ser "solicitud procesada"
    And no debería recibir un correo con instrucciones

  Scenario: Finalización del restablecimiento de contraseña exitosa
    Given que he recibido un correo con un enlace para restablecer la contraseña
    And tengo una nueva contraseña válida
    And tengo la clave de restablecimiento proporcionada en el correo
    When envío la nueva contraseña y la clave para finalizar el restablecimiento
    Then la respuesta debe ser "contraseña restablecida exitosamente"
    And la nueva contraseña debe ser válida para futuros inicios de sesión

  Scenario: Finalización del restablecimiento de contraseña con clave inválida
    Given que tengo una nueva contraseña válida
    And tengo una clave de restablecimiento inválida
    When envío la nueva contraseña y la clave para finalizar el restablecimiento
    Then la respuesta debe ser "solicitud rechazada"
    And debería ver un mensaje de error "No se encontró el usuario para esta clave de restablecimiento"

  Scenario: Finalización del restablecimiento de contraseña con una nueva contraseña inválida
    Given que tengo una clave de restablecimiento válida
    And tengo una nueva contraseña que no cumple con los requisitos
    When envío la nueva contraseña y la clave para finalizar el restablecimiento
    Then la respuesta debe ser "solicitud rechazada"
    And debería ver un mensaje de error "La nueva contraseña no es válida"
