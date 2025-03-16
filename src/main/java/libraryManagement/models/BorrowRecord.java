package libraryManagement.models;

import java.sql.Date;

public class BorrowRecord {
    private int recordId;
    private int bookId;
    private int memberId;
    private Date borrowDate;
    private Date returnDate;

    public BorrowRecord() {}

    public BorrowRecord(int bookId, int memberId, Date borrowDate, Date returnDate){
        this.bookId = bookId;
        this.memberId = memberId;
        this.borrowDate = borrowDate != null ? borrowDate : new Date(System.currentTimeMillis());
        this.returnDate = returnDate;
    }

    public BorrowRecord(int recordId, int bookId, int memberId, Date borrowDate, Date returnDate){
        this.recordId = recordId;
        this.bookId = bookId;
        this.memberId = memberId;
        this.borrowDate = borrowDate != null ? borrowDate : new Date(System.currentTimeMillis());
        this.returnDate = returnDate;
    }

    public int getRecordId() { return recordId; }
    public void setRecordId(int recordId) { this.recordId = recordId; }

    public int getBookId() { return bookId; }
    public void setBookId(int bookId) { this.bookId = bookId; }

    public int getMemberId() { return memberId; }
    public void setMemberId(int memberId) { this.memberId = memberId; }

    public Date getBorrowDate() { return borrowDate; }
    public void setBorrowDate(Date borrowDate) { this.borrowDate = borrowDate; }

    public Date getReturnDate() { return returnDate; }
    public void setReturnDate(Date returnDate) { this.returnDate = returnDate; }

    public boolean isActive() { return returnDate == null; }

    @Override
    public String toString() {
        return "BorrowRecord{" +
                "recordId=" + recordId +
                ", bookId=" + bookId +
                ", memberId=" + memberId +
                ", borrowDate=" + borrowDate +
                ", returnDate=" + (returnDate != null ? returnDate : "Not returned yet") +
                '}';
    }
}
