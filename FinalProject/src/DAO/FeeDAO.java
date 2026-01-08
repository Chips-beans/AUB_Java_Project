package DAO;

import Model.Fee;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import DatabaseConnection.DatabaseConnection;
public class FeeDAO {
    public static boolean insert(Fee f) {
        String sql = "INSERT INTO Fees (Student_ID, Amount_Due, Amount_Paid, Due_Date, Status) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, f.getStudentId());
            ps.setDouble(2, f.getAmountDue());
            ps.setDouble(3, f.getAmountPaid());
            ps.setDate(4, f.getDueDate());
            ps.setString(5, f.getStatus());

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 2. READ (Search by Student ID)
    public static List<Fee> search(String studentIdKeyword) {
        List<Fee> list = new ArrayList<>();
        String sql = "SELECT * FROM Fees WHERE CAST(Student_ID AS VARCHAR) LIKE ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, "%" + studentIdKeyword + "%");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(new Fee(
                        rs.getInt("Fee_ID"),
                        rs.getInt("Student_ID"),
                        rs.getDouble("Amount_Due"),
                        rs.getDouble("Amount_Paid"),
                        rs.getDate("Due_Date"),
                        rs.getString("Status")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // 3. UPDATE
    public static boolean update(Fee f) {
        String sql = "UPDATE Fees SET Student_ID=?, Amount_Due=?, Amount_Paid=?, Due_Date=?, Status=? WHERE Fee_ID=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, f.getStudentId());
            ps.setDouble(2, f.getAmountDue());
            ps.setDouble(3, f.getAmountPaid());
            ps.setDate(4, f.getDueDate());
            ps.setString(5, f.getStatus());
            ps.setInt(6, f.getFeeId());

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 4. DELETE
    public static boolean delete(int id) {
        String sql = "DELETE FROM Fees WHERE Fee_ID = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
