package libraryManagement.tests;

import libraryManagement.services.BookService;
import libraryManagement.daos.BookDAO;
import libraryManagement.daos.BookDAOImpl;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class BookServiceTest {
    public static void main(String[] args) {
        String url = "jdbc:postgresql://localhost:5432/library_management";
        String user = "oluchiiii";
        String password = "password";

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            BookDAO bookDAO = new BookDAOImpl(conn);
            BookService bookService = new BookService(bookDAO);

            int bookId = 1;  // Use a valid book ID
            int memberId = 1; // Use a valid member ID

            // Borrow a book
            boolean borrowed = bookService.borrowBook(bookId, memberId);
            System.out.println("Book " + bookId + " borrowed by Member " + memberId + ": " + borrowed);

            // Return a book
            boolean returned = bookService.returnBook(bookId, memberId);
            System.out.println("Book " + bookId + " returned by Member " + memberId + ": " + returned);

            // Test exporting books
            String exportPath = "books_export.csv";
            bookService.exportBooksData(exportPath);
            System.out.println("Books exported to: " + exportPath);

        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
