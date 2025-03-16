package libraryManagement.tests;

import libraryManagement.daos.MemberDAO;
import libraryManagement.daos.MemberDAOImpl;
import libraryManagement.models.Member;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

public class MemberDAOTest {
    public static void main(String[] args) {
        String url = "jdbc:postgresql://localhost:5432/library_management";
        String user = "oluchiiii";
        String password = "password";

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            MemberDAO memberDAO = new MemberDAOImpl(conn);

            // Add a new member
            Member newMember = new Member("John Doe", "john.doe@email.com", "987-654-3210");
            memberDAO.addMember(newMember);
            System.out.println("Added Member: " + newMember);

            // Retrieve all members
            List<Member> members = memberDAO.getAllMembers();
            System.out.println("\nAll Members:");
            for (Member member : members) {
                System.out.println(member);
            }

            // Retrieve member by ID
            Member fetchedMember = memberDAO.getMemberById(newMember.getMemberId());
            System.out.println("\nFetched Member: " + fetchedMember);

            // Update member points
            memberDAO.updateMemberPoints(newMember.getMemberId(), 100);
            System.out.println("\nUpdated Member Points: " + memberDAO.getMemberById(newMember.getMemberId()));

            // Delete the member
            memberDAO.deleteMember(newMember.getMemberId());
            System.out.println("\nAfter Deletion: " + memberDAO.getMemberById(newMember.getMemberId()));

        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
