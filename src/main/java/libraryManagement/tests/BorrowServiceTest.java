package libraryManagement.tests;

import libraryManagement.daos.*;
import libraryManagement.models.Book;
import libraryManagement.models.BorrowRecord;
import libraryManagement.services.BookService;
import libraryManagement.services.BorrowService;
import libraryManagement.services.MemberService;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

public class BorrowServiceTest {
    public static void main(String[] args) {
        String url = "jdbc:postgresql://localhost:5432/library_management";
        String user = "oluchiiii";
        String password = "password";

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            MemberDAO memberDAO = new MemberDAOImpl(conn);
            MemberService memberService = new MemberService(memberDAO);

            BookDAO bookDAO = new BookDAOImpl(conn);
            BorrowRecordDAO borrowRecordDAO = new BorrowRecordDAOImpl(conn);
            BookService bookService = new BookService(bookDAO);
//
            BorrowService borrowService = new BorrowService(borrowRecordDAO, bookDAO, bookService, memberService);

            List<Book> books = bookDAO.getAllBooks();
            if (books.isEmpty()) {
                System.out.println("No books available for testing. Please add books to the database.");
                return;
            }

            Book bookToBorrow = books.stream().filter(b -> b.getAvailableCopies() > 0).findFirst().orElse(null);
            if (bookToBorrow == null) {
                System.out.println("No books with available copies found. Please update available copies.");
                return;
            }

            int bookId = bookToBorrow.getBookId();
            int memberId = 1;

            System.out.println("Testing with Book: " + bookToBorrow.getTitle() + " (ID: " + bookId + ")");
            System.out.println("Available copies before borrowing: " + bookToBorrow.getAvailableCopies());

            boolean borrowed = borrowService.borrowBookByTitle(bookToBorrow.getTitle(), memberId);


            System.out.println("\nBook borrowed successfully: " + borrowed);
            System.out.println("Available copies after borrowing: " + bookDAO.getBookById(bookId).getAvailableCopies());

            System.out.println("\nActive Borrow Records:");
            borrowService.getActiveBorrowRecords().forEach(System.out::println);

            System.out.println("\nBorrow Records for Member " + memberId + ":");
            borrowService.getBorrowRecordsByMember(memberId).forEach(System.out::println);

            System.out.println("\nBorrow Records for Book " + bookId + ":");
            borrowService.getBorrowRecordsByBook(bookId).forEach(System.out::println);

            System.out.println("\nNumber of Books Borrowed by Member " + memberId + ": " + borrowService.getBorrowedBooksCount(memberId));

            boolean returned = borrowService.returnBook(bookId, memberId);
            System.out.println("\nBook returned successfully: " + returned);
            System.out.println("Available copies after returning: " + bookDAO.getBookById(bookId).getAvailableCopies());

            System.out.println("\nTesting Error Cases:");
            System.out.println("Borrowed non-existent book: " + borrowService.borrowBook(9999, memberId));
            System.out.println("Returned book not borrowed by member: " + borrowService.returnBook(bookId, 9999));

        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}