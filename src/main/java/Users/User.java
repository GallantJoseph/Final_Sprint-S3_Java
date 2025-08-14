package Users;

/**
 * Represents a User in the system.
 * This class contains user details such as username, password, personal information, and role.
 */
public class User {
    private int userId;
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String streetAddress;
    private String city;
    private String province;
    private String postalCode;
    private String email;
    private String phone;
    private Role role;

    /**
     * Constructs a User with the specified details.
     *
     * @param userId        the unique identifier for the user
     * @param username      the username of the user
     * @param password      the password of the user
     * @param firstName     the first name of the user
     * @param lastName      the last name of the user
     * @param streetAddress the street address of the user
     * @param city          the city of the user
     * @param province      the province of the user
     * @param postalCode    the postal code of the user
     * @param email         the email address of the user
     * @param phone         the phone number of the user
     * @param role          the role of the user
     */
   public User(int userId, String username,String password, String firstName, String lastName,
                String streetAddress, String city, String province, String postalCode,
                String email, String phone, Role role) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.streetAddress = streetAddress;
        this.city = city;
        this.province = province;
        this.postalCode = postalCode;
        this.email = email;
        this.phone = phone;
        this.role = role;
    }

    /**
     * Default constructor for User.
     * Initializes a User with default values.
     */
    public User(User user) {
        this.userId = user.userId;
        this.username = user.username;
        this.password = user.password;
        this.firstName = user.firstName;
        this.lastName = user.lastName;
        this.streetAddress = user.streetAddress;
        this.city = user.city;
        this.province = user.province;
        this.postalCode = user.postalCode;
        this.email = user.email;
        this.phone = user.phone;
        this.role = user.role;
    }

    /*     * Default constructor for User.
     * Initializes a User with default values.
     */
    public User() {
        this.userId = 0;
        this.username = "";
        this.password = "";
        this.firstName = "";
        this.lastName = "";
        this.streetAddress = "";
        this.city = "";
        this.province = "";
        this.postalCode = "";
        this.email = "";
        this.phone = "";
        this.role = new Role(0, "User"); // Default role
    }


    /**
     * Get the unique identifier for the user.
     * @return the user ID
     */
    public int getUserId() {
        return userId;
    }

    /**
     * Get the username of the user.
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Get the password of the user.
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Get the first name of the user.
     * @return the first name
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Get the last name of the user.
     * @return the last name
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Get the full name of the user by combining first and last names.
     * @return the full name
     */
    public String getFullName() {
        return firstName + " " + lastName;
    }

    /**
     * Get the street address of the user.
     * @return the street address
     */
    public String getStreetAddress() {
        return streetAddress;
    }

    /**
     * Get the city of the user.
     * @return the city
     */
    public String getCity() {
        return city;
    }

    /**
     * Get the province of the user.
     * @return the province
     */
    public String getProvince() {
        return province;
    }

    /**
     * Get the postal code of the user.
     * @return the postal code
     */
    public String getPostalCode() {
        return postalCode;
    }

    /**
     * Get the email address of the user.
     * @return the email address
     */
    public String getEmail() {
        return email;
    }

    /**
     * Get the phone number of the user.
     * @return the phone number
     */
    public String getPhone() {
        return phone;
    }

    /**
     * Get the role of the user.
     * @return the role
     */
    public Role getRole() {
        return role;
    }

    /**
     * Set the unique identifier for the user.
     * @param userId the user ID to set
     */
    public void setUserId(int userId) {
        this.userId = userId;
    }

    /**
     * Set the username of the user.
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Set the password of the user.
     * @param password the password to set
     */
    public void setPassword(String password){
        this.password = password;
    }

    /**
     * Set the first name of the user.
     * @param firstName the first name to set
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Set the last name of the user.
     * @param lastName the last name to set
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Set the street address of the user.
     * @param streetAddress the street address to set
     */
    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    /**
     * Set the city of the user.
     * @param city the city to set
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * Set the province of the user.
     * @param province the province to set
     */
    public void setProvince(String province) {
        this.province = province;
    }

    /**
     * Set the postal code of the user.
     * @param postalCode the postal code to set
     */
    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    /**
     * Set the email address of the user.
     * @param email the email address to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Set the phone number of the user.
     * @param phone the phone number to set
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * Set the role of the user.
     * @param role the role to set
     */
    public void setRole(Role role) {
        this.role = role;
    }
}
