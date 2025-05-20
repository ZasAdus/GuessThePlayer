package org.example;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database{
    private static Connection connection;

    public static void connect(){
        String url = "jdbc:mysql://localhost:3306";

        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(url, System.getenv("userDB"), System.getenv("passwordDB"));
            System.out.println("Connection established successfully!");
        }catch(ClassNotFoundException e){
            System.out.println("MySQL JDBC Driver not found.");
            e.printStackTrace();
        }catch(SQLException e){
            System.out.println("Connection failed!");
            e.printStackTrace();
        }
    }

    public static void disconnect(){
        try{
            if(connection != null && !connection.isClosed()){
                connection.close();
                System.out.println("Connection closed successfully!");
            }
        }catch(SQLException e){
            System.out.println("Error closing connection!");
            e.printStackTrace();
        }
    }
}


