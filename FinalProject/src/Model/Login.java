package Model;

public class Login {
    String Username, Password;

    public String getUsername() {
        return Username;
    }

    public String getPassword() {
        return Password;
    }

    public Login(String username, String password) {
        Username = username;
        Password = password;
    }
}
