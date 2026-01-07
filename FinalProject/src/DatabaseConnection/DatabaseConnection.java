package DatabaseConnection;/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author macbookpro2019
 */
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class DatabaseConnection {

    public static Connection getConnection() throws Exception{
        Properties connProp = new Properties();
        try (InputStream input = DatabaseConnection.class.getClassLoader().getResourceAsStream("resources/connection.properties")) {
            if (input == null) {
                throw new RuntimeException("Unable to find connection.properties");
            }
            connProp.load(input);
        }
        String url = connProp.getProperty("spring.datasource.url");
        String user = connProp.getProperty("spring.datasource.username");
        String pass = connProp.getProperty("spring.datasource.password");
        return DriverManager.getConnection(url, user, pass);
    }
}