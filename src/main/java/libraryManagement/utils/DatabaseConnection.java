package libraryManagement.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
   private static final String url = "jdbc:postgresql://localhost:5432/library_management";
   private static final String user = "oluchiiii";
   private static final String password = "password";

   public static Connection getConnection(){
       try{
           return DriverManager.getConnection(url, user, password);
       } catch (SQLException e){
           System.out.println("Error connecting to PostgreSQL: " + e.getMessage());
           return null;
       }
   }

    public static void main(String[] args){
       //using try-with-resources to ensure the connection is closing automatically

        try(Connection conn = getConnection()){
            if (conn != null){
                System.out.println("Connected to postgreSQL successfully");
            }
        }catch(SQLException e){
            System.out.println("Error: " + e.getMessage());
        }
    }
}
