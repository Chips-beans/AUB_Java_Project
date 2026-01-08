package Model;

public class ChangePassword {
    String username, OldPassword, NewPassword;

    public ChangePassword(String username, String oldPassword, String newPassword) {
        this.username = username;
        OldPassword = oldPassword;
        NewPassword = newPassword;
    }

    public String getUsername() {
        return username;
    }

    public String getOldPassword() {
        return OldPassword;
    }

    public String getNewPassword() {
        return NewPassword;
    }
}
