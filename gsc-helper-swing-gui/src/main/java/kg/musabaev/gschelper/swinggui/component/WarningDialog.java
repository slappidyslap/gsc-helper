package kg.musabaev.gschelper.swinggui.component;

import javax.swing.*;

public class WarningDialog {

    private final String message;

    public WarningDialog(String message) {
        this.message = message;
        setupUi();
    }

    private void setupUi() {
        JOptionPane optionPane = new JOptionPane(
                message,
                JOptionPane.WARNING_MESSAGE,
                JOptionPane.DEFAULT_OPTION);
        JDialog dialog = optionPane.createDialog(null, "Предупреждение");
        dialog.setVisible(true);
    }
}
