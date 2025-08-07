/**
 * Description: Final Sprint - SD13 - Gym Management Program - Menu Class
 * Authors: Ashton Dennis
 *          Joseph Gallant
 *          Justin Greenslade
 * Date(s): August 4, 2025
 */

package MainProgram;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Scanner;

import UserManagement.UserDAO;
import UserManagement.UserService;
import Users.*;
import WorkoutClassManagement.WorkoutClassTypesDAO;
import WorkoutClassManagement.WorkoutClassesDAO;
import WorkoutClasses.WorkoutClass;
import WorkoutClasses.WorkoutClassType;

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
                        loggedUser = loginMenu(scanner, roles);
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
                        loggedUser = trainerMenu(scanner, loggedUser, roles);
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

        // Header
        System.out.println("\nPlease enter your details to register:\n");

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

        // Select role
        do {
            System.out.println("Please enter your role (admin, trainer, member):");
            roleName = scanner.nextLine();

            for (Role r : roles) {
                if (r.getName().equalsIgnoreCase(roleName)) {
                    role = r;
                    break;
                }
            }

            if (role == null) {
                System.out.println("Invalid role. Please try again.");
            }
        } while (role == null);


        // Send the user data to the UserService to register
        User user = new User(0, username, password, firstName, lastName, address, city, province, postalCode, email, phoneNumber, role);
        UserService.register(user);
    }

    private static User loginMenu(Scanner scanner, ArrayList<Role> roles) {
        String username = null;
        String password = null;

        User user = null;

        // Header
        System.out.println("Please enter your login credentials:\n");

        do {
            System.out.println("Please enter your username:");
            username = scanner.nextLine();

            System.out.println("Please enter your password:");
            password = scanner.nextLine();

            user = UserService.login(username, password, roles);
        } while (user == null);

        return user;
    }

    private static User adminMenu(Scanner scanner, User loggedUser) {
        int option = 0;
        final int QUIT_OPTION = 5;

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


    private static User trainerMenu(Scanner scanner, User loggedUser, ArrayList<Role> roles) {
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
                    manageWorkoutClasses(scanner, loggedUser, roles);
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

            System.out.println("1. Browse available workout classes");
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

    private static void manageWorkoutClasses(Scanner scanner, User loggedUser, ArrayList<Role> roles) {
        int option = 0;
        final int QUIT_OPTION = 5;

        do {
            // Header
            System.out.println("\nWorkout Class Management Menu\nPlease make a selection:\n");

            System.out.println("1. Create a new workout class");
            System.out.println("2. Update an existing workout class");
            System.out.println("3. Delete a workout class");
            System.out.println("4. View your workout classes");
            System.out.println("5. Back to Trainer Menu");

            option = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (option) {
                case 1:
                    createNewWorkoutClass(scanner, loggedUser);
                    System.out.print("\nPress Enter to continue...");
                    scanner.nextLine();
                    break;
                case 2:
                    updateWorkoutClass(scanner, loggedUser);
                    System.out.print("\nPress Enter to continue...");
                    scanner.nextLine();
                    break;
                case 3:
                    deleteWorkoutClass(scanner, loggedUser);
                    System.out.print("\nPress Enter to continue...");
                    scanner.nextLine();
                    break;
                case 4:
                    showTrainerWorkoutClasses(loggedUser, roles);
                    System.out.print("\nPress Enter to continue...");
                    scanner.nextLine();
                    break;
                case 5:
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        } while (option != QUIT_OPTION);
    }

    private static void showAllWorkoutClasses(ArrayList<Role> roles) {
        ArrayList<WorkoutClass> workoutClasses = WorkoutClassesDAO.getWorkoutClasses(-1,roles);

        if (workoutClasses.isEmpty()) {
            System.out.println("\nNo workout classes available.");
        } else {
            // Print all workout classes
            System.out.println("\nAvailable workout classes:");
            System.out.println("-------------------------------");

            for (WorkoutClass workoutClass : workoutClasses) {
                System.out.println("Workout Class ID: " + workoutClass.getId());
                System.out.println("Workout Class Type: " + workoutClass.getWorkoutClassType().getName());
                System.out.println("Description: " + workoutClass.getDescription());
                System.out.println("Trainer: " + workoutClass.getTrainer().getFullName());
                System.out.println("Date and time: " + workoutClass.getDateTime());
                System.out.println("-------------------------------");
            }
        }
    }

    private static void showTrainerWorkoutClasses(User trainer, ArrayList<Role> roles) {
        ArrayList<WorkoutClass> workoutClasses = WorkoutClassesDAO.getWorkoutClasses(trainer.getUserId(),roles);

        if (workoutClasses.isEmpty()) {
            System.out.println("\nNo workout classes for trainer " + trainer.getFullName() + ".");
        } else {
            // Print all workout classes
            System.out.println("\nWorkout classes of " + trainer.getFullName() + ":");
            System.out.println("-------------------------------");

            for (WorkoutClass workoutClass : workoutClasses) {
                System.out.println("Workout Class ID: " + workoutClass.getId());
                System.out.println("Workout Class Type: " + workoutClass.getWorkoutClassType().getName());
                System.out.println("Description: " + workoutClass.getDescription());
                System.out.println("Date and time: " + workoutClass.getDateTime());
                System.out.println("-------------------------------");
            }
        }
    }

    private static void createNewWorkoutClass(Scanner scanner, User loggedUser) {
        WorkoutClassType workoutClassType = null;
        int workoutClassTypeId;
        String description;
        String dateString;
        String timeString;
        LocalDateTime dateTime;

        String input;

        System.out.println("Enter the Workout Class Type (\"l\" to show the full list):");

        // Check if the user entered "l" to show the list of workout class types or an ID
        do {
            if (scanner.hasNextInt()) {
                workoutClassTypeId = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                // Check if the Workout Class Type with this ID exists
                try {
                    //workoutClassType = new WorkoutClassType(1, "Yoga", "Yoga Exercises");
                    workoutClassType = WorkoutClassTypesDAO.getWorkoutClassType(workoutClassTypeId);
                } catch (Exception e) {
                    System.out.println("Error while retrieving the workout class types.");
                    // TODO Log the error
                    e.printStackTrace();
                }
            } else {
                input = scanner.nextLine();

                if (input.equalsIgnoreCase("l")) {
                    ArrayList<WorkoutClassType> workoutClassTypes = WorkoutClassTypesDAO.getAllWorkoutClassTypes();

                    if (workoutClassTypes.isEmpty()) {
                        System.out.println("No workout class types available.");
                    } else {
                        System.out.println("\nAvailable workout class types:");
                        System.out.println("-------------------------------");

                        for (WorkoutClassType wct : workoutClassTypes) {
                            System.out.println("ID: " + wct.getId());
                            System.out.println("Name: " + wct.getName());
                            System.out.println("Description: " + wct.getDescription());
                            System.out.println("-------------------------------");
                        }
                    }
                } else {
                    System.out.println("Invalid input. Please enter a valid Workout Class Type ID or \"l\" to list the types.");
                }
            }
        } while (workoutClassType == null);


        System.out.println("Enter the description of the new workout class:");
        description = scanner.nextLine();

        while (true) {
            System.out.println("Enter the date of the new workout class (YYYY-MM-DD)");

            dateString = scanner.nextLine();

            try {
                // Parse the date string to LocalDateTime
                dateTime = LocalDateTime.parse(dateString + "T00:00:00");
                break;
            } catch (Exception e) {
                System.out.println("Invalid date format. Please enter the date in YYYY-MM-DD format.");
            }
        }

        while (true) {
            System.out.println("Enter the time of the new workout class (HH:MM)");
            timeString = scanner.nextLine();

            try {
                // Parse the time string to LocalDateTime
                dateTime = dateTime.withHour(Integer.parseInt(timeString.split(":")[0]))
                                   .withMinute(Integer.parseInt(timeString.split(":")[1]));
                break;
            } catch (Exception e) {
                System.out.println("Invalid time format. Please enter the time in HH:MM format.");
            }
        }

        try {
            // TODO Return ID from createWorkoutClassType and display it to the user
            WorkoutClassesDAO.createWorkoutClass(workoutClassType.getId(), description, loggedUser.getUserId(), dateTime);

            System.out.println("Workout class created successfully.\n");
        } catch (Exception e) {
            System.out.println("Error while creating the workout class.\n");
            // TODO Log the error
            e.printStackTrace();
        }
    }

    private static void updateWorkoutClass(Scanner scanner, User loggedUser) {
        WorkoutClass workoutClass = null;

        int workoutClassId;
        int workoutTypeId;
        String workoutTypeName;
        String workoutTypeDescription;

        // Header
        System.out.println();
        System.out.println("\nEnter the ID of the workout class to update: ");

        workoutClassId = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        try {
            // Get the workout class by ID
            workoutClass = WorkoutClassesDAO.getWorkoutClassById(workoutClassId, UserDAO.getRoles());

            if (workoutClass == null) {
                System.out.println("No workout class found with ID: " + workoutClassId);
                return; // Exit if the workout class cannot be found
            }
        } catch (Exception e) {
            System.out.println("\nError while retrieving the workout class.\n");
            // TODO Log the error
            e.printStackTrace();
            return; // Exit if the workout class cannot be found
        }

        System.out.println("Current Workout Class Type: " + workoutClass.getWorkoutClassType().getName());
        System.out.println("Enter the new type of the workout class (ID for existing type, or name): ");

        if (scanner.hasNextInt()){
            workoutTypeId = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            workoutClass.setWorkoutClassType(WorkoutClassTypesDAO.getWorkoutClassType(workoutTypeId));
        } else {
            workoutTypeName = scanner.nextLine();

            System.out.println("Enter the description for the new workout class type:");
            workoutTypeDescription = scanner.nextLine();

            try {
                workoutTypeId = WorkoutClassTypesDAO.createWorkoutClassType(workoutTypeName, workoutTypeDescription);

                WorkoutClassType workoutClassType = new WorkoutClassType(workoutTypeId, workoutTypeName, workoutTypeDescription);
                workoutClass.setWorkoutClassType(workoutClassType);

                // Print success message
                System.out.println("\nWorkout class type created successfully with ID: " + workoutTypeId);
            } catch (Exception e) {
                System.out.println("\nError while creating the workout class type.\n");
                // TODO Log the error
                e.printStackTrace();
                return;
            }
        }

        try {
            WorkoutClassesDAO.updateWorkoutClass(workoutClassId, workoutClass.getWorkoutClassType().getId(),
                    workoutClass.getDescription(), loggedUser.getUserId(), LocalDateTime.now());

            System.out.println("\nWorkout class updated successfully.\n");
        } catch (Exception e) {
            System.out.println("\nError while updating the workout class.\n");
            // TODO Log the error
            e.printStackTrace();
        }
    }

    private static void deleteWorkoutClass(Scanner scanner, User loggedUser) {
        int workoutClassId;
        int deletedRows;

        System.out.println("\nEnter the ID of the workout class to delete: ");
        workoutClassId = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        try {
            deletedRows = WorkoutClassesDAO.deleteWorkoutClass(workoutClassId);

            // Check if any rows were deleted
            if (deletedRows == 0) {
                System.out.println("No Workout Class found with ID: " + workoutClassId);
            } else {
                // If the deletion was successful, print a success message
                System.out.println("Workout Class with ID: " + workoutClassId + " deleted successfully.");
            }
        } catch (Exception e) {
            System.out.println("Error while deleting the workout class.");
            // TODO Log the error
            e.printStackTrace();
        }
    }
}
