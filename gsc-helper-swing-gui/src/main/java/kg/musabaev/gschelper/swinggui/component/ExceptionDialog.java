package kg.musabaev.gschelper.swinggui.component;

import com.formdev.flatlaf.FlatLaf;

import javax.swing.*;
import java.awt.*;
import java.io.PrintWriter;
import java.io.StringWriter;

public class ExceptionDialog extends JDialog {

    public ExceptionDialog(Throwable throwable) {
        setupUi(throwable);
    }

    private void setupUi(Throwable throwable) {
        super.setTitle("Произошло исключение");
        super.setLayout(new BorderLayout(10, 10));
        super.setSize(850, 500);
        super.setLocationRelativeTo(null);

        JPanel messagePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        Icon icon = UIManager.getIcon("OptionPane.errorIcon");
        JLabel iconLabel = new JLabel(icon);

        JLabel messageLabel = new JLabel(
                "Произошло исключение: " +
                        throwable.getClass().getSimpleName() +
                        ". Сообщение: " + throwable.getMessage());
        messagePanel.add(iconLabel);
        messagePanel.add(messageLabel);
        messagePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JTextArea stackTraceArea = new JTextArea();
        stackTraceArea.setEditable(false);
        stackTraceArea.setFont(new Font("Consolas", Font.PLAIN, 12));
        stackTraceArea.setText(getStackTraceAsString(throwable));
        stackTraceArea.setCaretPosition(0);
        if (FlatLaf.isLafDark())
            stackTraceArea.setBackground(UIManager.getColor("Panel.background").darker());
        else
            stackTraceArea.setBackground(UIManager.getColor("Panel.background").brighter());

        JScrollPane scrollPane = new JScrollPane(stackTraceArea);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(0, 15, 10, 15));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton okButton = new JButton("OK");
        okButton.addActionListener(e -> dispose());
        buttonPanel.add(okButton);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));

        super.add(messagePanel, BorderLayout.NORTH);
        super.add(scrollPane, BorderLayout.CENTER);
        super.add(buttonPanel, BorderLayout.SOUTH);

        super.getRootPane().setDefaultButton(okButton);
        super.getRootPane().registerKeyboardAction(
                e -> dispose(),
                KeyStroke.getKeyStroke("ESCAPE"),
                JComponent.WHEN_IN_FOCUSED_WINDOW);
    }

    private String getStackTraceAsString(Throwable throwable) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        throwable.printStackTrace(pw);
        System.out.println(sw);
        return sw.toString();
    }

    public static void show(Throwable t) {
        new ExceptionDialog(t).setVisible(true);
    }
}
