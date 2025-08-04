package Users;

public class Administrator extends User {
        public Administrator(int userId, String username,String password, String firstName, String lastName,
                String streetAddress, String city, String province, String postalCode,
                String email, String phone, Role role) {
        super(userId, username, password, firstName, lastName, streetAddress, city, province, postalCode, email, phone, role);
    }
}
