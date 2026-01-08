package DAO;
import DatabaseConnection.DatabaseConnection;
import Model.Student;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentDAO {
    // --- INSERT (CREATE) ---
    public static boolean insert(Student s) {
        String sql = "INSERT INTO Students (First_Name, Last_Name, DOB, Gender, Address, Guardian_Contact) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, s.getFirstName());
            ps.setString(2, s.getLastName());
            ps.setDate(3, new java.sql.Date(s.getDob().getTime()));
            ps.setString(4, s.getGender());
            ps.setString(5, s.getAddress());
            ps.setString(6, s.getGuardianContact());
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); return false; }
    }

    // --- UPDATE (EDIT) ---
    public static boolean update(Student s) {
        String sql = "UPDATE Students SET First_Name=?, Last_Name=?, DOB=?, Gender=?, Address=?, Guardian_Contact=? WHERE Student_ID=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, s.getFirstName());
            ps.setString(2, s.getLastName());
            ps.setDate(3, new java.sql.Date(s.getDob().getTime()));
            ps.setString(4, s.getGender());
            ps.setString(5, s.getAddress());
            ps.setString(6, s.getGuardianContact());
            ps.setInt(7, s.getStudentId());
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); return false; }
    }

    // --- DELETE ---
    public static boolean delete(int id) {
        String sql = "DELETE FROM Students WHERE Student_ID = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); return false; }
    }

    // --- SEARCH & READ ---
    public static List<Student> search(String keyword) {
        List<Student> list = new ArrayList<>();
        // Searches by First Name, Last Name, or ID
        String sql = "SELECT * FROM Students WHERE First_Name LIKE ? OR Last_Name LIKE ? OR Student_ID LIKE ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            String query = "%" + keyword + "%";
            ps.setString(1, query);
            ps.setString(2, query);
            ps.setString(3, query);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new Student(
                        rs.getInt("Student_ID"),
                        rs.getString("First_Name"),
                        rs.getString("Last_Name"),
                        rs.getDate("DOB"),
                        rs.getString("Gender"),
                        rs.getString("Address"),
                        rs.getString("Guardian_Contact"),
                        rs.getDate("Admission_Date")
                ));
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }
    // Add this inside StudentDAO.java
    public static List<Integer> getAllStudentIDs() {
        List<Integer> ids = new ArrayList<>();
        String sql = "SELECT Student_ID FROM Students"; // Matches your schema
        try (Connection conn = DatabaseConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                ids.add(rs.getInt("Student_ID"));
            }
        } catch (Exception e) { e.printStackTrace(); }
        return ids;
    }

}
