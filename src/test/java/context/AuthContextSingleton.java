package context;

public class AuthContextSingleton {
    private static AuthContextSingleton instance;
    private String token;

    private AuthContextSingleton() {
        // Constructor privado para evitar la creación de nuevas instancias
    }

    public static synchronized AuthContextSingleton getInstance() {
        if (instance == null) {
            instance = new AuthContextSingleton();
        }
        return instance;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
