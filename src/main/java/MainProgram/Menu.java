/**
 * Description: Final Sprint - SD13 - Gym Management Program - Menu Class
 * Authors: Asthon Dennis
 *          Joseph Gallant
 *          Justin Greenslade
 * Date(s): August 4, 2025
 */

package MainProgram;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

import DBManager.DatabaseConnection;
import GymMerchManagement.GymMerchDAO;
import GymMerchandise.GymMerchandise;
import GymMerchandise.MerchandiseTypes;
import MembershipManagement.MembershipsDAO;
import Memberships.Membership;
import Memberships.MembershipType;
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
                System.out.print("> ");

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
                        loggedUser = trainerMenu(scanner, loggedUser, roles);
                        break;
                    case "member":
                        loggedUser = memberMenu(scanner, loggedUser, roles);
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
    final int QUIT_OPTION = 5;

    do {
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
                    viewAllMembershipsAndRevenue();
                    break;
                case 4:
                    merchManagementMenu(scanner); // already implemented by you
                    break;
                case 5:
                    System.out.println("Logging out...");
                    loggedUser = null;
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a number.");
        }

    } while (option != QUIT_OPTION);

    return loggedUser;
}

private static void viewAllUsers() {
    ArrayList<Role> roles = UserDAO.getRoles();
    final String SQL = "SELECT * FROM users";

    try {
        Connection connection = DatabaseConnection.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(SQL);
        ResultSet resultSet = preparedStatement.executeQuery();

        System.out.println("\n=== All Users and Contact Information ===");

        while (resultSet.next()) {
            int roleId = resultSet.getInt("role_id");
            Role userRole = roles.stream().filter(r -> r.getId() == roleId).findFirst().orElse(null);

            System.out.println("User ID: " + resultSet.getInt("user_id"));
            System.out.println("Username: " + resultSet.getString("username"));
            System.out.println("Name: " + resultSet.getString("first_name") + " " + resultSet.getString("last_name"));
            System.out.println("Email: " + resultSet.getString("email"));
            System.out.println("Phone: " + resultSet.getString("phone"));
            System.out.println("Address: " + resultSet.getString("street_address") + ", " +
                    resultSet.getString("city") + ", " +
                    resultSet.getString("province") + ", " +
                    resultSet.getString("postal_code"));
            System.out.println("Role: " + (userRole != null ? userRole.getName() : "Unknown"));
            System.out.println("---------------------------------------------------");
        }

    } catch (SQLException e) {
        System.out.println("Error retrieving users.");
        e.printStackTrace();
    }
}

private static void deleteUser(Scanner scanner) {
    System.out.print("Enter user ID to delete: ");
    try {
        int userId = Integer.parseInt(scanner.nextLine());
        ArrayList<Role> roles = UserDAO.getRoles();
        User userToDelete = UserDAO.getUserById(userId, roles);

        if (userToDelete != null) {
            UserDAO.deleteUser(userToDelete);
        } else {
            System.out.println("User with ID " + userId + " not found.");
        }

    } catch (NumberFormatException e) {
        System.out.println("Invalid input. Please enter a valid number.");
    }
}

