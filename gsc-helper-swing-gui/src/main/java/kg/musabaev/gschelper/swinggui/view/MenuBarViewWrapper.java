package kg.musabaev.gschelper.swinggui.view;

import kg.musabaev.gschelper.swinggui.listener.MenuBarItemsClickListener;
import kg.musabaev.gschelper.swinggui.view.contract.MenuBarPresenterViewContract;

import javax.swing.*;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

public class MenuBarViewWrapper extends JMenuBar implements MenuBarPresenterViewContract {

    private final JMenu usefulMenu;
    private final JMenuItem toggleDarkModeItem;
    private final JMenuItem openLogItem;
    private final JMenuItem logoutGoogleItem;

    private final List<MenuBarItemsClickListener> listeners;

    public MenuBarViewWrapper() {
        this.usefulMenu = new JMenu("Полезное");
        this.toggleDarkModeItem = new JMenuItem("Выкл/вкл тёмный режим");
        this.openLogItem = new JMenuItem("Открыть логи");
        this.logoutGoogleItem = new JMenuItem("Выйти из Google аккаунта");
        this.listeners = new ArrayList<>();

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
        toggleDarkModeItem.addActionListener(e ->
            fireToggleDarkModeClick());
        openLogItem.addActionListener(e ->
            fireOpenLogMenuClick());
        logoutGoogleItem.addActionListener(e ->
            fireLogoutGoogleItemClick());
    }

    public void addMenuBarItemsClickListener(MenuBarItemsClickListener l) {
        synchronized (listeners) {
            listeners.add(checkNotNull(l));
        }
    }

    public void removeMenuBarItemsClickListener(MenuBarItemsClickListener l) {
        synchronized (listeners) {
            listeners.remove(checkNotNull(l));
        }
    }

    public void fireToggleDarkModeClick() {
        synchronized (listeners) {
            for (MenuBarItemsClickListener l : listeners)
                l.toggleDarkModeClicked();
        }
    }

    public void fireOpenLogMenuClick() {
        synchronized (listeners) {
            for (MenuBarItemsClickListener l : listeners)
                l.openLogClicked();
        }
    }

    public void fireLogoutGoogleItemClick() {
        synchronized (listeners) {
            for (MenuBarItemsClickListener l : listeners)
                l.logoutGoogleClicked();
        }
    }
}
