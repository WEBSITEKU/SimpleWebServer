package EarlyBird;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class DirectoryHandler implements HttpHandler {

    private static final String BACKGROUND_IMAGE_PATH = "/image/Logo-Udinus2.png";

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Path directoryPath = Paths.get("");
        handleDirectoryRequest(exchange, directoryPath);
    }

    public void handleDirectoryRequest(HttpExchange exchange, Path directoryPath) throws IOException {
        StringBuilder response = new StringBuilder("<html><head><meta charset=\"UTF-8\"><style>");
        response.append("body {");
        response.append("    font-family: Arial, sans-serif;");
        response.append("    margin: 0;");
        response.append("    padding: 20px;");
        response.append("    background-image: url('").append(BACKGROUND_IMAGE_PATH).append("');");
        response.append("    background-size: 500px;");
        response.append("    background-position: center;");
        response.append("    background-repeat: no-repeat;");
        response.append("    background-attachment: fixed;");
        response.append("}");
        response.append("header {");
        response.append("    background-color: rgba(51, 51, 51, 0.8);");
        response.append("    color: #FFF;");
        response.append("    padding: 10px 0;");
        response.append("    text-align: center;");
        response.append("}");
        response.append("h1 { margin: 0; }");
        response.append("table { width: 100%; border-collapse: collapse; margin-top: 20px; background-color: rgba(255, 255, 255, 0.6); }");
        response.append("th, td { padding: 10px; text-align: left; border-bottom: 1px solid #ddd; background-color: transparent; }");
        response.append("th { background-color: rgba(242, 242, 242, 0.8); }");
        response.append("tr:hover td { background-color: rgba(241, 241, 241, 0.8); }");
        response.append("a { text-decoration: none; color: #333; }");
        response.append("a:hover { color: #007BFF; }");
        response.append(".icon { display: inline-block; width: 20px; height: 20px; margin-right: 10px; font-size: 20px; }");
        response.append("th.icon-header, td.icon-cell { width: 30px; }"); // Menambah class untuk kolom ikon
        response.append("</style></head><body>");
        response.append("<header><h1>Daftar Direktori</h1></header>");
        response.append("<table><tr><th class=\"icon-header\"></th><th>Nama</th><th>Terakhir Diubah</th><th>Ukuran</th></tr>");
        String requestPath = exchange.getRequestURI().getPath();

        try {
            Files.list(directoryPath)
                    .forEach(file -> {
                        String fileName = file.getFileName().toString();
                        String fileUrl = requestPath + (requestPath.endsWith("/") ? "" : "/") + fileName;
                        String icon;

                        BasicFileAttributes attrs;
                        try {
                            attrs = Files.readAttributes(file, BasicFileAttributes.class);
                        } catch (IOException e) {
                            return;
                        }
 
                        if (attrs.isDirectory()) {
                            icon = "📁";
                        } else {
                            icon = "📄";
                        }

                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
                        String lastModified = sdf.format(new Date(attrs.lastModifiedTime().toMillis()));

                        long size = attrs.size();

                        response.append("<tr>")
                                .append("<td class=\"icon-cell\"><span class=\"icon\">").append(icon).append("</span></td>")
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
