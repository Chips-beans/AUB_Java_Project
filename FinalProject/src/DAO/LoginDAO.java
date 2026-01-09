package DAO;

import DatabaseConnection.DatabaseConnection;
import Model.Login;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LoginDAO {
    public static List<Login> getAll() {
        List<Login> loginList = new ArrayList<>();
        // Ensure your SQL table 'Users' has a column named 'Role'
        String sql = "SELECT Username, Password_Hash, Role FROM Users";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String encryptedFromDB = rs.getString("Password_Hash");
                String roleFromDB = rs.getString("Role"); // Fetch role

                // Decrypt it back to original text
                String plainPassword = decrypt(encryptedFromDB);

                // Add the role to the model
                loginList.add(new Login(
                        rs.getString("Username"),
                        plainPassword,
                        roleFromDB
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return loginList;
    }

    public static String encrypt(String password) {
        if (password == null) return null;
        StringBuilder result = new StringBuilder();
        for (char c : password.toCharArray()) {
            result.append((char) (c + 4));
        }
        return result.toString();
    }

    public static String decrypt(String encryptedPassword) {
        if (encryptedPassword == null) return null;
        StringBuilder result = new StringBuilder();
        for (char c : encryptedPassword.toCharArray()) {
            result.append((char) (c - 4));
        }
        return result.toString();
    }
    // 1. ADD NEW USER
    public static boolean insert(Login user) {
        String sql = "INSERT INTO Users (Username, Password_Hash, Role) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, user.getUsername());
            // Encrypt the plain text password before saving
            ps.setString(2, encrypt(user.getPassword()));
            ps.setString(3, user.getRole());

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    // UPDATE EXISTING USER
    public static boolean update(Login user) {
        String sql = "UPDATE Users SET Password_Hash = ?, Role = ? WHERE Username = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            // We re-encrypt the new password before saving
            ps.setString(1, encrypt(user.getPassword()));
            ps.setString(2, user.getRole());
            ps.setString(3, user.getUsername());

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 2. DELETE USER
    public static boolean delete(String username) {
        String sql = "DELETE FROM Users WHERE Username = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
