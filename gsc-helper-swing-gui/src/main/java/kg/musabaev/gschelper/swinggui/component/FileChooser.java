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
        setupListeners();
    }

    protected void setupUi() {
        super.setEditable(false);
        button.setIcon(new FlatSVGIcon("file-chooser-icon.svg", 0.7F));
        toolBar.add(button);
        super.putClientProperty("JTextField.trailingComponent", toolBar);
    }

    private void setupListeners() {
        button.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser(); // todo native
            int returnVal = chooser.showOpenDialog(button);
            if(returnVal == JFileChooser.APPROVE_OPTION) {
                super.setText(chooser.getSelectedFile().getAbsolutePath());
            }
        });
    }
}
