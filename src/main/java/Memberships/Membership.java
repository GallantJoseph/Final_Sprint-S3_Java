package Memberships;

import Users.User;

import java.time.LocalDate;

/**
 * Represents a Membership in the gym management system.
 * Contains details about the membership type, member, and start date and end date.
 */
public class Membership {
    private int membershipId;
    private MembershipType membershipType;
    private User member;
    private LocalDate startDate;
    private LocalDate endDate;

    /**
     * Constructs a Membership with the specified details.
     *
     * @param membershipId   the unique identifier of the membership
     * @param membershipType the type of the membership
     * @param member         the user who holds the membership
     * @param startDate      the start date of the membership
     * @param endDate        the end date of the membership
     */
    public Membership(int membershipId, MembershipType membershipType, User member, LocalDate startDate, LocalDate endDate) {
        this.membershipId = membershipId;
        this.membershipType = membershipType;
        this.member = member;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    /**
     * Gets the unique identifier of the membership.
     * @return the unique identifier of the membership
     */
    public int getMembershipId() {
        return membershipId;
    }

    /**
     * Sets the unique identifier of the membership.
     * @param membershipId the unique identifier to set
     */
    public void setMembershipId(int membershipId) {
        this.membershipId = membershipId;
    }

    /**
     * Gets the type of the membership.
     * @return the type of the membership
     */
    public MembershipType getMembershipType() {
        return membershipType;
    }

    /**
     * Sets the type of the membership.
     * @param membershipType the type to set for the membership
     */
    public void setMembershipType(MembershipType membershipType) {
        this.membershipType = membershipType;
    }

    /**
     * Gets the user who holds the membership.
     * @return the user who holds the membership
     */
    public User getMember() {
        return member;
    }

    /**
     * Sets the user who holds the membership.
     * @param member the user to set as the member of the membership
     */
    public void setMember(User member) {
        this.member = member;
    }

    /**
     * Gets the start date of the membership.
     * @return the start date of the membership
     */
    public LocalDate getStartDate() {
        return startDate;
    }

    /**
     * Sets the start date of the membership.
     * @param startDate the start date to set for the membership
     */
    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    /**
     * Gets the end date of the membership.
     * @return the end date of the membership
     */
    public LocalDate getEndDate() {
        return endDate;
    }

    /**
     * Sets the end date of the membership.
     * @param endDate the end date to set for the membership
     */
    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
}
