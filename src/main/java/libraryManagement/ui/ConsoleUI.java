package libraryManagement.ui;

import libraryManagement.daos.*;
import libraryManagement.models.Book;
import libraryManagement.models.Member;
import libraryManagement.services.BookService;
import libraryManagement.services.BorrowService;
import libraryManagement.services.MemberService;
import libraryManagement.utils.DatabaseConnection;
import java.sql.Connection;
import java.util.List;
import java.util.Scanner;

public class ConsoleUI {
    private final Scanner scanner = new Scanner(System.in);
    private final BookService bookService;
    private final MemberService memberService;
    private final BorrowService borrowService;

    public ConsoleUI(BookService bookService, MemberService memberService, BorrowService borrowService) {
        this.bookService = bookService;
        this.memberService = memberService;
        this.borrowService = borrowService;
    }

    public void start() {
        while (true) {
            System.out.println("\n✨ Welcome to the Library Management System! ✨");
            System.out.println("1. Librarian 🗂");
            System.out.println("2. Library Member 📚");
            System.out.println("3. Register as a New Member 📝");
            System.out.println("4. Exit ⏹");
            System.out.print("Select your role: ");

            int roleChoice = getValidIntInput();

            switch (roleChoice) {
                case 1 -> librarianMenu();
                case 2 -> memberMenu();
                case 3 -> selfRegisterMember();  // Allow self-registration
                case 4 -> {
                    System.out.println("👋 Exiting the system...");
                    return;
                }
                default -> System.out.println("⚠ Invalid choice. Try again.");
            }
        }
    }

    private void selfRegisterMember() {
        System.out.println("\n📝 Register as a New Member:");

        scanner.nextLine(); // Fix scanner issue

        System.out.print("Enter your name: ");
        String name = scanner.nextLine();

        System.out.print("Enter your email: ");
        String email = scanner.nextLine();

        System.out.print("Enter your phone number: ");
        String phone = scanner.nextLine();

        Member newMember = new Member(name, email, phone);
        memberService.addMember(newMember);

        // Retrieve member to display their ID
        Member registeredMember = memberService.getMemberByEmail(email);

        if (registeredMember != null) {
            System.out.println("\n✅ Registration successful!");
            System.out.println("🆔 Your Member ID: " + registeredMember.getMemberId());
            System.out.println("🔑 Use this ID or your email to borrow books.");
        } else {
            System.out.println("❌ Registration failed. Please try again.");
        }
    }


    private void librarianMenu() {
        while (true) {
            System.out.println("\n\u2699 Librarian Menu:");
            System.out.println("1. Manage Books \ud83d\udcda");
            System.out.println("2. Manage Members \ud83d\udc65");
            System.out.println("3. Exit \u23F9");
            System.out.print("Enter choice: ");
            int choice = getValidIntInput();

            switch (choice) {
                case 1 -> manageBooks();
                case 2 -> manageMembers();
                case 3 -> {
                    System.out.println("Exiting Librarian Menu...\u2705");
                    return;
                }
                default -> System.out.println("\u26A0 Invalid option. Try again.");
            }
        }
    }

    private void memberMenu() {
        while (true) {
            System.out.println("\n\ud83d\udcda Member Menu:");
            System.out.println("1. Borrow a Book \ud83d\udcda");
            System.out.println("2. Return a Book \ud83d\uddd1");
            System.out.println("3. View Borrowed Books \ud83d\udcd6");
            System.out.println("4. Exit \u23F9");
            System.out.print("Enter choice: ");
            int choice = getValidIntInput();

            switch (choice) {
                case 1 -> borrowBook();
                case 2 -> returnBook();
                case 3 -> viewBorrowedBooks();
                case 4 -> {
                    System.out.println("\n👋 Thank you for using the Library System. Have a great day! ✅");
                    return;
                }

                default -> System.out.println("\u26A0 Invalid option. Try again.");
            }
        }
    }

    private void manageBooks() {
        while (true) {
            System.out.println("\n📚 Manage Books:");
            System.out.println("1. Add a Book ➕");
            System.out.println("2. View All Books 📖");
            System.out.println("3. Delete a Book 🗑");
            System.out.println("4. Go Back 🔙");
            System.out.print("Enter choice: ");

            int choice = getValidIntInput();

            switch (choice) {
                case 1 -> addBook();      // ✅ Add Book
                case 2 -> viewAllBooks(); // ✅ View All Books
                case 3 -> deleteBook();   // ✅ Delete Book
                case 4 -> {
                    System.out.println("\nReturning to Librarian Menu... ✅");
                    return;
                }
                default -> System.out.println("\u26A0 Invalid choice. Please try again.");
            }
        }
    }


    private void manageMembers() {
        while (true) {
            System.out.println("\n👥 Manage Members:");
            System.out.println("1. Add a Member ➕");
            System.out.println("2. View All Members 📋");
            System.out.println("3. Go Back 🔙");
            System.out.print("Enter choice: ");

            int choice = getValidIntInput();
            switch (choice) {
                case 1 -> addMember();
                case 2 -> viewAllMembers();
                case 3 -> {
                    System.out.println("Returning to Librarian Menu... 🔙");
                    return;
                }
                default -> System.out.println("⚠ Invalid option. Try again.");
            }
        }
    }

