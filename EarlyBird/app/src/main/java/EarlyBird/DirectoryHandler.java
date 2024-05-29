package EarlyBird;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class DirectoryHandler {

    public void handleDirectoryRequest(HttpExchange exchange, Path directoryPath) throws IOException {
        StringBuilder response = new StringBuilder("<html><head><style>");
        response.append("body { font-family: Arial, sans-serif; margin: 0; padding: 20px; background-color: #f4f4f9; }");
        response.append("header { background-color: #333; color: #fff; padding: 10px 0; text-align: center; }");
        response.append("h1 { margin: 0; }");
        response.append("table { width: 100%; border-collapse: collapse; margin-top: 20px; }");
        response.append("th, td { padding: 10px; text-align: left; border-bottom: 1px solid #ddd; }");
        response.append("th { background-color: #f2f2f2; }");
        response.append("tr:hover { background-color: #f1f1f1; }");
        response.append("a { text-decoration: none; color: #333; }");
        response.append("a:hover { color: #007BFF; }");
        response.append("</style></head><body>");
        response.append("<header><h1>Directory Listing</h1></header>");
        response.append("<table><tr><th>Name</th><th>Last Modified</th><th>Size</th></tr>");
        String requestPath = exchange.getRequestURI().getPath();

        try {
            Files.list(directoryPath)
                    .forEach(file -> {
                        String fileName = file.getFileName().toString();
                        String fileUrl = requestPath + (requestPath.endsWith("/") ? "" : "/") + fileName;

                        BasicFileAttributes attrs;
                        try {
                            attrs = Files.readAttributes(file, BasicFileAttributes.class);
                        } catch (IOException e) {
                            return;
                        }

                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
                        String lastModified = sdf.format(new Date(attrs.lastModifiedTime().toMillis()));

                        long size = attrs.size();

                        response.append("<tr>")
                                .append("<td><a href=\"").append(fileUrl).append("\">")
                                .append(fileName).append("</a></td>")
                                .append("<td>").append(lastModified).append("</td>")
                                .append("<td>").append(size).append(" bytes").append("</td>")
                                .append("</tr>");
                    });
        } catch (IOException e) {
        }
        response.append("</table></body></html>");
        sendResponse(exchange, 200, "text/html", response.toString().getBytes());
    }

    private void sendResponse(HttpExchange exchange, int statusCode, String contentType, byte[] response) throws IOException {
        exchange.getResponseHeaders().set("Content-Type", contentType);
        exchange.sendResponseHeaders(statusCode, response.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response);
        }
    }
}
