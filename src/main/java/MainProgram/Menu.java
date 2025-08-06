/**
 * Description: Final Sprint - SD13 - Gym Management Program - Menu Class
 * Authors: Asthon Dennis
 *          Joseph Gallant
 *          Justin Greenslade
 * Date(s): August 4, 2025
 */

package MainProgram;

import java.util.ArrayList;
import java.util.Scanner;
import UserManagement.UserDAO;
import UserManagement.UserService;
import Users.*;
import WorkoutClassManagement.WorkoutClassesDAO;
import WorkoutClasses.WorkoutClass;

public class Menu {
    public static void mainMenu(Scanner scanner, ArrayList<Role> roles, User loggedUser){
        int option = 0;
        final int QUIT_OPTION = 3;

        // If no user is logged in. Show Login/Registration

        do {
            if (loggedUser == null) {
                // Header
                System.out.println("Welcome to the Gym Management System\nPlease make a selection:\n");

                System.out.println("1. Register");
                System.out.println("2. Login");
                System.out.println("3. Quit");

                // Get user input
                option = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                switch (option) {
                    case 1:
                        registerMenu(scanner, roles);
                        break;
                    case 2:
                        loginMenu(scanner, loggedUser, roles);
                        break;
                    case 3:
                        System.out.println("Thank you for using the Gym Management System. Goodbye!");
                        break;
                    default:
                        System.out.println("Invalid option. Please try again.");
                }
            } else{
                String roleName = null;

                for (Role role: roles) {
                    if (loggedUser.getRole().getId() == role.getId()){
                        roleName = role.getName();
                        break;
                    }
                }
                switch (roleName.toLowerCase()) {
                    case "admin":
                        loggedUser = adminMenu(scanner, loggedUser);
                        break;
                    case "trainer":
                        loggedUser = trainerMenu(scanner, loggedUser);
                        break;
                    case "member":
                        loggedUser = memberMenu(scanner, loggedUser);
                        break;
                    default:
                        System.out.println("Unrecognized role.");
                }

            }
        } while (option != QUIT_OPTION);
    }

    private static void registerMenu(Scanner scanner, ArrayList<Role> roles) {
        String username = null;
        String password = null;
        String firstName = null;
        String lastName = null;
        String address = null;
        String city = null;
        String province = null;
        String postalCode = null;
        String email = null;
        String phoneNumber = null;
        String roleName = null;
        Role role = null;

        System.out.println("Please enter a username:");
        username = scanner.nextLine();

        System.out.println("Please enter a password:");
        password = scanner.nextLine();

        System.out.println("Please enter your first name:");
        firstName = scanner.nextLine();

        System.out.println("Please enter your last name:");
        lastName = scanner.nextLine();

        System.out.println("Please enter your address:");
        address = scanner.nextLine();

        System.out.println("Please enter your city:");
        city = scanner.nextLine();

        System.out.println("Please enter your province:");
        province = scanner.nextLine();

        System.out.println("Please enter your postal code:");
        postalCode = scanner.nextLine();

        System.out.println("Please enter your email:");
        email = scanner.nextLine();

        System.out.println("Please enter your phone number:");
        phoneNumber = scanner.nextLine();

        System.out.println("Please enter your role (admin, trainer, member):");
        roleName = scanner.nextLine();

        if (roleName.equalsIgnoreCase("admin")) {
            role = roles.get(0);
        } else if (roleName.equalsIgnoreCase("trainer")) {
            role = roles.get(1);
        } else if (roleName.equalsIgnoreCase("member")) {
            role = roles.get(2);
        } else {
            System.out.println("Invalid role. Please try again.");
        }

        // Send the user data to the UserService to register
        User user = new User(0, username, password, firstName, lastName, address, city, province, postalCode, email, phoneNumber, role);
        UserService.register(user);
    }

    private static void loginMenu(Scanner scanner, User loggedUser, ArrayList<Role> roles) {
        String username = null;
        String password = null;

        System.out.println("Please enter your username:");
        username = scanner.nextLine();

        System.out.println("Please enter your password:");
        password = scanner.nextLine();

        UserService.login(username, password);
    }

