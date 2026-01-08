package DAO;

import Model.Grade;
import DatabaseConnection.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class GradeDAO {
    // 1. CREATE (Insert)
    public static boolean insert(Grade g) {
        String sql = "INSERT INTO Grades (Student_ID, Subject_ID, Marks_Obtained, Exam_Date, Exam_Type) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, g.getStudentId());
            ps.setInt(2, g.getSubjectId());
            ps.setDouble(3, g.getMarksObtained());
            ps.setDate(4, g.getExamDate());
            ps.setString(5, g.getExamType());

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 2. READ (Search by Student ID)
    public static List<Grade> searchByStudent(int studentId) {
        List<Grade> list = new ArrayList<>();
        String sql;

        // If ID is 0, we want everything. Otherwise, we filter.
        if (studentId == 0) {
            sql = "SELECT * FROM Grades";
        } else {
            sql = "SELECT * FROM Grades WHERE Student_ID = ?";
        }

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            // Only set the parameter if we are actually filtering
            if (studentId != 0) {
                ps.setInt(1, studentId);
            }

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(new Grade(
                        rs.getInt("Grade_ID"),       //
                        rs.getInt("Student_ID"),     //
                        rs.getInt("Subject_ID"),     //
                        rs.getDouble("Marks_Obtained"), //
                        rs.getDate("Exam_Date"),     //
                        rs.getString("Exam_Type")    //
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // 3. UPDATE
    public static boolean update(Grade g) {
        String sql = "UPDATE Grades SET Student_ID=?, Subject_ID=?, Marks_Obtained=?, Exam_Date=?, Exam_Type=? WHERE Grade_ID=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, g.getStudentId());
            ps.setInt(2, g.getSubjectId());
            ps.setDouble(3, g.getMarksObtained());
            ps.setDate(4, g.getExamDate());
            ps.setString(5, g.getExamType());
            ps.setInt(6, g.getGradeId());

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 4. DELETE
    public static boolean delete(int id) {
        String sql = "DELETE FROM Grades WHERE Grade_ID = ?";
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
