package kg.musabaev.gsc_helper.gui.exception;

import com.formdev.flatlaf.themes.FlatMacLightLaf;
import de.milchreis.uibooster.UiBooster;
import de.milchreis.uibooster.model.UiBoosterOptions;
import kg.musabaev.gsc_helper.core.table.exception.LocalFileNotFoundException;
import kg.musabaev.gsc_helper.core.gsc.exception.CredentialsFileNotFoundException;
import kg.musabaev.gsc_helper.core.gsc.exception.GscSitesNotFoundException;

/**
 * Глобальный обработчик необработанных исключений для приложения.
 * <p>
 * Класс реализует {@link Thread.UncaughtExceptionHandler} и предназначен для
 * отображения ошибок в виде диалоговых окон через {@link UiBooster}.
 * <br><br>
 * Если исключение относится к одному из известных типов
 * ({@link LocalFileNotFoundException}, {@link CredentialsFileNotFoundException},
 * {@link GscSitesNotFoundException}), пользователю показывается простое окно
 * с текстом ошибки.
 * <br>
 * Для всех остальных исключений выводится окно с полным стек трейсом.
 * </p>
 *
 * @see LocalFileNotFoundException
 * @see CredentialsFileNotFoundException
 * @see GscSitesNotFoundException
 */
public class GlobalExceptionHandler implements Thread.UncaughtExceptionHandler {

    private final UiBooster uiBooster = new UiBooster(
            new UiBoosterOptions(new FlatMacLightLaf(), null, null));

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        if (e instanceof LocalFileNotFoundException ||
                e instanceof CredentialsFileNotFoundException ||
                e instanceof GscSitesNotFoundException) {
            showErrorDialog(e.getMessage());
        } else {
            showExceptionDialog(e);
        }
    }

    /**
     * Показывает простое диалоговое окно с сообщением об ошибке.
     *
     * @param message текст ошибки для отображения
     */
    private void showErrorDialog(String message) {
        uiBooster.showErrorDialog(message, "Ошибка");
    }

    /**
     * Показывает окно с деталями исключения (включая стек трейс).
     *
     * @param e исключение, информацию о котором нужно отобразить
     */
    private void showExceptionDialog(Throwable e) {
        uiBooster.showException(
                "Произошла ошибка",
                "Сообщение исключения",
                (Exception) e
        );
    }
}
