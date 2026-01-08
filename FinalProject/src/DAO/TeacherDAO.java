package DAO;
import DatabaseConnection.DatabaseConnection;
import Model.Teacher;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TeacherDAO {

    // 1. CREATE (Insert)
    public static boolean insert(Teacher t) {
        String sql = "INSERT INTO Teachers (First_Name, Last_Name, Email, Phone, Hire_Date) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, t.getFirstName());
            ps.setString(2, t.getLastName());
            ps.setString(3, t.getEmail());
            ps.setString(4, t.getPhone());
            ps.setDate(5, t.getHireDate()); // Using java.sql.Date

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 2. READ (Search/List)
    public static List<Teacher> search(String keyword) {
        List<Teacher> list = new ArrayList<>();
        String sql = "SELECT * FROM Teachers WHERE First_Name LIKE ? OR Last_Name LIKE ? OR Email LIKE ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            String query = "%" + keyword + "%";
            ps.setString(1, query);
            ps.setString(2, query);
            ps.setString(3, query);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new Teacher(
                        rs.getInt("Teacher_ID"),
                        rs.getString("First_Name"),
                        rs.getString("Last_Name"),
                        rs.getString("Email"),
                        rs.getString("Phone"),
                        rs.getDate("Hire_Date")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // 3. UPDATE (Edit)
    public static boolean update(Teacher t) {
        String sql = "UPDATE Teachers SET First_Name=?, Last_Name=?, Email=?, Phone=?, Hire_Date=? WHERE Teacher_ID=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, t.getFirstName());
            ps.setString(2, t.getLastName());
            ps.setString(3, t.getEmail());
            ps.setString(4, t.getPhone());
            ps.setDate(5, t.getHireDate());
            ps.setInt(6, t.getTeacherId());

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 4. DELETE
    public static boolean delete(int id) {
        String sql = "DELETE FROM Teachers WHERE Teacher_ID = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public static List<Integer> getAllTeacherIDs() {
        List<Integer> ids = new ArrayList<>();
        String sql = "SELECT Teacher_ID FROM Teachers ORDER BY Teacher_ID ASC";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                ids.add(rs.getInt("Teacher_ID"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ids;
    }
}