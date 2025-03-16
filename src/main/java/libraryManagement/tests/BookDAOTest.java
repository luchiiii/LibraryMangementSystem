package libraryManagement.tests;

import libraryManagement.daos.BookDAO;
import libraryManagement.daos.BookDAOImpl;
import libraryManagement.models.Book;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

public class BookDAOTest {
    public static void main(String[] args){
        String url = "jdbc:postgresql://localhost:5432/library_management";
        String user = "oluchiiii";
        String password = "password";

        try(Connection conn = DriverManager.getConnection(url, user, password)){
            BookDAO bookDAO = new BookDAOImpl(conn);

            //Add a book
            Book newBook = new Book("The Great Gatsby", "F. Scott Fitzgerald", "Fiction", 5);
            bookDAO.addBook(newBook);
            System.out.println("Added: " + newBook);

            //Get all books
            List<Book> books = bookDAO.getAllBooks();
            System.out.println("\nAll Books:");
            for (Book book:books){
                System.out.println(book);
            }

            //Get book by id
            Book fetchedBook = bookDAO.getBookById(newBook.getBookId());
            System.out.println("\nFetched Book: " + fetchedBook);

            //update book
            fetchedBook.setAvailableCopies(10);
            bookDAO.updateBook(fetchedBook);
            System.out.println("\nUpdated Book: " + bookDAO.getBookById(newBook.getBookId()));

            //delete the book
//            bookDAO.deleteBook(newBook.getBookId());
//            System.out.println("\nAfter Deletion: " + bookDAO.getBookById(newBook.getBookId()));

        }catch (SQLException e){
            System.err.println("Database error: " + e.getMessage());
            e.printStackTrace();
        }
    }


}
