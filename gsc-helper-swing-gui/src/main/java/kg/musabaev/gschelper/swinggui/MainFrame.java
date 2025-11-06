package kg.musabaev.gschelper.swinggui;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.fonts.roboto.FlatRobotoFont;
import kg.musabaev.gschelper.swinggui.component.FormPanel;
import kg.musabaev.gschelper.swinggui.component.MenuBar;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    public static JFrame root = null;

    private final MainViewModel viewModel;
    private final JMenuBar menuBar;
    private final FormPanel form;

    public MainFrame() {
        setupLaf();

        root = this;
        this.viewModel = new MainViewModel();
        this.menuBar = new MenuBar(viewModel);
        this.form = new FormPanel();

        setupAppIcon();
        setupMenuBar();
        setupUi();
    }

    private void setupUi() {
        super.setTitle("GSC Helper");
        super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        super.setSize(650, 200);
        super.setLocationRelativeTo(null);

        super.add(form);
    }

    public void showFrame() {
        SwingUtilities.invokeLater(() -> {
            super.pack();
            SwingUtilities.updateComponentTreeUI(this);
            super.setVisible(true);
            super.getContentPane().requestFocusInWindow();
        });
    }

    private void setupLaf() {
        FlatDarkLaf.setup();
        FlatRobotoFont.install();
        UIManager.put("defaultFont", new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 15));
        UIManager.put("TextComponent.arc", 7);
        UIManager.put("Button.arc", 7);
    }

    private void setupAppIcon() {
        Image icon = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/icon.png"));
        super.setIconImage(icon);
    }

    private void setupMenuBar() {
        super.setJMenuBar(menuBar); // todo
    }
}
