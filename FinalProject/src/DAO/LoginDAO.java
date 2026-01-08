package DAO;

import DatabaseConnection.DatabaseConnection;
import Model.Login;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LoginDAO {
    public static List<Login> getAll() {
        List<Login> loginList = new ArrayList<>();
        String sql = "SELECT * FROM Users";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                // 1. Get the encrypted string from the database
                String encryptedFromDB = rs.getString("Password_Hash");

                // 2. Decrypt it back to original text
                String plainPassword = decrypt(encryptedFromDB);

                // 3. Store the decrypted version in your Model
                loginList.add(new Login(
                        rs.getString("Username"),
                        plainPassword
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

}
