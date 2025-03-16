package libraryManagement.models;

public class Member {
    private int memberId;
    private String name;
    private String email;
    private String phone;
    private int points;

    //constructor
    public Member(int memberId, String name, String email, String phone, int points){
        this.memberId = memberId;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.points = points;

    }

    public Member(String name, String email, String phone){
        this.name = name;
        this.email = email;
        this.phone = phone;
    }


    //getters and setters
    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    @Override
    public String toString() {
        return "Member{" +
                "memberId=" + memberId +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", points=" + points + // Include points
                '}';
    }
}

