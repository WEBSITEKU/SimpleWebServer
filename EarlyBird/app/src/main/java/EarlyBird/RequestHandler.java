package EarlyBird;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import javax.swing.*;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class RequestHandler implements HttpHandler {

    private final String webDir;
    private final Logger logger;

    public RequestHandler(String webDir, String logDir, JTextArea logArea) {
        this.webDir = webDir;
        this.logger = new Logger(logDir, logArea);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();

        
        String fullPath = webDir + path.replace("/", java.io.File.separator);
        fullPath = Paths.get(fullPath).normalize().toString();

        logger.logRequest(exchange);

        
        Path filePath = Paths.get(fullPath);

        if ("GET".equalsIgnoreCase(method)) {
            if (Files.exists(filePath)) {
                if (Files.isDirectory(filePath)) {
                    new DirectoryHandler().handleDirectoryRequest(exchange, filePath);
                } else {
                    new FileHandler().handleFileRequest(exchange, filePath);
                }
            } else {
                handleNotFound(exchange);
            }
        } else {
            handleMethodNotAllowed(exchange);
        }
    }

    private void handleNotFound(HttpExchange exchange) throws IOException {
        sendResponse(exchange, 404, "text/plain", "404 (Not Found)\n".getBytes());
    }

    private void handleMethodNotAllowed(HttpExchange exchange) throws IOException {
        sendResponse(exchange, 405, "text/plain", "405 (Method Not Allowed)\n".getBytes());
    }

    private void sendResponse(HttpExchange exchange, int statusCode, String contentType, byte[] response) throws IOException {
        exchange.getResponseHeaders().set("Content-Type", contentType);
        exchange.sendResponseHeaders(statusCode, response.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response);
        }
    }
}
