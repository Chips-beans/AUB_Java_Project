package DAO;

import Model.Subject;
import DatabaseConnection.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SubjectDAO {
    public static boolean insert(Subject s) {
        String sql = "INSERT INTO Subjects (Subject_Code, Subject_Name) VALUES (?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, s.getSubjectCode());
            ps.setString(2, s.getSubjectName());

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 2. READ (Search/List)
    public static List<Subject> search(String keyword) {
        List<Subject> list = new ArrayList<>();
        String sql = "SELECT * FROM Subjects WHERE Subject_Name LIKE ? OR Subject_Code LIKE ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            String query = "%" + keyword + "%";
            ps.setString(1, query);
            ps.setString(2, query);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new Subject(
                        rs.getString("Subject_Code"),
                        rs.getString("Subject_Name"),
                        rs.getInt("Subject_ID")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // 3. UPDATE
    public static boolean update(Subject s) {
        String sql = "UPDATE Subjects SET Subject_Code = ?, Subject_Name = ? WHERE Subject_ID = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, s.getSubjectCode());
            ps.setString(2, s.getSubjectName());
            ps.setInt(3, s.getSubjectID());

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 4. DELETE
    public static boolean delete(int id) {
        String sql = "DELETE FROM Subjects WHERE Subject_ID = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public static List<Integer> getAllSubjectIDs() {
        List<Integer> ids = new ArrayList<>();
        // SQL query to select IDs from your Subjects table
        String sql = "SELECT Subject_ID FROM Subjects ORDER BY Subject_ID ASC";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                // Adding each Subject_ID to the list
                ids.add(rs.getInt("Subject_ID"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ids;
    }
}
