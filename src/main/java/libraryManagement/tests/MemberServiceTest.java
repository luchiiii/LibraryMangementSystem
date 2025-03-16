package libraryManagement.tests;

import libraryManagement.services.MemberService;
import libraryManagement.daos.MemberDAO;
import libraryManagement.daos.MemberDAOImpl;
import libraryManagement.models.Member;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

public class MemberServiceTest {
    public static void main(String[] args) {
        String url = "jdbc:postgresql://localhost:5432/library_management";
        String user = "oluchiiii";
        String password = "password";

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            MemberDAO memberDAO = new MemberDAOImpl(conn);
            MemberService memberService = new MemberService(memberDAO);

            // ✅ Add a member
            Member newMember = new Member("Jane Doe", "jane.doe@email.com", "123-456-7890");
            memberService.addMember(newMember);
            System.out.println("Added Member: " + newMember);

            // ✅ Get all members
            List<Member> members = memberService.searchByName("Jane");
            System.out.println("\nAll Members:");
            for (Member member : members) {
                System.out.println(member);
            }

            // ✅ Get member by ID
            Member fetchedMember = memberService.getMemberById(newMember.getMemberId());
            System.out.println("\nFetched Member: " + fetchedMember);

            // ✅ Update member details
            fetchedMember.setEmail("updated.email@email.com");
            memberService.updateMember(fetchedMember);
            System.out.println("\nUpdated Member: " + memberService.getMemberById(newMember.getMemberId()));

            // ✅ Export members to CSV
            String exportPath = "members_export.csv";
            memberService.exportMembersData(exportPath);
            System.out.println("Members exported to: " + exportPath);

            // ✅ Delete the member
            memberService.deleteMember(newMember.getMemberId());
            System.out.println("\nAfter Deletion: " + memberService.getMemberById(newMember.getMemberId()));

        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
