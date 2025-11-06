package kg.musabaev.gschelper.swinggui.component;

import com.formdev.flatlaf.extras.FlatSVGIcon;

import javax.swing.*;

public class FileChooser extends JTextField {

    private final JToolBar toolBar;
    private final JButton button;

    public FileChooser() {
        this.toolBar = new JToolBar();
        this.button = new JButton();
        setupUi();
    }

    protected void setupUi() {
        button.setIcon(new FlatSVGIcon("file-chooser-icon.svg", 0.7F));
        toolBar.add(button);
        this.putClientProperty("JTextField.trailingComponent", toolBar);
    }
}
