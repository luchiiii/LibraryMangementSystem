package libraryManagement.utilTests;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TestDatabaseConnection {

    private static final String URL = "jdbc:postgresql://localhost:5432/test_library_management";
    private static final String USER = "oluchiiii";
    private static final String PASSWORD = "password";

    @Test
    void testConnection() {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            assertNotNull(connection, "Database connection should be established.");
        } catch (SQLException e) {
            fail("Database connection failed: " + e.getMessage());
        }
    }
}
