/**
 * Description: Final Sprint - SD13 - Gym Management Program - Menu Class
 * Authors: Ashton Dennis
 *          Joseph Gallant
 *          Justin Greenslade
 * Date(s): August 4, 2025
 */

package MainProgram;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
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
import WorkoutClassManagement.WorkoutClassTypesDAO;
import WorkoutClassManagement.WorkoutClassesDAO;
import WorkoutClasses.WorkoutClass;
import WorkoutClasses.WorkoutClassType;
import Users.*;
import WorkoutClassManagement.*;
import WorkoutClasses.*;



public class Menu {

    private static final Scanner scanner = new Scanner(System.in);

    public static void enterToContinue() {
        System.out.println();
        System.out.print("Press Enter to continue...");
        scanner.nextLine();
    }

    public static void clearConsole() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
    

    public static void mainMenu(Scanner scanner, ArrayList<Role> roles, User loggedUser){
        int option = 0;
        final int QUIT_OPTION = 3;
        ;

        // If no user is logged in. Show Login/Registration
        do {
            clearConsole();
            if (loggedUser == null) {
                // Header
                System.out.println("Welcome to the Gym Management System\nPlease make a selection:\n");

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
                        System.out.println("Thank you for using the Gym Management System. Goodbye!");
                        break;
                    default:
                        System.out.println("Invalid option. Please try again.");
                        enterToContinue();
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
                        loggedUser = memberMenu(scanner, loggedUser, roles);
                        break;
                    default:
                        System.out.println("Unrecognized role.");
                        enterToContinue();
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
        clearConsole();
        // Header
        System.out.println("\nPlease enter your details to register:\n");

        // TODO Validate user input for each field
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
                enterToContinue();
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
        clearConsole();
        // Header
        System.out.println("\nPlease enter your login credentials:\n");

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
                    deleteUser(scanner);
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
        System.out.println("-----------------------------------------------------------------------------------------------------------------------------------------------");

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
        System.out.println("Error retrieving users.");
        enterToContinue();
        e.printStackTrace();
    }
}

    private static void deleteUser(Scanner scanner) {
    clearConsole();
    System.out.print("Enter user ID to delete: ");
    try {
        int userId = Integer.parseInt(scanner.nextLine());

        // Call DAO method that deletes user and their memberships by user ID
        UserDAO.deleteUserAndMembershipsByUserId(userId);
         enterToContinue();

    } catch (NumberFormatException e) {
        System.out.println("Invalid input. Please enter a valid number.");
        enterToContinue();
    }
}

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
        "Membership ID", "Member Name", "Membership Type", "Start Date", "End Date", "Monthly Cost", "Active Months This Year");
    System.out.println("-----------------------------------------------------------------------------------------------" +
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
            effectiveEnd.withDayOfMonth(1)
        ).getMonths() + 1;

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

