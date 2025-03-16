package libraryManagement.daos;

import libraryManagement.models.Member;
import java.util.List;

public interface MemberDAO {
    void addMember(Member member);
    Member getMemberById(int memberId);
    List<Member> getAllMembers();
    void updateMember(Member member);
    void deleteMember(int memberId);
    Member getMemberByEmail(String email);
    void updateMemberPoints(int memberId, int points); // Added for loyalty program

}