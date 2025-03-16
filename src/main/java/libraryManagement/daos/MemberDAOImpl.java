package libraryManagement.daos;

import libraryManagement.models.Member;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MemberDAOImpl implements MemberDAO {
    private Connection conn;

    public MemberDAOImpl(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void addMember(Member member) {
        String query = "INSERT INTO members (name, email, phone, points) VALUES (?, ?, ?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, member.getName());
            stmt.setString(2, member.getEmail());
            stmt.setString(3, member.getPhone() != null ? member.getPhone() : "");
            stmt.setInt(4, 0); // Default points to 0

            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                member.setMemberId(rs.getInt(1));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Member getMemberById(int memberId) {
        String query = "SELECT * FROM members WHERE member_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, memberId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return extractMemberFromResultSet(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Member getMemberByEmail(String email) {
        String sql = "SELECT * FROM members WHERE email = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Member(
                        rs.getInt("member_id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getInt("points")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    public List<Member> getAllMembers() {
        List<Member> members = new ArrayList<>();
        String query = "SELECT * FROM members";
        try (PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                members.add(extractMemberFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return members;
    }

    @Override
    public void updateMember(Member member) {
        String query = "UPDATE members SET name = ?, email = ?, phone = ?, points = ? WHERE member_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, member.getName());
            stmt.setString(2, member.getEmail());
            stmt.setString(3, member.getPhone() != null ? member.getPhone() : "");
            stmt.setInt(4, member.getPoints());
            stmt.setInt(5, member.getMemberId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteMember(int memberId) {
        String query = "DELETE FROM members WHERE member_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, memberId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateMemberPoints(int memberId, int points) {
        String query = "UPDATE members SET points = ? WHERE member_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, points);
            stmt.setInt(2, memberId);

            int rowsAffected = stmt.executeUpdate(); // ✅ Track update success
            if (rowsAffected > 0) {
                System.out.println("✅ Member (ID: " + memberId + ") points updated to: " + points);
            } else {
                System.out.println("⚠️ No member found with ID: " + memberId);
            }
        } catch (SQLException e) {
            System.out.println("❌ Error updating points for Member ID " + memberId + ": " + e.getMessage());
            e.printStackTrace();
        }
    }


    private Member extractMemberFromResultSet(ResultSet rs) throws SQLException {
        return new Member(
                rs.getInt("member_id"),
                rs.getString("name"),
                rs.getString("email"),
                rs.getString("phone"),
                rs.getInt("points")
        );
    }
}
