package libraryManagement.services;

import libraryManagement.daos.BookDAO;
import libraryManagement.models.Book;
import libraryManagement.utils.LogTracker;
import libraryManagement.utils.DataExporter;
import java.util.*;

public class BookService {
    private BookDAO bookDAO;
    private List<Book> cachedBooks;
    private Map<Integer, Integer> borrowedBooks; // Tracks bookId -> memberId

    public BookService(BookDAO bookDAO) {
        this.bookDAO = bookDAO;
        this.cachedBooks = new ArrayList<>();
        this.borrowedBooks = new HashMap<>();
        refreshCache();
    }

    private void refreshCache() {
        cachedBooks = bookDAO.getAllBooks();
    }

    public void addBook(Book book) {
        bookDAO.addBook(book);
        refreshCache();
        LogTracker.log("Added new book: " + book.getTitle());
    }

    public boolean borrowBook(int bookId, int memberId) {
        Book book = bookDAO.getBookById(bookId);
        if (book == null || book.getAvailableCopies() <= 0) {
            LogTracker.log("Cannot borrow: Book unavailable.");
            return false;
        }
        book.setAvailableCopies(book.getAvailableCopies() - 1);
        bookDAO.updateBook(book);
        refreshCache();
        borrowedBooks.put(bookId, memberId);
        LogTracker.log("Book borrowed: " + book.getTitle() + " by Member " + memberId);
        return true;
    }



    public boolean borrowBookByTitle(String bookTitle, int memberId) {
        Book book = bookDAO.getBookByTitle(bookTitle);

        if (book == null) {
            LogTracker.log("Attempted to borrow non-existent book: " + bookTitle);
            System.out.println("Book not found.");
            return false;
        }

        boolean success = borrowBook(book.getBookId(), memberId);

        if (success) {
            LogTracker.log("Member " + memberId + " borrowed book: " + book.getTitle());
        } else {
            LogTracker.log("Failed to borrow book: " + book.getTitle());
        }

        return success;
    }



    public Book getBookById(int bookId) {
        return bookDAO.getBookById(bookId);
    }

    public Book getBookByTitle(String title) {
        List<Book> books = bookDAO.getAllBooks();
        for (Book book : books) {
            if (book.getTitle().equalsIgnoreCase(title)) { // Case-insensitive match
                return book;
            }
        }
        return null; // Book not found
    }



    public void deleteBook(int bookId) {
        bookDAO.deleteBook(bookId);
        refreshCache();
        LogTracker.log("Book deleted: ID " + bookId);
    }


    public List<Book> getAllBooks() {
        return bookDAO.getAllBooks();
    }


    public boolean returnBook(int bookId, int memberId) {
        if (!borrowedBooks.containsKey(bookId)) {
            LogTracker.log("Cannot return: Book not found in borrowed list.");
            return false;
        }
        borrowedBooks.remove(bookId);
        Book book = bookDAO.getBookById(bookId);
        book.setAvailableCopies(book.getAvailableCopies() + 1);
        bookDAO.updateBook(book);
        refreshCache();
        LogTracker.log("Book returned: " + book.getTitle() + " by Member " + memberId);
        return true;
    }

    public void exportBooksData(String filePath) {
        DataExporter.exportBooksToCSV(cachedBooks, filePath);
        LogTracker.log("Exported book data to: " + filePath);
    }
}