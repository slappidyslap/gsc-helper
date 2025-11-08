package kg.musabaev.gschelper.swinggui.listener.impl;

import ch.qos.logback.classic.LoggerContext;
import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.FlatLightLaf;
import kg.musabaev.gschelper.swinggui.component.ConfirmDialog;
import kg.musabaev.gschelper.swinggui.component.ErrorDialog;
import kg.musabaev.gschelper.swinggui.listener.MenuBarListener;
import kg.musabaev.gschelper.swinggui.util.Constants;
import kg.musabaev.gschelper.swinggui.util.Utils;
import kg.musabaev.gschelper.swinggui.view.ReportGenerateView;
import kg.musabaev.gschelper.swinggui.component.MenuBar;
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

/**
 * Реализация {@link MenuBarListener},
 * обрабатывающая клики по элементам {@link MenuBar}
 */
public class MenuBarListenerImpl implements MenuBarListener {

    private static final Logger log = LoggerFactory.getLogger(MenuBarListenerImpl.class);

    private final ReportGenerateView view;

    public MenuBarListenerImpl(ReportGenerateView view) {
        this.view = view;
    }

    /**
     * Переключает тему приложения между тёмной и светлой.
     */
    @Override
    public void toggleDarkModeMenuItemClicked() {
        boolean isDarkMode = FlatLaf.isLafDark();
        if (isDarkMode)
            FlatLightLaf.setup();
        else
            FlatDarkLaf.setup();
        SwingUtilities.updateComponentTreeUI(view);
        log.info("Текущая тема переключена на {}", isDarkMode ? "темную" : "светлую");
    }

    /**
     * Открывает текущий файл логов.
     */
    @Override
    public void openLogMenuItemClicked() {
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
        String currentTimestamp = "currentTimestamp";
        String curTimestamp = context.getProperty(currentTimestamp);
        try {
            Path logFilePath = Utils.paths(Constants.LOGS_FOLDER, format("log-%s.log", curTimestamp));
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
    public void logoutGoogleItemMenuItemClicked() {
        if (ConfirmDialog.show("Вы правда хотите выйти из Google аккаунта?")) {
            try (Stream<Path> dirStream = Files.walk(Constants.APP_HOME)) {
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
