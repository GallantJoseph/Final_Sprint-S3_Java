/**
 * Description: Final Sprint - SD13 - Gym Management Program - Main Class
 * Authors: Ashton Dennis
 *          Joseph Gallant
 *          Justin Greenslade
 * Date(s): August 4, 2025
 */

package MainProgram;

import java.util.ArrayList;
import java.util.Scanner;

import UserManagement.UserDAO;
import Users.*;

/**
 * Main class to run the Gym Management Program.
 * Initializes roles and a logged-in user, and starts the main menu.
 */
public class Main {
    public static void main(String[] args) {
        // Get the roles from the database
        ArrayList<Role> roles = UserDAO.getRoles();
        Scanner scanner = new Scanner(System.in);

        // Initialize a logged-in user as null
        User loggedUser = null;

        Menu.mainMenu(scanner, roles, loggedUser);
    }
}