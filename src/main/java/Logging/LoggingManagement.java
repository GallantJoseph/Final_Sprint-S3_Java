package Logging;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

/**
 * LoggingManagement class.
 * This class is responsible for handling logging operations to files.
 */
public class LoggingManagement {
    /**
     * File paths for error logs and info logs.
     */
    public static final String ERROR_FILE_PATH = "gym_management_error_log.txt";
    public static final String INFO_FILE_PATH = "gym_management_log.txt";

    /**
     * Logs a message to the appropriate log file based on the error status.
     *
     * @param message  The message to log.
     * @param isError  If true, logs to the error file; otherwise, logs to the info file.
     */
    public static void log(String message, boolean isError) {
        LocalDateTime currentTime = LocalDateTime.now();
        BufferedWriter writer;

        if (isError) {
            message = currentTime + " - ERROR: " + message;
        } else {
            message = currentTime + " - INFO: " + message;
        }

        try {
            writer = new BufferedWriter(new FileWriter(isError ? ERROR_FILE_PATH : INFO_FILE_PATH, true));

            writer.write(message);
            writer.newLine();

            writer.close();
        } catch (IOException e) {
            System.out.println("Failed to initialize logger: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
