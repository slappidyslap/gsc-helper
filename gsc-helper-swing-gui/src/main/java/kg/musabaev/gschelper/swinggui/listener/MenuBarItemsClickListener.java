package kg.musabaev.gschelper.swinggui.listener;

import kg.musabaev.gschelper.swinggui.component.MenuBar;

/**
 * Слушатель на нажатия элементы в {@link MenuBar}
 */
public interface MenuBarItemsClickListener {

    /**
     * Клик по элементу в {@link MenuBar} для переключения темы приложения между тёмной и светлой.
     */
    void toggleDarkModeMenuItemClicked();

    /**
     * Клик по элементу в {@link MenuBar} для открытия текущего файла логов.
     */
    void openLogMenuItemClicked();

    /**
     * Клик по элементу в {@link MenuBar} для выхода из Google аккаунта.
     */
    void logoutGoogleItemMenuItemClicked();
}
