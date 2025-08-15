/**
 * Description: Final Sprint - SD13 - Gym Management Program - Menu Class
 * Authors: Ashton Dennis
 *          Joseph Gallant
 *          Justin Greenslade
 * Date(s): August 4, 2025 - August 15, 2025
 */

package MainProgram;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;
import DBManager.DatabaseConnection;
import GymMerchManagement.GymMerchDAO;
import GymMerchandise.GymMerchandise;
import GymMerchandise.MerchandiseType;
import Logging.LoggingManagement;
import MembershipManagement.MembershipTypesDAO;
import MembershipManagement.MembershipsDAO;
import Memberships.Membership;
import Memberships.MembershipType;
import UserManagement.UserDAO;
import UserManagement.UserService;
import Users.*;
import WorkoutClassManagement.WorkoutClassTypeDAO;
import WorkoutClassManagement.WorkoutClassesDAO;
import WorkoutClasses.WorkoutClass;
import WorkoutClasses.WorkoutClassType;

/**
 * Menu class.
 * This class is responsible for handling the main menu and user interactions in the Gym Management System.
 * It provides options for user registration, login, and role-specific functionalities.
 */
public class Menu {
    private static final Scanner scanner = new Scanner(System.in);

    /**
     * Prompts the user to press Enter to continue.
     * This method is used to pause the console output until the user presses Enter.
     */
    public static void enterToContinue() {
        System.out.println();
        System.out.print("Press Enter to continue...");
        scanner.nextLine();
    }

/**
 * Clears the console output.
 *
 * This method prints blank lines first (works in IntelliJ),
 * then uses ANSI escape codes to move the cursor to the top
 * (works in VS Code). Ensures new output starts at the top.
 */
public static void clearConsole() {
    // Print blank lines first
    for (int i = 0; i < 50; i++) {
        System.out.println();
    }

    // ANSI code to move cursor to top
    System.out.print("\033[H");
    System.out.flush();
}


