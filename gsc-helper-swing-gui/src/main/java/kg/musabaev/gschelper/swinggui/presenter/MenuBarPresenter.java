package kg.musabaev.gschelper.swinggui.presenter;

import ch.qos.logback.classic.LoggerContext;
import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.FlatLightLaf;
import kg.musabaev.gschelper.swinggui.component.dialog.ConfirmDialog;
import kg.musabaev.gschelper.swinggui.component.dialog.ErrorDialog;
import kg.musabaev.gschelper.swinggui.listener.MenuBarItemsClickListener;
import kg.musabaev.gschelper.swinggui.model.MenuBarModel;
import kg.musabaev.gschelper.swinggui.util.Paths;
import kg.musabaev.gschelper.swinggui.view.contract.MenuBarPresenterViewContract;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.stream.Stream;

import static java.lang.String.format;
import static kg.musabaev.gschelper.swinggui.util.Paths.paths;

public class MenuBarPresenter implements MenuBarItemsClickListener {

    private static final Logger log = LoggerFactory.getLogger(MenuBarPresenter.class);

    private final MenuBarPresenterViewContract view;
    private final MenuBarModel model;

    public MenuBarPresenter(MenuBarPresenterViewContract view, MenuBarModel model) {
        this.view = view;
        this.model = model;

        attachListeners();
    }

    private void attachListeners() {
        view.addMenuBarItemsClickListener(this);
    }

    /**
     * Переключает тему приложения между тёмной и светлой.
     */
    @Override
    public void toggleDarkModeClicked() {
        boolean isDarkMode = FlatLaf.isLafDark();
        if (isDarkMode)
            FlatLightLaf.setup();
        else
            FlatDarkLaf.setup();
        SwingUtilities.updateComponentTreeUI(SwingUtilities.getWindowAncestor((Component) view));
        log.info("Текущая тема переключена на {}", isDarkMode ? "темную" : "светлую");
    }

    /**
     * Открывает текущий файл логов.
     */
    @Override
    public void openLogClicked() {
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
        String currentTimestamp = "currentTimestamp";
        String curTimestamp = context.getProperty(currentTimestamp);
        try {
            Path logFilePath = paths(Paths.LOGS_FOLDER, format("log-%s.log", curTimestamp));
            Desktop.getDesktop().open(logFilePath.toFile());
            log.info("Текущий лог файл открыт");
        } catch (IOException e) {
            log.error("Ошибка при попытке открыть текущего лог файла", e);
            ErrorDialog.show("Программа не смогла открыть папку с логами из-за ошибки: " + e.getMessage());
        }
    }

    /**
     * Выполняет выход из Google аккаунта, удаляя сохранённые токены и кэш авторизации.
     */
    @Override
    public void logoutGoogleClicked() {
        if (ConfirmDialog.show("Вы правда хотите выйти из Google аккаунта?")) {
            try (Stream<Path> dirStream = Files.walk(Paths.APP_HOME)) {
                //noinspection ResultOfMethodCallIgnored
                dirStream
                    .sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(File::delete);
                log.info("Файлы авторизации успешно удалены");
            } catch (IOException e) {
                log.error("Ошибка при удалении файлов авторизации", e);
                ErrorDialog.show("Программа не смогла выйти из аккаунта Google из-за ошибки: " + e.getMessage());
            }
        }
    }
}