    System.out.println("-----------------------------------------------------------------------------------------------" +
                       "----------------------------");
    System.out.printf("Total Annual Revenue for %d: $%.2f%n", currentYear, totalAnnualRevenue);
    enterToContinue();
}

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


    private static void printAllMerchandiseAndStockValue() {
    clearConsole();
    ArrayList<GymMerchandise> allMerch = GymMerchDAO.getAllGymMerchandise();
    System.out.println();
    System.out.println("                                === All Merchandise ===");
    System.out.println();
        System.out.println();
    System.out.printf("%5s  %-25s %-20s %10s %10s %15s%n",
        "ID", "Name", "Type", "Price", "Quantity", "Item Value");
    System.out.println("--------------------------------------------------------------------------------------------");

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

    System.out.println("--------------------------------------------------------------------------------------------");
    System.out.printf("%-75s %15.2f%n", "Total Stock Value:", totalValue);
    enterToContinue();
}

    private static void printAllMerchandise() {
        ArrayList<GymMerchandise> allMerch = GymMerchDAO.getAllGymMerchandise();

        System.out.println("\n--- All Merchandise ---");

        for (GymMerchandise merch : allMerch) {
            System.out.println("Name: " + merch.getMerchandiseName());
            System.out.println("Type: " + merch.getMerchandiseType().getMerchandiseTypeName());
            System.out.println("Price: $" + merch.getMerchandisePrice());
            System.out.println("-----------------------------");
        }
        enterToContinue();
    }

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
            scanner.nextLine();

            switch (option){
                case 1:
                    manageWorkoutClasses(scanner, loggedUser, roles);
                    break;
                case 2:
                    // Purchase membership
                    break;
                case 3:
                    printAllMerchandise();
                    System.out.print("\nPress Enter to continue...");
                    scanner.nextLine();
                    break;
                case 4:
                    System.out.println("\nLogging out...\n");
                    loggedUser = null; // Clear the logged user
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
                    enterToContinue();
            }

        } while (option != QUIT_OPTION);

        return loggedUser;
    }

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
                    // Browse workout classes
                    showAllWorkoutClasses(roles);

                    break;
                case 2:
                    // Show membership expenses
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
;
                    break;
                case 5:
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
                    enterToContinue();
            }
        } while (option != QUIT_OPTION);
    }

    private static void showAllWorkoutClasses(ArrayList<Role> roles) {
        ArrayList<WorkoutClass> workoutClasses = WorkoutClassesDAO.getWorkoutClasses(-1,roles);
        clearConsole();
        if (workoutClasses.isEmpty()) {
            System.out.println("\nNo workout classes available.");
            enterToContinue();
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
            enterToContinue();
        }
    }

    private static void showTrainerWorkoutClasses(User trainer, ArrayList<Role> roles) {
        clearConsole();
        ArrayList<WorkoutClass> workoutClasses = WorkoutClassesDAO.getWorkoutClasses(trainer.getUserId(),roles);

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
                System.out.println("Date and time: " + workoutClass.getDateTime());
                System.out.println("-------------------------------");
            }
            enterToContinue();
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
        clearConsole();

        System.out.println("Enter the Workout Class Type (\"l\" to show the full list):");

        // Check if the user entered "l" to show the list of workout class types or an ID
        do {
            if (scanner.hasNextInt()) {
                workoutClassTypeId = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                // Check if the Workout Class Type with this ID exists
                try {
                    workoutClassType = WorkoutClassTypesDAO.getWorkoutClassType(workoutClassTypeId);
                    enterToContinue();
                } catch (Exception e) {
                    String errorMessage = "Error while retrieving the workout class type with ID: " + workoutClassTypeId;
 enterToContinue();
                    System.out.println(errorMessage);
                    LoggingManagement.log(errorMessage + ": " + e.getMessage(), true);
                }
            } else {
                input = scanner.nextLine();

                if (input.equalsIgnoreCase("l")) {
                    ArrayList<WorkoutClassType> workoutClassTypes = WorkoutClassTypesDAO.getAllWorkoutClassTypes();

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
                enterToContinue();
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
                enterToContinue();
            }
        }

        try {
            int newWorkoutClassId = WorkoutClassesDAO.createWorkoutClass(workoutClassType.getId(), description, loggedUser.getUserId(), dateTime);


            System.out.println("Workout class with ID: " + newWorkoutClassId + " created successfully.\n");
                    enterToContinue();
        } catch (Exception e) {
            String errorMessage = "Error while creating the workout class.";

            System.out.println(errorMessage);
            LoggingManagement.log(errorMessage + ": " + e.getMessage(), true);
                      enterToContinue();
        }
    }

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
            enterToContinue();
            System.out.println(errorMessage);
            LoggingManagement.log(errorMessage + ": " + e.getMessage(), true);

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

            do {
                System.out.println("Enter the date of the workout class (YYYY-MM-DD):");
                workoutClassDate = scanner.nextLine();

                System.out.println("Enter the time of the workout class (HH:MM):");
                workoutClassTime = scanner.nextLine();

                try {
                    // Parse the date and time to LocalDateTime
                    workoutClassDateTime = LocalDateTime.parse(workoutClassDate + "T" + workoutClassTime + ":00");
                } catch (Exception e) {
                    System.out.println("Invalid date or time format. Please enter the date in YYYY-MM-DD and time in HH:MM format.");
                }
            } while (workoutClassDateTime == null);

            try {
                workoutTypeId = WorkoutClassTypesDAO.createWorkoutClassType(workoutTypeName, workoutTypeDescription);

                WorkoutClassType workoutClassType = new WorkoutClassType(workoutTypeId, workoutTypeName, workoutTypeDescription);
                workoutClass.setWorkoutClassType(workoutClassType);

                // Print success message

                String successMessage = "Workout class type created successfully with ID: " + workoutTypeId;

                System.out.println(successMessage);
                              enterToContinue();
                LoggingManagement.log(successMessage, false);
            } catch (Exception e) {
                String errorMessage = "Error while creating the workout class type.";
           enterToContinue();
                System.out.println(errorMessage);
                LoggingManagement.log(errorMessage + ": " + e.getMessage(), true);

                return;
            }
        }

        try {
            WorkoutClassesDAO.updateWorkoutClass(workoutClassId, workoutClass.getWorkoutClassType().getId(),
                    workoutClass.getDescription(), loggedUser.getUserId(), workoutClassDateTime);

            System.out.println("\nWorkout class updated successfully.\n");

            LoggingManagement.log("Workout class with ID: " + workoutClassId + " updated successfully.", false);
                 enterToContinue();
        } catch (Exception e) {
            String errorMessage = "Error while updating the workout class with ID: " + workoutClassId;
          enterToContinue();
            System.out.println(errorMessage);
            LoggingManagement.log(errorMessage + ": " + e.getMessage(), true);

        }
    }

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
            if (deletedRows == 0) {
                System.out.println("No Workout Class found with ID: " + workoutClassId);
                enterToContinue();
            } else {
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

    private static void purchaseMembership(Scanner scanner, User loggedUser) {
        // int Type id, int member id, localdate start date, localdate end date
        int memberID = loggedUser.getUserId();
        ArrayList<MembershipType> types = MembershipTypesDAO.getAllMembershipTypes();

        clearConsole();
        System.out.println("Please select a membership type: \n");

        for (MembershipType type : types) {
            System.out.println(type.getId() + ":");
            System.out.println(type.getName());
            System.out.println(type.getDescription());
            System.out.println("$" + type.getCost() + "\n");
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

        System.out.println("1: 3 months");
        System.out.println("2: 6 months");
        System.out.println("3: 1 year");

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
}
