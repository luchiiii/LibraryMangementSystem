package libraryManagement.daos;

import libraryManagement.models.Review;
import java.util.List;

public interface ReviewDAO {

    // Create a new review
    int addReview(Review review);

    // Retrieve a review by ID
    Review getReviewById(int reviewId);

    // Get all reviews
    List<Review> getAllReviews();

    // Update an existing review
    boolean updateReview(Review review);

    // Delete a review
    boolean deleteReview(int reviewId);


    // Get reviews for a specific book
    List<Review> getReviewsByBook(int bookId);

    // Get reviews by a specific member
    List<Review> getReviewsByMember(int memberId);

    // Calculate average rating for a book
    double getAverageRatingForBook(int bookId);

    // Check if a member has already reviewed a book
    boolean hasReviewed(int memberId, int bookId);

    // Get count of reviews for a book
    int getReviewCountForBook(int bookId);
}