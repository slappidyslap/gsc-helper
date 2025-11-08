package kg.musabaev.gschelper.swinggui.exception;

import kg.musabaev.gschelper.swinggui.component.ExceptionDialog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;

public class GlobalExceptionHandler implements Thread.UncaughtExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @Override
    public void uncaughtException(Thread thread, Throwable throwable) {
        if (thread.getName().contains("AWT-EventQueue"))
            ExceptionDialog.show(throwable);
        else {
            String threadName = Thread.currentThread().getName();
            log.error("Произошла непредвиденная ошибка в потоке {}", threadName, throwable);
            SwingUtilities.invokeLater(() -> ExceptionDialog.show(new RuntimeException(
                "Произошла непредвиденная ошибка в потоке " + threadName +
                    ". Работа программы может быть нестабильной. " +
                    "Рекомендуется перезапустить приложение.", throwable)));
        }
    }
}
