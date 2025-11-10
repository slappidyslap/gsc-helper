package kg.musabaev.gschelper.swinggui.listener;

import kg.musabaev.gschelper.swinggui.view.MenuBarViewWrapper;

/**
 * Слушатель на нажатия элементы в {@link MenuBarViewWrapper}
 */
public interface MenuBarListener {

    /**
     * Клик по элементу в {@link MenuBarViewWrapper} для переключения темы приложения между тёмной и светлой.
     */
    void toggleDarkModeMenuItemClicked();

    /**
     * Клик по элементу в {@link MenuBarViewWrapper} для открытия текущего файла логов.
     */
    void openLogMenuItemClicked();

    /**
     * Клик по элементу в {@link MenuBarViewWrapper} для выхода из Google аккаунта.
     */
    void logoutGoogleItemMenuItemClicked();
}
