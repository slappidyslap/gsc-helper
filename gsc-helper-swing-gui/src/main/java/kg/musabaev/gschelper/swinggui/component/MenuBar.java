package kg.musabaev.gschelper.swinggui.component;

import kg.musabaev.gschelper.swinggui.listener.MenuBarListener;

import javax.swing.*;

import static com.google.common.base.Preconditions.checkNotNull;

public class MenuBar extends JMenuBar {

    private final JMenu usefulMenu;
    private final JMenuItem toggleDarkModeItem;
    private final JMenuItem openLogItem;
    private final JMenuItem logoutGoogleItem;

    private MenuBarListener listener;

    public MenuBar() {
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
            checkNotNull(listener);
            listener.toggleDarkModeMenuItemClicked();
        });
        openLogItem.addActionListener(e -> {
            checkNotNull(listener);
            listener.openLogMenuItemClicked();
        });
        logoutGoogleItem.addActionListener(e -> {
            checkNotNull(listener);
            listener.logoutGoogleItemMenuItemClicked();
        });
    }

    public void setListener(MenuBarListener listener) {
        checkNotNull(listener);
        this.listener = listener;
    }
}