    private static User adminMenu(Scanner scanner, User loggedUser) {
        int option = 0;
        final int QUIT_OPTION = 6;

        do {

            System.out.println("Welcome " + loggedUser.getFirstName());
            System.out.println("Please choose an option:");
            System.out.println("1. View all users and contact information");
            System.out.println("2. Delete a user");
            System.out.println("3. View all gym memberships and total revenue");
            System.out.println("4. Merchandise Management");
            System.out.println("5. Logout");

            option = scanner.nextInt();
            scanner.nextLine();

            switch (option) {
                case 1:
                    System.out.println("Viewing all users and contact information... under constuction");
                    break;

                case 2:
                    System.out.print("Enter username of user to delete: ");
                    String usernameToDelete = scanner.nextLine();
                    System.out.println("[Attempting to delete user... under constuction]");
                    break;

                case 3:
                    System.out.println("[Viewing all gym memberships and calculating revenue... under constuction]");
                    break;

                case 4:
                    System.out.println("merchManagementMenu ...under constuction");
                    merchManagementMenu(scanner);
                    break;

                case 5:
                    System.out.println("Logging out...");
                    loggedUser = null;
                    break;

                default:
                    System.out.println("Invalid option. Please try again.");
            }

        } while (option != QUIT_OPTION);

        return loggedUser;
    }

private static void merchManagementMenu(Scanner scanner) {
    int option = 0;
    final int QUIT_OPTION = 5;

    do {
        System.out.println("\nMerchandise Management Menu");
        System.out.println("1. Add new merchandise");
        System.out.println("2. Edit merchandise");
        System.out.println("3. Delete merchandise");
        System.out.println("4. Print all merchandise report and total stock value");
        System.out.println("5. Back to Admin Menu");

        option = scanner.nextInt();
        scanner.nextLine(); 

        switch (option) {
            case 1:
                System.out.println("Add new merchandise... under construction");
                break;

            case 2:
                System.out.print("Enter merchandise ID to edit: ");
                scanner.nextLine(); 
                System.out.println("Edit merchandise... under construction");
                break;

            case 3:
                System.out.println("Delete merchandise... under construction");
                break;

            case 4:
                System.out.println("Print merchandise report... under construction");
                break;

            case 5:
                System.out.println("Returning to Admin Menu.");
                break;

            default:
                System.out.println("Invalid option. Please try again.");
        }
    } while (option != QUIT_OPTION);
}

    private static User trainerMenu(Scanner scanner, User loggedUser) {
        int option = 0;
        final int QUIT_OPTION = 4;

        do {
            // Header
            System.out.println("\nWelcome, " + loggedUser.getFirstName() + "\nPlease make a selection:\n");

            System.out.println("1. Manage my workout classes");
            System.out.println("2. Purchase a membership");
            System.out.println("3. Show the gym merchandise");
            System.out.println("4. Logout");

            option = scanner.nextInt();
            scanner.nextLine();

            switch (option){
                case 1:
                    manageWorkoutClasses(scanner, loggedUser);
                    break;
                case 2:
                    // Purchase membership
                    break;
                case 3:
                    // Show gym merchandise
                    break;
                case 4:
                    System.out.println("Logging out...");
                    loggedUser = null; // Clear the logged user
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }

        } while (option != QUIT_OPTION);

        return loggedUser;
    }

    private static User memberMenu(Scanner scanner, User loggedUser) {
        int option = 0;
        final int QUIT_OPTION = 5;
        do {
            //Header
            System.out.println("Welcome " + loggedUser.getFirstName() + "\nPlease make a selection:\n");

            System.out.println("1. Browse availlable workout classes");
            System.out.println("2. View total membership expenses");
            System.out.println("3. Purchase a membership");
            System.out.println("4. Show the gym merchandise");
            System.out.println("5. Logout");

            option = scanner.nextInt();
            scanner.nextLine();

            switch (option) {
                case 1:
                    // Browse workout classes
                    break;
                case 2:
                    // Show membership expenses
                    break;
                case 3:
                    // Purchase membership
                    break;
                case 4:
                    // Show gym merch
                    break;
                case 5:
                    System.out.println("Logging out...");
                    loggedUser = null; // Clear the logged user
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }

        } while (option != QUIT_OPTION);

        return loggedUser;
    }

    private static void manageWorkoutClasses(Scanner scanner, User loggUser) {
        int option = 0;
        final int QUIT_OPTION = 5;

        do {
            // Header
            System.out.println("\nWorkout Class Management Menu\nPlease make a selection:\n");

            System.out.println("1. Create a new workout class");
            System.out.println("2. Update an existing workout class");
            System.out.println("3. Delete a workout class");
            System.out.println("4. View all workout classes");
            System.out.println("5. Back to Trainer Menu");

            option = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (option) {
                case 1:
                    // Create a new workout class
                    break;
                case 2:
                    // Update an existing workout class
                    break;
                case 3:
                    // Delete a workout class
                    break;
                case 4:
                    showAllWorkoutClasses();
                    break;
                case 5:
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        } while (option != QUIT_OPTION);
    }

    private static void showAllWorkoutClasses() {
        ArrayList<WorkoutClass> workoutClasses = WorkoutClassesDAO.getAllWorkoutClasses();

        if (workoutClasses.isEmpty()) {
            System.out.println("No workout classes available.");
        } else {
            // Print all workout classes
            System.out.println("Available Workout Classes:");
            System.out.println("-------------------------------");

            for (WorkoutClass workoutClass : workoutClasses) {
                System.out.println("Workout Class ID: " + workoutClass.getId());
                System.out.println("Workout Class Type: " + workoutClass.getWorkoutClassType().getName());
                System.out.println("Description: " + workoutClass.getDescription());
                System.out.println("Trainer: " + workoutClass.getTrainer().getFirstName() + " " + workoutClass.getTrainer().getLastName());
                System.out.println("Datetime: " + workoutClass.getDateTime());
                System.out.println("-------------------------------");
            }
        }
    }

}
