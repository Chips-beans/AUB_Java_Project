package DAO;
import DatabaseConnection.DatabaseConnection;

import Model.Enrollment;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EnrollmentDAO {

    // 1. CREATE
    public static boolean insert(Enrollment e) {
        String sql = "INSERT INTO Enrollments (Student_ID, Class_ID, Enrollment_Date, Status) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, e.getStudentId());
            ps.setInt(2, e.getClassId());
            ps.setDate(3, e.getEnrollmentDate());
            ps.setString(4, e.getStatus());

            return ps.executeUpdate() > 0;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    // 2. READ (Displays all if studentId is 0, otherwise filters)
    public static List<Enrollment> searchByStudent(int studentId) {
        List<Enrollment> list = new ArrayList<>();
        String sql = (studentId == 0) ? "SELECT * FROM Enrollments" : "SELECT * FROM Enrollments WHERE Student_ID = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            if (studentId != 0) {
                ps.setInt(1, studentId);
            }

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new Enrollment(
                        rs.getInt("Enrollment_ID"),
                        rs.getInt("Student_ID"),
                        rs.getInt("Class_ID"),
                        rs.getDate("Enrollment_Date"),
                        rs.getString("Status")
                ));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return list;
    }

    // 3. UPDATE
    public static boolean update(Enrollment e) {
        String sql = "UPDATE Enrollments SET Student_ID=?, Class_ID=?, Enrollment_Date=?, Status=? WHERE Enrollment_ID=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, e.getStudentId());
            ps.setInt(2, e.getClassId());
            ps.setDate(3, e.getEnrollmentDate());
            ps.setString(4, e.getStatus());
            ps.setInt(5, e.getEnrollmentId());

            return ps.executeUpdate() > 0;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    // 4. DELETE
    public static boolean delete(int id) {
        String sql = "DELETE FROM Enrollments WHERE Enrollment_ID = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }
}