package libraryManagement.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class TestDatabaseConnection {
    private static final String url = "jdbc:postgresql://localhost:5432/test_library_management";
    private static final String user = "oluchiiii";
    private static final String password = "password";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }
}
