package EarlyBird;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.prefs.Preferences;

public class WebServerUI extends JFrame {

    private JTextField portField;
    private JTextField webDirField;
    private JTextField logDirField;
    private JLabel webDirLabel;
    private JLabel logDirLabel;
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

        Preferences prefs = Preferences.userNodeForPackage(WebServerUI.class);

        int lastPort = prefs.getInt("lastPort", 8080);
        portField = new JTextField(String.valueOf(lastPort));
        panel.add(portField);

        panel.add(new JLabel("Web Directory:"));
        String lastWebDir = prefs.get("lastWebDir", System.getProperty("user.dir") + "/web");
        webDirField = new JTextField(lastWebDir);
        webDirLabel = new JLabel(lastWebDir);
        panel.add(webDirField);
        panel.add(webDirLabel);

        JButton webDirButton = new JButton("Choose...");
        webDirButton.addActionListener((ActionEvent e) -> {
            JFileChooser chooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
            chooser.setDialogTitle("Select Web Directory");
            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

            int returnValue = chooser.showOpenDialog(null);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                String selectedDir = chooser.getSelectedFile().getPath();
                webDirField.setText(selectedDir);
                webDirLabel.setText(selectedDir);
            }
        });
        panel.add(webDirButton);

        panel.add(new JLabel("Log Directory:"));
        String lastLogDir = prefs.get("lastLogDir", System.getProperty("user.dir") + "/logs");
        logDirField = new JTextField(lastLogDir);
        logDirLabel = new JLabel(lastLogDir);
        panel.add(logDirField);
        panel.add(logDirLabel);

        JButton logDirButton = new JButton("Choose...");
        logDirButton.addActionListener((ActionEvent e) -> {
            JFileChooser chooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
            chooser.setDialogTitle("Select Log Directory");
            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

            int returnValue = chooser.showOpenDialog(null);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                String selectedDir = chooser.getSelectedFile().getPath();
                logDirField.setText(selectedDir);
                logDirLabel.setText(selectedDir);
            }
        });
        panel.add(logDirButton);

        startButton = new JButton("Start");
        stopButton = new JButton("Stop");
        stopButton.setEnabled(false);

        panel.add(startButton);
        panel.add(stopButton);

        logArea = new JTextArea();
        logArea.setEditable(false);

        add(panel, BorderLayout.NORTH);
        add(new JScrollPane(logArea), BorderLayout.CENTER);

        startButton.addActionListener((ActionEvent e) -> {
            startServer();
            int port = Integer.parseInt(portField.getText());
            prefs.putInt("lastPort", port);
            prefs.put("lastWebDir", webDirField.getText());
            prefs.put("lastLogDir", logDirField.getText());
        });

        stopButton.addActionListener((ActionEvent e) -> {
            stopServer();
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
