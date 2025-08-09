package Logging;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

public class LoggingManagement {
    public static final String ERROR_FILE_PATH = "gym_management_error_log.txt";
    public static final String INFO_FILE_PATH = "gym_management_log.txt";

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
