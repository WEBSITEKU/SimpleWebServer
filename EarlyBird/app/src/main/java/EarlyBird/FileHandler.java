package EarlyBird;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileHandler {

    public void handleFileRequest(HttpExchange exchange, Path filePath) throws IOException {
        byte[] response = Files.readAllBytes(filePath);

        String contentType = guessContentType(filePath);
        exchange.getResponseHeaders().set("Content-Type", contentType);

        // Debugging logs
        System.out.println("Content-Type: " + contentType);

        if ("application/pdf".equals(contentType)) {
            exchange.getResponseHeaders().set("Content-Disposition", "inline; filename=\"" + filePath.getFileName().toString() + "\"");
            // Debugging logs
            System.out.println("Content-Disposition: inline; filename=\"" + filePath.getFileName().toString() + "\"");
        }

        sendResponse(exchange, 200, contentType, response);
    }

    private String guessContentType(Path filePath) throws IOException {
        String contentType = Files.probeContentType(filePath);
        if (contentType == null) {
            contentType = "application/octet-stream";
        }
        return contentType;
    }

    private void sendResponse(HttpExchange exchange, int statusCode, String contentType, byte[] response) throws IOException {
        // Debugging logs
        System.out.println("Sending response: " + statusCode);
        System.out.println("Content-Type: " + contentType);
        System.out.println("Response length: " + response.length);

        exchange.getResponseHeaders().set("Content-Type", contentType);
        exchange.sendResponseHeaders(statusCode, response.length);

        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response);
        }
    }
}
