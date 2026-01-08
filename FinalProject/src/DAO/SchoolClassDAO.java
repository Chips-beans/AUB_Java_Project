package DAO;

import Model.SchoolClass;
import DatabaseConnection.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SchoolClassDAO {

    // 1. CREATE
    public static boolean insert(SchoolClass sc) {
        String sql = "INSERT INTO Classes (Class_Name, Teacher_ID, Room_Number) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, sc.getClassName());
            ps.setInt(2, sc.getTeacherId());
            ps.setString(3, sc.getRoomNumber());

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 2. READ (Search/Load All)
    public static List<SchoolClass> search(String keyword) {
        List<SchoolClass> list = new ArrayList<>();
        String sql = "SELECT * FROM Classes WHERE Class_Name LIKE ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, "%" + keyword + "%");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(new SchoolClass(
                        rs.getInt("Class_ID"),
                        rs.getString("Class_Name"),
                        rs.getInt("Teacher_ID"),
                        rs.getString("Room_Number")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // 3. UPDATE
    public static boolean update(SchoolClass sc) {
        String sql = "UPDATE Classes SET Class_Name=?, Teacher_ID=?, Room_Number=? WHERE Class_ID=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, sc.getClassName());
            ps.setInt(2, sc.getTeacherId());
            ps.setString(3, sc.getRoomNumber());
            ps.setInt(4, sc.getClassId());

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 4. DELETE
    public static boolean delete(int id) {
        String sql = "DELETE FROM Classes WHERE Class_ID = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 5. HELPER: Get all IDs for Enrollment ComboBox
    public static List<Integer> getAllClassIDs() {
        List<Integer> ids = new ArrayList<>();
        String sql = "SELECT Class_ID FROM Classes ORDER BY Class_ID ASC";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                ids.add(rs.getInt("Class_ID"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ids;
    }
}