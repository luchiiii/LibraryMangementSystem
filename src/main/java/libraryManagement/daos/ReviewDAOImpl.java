package libraryManagement.daos;

import libraryManagement.models.Review;
import libraryManagement.utils.LogTracker;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReviewDAOImpl implements ReviewDAO {
    private Connection connection;

    public ReviewDAOImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public int addReview(Review review) {
        String sql = "INSERT INTO reviews (book_id, member_id, review_text, rating) VALUES (?, ?, ?, ?) RETURNING review_id";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, review.getBookId());
            stmt.setInt(2, review.getMemberId());
            stmt.setString(3, review.getReviewText());
            stmt.setInt(4, review.getRating());

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int reviewId = rs.getInt("review_id");
                LogTracker.log("New review added for Book (ID: " + review.getBookId() + ") by Member (ID: " + review.getMemberId() + ")");
                return reviewId;
            }
        } catch (SQLException e) {
            LogTracker.log("Error adding review: " + e.getMessage());
            e.printStackTrace();
        }
        return -1;
    }

    @Override
    public Review getReviewById(int reviewId) {
        String sql = "SELECT * FROM reviews WHERE review_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, reviewId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return extractReviewFromResultSet(rs);
            }
        } catch (SQLException e) {
            LogTracker.log("Error retrieving review: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean updateReview(Review review) {
        String sql = "UPDATE reviews SET book_id = ?, member_id = ?, review_text = ?, rating = ? WHERE review_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, review.getBookId());
            stmt.setInt(2, review.getMemberId());
            stmt.setString(3, review.getReviewText());
            stmt.setInt(4, review.getRating());
            stmt.setInt(5, review.getReviewId());

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                LogTracker.log("Review updated: " + review.getReviewId());
                return true;
            }
        } catch (SQLException e) {
            LogTracker.log("Error updating review: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean deleteReview(int reviewId) {
        String sql = "DELETE FROM reviews WHERE review_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, reviewId);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                LogTracker.log("Review deleted: " + reviewId);
                return true;
            }
        } catch (SQLException e) {
            LogTracker.log("Error deleting review: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<Review> getAllReviews() {
        List<Review> reviews = new ArrayList<>();
        String sql = "SELECT * FROM reviews ORDER BY review_id";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                reviews.add(extractReviewFromResultSet(rs));
            }
        } catch (SQLException e) {
            LogTracker.log("Error retrieving all reviews: " + e.getMessage());
            e.printStackTrace();
        }
        return reviews;
    }

    @Override
    public List<Review> getReviewsByBook(int bookId) {
        List<Review> reviews = new ArrayList<>();
        String sql = "SELECT * FROM reviews WHERE book_id = ? ORDER BY review_id";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, bookId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                reviews.add(extractReviewFromResultSet(rs));
            }
        } catch (SQLException e) {
            LogTracker.log("Error retrieving reviews for book: " + e.getMessage());
            e.printStackTrace();
        }
        return reviews;
    }

    @Override
    public List<Review> getReviewsByMember(int memberId) {
        List<Review> reviews = new ArrayList<>();
        String sql = "SELECT * FROM reviews WHERE member_id = ? ORDER BY review_id";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, memberId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                reviews.add(extractReviewFromResultSet(rs));
            }
        } catch (SQLException e) {
            LogTracker.log("Error retrieving reviews by member: " + e.getMessage());
            e.printStackTrace();
        }
        return reviews;
    }

    @Override
    public double getAverageRatingForBook(int bookId) {
        String sql = "SELECT AVG(rating) FROM reviews WHERE book_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, bookId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getDouble(1);
            }
        } catch (SQLException e) {
            LogTracker.log("Error calculating average rating: " + e.getMessage());
            e.printStackTrace();
        }
        return 0.0;
    }

    @Override
    public boolean hasReviewed(int memberId, int bookId) {
        String sql = "SELECT COUNT(*) FROM reviews WHERE member_id = ? AND book_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, memberId);
            stmt.setInt(2, bookId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            LogTracker.log("Error checking if member has reviewed book: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public int getReviewCountForBook(int bookId) {
        String sql = "SELECT COUNT(*) FROM reviews WHERE book_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, bookId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            LogTracker.log("Error counting reviews for book: " + e.getMessage());
            e.printStackTrace();
        }
        return 0;
    }

    // Helper method to extract a Review from a ResultSet
    private Review extractReviewFromResultSet(ResultSet rs) throws SQLException {
        Review review = new Review();
        review.setReviewId(rs.getInt("review_id"));
        review.setBookId(rs.getInt("book_id"));
        review.setMemberId(rs.getInt("member_id"));
        review.setReviewText(rs.getString("review_text"));
        review.setRating(rs.getInt("rating"));
        return review;
    }
}