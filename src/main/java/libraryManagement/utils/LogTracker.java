package libraryManagement.utils;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LogTracker {
    private static final String LOG_FILE = "library_log.txt";
    private static final ExecutorService executor = Executors.newSingleThreadExecutor();

    static {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            executor.shutdown();
            try {
                if (!executor.awaitTermination(5, java.util.concurrent.TimeUnit.SECONDS)) {
                    executor.shutdownNow();
                }
            } catch (InterruptedException e) {
                executor.shutdownNow();
            }
        }));
    }

    public static void log(String message) {
        executor.execute(() -> {
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            String logMessage = timestamp + " - " + message;

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(LOG_FILE, true))) {
                writer.write(logMessage);
                writer.newLine();
                writer.flush();
            } catch (IOException e) {
                System.err.println("Error writing log: " + e.getMessage());
            }
        });
    }
}
