package kg.musabaev.gschelper.swinggui.component;

import javax.swing.*;

public class ErrorDialog {

    private final String message;

    public ErrorDialog(String message) {
        this.message = message;
        setupUi();
    }

    private void setupUi() {
        JOptionPane optionPane = new JOptionPane(
                message,
                JOptionPane.ERROR_MESSAGE,
                JOptionPane.DEFAULT_OPTION);
        JDialog dialog = optionPane.createDialog(null, "Ошибка");
        dialog.setVisible(true);
    }

    public static void show(String message) {
        new ErrorDialog(message);
    }
}
