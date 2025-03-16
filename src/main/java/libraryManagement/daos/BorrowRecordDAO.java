package libraryManagement.daos;

import libraryManagement.models.BorrowRecord;
import java.util.List;

public interface BorrowRecordDAO {
    int addBorrowRecord(BorrowRecord record);
    BorrowRecord getBorrowRecordById(int recordId);
    boolean updateBorrowRecord(BorrowRecord record);
    boolean deleteBorrowRecord(int recordId);
    List<BorrowRecord> getAllBorrowedRecords();
    List<BorrowRecord> getActiveBorrowRecords();
    List<BorrowRecord> getBorrowRecordsByBook(int bookId);
    List<BorrowRecord> getBorrowRecordsByMember(int memberId);
    boolean returnBook(int bookId, int memberId);
    boolean isBookBorrowed(int bookId);
    boolean isBorrowedBookByMember(int memberId, int bookId);
    int getBorrowedBooksCountByMember(int memberId);
}