    /**
     * Displays the main menu and handles user interactions.
     * If no user is logged in, it shows options for registration and login.
     * If a user is logged in, it displays role-specific menus.
     *
     * @param scanner      The Scanner object for user input.
     * @param roles        The list of roles available in the system.
     * @param loggedUser   The currently logged-in user, or null if no user is logged in.
     */
    public static void mainMenu(Scanner scanner, ArrayList<Role> roles, User loggedUser) {
        int option = 0;
        final int QUIT_OPTION = 3;

        // If no user is logged in, show the main menu options (login and registration)
        // If a user is logged in, show the Role-specific menu
        do {
            clearConsole();
            if (loggedUser == null) {
                // Header
                System.out.println("\nWelcome to the Gym Management System\nPlease make a selection:\n");

                System.out.println("1. Register");
                System.out.println("2. Login");
                System.out.println("3. Quit");
                System.out.print("> ");

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
                        System.out.println("\nThank you for using the Gym Management System. Goodbye!");
                        break;
                    default:
                        System.out.println("\nInvalid option. Please try again.");
                        enterToContinue();
                }
            } else {
                String roleName = "";

                for (Role role : roles) {
                    if (loggedUser.getRole().getId() == role.getId()) {
                        roleName = role.getName();
                        break;
                    }
                }
                switch (roleName.toLowerCase()) {
                    case "admin":
                        loggedUser = adminMenu(scanner, loggedUser, roles);
                        break;
                    case "trainer":
                        loggedUser = trainerMenu(scanner, loggedUser, roles);
                        break;
                    case "member":
                        loggedUser = memberMenu(scanner, loggedUser, roles);
                        break;
                    default:
                        System.out.println("Unrecognized role.");
                        enterToContinue();
                }
            }
        } while (option != QUIT_OPTION);
    }

    /**
     * Registers a new user by collecting their details and saving them to the database.
     * Validates user input for each field and allows the user to select a role.
     *
     * @param scanner The Scanner object for user input.
     * @param roles   The list of roles available in the system.
     */
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
        clearConsole();
        // Header
        System.out.println("\nPlease enter your details to register:\n");

        // TODO Validate user input for each field
        System.out.println("Enter your username:");
        username = scanner.nextLine();

        System.out.println("\nEnter your password:");
        password = scanner.nextLine();

        System.out.println("\nEnter your first name:");
        firstName = scanner.nextLine();

        System.out.println("\nEnter your last name:");
        lastName = scanner.nextLine();

        System.out.println("\nEnter your address:");
        address = scanner.nextLine();

        System.out.println("\nEnter your city:");
        city = scanner.nextLine();

        System.out.println("\nEnter your province:");
        province = scanner.nextLine();

        System.out.println("\nEnter your postal code:");
        postalCode = scanner.nextLine();

        System.out.println("\nEnter your email:");
        email = scanner.nextLine();

        System.out.println("\nEnter your phone number:");
        phoneNumber = scanner.nextLine();

        // Select role
        do {
            System.out.println("\nEnter your role (admin, trainer, member):");

            roleName = scanner.nextLine();

            for (Role r : roles) {
                if (r.getName().equalsIgnoreCase(roleName)) {
                    role = r;
                    break;
                }
            }

            if (role == null) {
                System.out.println("\nInvalid role. Please try again.");
                enterToContinue();

            }
        } while (role == null);

        // Send the user data to the UserService to register
        User user = new User(0, username, password, firstName, lastName, address, city, province, postalCode, email,
                phoneNumber, role);
        UserService.register(user);
    }

    /**
     * Displays the login menu and prompts the user to enter their credentials.
     * Validates the credentials against the database and returns the logged-in user.
     *
     * @param scanner The Scanner object for user input.
     * @param roles   The list of roles available in the system.
     * @return The logged-in user if credentials are valid, otherwise null.
     */
    private static User loginMenu(Scanner scanner, ArrayList<Role> roles) {
        String username;
        String password;

        User user;
        clearConsole();

        // Header
        System.out.println("\nPlease enter your login credentials:");

        do {
            System.out.println("\nEnter your username:");
            username = scanner.nextLine();

            System.out.println("\nEnter your password:");
            password = scanner.nextLine();

            user = UserService.login(username, password, roles);
        } while (user == null);

        return user;
    }

    /**
     * Displays the admin menu and provides options for managing users, memberships, and merchandise.
     * Allows the admin to view all users, delete a user, view gym memberships, and manage merchandise.
     *
     * @param scanner    The Scanner object for user input.
     * @param loggedUser The currently logged-in user.
     * @param roles      The list of roles available in the system.
     * @return The logged-in user after performing admin actions, or null if logged out.
     */
    private static User adminMenu(Scanner scanner, User loggedUser,  ArrayList<Role> roles) {
        int option = 0;
        final int QUIT_OPTION = 5;

        do {
            clearConsole();
            System.out.println("\nWelcome " + loggedUser.getFirstName());
            System.out.println("Please choose an option:");
            System.out.println("1. View all users and contact information");
            System.out.println("2. Delete a user");
            System.out.println("3. View all gym memberships and total revenue");
            System.out.println("4. Merchandise Management");
            System.out.println("5. Logout");

            try {
                option = Integer.parseInt(scanner.nextLine());

                switch (option) {
                    case 1:
                        viewAllUsers();
                        break;
                    case 2:
                        deleteUser(scanner, roles);
                        break;
                    case 3:
                        viewAllGymMembershipsAndTotalAnnualRevenue();
                        break;
                    case 4:
                        merchManagementMenu(scanner);
                        break;
                    case 5:

                        System.out.println("\nLogging out...\n");
                        loggedUser = null; // Clear the logged user

                        break;
                    default:
                        System.out.println("Invalid option. Please try again.");
                        enterToContinue();
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
                enterToContinue();
            }

        } while (option != QUIT_OPTION);

        return loggedUser;
    }

    /**
     * Displays all users and their contact information in a formatted table.
     * Retrieves user data from the database and prints it to the console.
     */
    private static void viewAllUsers() {
        ArrayList<Role> roles = UserDAO.getRoles();
        final String SQL = "SELECT * FROM users";
        clearConsole();
        try {
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SQL);
            ResultSet resultSet = preparedStatement.executeQuery();
            System.out.println();
            System.out.println("=== All Users and Contact Information ===");

            // Print header row with aligned columns
            System.out.printf("%-8s %-15s %-20s %-30s %-15s %-40s %-10s%n",
                    "User ID", "Username", "Name", "Email", "Phone", "Address", "Role");
            System.out.println(
                    "-----------------------------------------------------------------------------------------------------------------------------------------------");

            while (resultSet.next()) {
                int roleId = resultSet.getInt("role_id");
                Role userRole = roles.stream().filter(r -> r.getId() == roleId).findFirst().orElse(null);

                int userId = resultSet.getInt("user_id");
                String username = resultSet.getString("username");
                String name = resultSet.getString("first_name") + " " + resultSet.getString("last_name");
                String email = resultSet.getString("email");
                String phone = resultSet.getString("phone");

                String address = resultSet.getString("street_address") + ", " +
                        resultSet.getString("city") + ", " +
                        resultSet.getString("province") + ", " +
                        resultSet.getString("postal_code");

                String roleName = (userRole != null) ? userRole.getName() : "Unknown";

                // Print one user per row
                System.out.printf("%-8d %-15s %-20s %-30s %-15s %-40s %-10s%n",
                        userId, username, name, email, phone, address, roleName);

            }
            enterToContinue();
        } catch (SQLException e) {
          String errorMessage = "Error while retrieving users.";

          // Print error message and log the error
          System.out.println(errorMessage);
          LoggingManagement.log(errorMessage + ": " + e.getMessage(), true);
          enterToContinue();
        }
    }

    /**
     * Deletes a user by their ID and removes their associated workout classes if they are a trainer.
     * Prompts the admin to enter the user ID to delete and handles any errors during deletion.
     *
     * @param scanner The Scanner object for user input.
     * @param roles   The list of roles available in the system.
     */
    private static void deleteUser(Scanner scanner, ArrayList<Role> roles) {
        clearConsole();
        System.out.print("Enter user ID to delete: ");
        try {
            int userId = Integer.parseInt(scanner.nextLine());

            User user = UserDAO.getUserById(userId, roles);

            if (user == null) {
                System.out.println("User not found.");
                enterToContinue();
                return;
            }

            // If the user is a trainer delete their workout classes first
            if (user.getRole().getName().equalsIgnoreCase("trainer")) {
                WorkoutClassesDAO.deleteWorkoutClassByTrainerId(userId);
                System.out.println("Deleted workout classes for this trainer.");
            }

            // Then delete the user and their memberships
            UserDAO.deleteUserAndMembershipsByUserId(userId);
            System.out.println("User deleted successfully.");

            enterToContinue();

        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a valid number.");
            enterToContinue();
        }
    }

    /**
     * Displays all gym memberships and calculates the total annual revenue for the current year.
     * Prints a formatted table with membership details and the total revenue.
     */
    private static void viewAllGymMembershipsAndTotalAnnualRevenue() {
        int currentYear = java.time.LocalDate.now().getYear();
        ArrayList<Role> roles = UserDAO.getRoles();
        ArrayList<Membership> memberships = MembershipsDAO.getAllMemberships(roles);
        clearConsole();

        if (memberships.isEmpty()) {
            System.out.println("No memberships found.");
            enterToContinue();
            return;
        }

        double totalAnnualRevenue = 0.0;

        // Print the header row with column titles
        System.out.printf("%-15s %-20s %-20s %-12s %-12s %-15s %-22s%n",
                "Membership ID", "Member Name", "Membership Type", "Start Date", "End Date", "Monthly Cost",
                "Active Months This Year");
        System.out.println(
                "-----------------------------------------------------------------------------------------------" +
                        "----------------------------");

        for (Membership membership : memberships) {
            User member = membership.getMember();
            MembershipType type = membership.getMembershipType();

            double monthlyCost = type.getCost();

            java.time.LocalDate startDate = membership.getStartDate();
            java.time.LocalDate endDate = membership.getEndDate();

            if (endDate == null) {
                endDate = java.time.LocalDate.of(currentYear, 12, 31);
            }

            java.time.LocalDate yearStart = java.time.LocalDate.of(currentYear, 1, 1);
            java.time.LocalDate yearEnd = java.time.LocalDate.of(currentYear, 12, 31);

            if (startDate.isAfter(yearEnd) || endDate.isBefore(yearStart)) {
                continue;
            }

            java.time.LocalDate effectiveStart = (startDate.isBefore(yearStart)) ? yearStart : startDate;
            java.time.LocalDate effectiveEnd = (endDate.isAfter(yearEnd)) ? yearEnd : endDate;

            int activeMonths = java.time.Period.between(
                    effectiveStart.withDayOfMonth(1),
                    effectiveEnd.withDayOfMonth(1)).getMonths() + 1;

            totalAnnualRevenue += activeMonths * monthlyCost;

            String endDateDisplay = (membership.getEndDate() != null) ? membership.getEndDate().toString() : "Ongoing";

            // Print membership details in aligned columns
            System.out.printf("%-15d %-20s %-20s %-12s %-12s $%-14.2f %-22d%n",
                    membership.getMembershipId(),
                    member.getFirstName() + " " + member.getLastName(),
                    type.getName(),
                    startDate.toString(),
                    endDateDisplay,
                    monthlyCost,
                    activeMonths);
        }

        System.out.println(
                "-----------------------------------------------------------------------------------------------" +
                        "----------------------------");
        System.out.printf("Total Annual Revenue for %d: $%.2f%n", currentYear, totalAnnualRevenue);
        enterToContinue();
    }

    /**
     * Displays the merchandise management menu and provides options for adding, editing, deleting,
     * and printing merchandise reports.
     *
     * @param scanner The Scanner object for user input.
     */
    public static void merchManagementMenu(Scanner scanner) {

        while (true) {
            clearConsole();
            System.out.println("\nMerchandise Management Menu");
            System.out.println("1. Add new merchandise");
            System.out.println("2. Edit merchandise");
            System.out.println("3. Delete merchandise");
            System.out.println("4. Print all merchandise report and total stock value");
            System.out.println("5. Back to Admin Menu");
            System.out.print("Enter your choice: ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    addNewMerchandise(scanner);
                    break;
                case "2":
                    editMerchandise(scanner);
                    break;
                case "3":
                    deleteMerchandise(scanner);
                    break;
                case "4":
                    printAllMerchandiseAndStockValue();
                    break;
                case "5":
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    /**
     * Adds new merchandise to the system by prompting the user for details.
     * Validates the merchandise type and checks for duplicates before adding.
     *
     * @param scanner The Scanner object for user input.
     */
    private static void addNewMerchandise(Scanner scanner) {
        clearConsole();
        System.out.print("Enter merchandise type name: ");
        String typeName = scanner.nextLine().trim();

        MerchandiseType type = null;
        ArrayList<MerchandiseType> types = GymMerchDAO.getAllMerchandiseType();
        for (MerchandiseType t : types) {
            if (t.getMerchandiseTypeName().equalsIgnoreCase(typeName)) {
                type = t;
                break;
            }
        }

        if (type == null) {
            GymMerchDAO.createMerchandiseType(typeName);
            types = GymMerchDAO.getAllMerchandiseType();
            for (MerchandiseType t : types) {

                if (t.getMerchandiseTypeName().equalsIgnoreCase(typeName)) {
                    type = t;
                    break;
                }
            }
        }

        System.out.print("Enter merchandise name: ");
        String name = scanner.nextLine().trim();

        ArrayList<GymMerchandise> allMerch = GymMerchDAO.getAllGymMerchandise();
        for (GymMerchandise m : allMerch) {
            if (m.getMerchandiseName().equalsIgnoreCase(name) && m.getMerchandiseType().getId() == type.getId()) {
                System.out.println("Merchandise with that name and type already exists.");
                enterToContinue();
                return;
            }
        }

        System.out.print("Enter merchandise price: ");
        double price = Double.parseDouble(scanner.nextLine());

        System.out.print("Enter quantity in stock: ");
        int quantity = Integer.parseInt(scanner.nextLine());

        GymMerchDAO.createGymMerchandise(type.getId(), name, price, quantity);
        System.out.println("Merchandise added successfully.");
        enterToContinue();
    }

    /**
     * Edits existing merchandise by prompting the user for new details.
     * Validates the merchandise ID and updates the merchandise information.
     *
     * @param scanner The Scanner object for user input.
     */
    private static void editMerchandise(Scanner scanner) {
        clearConsole();
        System.out.print("Enter the ID of the merchandise to edit: ");
        int id = Integer.parseInt(scanner.nextLine());

        GymMerchandise merch = GymMerchDAO.getGymMerchandiseById(id);
        if (merch == null) {
            System.out.println("Merchandise not found.");
            enterToContinue();
            return;
        }

        System.out.println("Editing: " + merch.getMerchandiseName());

        System.out.print("Enter new name (leave blank to keep \"" + merch.getMerchandiseName() + "\"): ");
        String newName = scanner.nextLine();
        if (!newName.trim().isEmpty()) {
            merch.setMerchandiseName(newName);
        }

        System.out.print("Enter new price (leave blank to keep $" + merch.getMerchandisePrice() + "): ");
        String newPrice = scanner.nextLine();
        if (!newPrice.trim().isEmpty()) {
            merch.setMerchandisePrice(Double.parseDouble(newPrice));
        }

        System.out.print("Enter new quantity (leave blank to keep " + merch.getQuantityInStock() + "): ");
        String newQty = scanner.nextLine();
        if (!newQty.trim().isEmpty()) {
            merch.setQuantityInStock(Integer.parseInt(newQty));
        }

        GymMerchDAO.updateGymMerchandise(merch);
        System.out.println("Merchandise updated successfully.");
        enterToContinue();
    }

    /**
     * Deletes merchandise by its ID and confirms the deletion.
     * Prompts the user for the merchandise ID to delete and handles any errors during deletion.
     *
     * @param scanner The Scanner object for user input.
     */
    private static void deleteMerchandise(Scanner scanner) {
        clearConsole();
        System.out.print("Enter the ID of the merchandise to delete: ");
        int id = Integer.parseInt(scanner.nextLine());

        GymMerchandise merch = GymMerchDAO.getGymMerchandiseById(id);
        if (merch != null) {
            GymMerchDAO.deleteGymMerchandise(id);
            System.out.println("Merchandise deleted successfully.");
        } else {
            System.out.println("No merchandise found with that ID.");
        }

        System.out.println("Press Enter to continue...");
        scanner.nextLine();
    }

    /**
     * Prints all merchandise and their total stock value in a formatted table.
     * Calculates the total value of all merchandise in stock.
     */
    private static void printAllMerchandiseAndStockValue() {
        clearConsole();
        ArrayList<GymMerchandise> allMerch = GymMerchDAO.getAllGymMerchandise();
        System.out.println();
        System.out.println("                                === All Merchandise ===");
        System.out.println();
        System.out.println();
        System.out.printf("%5s  %-25s %-20s %10s %10s %15s%n",
                "ID", "Name", "Type", "Price", "Quantity", "Item Value");
        System.out.println(
                "--------------------------------------------------------------------------------------------");

        double totalValue = 0.0;

        for (GymMerchandise merch : allMerch) {
            double itemTotal = merch.getMerchandisePrice() * merch.getQuantityInStock();
            totalValue += itemTotal;

            System.out.printf("%5d  %-25s %-20s %10.2f %10d %15.2f%n",
                    merch.getId(),
                    merch.getMerchandiseName(),
                    merch.getMerchandiseType().getMerchandiseTypeName(),
                    merch.getMerchandisePrice(),
                    merch.getQuantityInStock(),
                    itemTotal);
        }

        System.out.println(
                "--------------------------------------------------------------------------------------------");
                String totalValueStr = "$" + String.format("%.2f", totalValue);
        System.out.printf("%-75s %15s", "Total Stock Value:", totalValueStr);
        enterToContinue();
    }

    /**
     * Prints all merchandise in a formatted table.
     * Displays the merchandise ID, name, type, and price.
     */
    private static void printAllMerchandise() {
        clearConsole();
        ArrayList<GymMerchandise> allMerch = GymMerchDAO.getAllGymMerchandise();

        System.out.println();
        System.out.println("                                === All Merchandise ===");
        System.out.println();

        // Table header
        System.out.printf("%5s  %-25s %-20s %10s%n",
                "ID", "Name", "Type", "Price");
        System.out.println("--------------------------------------------------------------------------------");

        // Table rows
        for (GymMerchandise merch : allMerch) {
            System.out.printf("%5d  %-25s %-20s %10.2f%n",
                    merch.getId(),
                    merch.getMerchandiseName(),
                    merch.getMerchandiseType().getMerchandiseTypeName(),
                    merch.getMerchandisePrice());
        }

        System.out.println("--------------------------------------------------------------------------------");
        enterToContinue();
    }

    /**
     * Displays the trainer menu and provides options for managing workout classes, purchasing memberships,
     * and viewing gym merchandise.
     *
     * @param scanner    The Scanner object for user input.
     * @param loggedUser The currently logged-in user.
     * @param roles      The list of roles available in the system.
     * @return The logged-in user after performing trainer actions, or null if logged out.
     */
    private static User trainerMenu(Scanner scanner, User loggedUser, ArrayList<Role> roles) {
        int option = 0;
        final int QUIT_OPTION = 4;

        do {
            clearConsole();
            // Header
            System.out.println("\nWelcome, " + loggedUser.getFirstName() + "\nPlease make a selection:\n");

            System.out.println("1. Manage my workout classes");
            System.out.println("2. Purchase a membership");
            System.out.println("3. Show the gym merchandise");
            System.out.println("4. Logout");

            option = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (option) {
                case 1:
                    manageWorkoutClasses(scanner, loggedUser, roles);
                    break;
                case 2:
                    purchaseMembership(scanner, loggedUser);
                    break;
                case 3:
                    printAllMerchandise();
                    break;
                case 4:
                    System.out.println("\nLogging out...\n");
                    loggedUser = null; // Clear the logged user
                    break;
                default:
                    System.out.println("\nInvalid option. Please try again.");
                    enterToContinue();

            }

        } while (option != QUIT_OPTION);

        return loggedUser;
    }

    /**
     * Displays the member menu and provides options for browsing workout classes, viewing membership expenses,
     * purchasing memberships, and viewing gym merchandise.
     *
     * @param scanner    The Scanner object for user input.
     * @param loggedUser The currently logged-in user.
     * @param roles      The list of roles available in the system.
     * @return The logged-in user after performing member actions, or null if logged out.
     */
    private static User memberMenu(Scanner scanner, User loggedUser, ArrayList<Role> roles) {
        int option;
        final int QUIT_OPTION = 5;

        do {
            clearConsole();
            //Header
            System.out.println("Welcome " + loggedUser.getFirstName() + "\nPlease make a selection:\n");

            System.out.println("1. Browse available workout classes");
            System.out.println("2. View total membership expenses");
            System.out.println("3. Purchase a membership");
            System.out.println("4. Show the gym merchandise");
            System.out.println("5. Logout");
            System.out.print("> ");

            option = scanner.nextInt();
            scanner.nextLine();

            switch (option) {
                case 1:
                    showAllWorkoutClasses(roles);

                    break;
                case 2:
                    showMembershipExpenses(loggedUser, roles);
                    break;
                case 3:
                    purchaseMembership(scanner, loggedUser);
                    break;
                case 4:
                    printAllMerchandise();

                    break;
                case 5:
                    // Logout user
                    System.out.println("\nLogging out...\n");
                    loggedUser = null; // Clear the logged user
                    enterToContinue();
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
                    enterToContinue();
            }

        } while (option != QUIT_OPTION);

        return loggedUser;
    }

    /**
     * Manages workout classes for trainers, allowing them to create, update, delete, and view their classes.
     * Provides a menu for trainers to perform these actions.
     *
     * @param scanner    The Scanner object for user input.
     * @param loggedUser The currently logged-in trainer user.
     * @param roles      The list of roles available in the system.
     */
    private static void manageWorkoutClasses(Scanner scanner, User loggedUser, ArrayList<Role> roles) {
        int option = 0;
        final int QUIT_OPTION = 5;

        do {
            clearConsole();
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
                    break;
                case 2:
                    updateWorkoutClass(scanner, loggedUser);
                    break;
                case 3:
                    deleteWorkoutClass(scanner, loggedUser);
                    break;
                case 4:
                    showTrainerWorkoutClasses(loggedUser, roles);
                    break;
                case 5:
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
                    enterToContinue();
            }
        } while (option != QUIT_OPTION);
    }

    /**
     * This method retrieves and displays all available workout classes from the database.
     * It formats the output in a table with columns for ID, type, description, trainer, and date & time.
     *
     * @param roles The list of roles available in the system to filter workout classes.
     */
    private static void showAllWorkoutClasses(ArrayList<Role> roles) {
        ArrayList<WorkoutClass> workoutClasses = WorkoutClassesDAO.getWorkoutClasses(-1, roles);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        clearConsole();

        if (workoutClasses.isEmpty()) {
            System.out.println("\nNo workout classes available.");
            enterToContinue();
        } else {
            System.out.println();
            System.out.println("                                === Available Workout Classes ===");
            System.out.println();

            // Table header
            System.out.printf("%5s  %-20s %-35s %-20s %-20s%n",
                    "ID", "Type", "Description", "Trainer", "Date & Time");
            System.out.println(
                    "---------------------------------------------------------------------------------------------------------------");

            // Table rows
            for (WorkoutClass workoutClass : workoutClasses) {
                System.out.printf("%5d  %-20s %-35s %-20s %-20s%n",
                        workoutClass.getId(),
                        workoutClass.getWorkoutClassType().getName(),
                        workoutClass.getDescription(),
                        workoutClass.getTrainer().getFullName(),
                        workoutClass.getDateTime().format(formatter));
            }

            System.out.println(
                    "---------------------------------------------------------------------------------------------------------------");
            enterToContinue();
        }
    }

    /**
     * Displays the workout classes for a specific trainer.
     * Retrieves the workout classes from the database and prints them in a formatted table.
     *
     * @param trainer The trainer whose workout classes are to be displayed.
     * @param roles   The list of roles available in the system to filter workout classes.
     */
    private static void showTrainerWorkoutClasses(User trainer, ArrayList<Role> roles) {
        clearConsole();
        ArrayList<WorkoutClass> workoutClasses = WorkoutClassesDAO.getWorkoutClasses(trainer.getUserId(), roles);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        if (workoutClasses.isEmpty()) {
            System.out.println("\nNo workout classes for trainer " + trainer.getFullName() + ".");
            enterToContinue();
        } else {
            // Print all workout classes
            System.out.println("\nWorkout classes of " + trainer.getFullName() + ":");
            System.out.println("-------------------------------");

            for (WorkoutClass workoutClass : workoutClasses) {
                System.out.println("Workout Class ID: " + workoutClass.getId());
                System.out.println("Workout Class Type: " + workoutClass.getWorkoutClassType().getName());
                System.out.println("Description: " + workoutClass.getDescription());
                // Format the date and time
                System.out.println("Date and time: " + workoutClass.getDateTime().format(formatter));
                System.out.println("-------------------------------");
            }
            enterToContinue();
        }
    }

    /**
     * Prompts the user to enter details for a new workout class, including type, description, date, and time.
     * Validates the input and creates the new workout class in the database.
     *
     * @param scanner    The Scanner object for user input.
     * @param loggedUser The currently logged-in user who is creating the workout class.
     */
    private static void createNewWorkoutClass(Scanner scanner, User loggedUser) {
        WorkoutClassType workoutClassType = null;
        int workoutClassTypeId;
        String description;
        String dateString;
        String timeString;
        LocalDateTime dateTime;

        String input;
        clearConsole();

        // Check if the user entered "l" to show the list of workout class types or an ID
        do {
            System.out.println("\nEnter the Workout Class Type ID (\"l\" to show available options):");

            if (scanner.hasNextInt()) {
                workoutClassTypeId = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                // Check if the Workout Class Type with this ID exists
                try {
                    workoutClassType = WorkoutClassTypeDAO.getWorkoutClassType(workoutClassTypeId);
                } catch (Exception e) {
                    String errorMessage = "Error while retrieving the workout class type with ID: "
                            + workoutClassTypeId;

                    System.out.println(errorMessage);
                    LoggingManagement.log(errorMessage + ": " + e.getMessage(), true);
                    enterToContinue();
                }
            } else {
                input = scanner.nextLine();

                if (input.equalsIgnoreCase("l")) {
                    ArrayList<WorkoutClassType> workoutClassTypes = WorkoutClassTypeDAO.getAllWorkoutClassTypes();

                    if (workoutClassTypes.isEmpty()) {
                        System.out.println("No workout class types available.");
                        enterToContinue();
                        return;

                    } else {
                        System.out.println("\nAvailable workout class types:");
                        System.out.println("-------------------------------");

                        for (WorkoutClassType wct : workoutClassTypes) {
                            System.out.println("ID: " + wct.getId());
                            System.out.println("Name: " + wct.getName());
                            System.out.println("Description: " + wct.getDescription());
                            System.out.println("-------------------------------");
                        }
                        enterToContinue();
                    }
                } else {
                    System.out.println(
                            "Invalid input. Please enter a valid Workout Class Type ID or \"l\" to list the types.");
                            enterToContinue();
                }
            }
        } while (workoutClassType == null);

        System.out.println("\nEnter the description of the new workout class:");
        description = scanner.nextLine();

        while (true) {
            System.out.println("\nEnter the date of the new workout class (YYYY-MM-DD)");

            dateString = scanner.nextLine();

            try {
                // Parse the date string to LocalDateTime
                dateTime = LocalDateTime.parse(dateString + "T00:00:00");
                break;
            } catch (Exception e) {
                System.out.println("Invalid date format. Please enter the date in YYYY-MM-DD format.");
                enterToContinue();
            }
        }

        while (true) {
            System.out.println("\nEnter the time of the new workout class (HH:MM)");
            timeString = scanner.nextLine();

            try {
                // Parse the time string to LocalDateTime
                dateTime = dateTime.withHour(Integer.parseInt(timeString.split(":")[0]))
                        .withMinute(Integer.parseInt(timeString.split(":")[1]));
                break;
            } catch (Exception e) {
                System.out.println("Invalid time format. Please enter the time in HH:MM format.");
                enterToContinue();
            }
        }

        try {
            int newWorkoutClassId = WorkoutClassesDAO.createWorkoutClass(workoutClassType.getId(), description,
                    loggedUser.getUserId(), dateTime);

            System.out.println("\nWorkout class with ID: " + newWorkoutClassId + " created successfully.\n");
            enterToContinue();
        } catch (Exception e) {
            String errorMessage = "Error while creating the workout class.";

            System.out.println(errorMessage);
            LoggingManagement.log(errorMessage + ": " + e.getMessage(), true);
            enterToContinue();
        }
    }

    /**
     * Updates an existing workout class by prompting the user for new details.
     * Validates the input and updates the workout class in the database.
     *
     * @param scanner    The Scanner object for user input.
     * @param loggedUser The currently logged-in user who is updating the workout class.
     */
    private static void updateWorkoutClass(Scanner scanner, User loggedUser) {
        WorkoutClass workoutClass = null;

        int workoutClassId;
        int workoutTypeId;
        String workoutTypeName;
        String workoutTypeDescription;
        String workoutClassDate;
        String workoutClassTime;
        LocalDateTime workoutClassDateTime = null;
        clearConsole();
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
                enterToContinue();
                return; // Exit if the workout class cannot be found
            }
        } catch (Exception e) {

            String errorMessage = "Error while retrieving the workout class with ID: " + workoutClassId;
            
            System.out.println(errorMessage);
            LoggingManagement.log(errorMessage + ": " + e.getMessage(), true);
            enterToContinue();

            return; // Exit if the workout class cannot be found
        }

        System.out.println("\nCurrent Workout Class Type: " + workoutClass.getWorkoutClassType().getName());
        System.out.println("\nEnter the new type of the workout class (ID for existing type, or name): ");

        if (scanner.hasNextInt()) {
            workoutTypeId = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            workoutClass.setWorkoutClassType(WorkoutClassTypeDAO.getWorkoutClassType(workoutTypeId));
        } else {
            workoutTypeName = scanner.nextLine();

            System.out.println("\nEnter the description for the new workout class type:");
            workoutTypeDescription = scanner.nextLine();

            // Validate date and time input
            do {
                System.out.println("\nEnter the date of the workout class (YYYY-MM-DD):");
                workoutClassDate = scanner.nextLine();

                System.out.println("\nEnter the time of the workout class (HH:MM):");
                workoutClassTime = scanner.nextLine();

                try {
                    // Parse the date and time to LocalDateTime
                    workoutClassDateTime = LocalDateTime.parse(workoutClassDate + "T" + workoutClassTime + ":00");
                } catch (Exception e) {
                    System.out.println(
                            "Invalid date or time format. Please enter the date in YYYY-MM-DD and time in HH:MM format.");
                            enterToContinue();
                }
            } while (workoutClassDateTime == null);

            // Create the new workout class type in the database
            try {
                workoutTypeId = WorkoutClassTypeDAO.createWorkoutClassType(workoutTypeName, workoutTypeDescription);

                WorkoutClassType workoutClassType = new WorkoutClassType(workoutTypeId, workoutTypeName,
                        workoutTypeDescription);
                workoutClass.setWorkoutClassType(workoutClassType);

                // Print success message
                String successMessage = "Workout class type created successfully with ID: " + workoutTypeId;

                System.out.println(successMessage);
                enterToContinue();
                LoggingManagement.log(successMessage, false);
            } catch (Exception e) {
                String errorMessage = "Error while creating the workout class type.";

                System.out.println(errorMessage);
                LoggingManagement.log(errorMessage + ": " + e.getMessage(), true);
                                enterToContinue();

                return;
            }
        }

        try {
            // Update the workout class in the database with new values
            WorkoutClassesDAO.updateWorkoutClass(workoutClassId, workoutClass.getWorkoutClassType().getId(),
                    workoutClass.getDescription(), loggedUser.getUserId(), workoutClassDateTime);

            System.out.println("\nWorkout class updated successfully.\n");

            // Print success message
            LoggingManagement.log("Workout class with ID: " + workoutClassId + " updated successfully.", false);
            enterToContinue();
        } catch (Exception e) {
            String errorMessage = "Error while updating the workout class with ID: " + workoutClassId;
            
            System.out.println(errorMessage);
            LoggingManagement.log(errorMessage + ": " + e.getMessage(), true);
            enterToContinue();

        }
    }

    /**
     * Deletes a workout class by its ID and confirms the deletion.
     * Prompts the user for the workout class ID to delete and handles any errors during deletion.
     *
     * @param scanner    The Scanner object for user input.
     * @param loggedUser The currently logged-in user who is deleting the workout class.
     */
    private static void deleteWorkoutClass(Scanner scanner, User loggedUser) {
        int workoutClassId;
        int deletedRows;
        clearConsole();
        System.out.println("\nEnter the ID of the workout class to delete: ");
        workoutClassId = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        try {
            deletedRows = WorkoutClassesDAO.deleteWorkoutClass(workoutClassId);

            // Check if any rows were deleted
            if (deletedRows > 0) {
                // If the deletion was successful, print a success message
                System.out.println("Workout Class with ID: " + workoutClassId + " deleted successfully.");
                enterToContinue();
            }

        } catch (Exception e) {

            String errorMessage = "Error while deleting the workout class with ID." + workoutClassId;
            System.out.println(errorMessage);
            enterToContinue();
            LoggingManagement.log(errorMessage + ": " + e.getMessage(), true);

        }
    }

    /**
     * Allows a user to purchase a membership by selecting a membership type and duration.
     * Prompts the user for input and creates the membership in the database.
     *
     * @param scanner    The Scanner object for user input.
     * @param loggedUser The currently logged-in user who is purchasing the membership.
     */
    private static void purchaseMembership(Scanner scanner, User loggedUser) {
        // int Type id, int member id, localdate start date, localdate end date
        int memberID = loggedUser.getUserId();
        ArrayList<MembershipType> types = MembershipTypesDAO.getAllMembershipTypes();

        clearConsole();
        System.out.println("Please select a membership type (1-3): \n");

        for (MembershipType type : types) {
            System.out.println(
                    type.getId() + " " + type.getName() + " " + "($" + type.getCost() + ") " + type.getDescription());
            // System.out.println(type.getDescription());

        }

        int selectedType;
        while (true) {
            System.out.print("> ");
            selectedType = scanner.nextInt();
            if (selectedType < 1 || selectedType > types.size()) {
                System.out.println("That is not a valid option, try again");
            } else {
                break;
            }
        }

        clearConsole();
        System.out.println("Please select a membership duration: \n");

        System.out.println("1 (3 months)");
        System.out.println("2 (6 months)");
        System.out.println("3 (1 year)");

        LocalDate start;
        LocalDate end;
        while (true) {
            System.out.print("> ");
            int selectedDuration = scanner.nextInt();

            start = LocalDate.now();

            if (selectedDuration == 1) {
                end = start.plusMonths(3);
                break;
            } else if (selectedDuration == 2) {
                end = start.plusMonths(6);
                break;
            } else if (selectedDuration == 3) {
                end = start.plusYears(1);
                break;
            } else {
                System.out.println("That is not a valid option, try again");
            }
        }

        int result = MembershipsDAO.createMembership(selectedType, memberID, start, end);

        clearConsole();
        if (result != -1) {
            System.out.println("Membership purchased sucessfully. We hope you enjoy your workouts!");
            System.out.println("Membership id: " + result);
        } else {
            System.out.println("There was an error creating your memebrship, please wait a few minutes and try again.");
        }

        enterToContinue();
    }

    /**
     * Displays a receipt of all memberships for the logged-in user, including costs and total expenses.
     * Retrieves the memberships from the database and formats the output in a table.
     *
     * @param loggedUser The currently logged-in user whose membership expenses are to be displayed.
     * @param roles      The list of roles available in the system to filter memberships.
     */
    private static void showMembershipExpenses(User loggedUser, ArrayList<Role> roles) {
        // get all memberships for the user
        ArrayList<Membership> memberships = MembershipsDAO.getUserMemberships(loggedUser.getUserId(), roles);

        if (memberships.isEmpty()) {
            System.out.println("No memberships found.");
            enterToContinue();
            return;
        }

        clearConsole();
        System.out.println("=== Membership Expenses Receipt ===\n");

        double totalExpenses = 0;

        // headings
        System.out.printf("%-20s %-15s %-15s %-15s%n", "Membership Type", "Cost/Month", "Term Length", "Total Cost");
        System.out.println("-------------------------------------------------------------------");

        for (Membership membership : memberships) {

            LocalDate startDate = membership.getStartDate();
            LocalDate endDate = membership.getEndDate();
            MembershipType type = membership.getMembershipType();

            if (endDate == null) {
                System.out.println(type.getName() + " - ERROR: No end date");
                continue;
            }

            // calculate exact number of months between start and end
            int termMonths = (endDate.getYear() - startDate.getYear()) * 12
                           + (endDate.getMonthValue() - startDate.getMonthValue());

            // adjust if end day is before start day
            if (endDate.getDayOfMonth() < startDate.getDayOfMonth()) {
                termMonths--;
            }

            // calculate costs
            double monthlyCost = type.getCost();
            double totalCost = monthlyCost * termMonths;

            totalExpenses += totalCost;

            // print each membership row
            System.out.printf("%-20s $%-14.2f %-15s $%-14.2f%n",
                    type.getName(),
                    monthlyCost,
                    termMonths + " months",
                    totalCost);
        }

        System.out.println("-------------------------------------------------------------------");
        System.out.printf("Total Membership Expenses: $%.2f%n", totalExpenses);

        enterToContinue();
    }

}


