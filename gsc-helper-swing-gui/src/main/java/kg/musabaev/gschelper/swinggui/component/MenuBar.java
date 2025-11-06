package kg.musabaev.gschelper.swinggui.component;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;
import kg.musabaev.gschelper.swinggui.viewmodel.ReportViewModel;

import javax.swing.*;

// fixme
public class MenuBar extends JMenuBar {

    private final ReportViewModel viewModel;

    private final JMenu usefulMenu;
    private final JMenuItem toggleDarkModeItem;
    private final JMenuItem openLogItem;
    private final JMenuItem logoutGoogleItem;

    public MenuBar(ReportViewModel viewModel) {
        this.viewModel = viewModel;
        this.usefulMenu = new JMenu("Полезное");
        this.toggleDarkModeItem = new JMenuItem("Выкл/вкл тёмный режим");
        this.openLogItem = new JMenuItem("Открыть логи");
        this.logoutGoogleItem = new JMenuItem("Выйти из Google аккаунта");
        setupUi();
        setupListeners();
    }

    private void setupUi() {
        super.add(usefulMenu);
        usefulMenu.add(toggleDarkModeItem);
        usefulMenu.add(openLogItem);
        usefulMenu.add(logoutGoogleItem);
    }

    private void setupListeners() {
        toggleDarkModeItem.addActionListener(e -> {
            if (viewModel.darkModeEnabled())
                FlatLightLaf.setup();
            else
                FlatDarkLaf.setup();
            viewModel.toggleDarkModeEnabled();
            SwingUtilities.updateComponentTreeUI(SwingUtilities.getWindowAncestor(this));
        });
        openLogItem.addActionListener(e -> {});
        logoutGoogleItem.addActionListener(e -> {});
    }
}
