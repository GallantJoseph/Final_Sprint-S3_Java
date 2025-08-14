package Users;

/**
 * Represents a Member user role in the gym management system.
 * Extends the base User class and provides constructors for creating a Member.
 */
public class Member extends User {
    /**
     * Constructs a Member with the specified user details.
     *
     * @param userId         the unique identifier for the user
     * @param username       the username of the member
     * @param password       the password of the member
     * @param firstName      the first name of the member
     * @param lastName       the last name of the member
     * @param streetAddress  the street address of the member
     * @param city           the city of the member
     * @param province       the province of the member
     * @param postalCode     the postal code of the member
     * @param email          the email address of the member
     * @param phone          the phone number of the member
     * @param role           the role of the user (should be Member)
     */
    public Member(int userId, String username,String password, String firstName, String lastName,
                String streetAddress, String city, String province, String postalCode,
                String email, String phone, Role role) {
        super(userId, username, password, firstName, lastName, streetAddress, city, province, postalCode, email, phone, role);
    }

    /**
     * Default constructor for Member.
     * Initializes a Member with default values.
     */
    public Member(User user) {
        super(user);
    }
}
