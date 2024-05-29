package EarlyBird;

import com.sun.net.httpserver.HttpExchange;
import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger {

    private final String logDir;
    private final JTextArea logArea;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public Logger(String logDir, JTextArea logArea) {
        this.logDir = logDir;
        this.logArea = logArea;
    }

    public void logRequest(HttpExchange exchange) {
        Date now = new Date();
        String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(now);
        String sourceIP = exchange.getRemoteAddress().getAddress().getHostAddress();
        String url = exchange.getRequestURI().toString();
        
        String logEntry = String.format("Timestamp: %s\nURL: %s\nSource IP: %s\n", timestamp, url, sourceIP);
        logArea.append(logEntry + "\n");

        String logFileName = dateFormat.format(now) + "-access.log";
        File logFile = new File(logDir, logFileName);
        logFile.getParentFile().mkdirs();
        try {
            Files.write(logFile.toPath(), (logEntry + "\n").getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException e) {
            String errorLog = "Failed to write log: " + e.getMessage() + "\n";
            logArea.append(errorLog);
            logEntry += "Error: " + e.getMessage() + "\n";
            try {
                Files.write(logFile.toPath(), (logEntry + "\n").getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            } catch (IOException ex) {
                logArea.append("Failed to write log error: " + ex.getMessage() + "\n");
            }
        }
    }
}
