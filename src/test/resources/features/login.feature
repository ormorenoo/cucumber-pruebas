Feature: Login de usuario
  Se requiere iniciar sesión

  Scenario: Inicio de sesión exitoso
    Given que soy un usuario registrado
    And ingreso un nombre de usuario y contraseña
    When envío las credenciales al sistema
    Then la respuesta debe ser: "sesión iniciada correctamente"
    And  debería obtener un token JWT
    And el token JWT debe ser válido

  Scenario: Inicio de sesión fallido con credenciales incorrectas
    Given que soy un usuario registrado
    And ingreso un nombre de usuario y contraseña
    And la contraseña es incorrecta
    When envío las credenciales al sistema
    Then la respuesta debe ser: "credenciales incorrectas"

  Scenario: Inicio de sesión fallido con un usuario no registrado
    Given que soy un usuario no registrado
    And ingreso un nombre de usuario y contraseña
    And el nombre de usuario no existe
    When envío las credenciales al sistema
    Then la respuesta debe ser: "credenciales incorrectas"
