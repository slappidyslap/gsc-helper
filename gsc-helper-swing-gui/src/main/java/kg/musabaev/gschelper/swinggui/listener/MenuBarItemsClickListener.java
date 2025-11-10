package kg.musabaev.gschelper.swinggui.listener;

import kg.musabaev.gschelper.swinggui.view.MenuBarViewWrapper;

/**
 * Слушатель на нажатия элементы в {@link MenuBarViewWrapper}
 */
public interface MenuBarItemsClickListener {

    /**
     * Переключает тему приложения между тёмной и светлой.
     */
    void toggleDarkModeClicked();

    /**
     * Открывает текущий файл логов.
     */
    void openLogClicked();

    /**
     * Выполняет выход из Google аккаунта, удаляя сохранённые токены и кэш авторизации.
     */
    void logoutGoogleClicked();
}
