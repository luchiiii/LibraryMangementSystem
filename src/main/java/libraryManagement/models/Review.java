package libraryManagement.models;

public class Review {
    private int reviewId;
    private int bookId;
    private int memberId;
    private String reviewText;
    private int rating;

    //Default constructor
    public Review(){};

    //constructor without reviewId (for new reviews)
    public Review(int bookId, int memberId, String reviewText, int rating){
        this.bookId = bookId;
        this.memberId = memberId;
        this.reviewText =  reviewText;
        this.rating = rating;
    }

    //constructor with reviewId
    public Review(int reviewId, int bookId, int memberId, String reviewText, int rating){
        this.reviewId = reviewId;
        this.bookId = bookId;
        this.memberId = memberId;
        this.reviewText = reviewText;
        this.rating = rating;
    }


    //getters and setters
    public int getReviewId() {
        return reviewId;
    }

    public void setReviewId(int reviewId) {
        this.reviewId = reviewId;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public String getReviewText() {
        return reviewText;
    }

    public void setReviewText(String reviewText) {
        this.reviewText = reviewText;
    }

    public int getRating() {
        return rating;
    }


    public void setRating(int rating) {
        // Enforce rating constraint (1-5)
        if (rating < 1 || rating > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5");
        }
        this.rating = rating;
    }

    @Override
    public String toString() {
        return "Review{" +
                "reviewId=" + reviewId +
                ", bookId=" + bookId +
                ", memberId=" + memberId +
                ", reviewText='" + reviewText + '\'' +
                ", rating=" + rating +
                '}';
    }
}
