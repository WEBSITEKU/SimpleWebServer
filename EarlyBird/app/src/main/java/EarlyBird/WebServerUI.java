package EarlyBird;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class WebServerUI extends JFrame {

    private JTextField portField;
    private JTextField webDirField;
    private JTextField logDirField;
    private JTextArea logArea;
    private JButton startButton;
    private JButton stopButton;
    private WebServer server;

    public WebServerUI() {
        setTitle("Simple Web Server");
        setSize(500, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        initComponents();
    }

    private void initComponents() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(6, 2));

        panel.add(new JLabel("Port:"));
        portField = new JTextField("8080");
        panel.add(portField);

        panel.add(new JLabel("Web Directory:"));
        webDirField = new JTextField(System.getProperty("user.dir") + "/web");
        panel.add(webDirField);

        panel.add(new JLabel("Log Directory:"));
        logDirField = new JTextField(System.getProperty("user.dir") + "/logs");
        panel.add(logDirField);

        startButton = new JButton("Start");
        stopButton = new JButton("Stop");
        stopButton.setEnabled(false);

        panel.add(startButton);
        panel.add(stopButton);

        logArea = new JTextArea();
        logArea.setEditable(false);

        add(panel, BorderLayout.NORTH);
        add(new JScrollPane(logArea), BorderLayout.CENTER);

        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startServer();
            }
        });

        stopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                stopServer();
            }
        });
    }

    private void startServer() {
        int port = Integer.parseInt(portField.getText());
        String webDir = webDirField.getText();
        String logDir = logDirField.getText();

        server = new WebServer(port, webDir, logDir, logArea);

        if (server.start()) {
            logArea.append("Server started on port " + port + "\n");
            startButton.setEnabled(false);
            stopButton.setEnabled(true);
        } else {
            logArea.append("Failed to start server\n");
        }
    }

    private void stopServer() {
        if (server != null) {
            server.stop();
            logArea.append("Server stopped\n");

            startButton.setEnabled(true);
            stopButton.setEnabled(false);
        }
    }
}
