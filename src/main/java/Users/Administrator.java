package Users;

/**
 * Represents an Administrator user role in the gym management system.
 * Extends the base User class and provides constructors for creating an Administrator.
 */
public class Administrator extends User {

    /**
     * Constructs an Administrator with the specified user details.
     *
     * @param userId         the unique identifier for the user
     * @param username       the username of the administrator
     * @param password       the password of the administrator
     * @param firstName      the first name of the administrator
     * @param lastName       the last name of the administrator
     * @param streetAddress  the street address of the administrator
     * @param city           the city of the administrator
     * @param province       the province of the administrator
     * @param postalCode     the postal code of the administrator
     * @param email          the email address of the administrator
     * @param phone          the phone number of the administrator
     * @param role           the role of the user (should be Administrator)
     */
    public Administrator(int userId, String username,String password, String firstName, String lastName,
                String streetAddress, String city, String province, String postalCode,
                String email, String phone, Role role) {

        super(userId, username, password, firstName, lastName, streetAddress, city, province, postalCode, email, phone, role);
    }

    /**
     * Default constructor for Administrator.
     * Initializes an Administrator with default values.
     */
    public Administrator(User user) {
        super(user);
    }
}
