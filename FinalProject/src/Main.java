import DatabaseConnection.DatabaseConnection;
import java.sql.Connection;

void main() {
    try(Connection conn = DatabaseConnection.getConnection()){
        if (conn != null){
            System.out.println("Success");
        }
    }catch (Exception e){
        e.printStackTrace();
    }
}
