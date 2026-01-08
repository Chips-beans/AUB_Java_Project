package DAO;

import DatabaseConnection.DatabaseConnection;
import Model.ChangePassword;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import static DAO.LoginDAO.encrypt;

public class ChangePasswordDAO {
    public static boolean ChangePassword(ChangePassword Data) {
        // 1. Use a CallableStatement for Stored Procedures to prevent SQL Injection
        String sql = "{call dbo.ChangePassword(?, ?, ?)}";

        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {

            // 2. Encrypt the passwords before sending them to the database
            // This ensures you are comparing 'shifted' Unicode values in your DB
            String encryptedOld = encrypt(Data.getOldPassword());
            String encryptedNew = encrypt(Data.getNewPassword());

            // 3. Set parameters safely
            stmt.setString(1, Data.getUsername());
            stmt.setString(2, encryptedOld);
            stmt.setString(3, encryptedNew);

            // 4. Execute and check the result
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    // JDBC ResultSet indexes start at 1
                    return rs.getBoolean(1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
