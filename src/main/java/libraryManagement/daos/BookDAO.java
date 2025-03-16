package libraryManagement.daos;

import libraryManagement.models.Book;
import java.util.List;

public interface BookDAO {
    int addBook(Book book);
    Book getBookById(int bookId);
    List<Book> getAllBooks();
    void updateBook(Book book);
    void deleteBook(int bookId);
    boolean isBookAvailable(int bookId);
    Book getBookByTitle(String title);
}