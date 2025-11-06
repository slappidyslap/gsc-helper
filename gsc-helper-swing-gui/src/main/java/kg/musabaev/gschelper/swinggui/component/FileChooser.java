package kg.musabaev.gschelper.swinggui.component;

import com.formdev.flatlaf.extras.FlatSVGIcon;

import javax.swing.*;

public class FileChooser extends BaseInput {

    private final JLabel label;
    private final JTextField textField;
    private final JToolBar toolBar;
    private final JButton button;

    public FileChooser() {
        this.label = new JLabel();
        this.textField = new JTextField();
        this.toolBar = new JToolBar();
        this.button = new JButton();
        setupUi();
    }

    @Override
    protected void setupUi() {
        super.setupUi();

        label.setText("Путь сохранения:");
        button.setIcon(new FlatSVGIcon("file-chooser-icon.svg", 0.7F));
        toolBar.add(button);
        textField.putClientProperty("JTextField.trailingComponent", toolBar);

        super.add(label);
        super.add(textField);
    }
}
