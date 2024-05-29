package EarlyBird;

import com.sun.net.httpserver.HttpServer;
import javax.swing.*;
import java.io.IOException;
import java.net.InetSocketAddress;

public class WebServer {

    private final int port;
    private final String webDir;
    private final String logDir;
    private HttpServer server;
    private final JTextArea logArea;

    public WebServer(int port, String webDir, String logDir, JTextArea logArea) {
        this.port = port;
        this.webDir = webDir;
        this.logDir = logDir;
        this.logArea = logArea;
    }

    public boolean start() {
        try {
            server = HttpServer.create(new InetSocketAddress(port), 0);
            server.createContext("/", new RequestHandler(webDir, logDir, logArea));
            server.setExecutor(null);
            server.start();
            return true;
        } catch (IOException e) {
            logArea.append("Failed to start server: " + e.getMessage() + "\n");
            return false;
        }
    }

    public void stop() {
        if (server != null) {
            server.stop(0);
        }
    }
}
