package libraryManagement.tests;

import libraryManagement.daos.ReviewDAO;
import libraryManagement.daos.ReviewDAOImpl;
import libraryManagement.daos.BookDAO;
import libraryManagement.daos.BookDAOImpl;
import libraryManagement.models.Review;
import libraryManagement.services.ReviewService;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

public class ReviewServiceTest {
    public static void main(String[] args) {
        String url = "jdbc:postgresql://localhost:5432/library_management";
        String user = "oluchiiii";
        String password = "password";

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            ReviewDAO reviewDAO = new ReviewDAOImpl(conn);
            BookDAO bookDAO = new BookDAOImpl(conn);
            ReviewService reviewService = new ReviewService(reviewDAO, bookDAO);

            int bookId = 1;  // Use a valid book ID
            int memberId = 1;  // Use a valid member ID

            // Add a review
            boolean added = reviewService.addReview(bookId, memberId, "Amazing book!", 5);
            System.out.println("Review added: " + added);

            // Get all reviews for the book
            List<Review> reviews = reviewService.getReviewsByBook(bookId);
            System.out.println("\nReviews for Book ID " + bookId + ":");
            for (Review review : reviews) {
                System.out.println(review);
            }

            // Update a review
            if (!reviews.isEmpty()) {
                Review reviewToUpdate = reviews.get(0);
                reviewToUpdate.setReviewText("Updated review text");
                boolean updated = reviewService.updateReview(reviewToUpdate);
                System.out.println("Review updated: " + updated);
            }

            // Get average rating
            double avgRating = reviewService.getAverageRatingForBook(bookId);
            System.out.println("\nAverage Rating for Book ID " + bookId + ": " + avgRating);

            // Delete a review
            if (!reviews.isEmpty()) {
                int reviewId = reviews.get(0).getReviewId();
                boolean deleted = reviewService.deleteReview(reviewId);
                System.out.println("Review deleted: " + deleted);
            }

        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
