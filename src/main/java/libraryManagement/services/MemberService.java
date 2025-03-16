package libraryManagement.services;

import libraryManagement.daos.MemberDAO;
import libraryManagement.models.Member;
import libraryManagement.utils.LogTracker;
import libraryManagement.utils.DataExporter;
import java.util.List;

public class MemberService {
    private MemberDAO memberDAO;
    private List<Member> cachedMembers;

    public MemberService(MemberDAO memberDAO) {
        this.memberDAO = memberDAO;
        refreshCache();
    }

    // Refresh cache with members from database
    private void refreshCache() {
        cachedMembers = memberDAO.getAllMembers();
    }

    // Add a new member and log the action
    public void addMember(Member member) {
        memberDAO.addMember(member);
        refreshCache();
        LogTracker.log("Added new member: " + member.getName());
    }

    // Get a member by ID
    public Member getMemberById(int memberId) {
        return memberDAO.getMemberById(memberId);
    }

    //get all members
    public List<Member> getAllMembers() {
        return memberDAO.getAllMembers();
    }

    //get member by email
    public Member getMemberByEmail(String email) {
        return memberDAO.getMemberByEmail(email);
    }


    // Update member details
    public void updateMember(Member member) {
        memberDAO.updateMember(member);
        refreshCache();
        LogTracker.log("Updated member: " + member.getName() + " (ID: " + member.getMemberId() + ")");
    }

    // Delete a member
    public void deleteMember(int memberId) {
        Member member = getMemberById(memberId);
        if (member != null) {
            memberDAO.deleteMember(memberId);
            refreshCache();
            LogTracker.log("Deleted member: " + member.getName() + " (ID: " + memberId + ")");
        }
    }

    // Search for members by name
    public List<Member> searchByName(String nameKeyword) {
        return cachedMembers.stream()
                .filter(member -> member.getName().toLowerCase().contains(nameKeyword.toLowerCase()))
                .toList();
    }

    // Award points to a member
    public void awardPoints(int memberId, int points) {
        Member member = getMemberById(memberId);
        if (member != null) {
            memberDAO.updateMemberPoints(memberId, member.getPoints() + points);
            LogTracker.log("Awarded " + points + " points to member ID " + memberId);
        }
    }

    // Deduct points from a member (e.g., for late returns)
    public void deductPoints(int memberId, int points) {
        Member member = getMemberById(memberId);
        if (member != null) {
            int newPoints = Math.max(0, member.getPoints() - points); // Prevent negative points
            memberDAO.updateMemberPoints(memberId, newPoints);
            LogTracker.log("Deducted " + points + " points from member ID " + memberId);
        }
    }

    // Get a member's loyalty tier
    public String getMemberTier(int memberId) {
        Member member = getMemberById(memberId);
        if (member == null) return "Unknown";

        int points = member.getPoints();
        if (points >= 201) return "Gold";
        if (points >= 51) return "Silver";
        return "Bronze";
    }

    // Export member data to CSV
    public void exportMembersData(String filePath) {
        DataExporter.exportMembersToCSV(memberDAO.getAllMembers(), filePath);
        LogTracker.log("Exported member data to: " + filePath);
    }
}
