package libraryManagement.services;

import libraryManagement.daos.ReviewDAO;
import libraryManagement.daos.BookDAO;
import libraryManagement.models.Review;
import libraryManagement.models.Book;
import libraryManagement.utils.LogTracker;

import java.util.List;

public class ReviewService {
    private ReviewDAO reviewDAO;
    private BookDAO bookDAO;

    public ReviewService(ReviewDAO reviewDAO, BookDAO bookDAO) {
        this.reviewDAO = reviewDAO;
        this.bookDAO = bookDAO;
    }

    // Add a new review
    public boolean addReview(int bookId, int memberId, String reviewText, int rating) {
        // Validate rating
        if (rating < 1 || rating > 5) {
            LogTracker.log("Invalid rating: " + rating + ". Rating must be between 1 and 5.");
            return false;
        }

        // Check if the book exists
        Book book = bookDAO.getBookById(bookId);
        if (book == null) {
            LogTracker.log("Book with ID " + bookId + " not found.");
            return false;
        }

        // Check if the member has already reviewed this book
        if (reviewDAO.hasReviewed(memberId, bookId)) {
            LogTracker.log("Member " + memberId + " has already reviewed book " + bookId);
            return false;
        }

        // Create a new review
        Review review = new Review();
        review.setBookId(bookId);
        review.setMemberId(memberId);
        review.setReviewText(reviewText);
        review.setRating(rating);

        // Add the review
        int reviewId = reviewDAO.addReview(review);
        if (reviewId > 0) {
            LogTracker.log("Review added successfully for book ID: " + bookId);
            return true;
        } else {
            LogTracker.log("Failed to add review for book ID: " + bookId);
            return false;
        }
    }

    // Get all reviews for a specific book
    public List<Review> getReviewsByBook(int bookId) {
        // Check if the book exists
        Book book = bookDAO.getBookById(bookId);
        if (book == null) {
            LogTracker.log("Book with ID " + bookId + " not found.");
            return null;
        }

        return reviewDAO.getReviewsByBook(bookId);
    }

    // Get all reviews by a specific member
    public List<Review> getReviewsByMember(int memberId) {
        return reviewDAO.getReviewsByMember(memberId);
    }

    // Update a review
    public boolean updateReview(Review review) {
        // Validate rating
        if (review.getRating() < 1 || review.getRating() > 5) {
            LogTracker.log("Invalid rating: " + review.getRating() + ". Rating must be between 1 and 5.");
            return false;
        }

        boolean success = reviewDAO.updateReview(review);
        if (success) {
            LogTracker.log("Review updated successfully: " + review.getReviewId());
        } else {
            LogTracker.log("Failed to update review: " + review.getReviewId());
        }
        return success;
    }

    // Delete a review
    public boolean deleteReview(int reviewId) {
        // Get the review to verify it exists
        Review review = reviewDAO.getReviewById(reviewId);
        if (review == null) {
            LogTracker.log("Review with ID " + reviewId + " not found.");
            return false;
        }

        boolean success = reviewDAO.deleteReview(reviewId);
        if (success) {
            LogTracker.log("Review deleted successfully: " + reviewId);
        } else {
            LogTracker.log("Failed to delete review: " + reviewId);
        }
        return success;
    }

    // Get a review by its ID
    public Review getReviewById(int reviewId) {
        return reviewDAO.getReviewById(reviewId);
    }

    // Get all reviews
    public List<Review> getAllReviews() {
        return reviewDAO.getAllReviews();
    }

    // Get the average rating for a book
    public double getAverageRatingForBook(int bookId) {
        // Check if the book exists
        Book book = bookDAO.getBookById(bookId);
        if (book == null) {
            LogTracker.log("Book with ID " + bookId + " not found.");
            return 0.0;
        }

        return reviewDAO.getAverageRatingForBook(bookId);
    }

    // Get the count of reviews for a book
    public int getReviewCountForBook(int bookId) {
        // Check if the book exists
        Book book = bookDAO.getBookById(bookId);
        if (book == null) {
            LogTracker.log("Book with ID " + bookId + " not found.");
            return 0;
        }

        return reviewDAO.getReviewCountForBook(bookId);
    }

    // Check if a member has already reviewed a book
    public boolean hasReviewed(int memberId, int bookId) {
        return reviewDAO.hasReviewed(memberId, bookId);
    }
}