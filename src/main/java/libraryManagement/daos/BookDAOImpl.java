package libraryManagement.daos;

import libraryManagement.models.Book;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookDAOImpl implements BookDAO {
    private Connection conn;

    public BookDAOImpl(Connection conn) {
        this.conn = conn;
    }


    //add book method
    @Override
    public int addBook(Book book) {
        String query = "INSERT INTO books (title, author, genre, available_copies) VALUES (?, ?, ?, ?) RETURNING book_id";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, book.getTitle());
            stmt.setString(2, book.getAuthor());
            stmt.setString(3, book.getGenre());
            stmt.setInt(4, book.getAvailableCopies());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                book.setBookId(rs.getInt("book_id"));
                return book.getBookId();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }


    //get book by id method
    @Override
    public Book getBookById(int bookId) {
        String query = "SELECT * FROM books WHERE book_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, bookId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Book(
                        rs.getInt("book_id"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getString("genre"),
                        rs.getInt("available_copies")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    //get book by title method
    @Override
    public Book getBookByTitle(String title) {
        String sql = "SELECT * FROM books WHERE LOWER(title) = LOWER(?) LIMIT 1";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, title.trim());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Book(
                        rs.getInt("book_id"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getString("genre"),
                        rs.getInt("available_copies")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }




    //get all books method
    @Override
    public List<Book> getAllBooks() {
        List<Book> books = new ArrayList<>();
        String query = "SELECT * FROM books ORDER BY title";
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                books.add(new Book(
                        rs.getInt("book_id"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getString("genre"),
                        rs.getInt("available_copies")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return books;
    }


    //update book method
    @Override
    public void updateBook(Book book) {
        String query = "UPDATE books SET title = ?, author = ?, genre = ?, available_copies = ? WHERE book_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, book.getTitle());
            stmt.setString(2, book.getAuthor());
            stmt.setString(3, book.getGenre());
            stmt.setInt(4, book.getAvailableCopies());
            stmt.setInt(5, book.getBookId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    //method to delete book
    @Override
    public void deleteBook(int bookId) {
        String query = "DELETE FROM books WHERE book_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, bookId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    //method to check availability of book
    @Override
    public boolean isBookAvailable(int bookId) {
        String query = "SELECT available_copies FROM books WHERE book_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, bookId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("available_copies") > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
