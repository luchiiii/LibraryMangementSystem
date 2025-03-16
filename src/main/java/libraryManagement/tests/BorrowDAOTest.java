package libraryManagement.tests;

import libraryManagement.daos.BookDAO;
import libraryManagement.daos.BookDAOImpl;
import libraryManagement.daos.BorrowRecordDAO;
import libraryManagement.daos.BorrowRecordDAOImpl;
import libraryManagement.models.Book;
import libraryManagement.models.BorrowRecord;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

public class BorrowDAOTest {
    public static void main(String[] args) {
        String url = "jdbc:postgresql://localhost:5432/library_management";
        String user = "oluchiiii";
        String password = "password";

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            BorrowRecordDAO borrowRecordDAO = new BorrowRecordDAOImpl(conn);
            BookDAO bookDAO = new BookDAOImpl(conn);

            List<Book> books = bookDAO.getAllBooks();
            if (books.isEmpty()) {
                System.out.println("No books found. Creating a test book...");
                Book testBook = new Book("Test Book", "Test Author", "Test Genre", 5);
                bookDAO.addBook(testBook);
                books = bookDAO.getAllBooks();
            }

            int bookId = books.get(0).getBookId();
            int memberId = 12;  // Use a valid member ID
            System.out.println("Testing with Book ID: " + bookId + " and Member ID: " + memberId);

            BorrowRecord newRecord = new BorrowRecord(bookId, memberId, new Date(System.currentTimeMillis()), null);
            int recordId = borrowRecordDAO.addBorrowRecord(newRecord);
            System.out.println("Added Borrow Record ID: " + recordId);

            if (recordId > 0) {
                System.out.println("\nFetched Borrow Record: " + borrowRecordDAO.getBorrowRecordById(recordId));
                System.out.println("\nActive Borrow Records:");
                borrowRecordDAO.getActiveBorrowRecords().forEach(System.out::println);
                System.out.println("\nIs Book Borrowed: " + borrowRecordDAO.isBookBorrowed(bookId));
                System.out.println("\nBorrowed Books Count: " + borrowRecordDAO.getBorrowedBooksCountByMember(memberId));

                boolean returned = borrowRecordDAO.returnBook(bookId, memberId);
                System.out.println("\nBook Returned: " + returned);
                System.out.println("\nIs Book Still Borrowed: " + borrowRecordDAO.isBookBorrowed(bookId));

                boolean deleted = borrowRecordDAO.deleteBorrowRecord(recordId);
                System.out.println("\nDeleted test borrow record: " + deleted);
            }
        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