private static void viewAllMembershipsAndRevenue() {
    ArrayList<Role> roles = UserDAO.getRoles();
    ArrayList<Membership> memberships = MembershipsDAO.getAllMemberships(roles);

    if (memberships.isEmpty()) {
        System.out.println("No memberships found.");
        return;
    }

    double totalRevenue = 0.0;

    System.out.println("\n========== All Gym Memberships ==========");

    for (Membership membership : memberships) {
        User member = membership.getMember();
        MembershipType type = membership.getMembershipType();

        System.out.println("----------------------------------------");
        System.out.println("Membership ID: " + membership.getMembershipId());
        System.out.println("Member Name: " + member.getFirstName() + " " + member.getLastName());
        System.out.println("Membership Type: " + type.getName());
        System.out.println("Description: " + type.getDescription());
        System.out.println("Start Date: " + membership.getStartDate());
        System.out.println("End Date: " + (membership.getEndDate() != null ? membership.getEndDate() : "Ongoing"));
        System.out.println("Cost: $" + String.format("%.2f", type.getCost()));

        totalRevenue += type.getCost();
    }

    System.out.println("========================================");
    System.out.printf("Total Revenue from All Memberships: $%.2f%n", totalRevenue);
}

    public static void merchManagementMenu(Scanner scanner) {
    while (true) {
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
        System.out.print("Enter merchandise type name: ");
        String typeName = scanner.nextLine().trim();

        MerchandiseTypes type = null;
        ArrayList<MerchandiseTypes> types = GymMerchDAO.getAllMerchandiseTypes();

        for (MerchandiseTypes t : types) {
            if (t.getMerchandiseTypeName().equalsIgnoreCase(typeName)) {
                type = t;
                break;
            }
        }

        if (type == null) {
            GymMerchDAO.createMerchandiseType(typeName);
            types = GymMerchDAO.getAllMerchandiseTypes();
            for (MerchandiseTypes t : types) {
                if (t.getMerchandiseTypeName().equalsIgnoreCase(typeName)) {
                    type = t;
                    break;
                }
            }
        }

        System.out.print("Enter merchandise name: ");
        String name = scanner.nextLine();

        System.out.print("Enter merchandise price: ");
        double price = Double.parseDouble(scanner.nextLine());

        System.out.print("Enter quantity in stock: ");
        int quantity = Integer.parseInt(scanner.nextLine());

        GymMerchDAO.createGymMerchandise(type.getId(), name, price, quantity);
        System.out.println("Merchandise added successfully.");
    }

    private static void editMerchandise(Scanner scanner) {
        System.out.print("Enter the ID of the merchandise to edit: ");
        int id = Integer.parseInt(scanner.nextLine());

        GymMerchandise merch = GymMerchDAO.getGymMerchandiseById(id);
        if (merch == null) {
            System.out.println("Merchandise not found.");
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
    }

    private static void deleteMerchandise(Scanner scanner) {
        System.out.print("Enter the ID of the merchandise to delete: ");
        int id = Integer.parseInt(scanner.nextLine());

        GymMerchDAO.deleteGymMerchandise(id);
        System.out.println("Deleted (if item existed).");
    }

    private static void printAllMerchandiseAndStockValue() {
        ArrayList<GymMerchandise> allMerch = GymMerchDAO.getAllGymMerchandise();

        System.out.println("\n--- All Merchandise ---");
        double totalValue = 0.0;

        for (GymMerchandise merch : allMerch) {
            double itemTotal = merch.getMerchandisePrice() * merch.getQuantityInStock();
            totalValue += itemTotal;

            System.out.println("ID: " + merch.getId());
            System.out.println("Name: " + merch.getMerchandiseName());
            System.out.println("Type: " + merch.getMerchandiseType().getMerchandiseTypeName());
            System.out.println("Price: $" + merch.getMerchandisePrice());
            System.out.println("Quantity: " + merch.getQuantityInStock());
            System.out.println("Item Total Value: $" + itemTotal);
            System.out.println("-----------------------------");
        }

        System.out.println("Total Stock Value: $" + totalValue);
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

    private static User memberMenu(Scanner scanner, User loggedUser, ArrayList<Role> roles) {
        int option;
        final int QUIT_OPTION = 5;
        do {
            //Header
            System.out.println("Welcome " + loggedUser.getFirstName() + "\nPlease make a selection:\n");

            System.out.println("1. Browse availlable workout classes");
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
                    System.out.println("\nPress ENTER to return to menu...");
                    scanner.nextLine();
                    break;
                case 2:
                    // Show membership expenses
                    break;
                case 3:
                    // Purchase membership
                    break;
                case 4:
                    printAllMerchandise();
                    System.out.println("\nPress ENTER to return to menu...");
                    scanner.nextLine();
                    break;
                case 5:
                    // Logout user
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
                    // Create a new workout class
                    break;
                case 2:
                    // Update an existing workout class
                    break;
                case 3:
                    // Delete a workout class
                    break;
                case 4:
                    showTrainerWorkoutClasses(loggedUser, roles);
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
                System.out.println("Workout Class Type: " + workoutClass.getWorkoutClassType().getName());
                System.out.println("Description: " + workoutClass.getDescription());
                System.out.println("Date and time: " + workoutClass.getDateTime());
                System.out.println("-------------------------------");
            }
        }
    }
}
