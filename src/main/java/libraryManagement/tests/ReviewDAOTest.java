package libraryManagement.tests;

import libraryManagement.daos.ReviewDAO;
import libraryManagement.daos.ReviewDAOImpl;
import libraryManagement.models.Review;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

public class ReviewDAOTest {
    public static void main(String[] args) {
        String url = "jdbc:postgresql://localhost:5432/library_management";
        String user = "oluchiiii";
        String password = "password";

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            ReviewDAO reviewDAO = new ReviewDAOImpl(conn);

            int bookId = 1;  // Use a valid book ID
            int memberId = 1;  // Use a valid member ID

            // Add a review
            Review newReview = new Review(bookId, memberId, "Great book!", 5);
            int reviewId = reviewDAO.addReview(newReview);
            System.out.println("Added Review with ID: " + reviewId);

            // Get review by ID
            Review fetchedReview = reviewDAO.getReviewById(reviewId);
            System.out.println("\nFetched Review: " + fetchedReview);

            // Get all reviews
            List<Review> allReviews = reviewDAO.getAllReviews();
            System.out.println("\nAll Reviews:");
            for (Review review : allReviews) {
                System.out.println(review);
            }

            // Update review
            if (fetchedReview != null) {
                fetchedReview.setReviewText("Updated review text");
                boolean updated = reviewDAO.updateReview(fetchedReview);
                System.out.println("\nReview updated: " + updated);
            }

            // Get reviews by book
            List<Review> bookReviews = reviewDAO.getReviewsByBook(bookId);
            System.out.println("\nReviews for Book ID " + bookId + ":");
            for (Review review : bookReviews) {
                System.out.println(review);
            }

            // Get reviews by member
            List<Review> memberReviews = reviewDAO.getReviewsByMember(memberId);
            System.out.println("\nReviews by Member ID " + memberId + ":");
            for (Review review : memberReviews) {
                System.out.println(review);
            }

            // Get average rating
            double avgRating = reviewDAO.getAverageRatingForBook(bookId);
            System.out.println("\nAverage Rating for Book ID " + bookId + ": " + avgRating);

            // Delete review
            boolean deleted = reviewDAO.deleteReview(reviewId);
            System.out.println("\nReview deleted: " + deleted);

        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
