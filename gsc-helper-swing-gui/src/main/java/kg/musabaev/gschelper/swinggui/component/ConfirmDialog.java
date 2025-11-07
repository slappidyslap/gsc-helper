package kg.musabaev.gschelper.swinggui.component;

import javax.swing.*;

public class ConfirmDialog extends JDialog {

    public static final String LOGOUT_FROM_GOOGLE = "Выйти из Google аккаунта";
    private final Object[] OPTIONS = {"Да", "Нет"};

    private final String message;
    private boolean confirmed = false;

    public ConfirmDialog(String message) {
        this.message = message;
        setupUi();
    }

    private void setupUi() {
        JOptionPane optionPane = new JOptionPane(
                message,
                JOptionPane.QUESTION_MESSAGE,
                JOptionPane.YES_NO_OPTION,
                null,
                OPTIONS,
                OPTIONS[1]);
        JDialog dialog = optionPane.createDialog(null, LOGOUT_FROM_GOOGLE);
        dialog.setVisible(true);

        Object selectedOption = optionPane.getValue();
        if (selectedOption == null)
            confirmed = false;
        else if (selectedOption == OPTIONS[1])
            confirmed = false;
        else if (selectedOption == OPTIONS[0])
            confirmed = true;
        else
            throw new IllegalStateException(
                    "Опция " + selectedOption + " не реализован " +
                    "окне подтверждения " + LOGOUT_FROM_GOOGLE);
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public static boolean show(String message) {
        return new ConfirmDialog(message).isConfirmed();
    }
}
