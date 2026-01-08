package DAO;

import Model.Attendance;
import DatabaseConnection.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AttendanceDAO {

    // 1. CREATE (Insert)
    public static boolean insert(Attendance a) {
        String sql = "INSERT INTO Attendance (Student_ID, Class_ID, Attendance_Date, Status) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, a.getStudentId());
            ps.setInt(2, a.getClassId());
            ps.setDate(3, a.getAttendanceDate());
            ps.setString(4, a.getStatus());

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 2. READ (Search all or by Student ID)
    public static List<Attendance> searchByStudent(int studentId) {
        List<Attendance> list = new ArrayList<>();
        // If studentId is 0, it selects all records
        String sql = (studentId == 0) ? "SELECT * FROM Attendance" : "SELECT * FROM Attendance WHERE Student_ID = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            if (studentId != 0) {
                ps.setInt(1, studentId);
            }

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new Attendance(
                        rs.getInt("Attendance_ID"),
                        rs.getInt("Student_ID"),
                        rs.getInt("Class_ID"),
                        rs.getDate("Attendance_Date"),
                        rs.getString("Status")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // 3. UPDATE
    public static boolean update(Attendance a) {
        String sql = "UPDATE Attendance SET Student_ID=?, Class_ID=?, Attendance_Date=?, Status=? WHERE Attendance_ID=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, a.getStudentId());
            ps.setInt(2, a.getClassId());
            ps.setDate(3, a.getAttendanceDate());
            ps.setString(4, a.getStatus());
            ps.setInt(5, a.getAttendanceId());

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 4. DELETE
    public static boolean delete(int id) {
        String sql = "DELETE FROM Attendance WHERE Attendance_ID = ?";
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