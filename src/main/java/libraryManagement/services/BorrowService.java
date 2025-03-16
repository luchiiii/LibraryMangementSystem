package libraryManagement.services;

import libraryManagement.daos.BorrowRecordDAO;
import libraryManagement.daos.BookDAO;
import libraryManagement.models.BorrowRecord;
import libraryManagement.models.Book;
import libraryManagement.utils.LogTracker;

import java.sql.Date;
import java.util.*;

public class BorrowService {
    private BorrowRecordDAO borrowRecordDAO;
    private BookDAO bookDAO;
    private BookService bookService;
    private MemberService memberService;
    private Map<Integer, List<BorrowRecord>> memberBorrowRecords;
    private Map<Integer, List<BorrowRecord>> bookBorrowRecords;
    private Map<Integer, Integer> borrowedBooks = new HashMap<>();


    public BorrowService(BorrowRecordDAO borrowRecordDAO, BookDAO bookDAO, BookService bookService, MemberService memberService) {
        this.borrowRecordDAO = borrowRecordDAO;
        this.bookDAO = bookDAO;
        this.memberBorrowRecords = new HashMap<>();
        this.bookBorrowRecords = new HashMap<>();
        this.bookService = bookService;
        this.memberService = memberService;
    }

    private void refreshBookCache() {
        bookDAO.getAllBooks(); // This will ensure the latest books are fetched
    }


    // Borrow a book
    public boolean borrowBook(int bookId, int memberId) {
        Book book = bookDAO.getBookById(bookId);
        if (book == null || book.getAvailableCopies() <= 0) {
            LogTracker.log("Cannot borrow: Book unavailable.");
            return false;
        }

        book.setAvailableCopies(book.getAvailableCopies() - 1);
        bookDAO.updateBook(book);
        refreshBookCache();
        borrowedBooks.put(bookId, memberId);

        LogTracker.log("✅ Book borrowed: " + book.getTitle() + " by Member " + memberId);

        // Export updated book list after borrowing
        bookService.exportBooksData("books_export.csv");

        // Export updated member list after borrowing
        memberService.exportMembersData("members_export.csv");

        return true;
    }



    public boolean borrowBookById(int bookId, int memberId) {
        Book book = bookDAO.getBookById(bookId);
        if (book == null || book.getAvailableCopies() <= 0) {
            System.out.println(" Cannot borrow: Book unavailable.");
            return false;
        }

        BorrowRecord record = new BorrowRecord(bookId, memberId, new java.sql.Date(System.currentTimeMillis()), null);
        int recordId = borrowRecordDAO.addBorrowRecord(record);
        if (recordId > 0) {
            book.setAvailableCopies(book.getAvailableCopies() - 1);
            bookDAO.updateBook(book);
            System.out.println("Book '" + book.getTitle() + "' borrowed successfully by Member ID: " + memberId);
            return true;
        }
        return false;
    }

    public boolean borrowBookByTitle(String bookTitle, int memberId) {
        // Get book details using title
        Book book = bookDAO.getBookByTitle(bookTitle);

        if (book == null) {
            System.out.println(" Book not found: " + bookTitle);
            return false;
        }

        // Borrow the book using its ID (needed for database tracking)
        boolean success = borrowBook(book.getBookId(), memberId);

        if (success) {
            System.out.println("Book borrowed successfully: " + book.getTitle());
        } else {
            System.out.println("Unable to borrow. Check availability.");
        }

        return success;
    }




    // Return a book
    public boolean returnBook(int bookId, int memberId) {
        Book book = bookDAO.getBookById(bookId);
        if (book == null) {
            LogTracker.log("Cannot return: Book not found");
            return false;
        }

        boolean returned = borrowRecordDAO.returnBook(bookId, memberId);
        if (returned) {
            book.setAvailableCopies(book.getAvailableCopies() + 1);
            bookDAO.updateBook(book);
            LogTracker.log("Book returned successfully");
            removeBorrowRecord(bookId, memberId);
            return true;
        }
        return false;
    }

    // Get active borrow records
    public List<BorrowRecord> getActiveBorrowRecords() {
        return borrowRecordDAO.getAllBorrowedRecords();
    }

    // Get borrow records for a specific member
    public List<BorrowRecord> getBorrowRecordsByMember(int memberId) {
        return memberBorrowRecords.getOrDefault(memberId, borrowRecordDAO.getBorrowRecordsByMember(memberId));
    }

    // Get borrow records for a specific book
    public List<BorrowRecord> getBorrowRecordsByBook(int bookId) {
        return bookBorrowRecords.getOrDefault(bookId, borrowRecordDAO.getBorrowRecordsByBook(bookId));
    }

    // Get count of books borrowed by a member
    public int getBorrowedBooksCount(int memberId) {
        return getBorrowRecordsByMember(memberId).size();
    }

    //Get borrowed books by member
    public List<Book> getBorrowedBooksByMember(int memberId) {
        List<BorrowRecord> records = borrowRecordDAO.getBorrowRecordsByMember(memberId);
        List<Book> books = new ArrayList<>();

        for (BorrowRecord record : records) {
            if (record.getReturnDate() == null) { // Only include books not returned
                Book book = bookDAO.getBookById(record.getBookId());
                if (book != null) {
                    books.add(book);
                }
            }
        }
        return books;
    }


    // Cache borrow record in maps
    private void cacheBorrowRecord(BorrowRecord record) {
        memberBorrowRecords.computeIfAbsent(record.getMemberId(), k -> new ArrayList<>()).add(record);
        bookBorrowRecords.computeIfAbsent(record.getBookId(), k -> new ArrayList<>()).add(record);
    }

    // Remove borrow record from maps on return
    private void removeBorrowRecord(int bookId, int memberId) {
        memberBorrowRecords.getOrDefault(memberId, new ArrayList<>()).removeIf(r -> r.getBookId() == bookId);
        bookBorrowRecords.getOrDefault(bookId, new ArrayList<>()).removeIf(r -> r.getMemberId() == memberId);
    }
}
