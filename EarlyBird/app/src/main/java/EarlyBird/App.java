package EarlyBird;

import javax.swing.SwingUtilities;

public class App {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new WebServerUI().setVisible(true));
    }
}