    private void addMember() {
        System.out.println("\n➕ Register a New Member:");

        scanner.nextLine();  // Fix scanner issue

        System.out.print("Enter member name: ");
        String name = scanner.nextLine();

        System.out.print("Enter email: ");
        String email = scanner.nextLine();

        System.out.print("Enter phone number: ");
        String phone = scanner.nextLine();

        Member newMember = new Member(name, email, phone);
        memberService.addMember(newMember);

        System.out.println("✅ Member added successfully: " + name);
        System.out.println("🆔 Your Member ID is: " + newMember.getMemberId());
    }

    private void viewAllMembers() {
        System.out.println("\n📋 Registered Members:");
        List<Member> members = memberService.getAllMembers();

        if (members.isEmpty()) {
            System.out.println("❌ No registered members.");
            return;
        }

        for (Member member : members) {
            System.out.println("🆔 " + member.getMemberId() + " | 👤 " + member.getName() + " | ✉ " + member.getEmail() + " | 📞 " + member.getPhone());
        }
    }


    private void addBook() {
        System.out.println("\n➕ Add a New Book:");

        scanner.nextLine();  // 🔴 FIX: Consume the leftover newline from previous input

        System.out.print("Enter book title: ");
        String title = scanner.nextLine();

        System.out.print("Enter author name: ");
        String author = scanner.nextLine();

        System.out.print("Enter genre: ");
        String genre = scanner.nextLine();

        System.out.print("Enter available copies: ");
        int availableCopies = getValidIntInput();

        Book newBook = new Book(title, author, genre, availableCopies);
        bookService.addBook(newBook);

        System.out.println("✅ Book added successfully: " + title + " by " + author);
    }



    private void viewAllBooks() {
        System.out.println("\n📚 Available Books:");
        List<Book> books = bookService.getAllBooks();

        if (books.isEmpty()) {
            System.out.println("❌ No books available.");
            return;
        }

        for (Book book : books) {
            System.out.println("🆔 " + book.getBookId() + " | 📖 " + book.getTitle() + " | ✍ " + book.getAuthor() + " | 📚 Copies: " + book.getAvailableCopies());
        }
    }



    private void deleteBook() {
        System.out.print("Enter the Book ID to delete: ");
        int bookId = getValidIntInput();

        // Check if the book exists
        Book book = bookService.getBookById(bookId);
        if (book == null) {
            System.out.println("\u26A0 Book not found. Please enter a valid ID.");
            return;
        }

        // Confirm deletion
        System.out.print("Are you sure you want to delete '" + book.getTitle() + "'? (yes/no): ");
        String confirmation = scanner.next().trim().toLowerCase();
        scanner.nextLine();  // Consume newline

        if (!confirmation.equals("yes")) {
            System.out.println("Deletion cancelled. ✅");
            return;
        }

        // Perform deletion
        bookService.deleteBook(bookId);
        System.out.println("\n🗑 Book '" + book.getTitle() + "' has been deleted successfully! ✅");
    }


    private void borrowBook() {
        System.out.println("\n📖 Borrow a Book:");

        System.out.print("Enter your Member ID: ");
        int memberId = getValidIntInput();
        scanner.nextLine();

        System.out.print("Enter the book title: ");
        String title = scanner.nextLine().trim();  // Ensure correct input

        if (title.isEmpty()) {
            System.out.println("❌ Book title cannot be empty.");
            return;
        }

        Book book = bookService.getBookByTitle(title);

        if (book == null) {
            System.out.println("❌ Book not found.");
            return;
        }

        boolean success = borrowService.borrowBookByTitle(title, memberId);

        if (success) {
            System.out.println("✅ Book borrowed successfully: " + book.getTitle());
        } else {
            System.out.println("❌ Unable to borrow. Check availability.");
        }
    }




    private void returnBook() {
        System.out.print("Enter Member ID: ");
        int memberId = getValidIntInput();
        System.out.print("Enter Book ID to return: ");
        int bookId = getValidIntInput();

        boolean success = borrowService.returnBook(memberId, bookId);
        if (success) {
            System.out.println("\u2705 Book returned successfully!");
        } else {
            System.out.println("\u26A0 Failed to return book. Check the details.");
        }
    }

    private void viewBorrowedBooks() {
        System.out.print("Enter Member ID: ");
        int memberId = getValidIntInput();
        List<Book> books = borrowService.getBorrowedBooksByMember(memberId);
        if (books.isEmpty()) {
            System.out.println("\u26A0 No borrowed books found.");
        } else {
            books.forEach(book -> System.out.println(book.getBookId() + " - " + book.getTitle()));
        }
    }

    private int getValidIntInput() {
        while (!scanner.hasNextInt()) {
            System.out.print("\u26A0 Invalid input. Enter a number: ");
            scanner.next();
        }
        return scanner.nextInt();
    }

    public static void main(String[] args) {
        Connection connection = DatabaseConnection.getConnection();

        BookDAO bookDAO = new BookDAOImpl(connection);
        BookService bookService = new BookService(bookDAO);

        MemberDAO memberDAO = new MemberDAOImpl(connection);
        MemberService memberService = new MemberService(memberDAO);

        BorrowRecordDAO borrowRecordDAO = new BorrowRecordDAOImpl(connection);
        BorrowService borrowService = new BorrowService(borrowRecordDAO, bookDAO, bookService, memberService);

        new ConsoleUI(bookService, memberService, borrowService).start();
    }
}
