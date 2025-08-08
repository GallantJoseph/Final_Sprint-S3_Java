package DBManager;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection {

    // Comment out eachothers connection DONT DELETE so easyer for testing

    private static final String URL = "jdbc:postgresql://localhost:5432/gymManagement";
    private static final String USER = "postgres";
    private static final String PASSWORD = "Keyin2024";

    // // Ashton's Connection:
    // private static final String URL = "jdbc:postgresql://localhost:5432/gymManagement";
    // private static final String USER = "postgres";
    // private static final String PASSWORD = "Youtube5018@";

    // Get a connection to the PostgreSQL database
    public static Connection getConnection() {
        Connection conn = null;
        try {
            Class.forName("org.postgresql.Driver");
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }

    // Run the create_tables.sql script to create all necessary tables
    public static void createTablesIfNotExist() {
        String sqlFilePath = "scripts/create_tables.sql";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             BufferedReader br = new BufferedReader(new FileReader(sqlFilePath))) {

            StringBuilder sqlBuilder = new StringBuilder();
            String line;

            while ((line = br.readLine()) != null) {
                sqlBuilder.append(line).append("\n");
            }

            // Split SQL by semicolon to handle multiple statements easyer
            String[] sqlStatements = sqlBuilder.toString().split(";");

            for (String sql : sqlStatements) {
                sql = sql.trim();
                if (!sql.isEmpty()) {
                    stmt.execute(sql);
                }
            }

            System.out.println("Database tables created/verified successfully.");

        } catch (SQLException e) {
            System.err.println("SQL error running create_tables.sql: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Error reading SQL file: " + e.getMessage());
        }
    }

    // TODO Delete after Run directly for quick setup can be done from main progrm
    public static void main(String[] args) {
        createTablesIfNotExist();
    }
}
