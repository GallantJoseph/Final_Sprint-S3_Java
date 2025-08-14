package Users;

/**
 * Represents a Trainer user role in the system.
 * Extends the base User class and provides constructors for creating Trainer objects.
 */
public class Trainer extends User {

    /**
     * Constructs a Trainer with the specified user details.
     *
     * @param userId        the unique identifier for the user
     * @param username      the username of the Trainer
     * @param password      the password of the Trainer
     * @param firstName     the first name of the Trainer
     * @param lastName      the last name of the Trainer
     * @param streetAddress the street address of the Trainer
     * @param city          the city of the Trainer
     * @param province      the province of the Trainer
     * @param postalCode    the postal code of the Trainer
     * @param email         the email address of the Trainer
     * @param phone         the phone number of the Trainer
     * @param role          the role of the Trainer
     */
    public Trainer(int userId, String username,String password, String firstName, String lastName,
               String streetAddress, String city, String province, String postalCode,
               String email, String phone, Role role) {

        super(userId, username, password, firstName, lastName, streetAddress, city, province, postalCode, email, phone, role);
    }

    /**
     * Default constructor for Trainer.
     * Initializes a Trainer with default values.
     */
    public Trainer(User user) {
        super(user);
    }
}

