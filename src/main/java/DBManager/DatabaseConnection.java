package DBManager;

import Logging.LoggingManagement;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;

/**
 * DatabaseConnection class.
 * This class is responsible for handling databaseconnection-related operations in the system.
 */
public class DatabaseConnection {

    // Database info
    private static final String URL = "jdbc:postgresql://localhost:5432/gymManagement";
    private static final String DEFAULT_URL = "jdbc:postgresql://localhost:5432/postgres";
    private static final String USER = "postgres";
    private static final String PASSWORD = "Keyin2024";

    public static void main(String[] args) {
        clearConsole();
        createDatabaseIfNotExists();
        if (connectAndNotify()) {
            boolean tablesCreated = createTablesIfNotExist();
            insertInitialDataIfNeeded(tablesCreated);
        }
    }

    /**
     * Simple console clear
     * Executes the logic for clearConsole.
     */
    private static void clearConsole() {
        try {
            if (System.getProperty("os.name").toLowerCase().contains("windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (Exception e) {
            // Just print some blank lines
            for (int i = 0; i < 30; i++) System.out.println();
        }
    }

    /**
     * Make DB if missing
     * Executes the logic for createDatabaseIfNotExists.
     */
    public static void createDatabaseIfNotExists() {
        try (Connection conn = DriverManager.getConnection(DEFAULT_URL, USER, PASSWORD);
             Statement stmt = conn.createStatement()) {

            ResultSet rs = stmt.executeQuery("SELECT 1 FROM pg_database WHERE datname = 'gymManagement'");
            if (!rs.next()) {
                stmt.executeUpdate("CREATE DATABASE \"gymManagement\"");
                System.out.println("Database created: gymManagement");
                LoggingManagement.log("Database created: gymManagement", false);
            }

        } catch (SQLException e) {
            System.out.println("Could not create database.");
            LoggingManagement.log("DB create error: " + e.getMessage(), true);
        }
    }

    /**
     * Connect to the DB and notify
     * Executes the logic for connectAndNotify.
     * @return boolean indicating success or failure.
     */
    public static boolean connectAndNotify() {
        try (Connection conn = getConnection()) {
            if (conn != null) {
                System.out.println("Connected to gymManagement");
                return true;
            } else {
                System.out.println("Cannot connect to gymManagement");
            }
        } catch (Exception e) {
            System.out.println("Connection error");
            LoggingManagement.log("Connection error: " + e.getMessage(), true);
        }
        return false;
    }

    /**
     * Get the DB connection
     * Executes the logic for getConnection.
     * @return Connection object.
     */
    public static Connection getConnection() {
        Connection conn = null;
        try {
            Class.forName("org.postgresql.Driver");
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (Exception e) {
            System.out.println("Connection failed: " + e.getMessage());
            LoggingManagement.log("Connection failed: " + e.getMessage(), true);
        }
        return conn;
    }

    /**
     * Create tables if they do not exist
     * Executes the logic for createTablesIfNotExist.
     * @return boolean indicating if tables were created.
     */
    public static boolean createTablesIfNotExist() {
        boolean created = false;
        try (Connection conn = getConnection(); Statement stmt = conn.createStatement()) {

            ResultSet rs = stmt.executeQuery("SELECT to_regclass('public.memberships')");
            boolean exists = rs.next() && rs.getString(1) != null;

            if (!exists) {
                BufferedReader br = new BufferedReader(new FileReader("scripts/create_tables.sql"));
                String line;
                String sql = "";
                while ((line = br.readLine()) != null) {
                    sql += line + "\n";
                }
                br.close();

                for (String s : sql.split(";")) {
                    s = s.trim();
                    if (!s.isEmpty()) stmt.execute(s);
                }

                System.out.println("Tables created");
                LoggingManagement.log("Tables created", false);
                created = true;
            }

        } catch (Exception e) {
            System.out.println("Error creating tables");
            LoggingManagement.log("Table error: " + e.getMessage(), true);
        }
        return created;
    }
    
    /**
     * Insert initial data if needed
     * Executes the logic for insertInitialDataIfNeeded.
     * @param tablesCreated boolean indicating if tables were created.
     */
    public static void insertInitialDataIfNeeded(boolean tablesCreated) {
        if (!tablesCreated) return;

        try (Connection conn = getConnection(); Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT 1 FROM memberships LIMIT 1");
            if (!rs.next()) {
                System.out.println("Initial data inserted (simulated)");
                LoggingManagement.log("Initial data inserted (simulated)", false);
            }
        } catch (Exception e) {
            System.out.println("Error inserting data");
            LoggingManagement.log("Data insert error: " + e.getMessage(), true);
        }
    }
}
