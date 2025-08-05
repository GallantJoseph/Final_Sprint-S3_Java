package Memberships;

import Users.User;

import java.time.LocalDate;

public class Membership {

    private int membershipId;
    private MembershipType membershipType;
    private User member;
    private LocalDate startDate;
    private LocalDate endDate;

    public Membership(int membershipId, MembershipType membershipType, User member, LocalDate startDate, LocalDate endDate) {
        this.membershipId = membershipId;
        this.membershipType = membershipType;
        this.member = member;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public int getMembershipId() {
        return membershipId;
    }

    public void setMembershipId(int membershipId) {
        this.membershipId = membershipId;
    }

    public MembershipType getMembershipType() {
        return membershipType;
    }

    public void setMembershipType(MembershipType membershipType) {
        this.membershipType = membershipType;
    }

    public User getMember() {
        return member;
    }

    public void setMember(User member) {
        this.member = member;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
}
