package libraryManagement.daos;

import libraryManagement.models.BorrowRecord;
import libraryManagement.utils.LogTracker;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BorrowRecordDAOImpl implements BorrowRecordDAO {
    private Connection connection;

    public BorrowRecordDAOImpl(Connection connection) {
        this.connection = connection;
    }


    //method to add borrowed record
    @Override
    public int addBorrowRecord(BorrowRecord record) {
        String sql = "INSERT INTO borrow_records (book_id, member_id, borrow_date) VALUES (?, ?, ?) RETURNING record_id";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, record.getBookId());
            stmt.setInt(2, record.getMemberId());
            stmt.setDate(3, record.getBorrowDate() != null ? record.getBorrowDate() : new Date(System.currentTimeMillis()));
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                LogTracker.log("Book borrowed: " + record.getBookId() + " by Member " + record.getMemberId());
                return rs.getInt("record_id");
            }
        } catch (SQLException e) {
            LogTracker.log("Error adding borrow record: " + e.getMessage());
            e.printStackTrace();
        }
        return -1;
    }


    //method to update borrow record
    @Override
    public boolean updateBorrowRecord(BorrowRecord record) {
        String sql = "UPDATE borrow_records SET book_id = ?, member_id = ?, borrow_date = ?, return_date = ? WHERE record_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, record.getBookId());
            stmt.setInt(2, record.getMemberId());
            stmt.setDate(3, record.getBorrowDate());
            stmt.setDate(4, record.getReturnDate() != null ? record.getReturnDate() : null);
            stmt.setInt(5, record.getRecordId());
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                LogTracker.log("Borrow record updated: " + record.getRecordId());
                return true;
            }
        } catch (SQLException e) {
            LogTracker.log("Error updating borrow record: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }


    //method for returning book
    @Override
    public boolean returnBook(int bookId, int memberId) {
        String sql = "UPDATE borrow_records SET return_date = CURRENT_DATE WHERE book_id = ? AND member_id = ? AND return_date IS NULL";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, bookId);
            stmt.setInt(2, memberId);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                LogTracker.log("Book returned: " + bookId + " by Member " + memberId);
                return true;
            }
        } catch (SQLException e) {
            LogTracker.log("Error returning book: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }


    //method to get number of books borrowed by a member using member id
    @Override
    public int getBorrowedBooksCountByMember(int memberId) {
        String sql = "SELECT COUNT(*) FROM borrow_records WHERE member_id = ? AND return_date IS NULL";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, memberId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            LogTracker.log("Error counting borrowed books for member: " + e.getMessage());
            e.printStackTrace();
        }
        return 0;
    }


    //method to get all the borrowed books
    @Override
    public List<BorrowRecord> getAllBorrowedRecords() {
        List<BorrowRecord> records = new ArrayList<>();
        String sql = "SELECT * FROM borrow_records WHERE return_date IS NULL";
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                records.add(extractBorrowRecordFromResultSet(rs));
            }
        } catch (SQLException e) {
            LogTracker.log("Error retrieving all active borrow records: " + e.getMessage());
            e.printStackTrace();
        }
        return records;
    }


    //method to get the record of borrowed books by bookid
    @Override
    public List<BorrowRecord> getBorrowRecordsByBook(int bookId) {
        List<BorrowRecord> records = new ArrayList<>();
        String sql = "SELECT * FROM borrow_records WHERE book_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, bookId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                records.add(extractBorrowRecordFromResultSet(rs));
            }
        } catch (SQLException e) {
            LogTracker.log("Error retrieving borrow records for book: " + e.getMessage());
            e.printStackTrace();
        }
        return records;
    }

    @Override
    public List<BorrowRecord> getBorrowRecordsByMember(int memberId) {
        List<BorrowRecord> records = new ArrayList<>();
        String sql = "SELECT * FROM borrow_records WHERE member_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, memberId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                records.add(extractBorrowRecordFromResultSet(rs));
            }
        } catch (SQLException e) {
            LogTracker.log("Error retrieving borrow records for member: " + e.getMessage());
            e.printStackTrace();
        }
        return records;
    }

    @Override
    public BorrowRecord getBorrowRecordById(int recordId) {
        String sql = "SELECT * FROM borrow_records WHERE record_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, recordId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return extractBorrowRecordFromResultSet(rs);
            }
        } catch (SQLException e) {
            LogTracker.log("Error retrieving borrow record by ID: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean deleteBorrowRecord(int recordId) {
        String sql = "DELETE FROM borrow_records WHERE record_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, recordId);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                LogTracker.log("Borrow record deleted: " + recordId);
                return true;
            }
        } catch (SQLException e) {
            LogTracker.log("Error deleting borrow record: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<BorrowRecord> getActiveBorrowRecords() {
        List<BorrowRecord> records = new ArrayList<>();
        String sql = "SELECT * FROM borrow_records WHERE return_date IS NULL";
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                records.add(extractBorrowRecordFromResultSet(rs));
            }
        } catch (SQLException e) {
            LogTracker.log("Error retrieving active borrow records: " + e.getMessage());
            e.printStackTrace();
        }
        return records;
    }

    @Override
    public boolean isBookBorrowed(int bookId) {
        String sql = "SELECT COUNT(*) FROM borrow_records WHERE book_id = ? AND return_date IS NULL";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, bookId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            LogTracker.log("Error checking if book is borrowed: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean isBorrowedBookByMember(int memberId, int bookId) {
        String sql = "SELECT COUNT(*) FROM borrow_records WHERE member_id = ? AND book_id = ? AND return_date IS NULL";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, memberId);
            stmt.setInt(2, bookId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            LogTracker.log("Error checking if book is borrowed by member: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    private BorrowRecord extractBorrowRecordFromResultSet(ResultSet rs) throws SQLException {
        return new BorrowRecord(
                rs.getInt("record_id"),
                rs.getInt("book_id"),
                rs.getInt("member_id"),
                rs.getDate("borrow_date"),
                rs.getDate("return_date")
        );
    }
}
