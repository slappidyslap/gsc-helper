package kg.musabaev.gschelper.swinggui.view;

import kg.musabaev.gschelper.swinggui.listener.DateRangeChangeListener;
import kg.musabaev.gschelper.swinggui.listener.MenuBarItemsClickListener;
import kg.musabaev.gschelper.swinggui.listener.ReportLocalSaveFormSubmitListener;
import kg.musabaev.gschelper.swinggui.listener.SavePathChangeListener;

import java.nio.file.Path;

/**
 * Интерфейс определяющий контракт между View и Presenter.
 */
public interface ReportLocalSavePresenterViewContract {

    // ========= Методы для изменения данных =========

    void setSavePath(Path savePath);

    void setSuggestedFilename(String suggestedFilename);

    // ========= Методы для управления UI состоянием =========

    void showFrame();

    void disableSubmitButton();

    void enableSubmitButton();

    // ========= Методы для отображения диалоговых окон =========

    void showExceptionDialog(Exception e);

    void showWarningDialog(String message);

    // ========= Методы для работы со слушателями в дочерних компонентах =========

    void setMenuBarItemsClickListener(MenuBarItemsClickListener menuBarItemsClickListener);

    void addGenerateReportFormSubmitListener(ReportLocalSaveFormSubmitListener l);

    @SuppressWarnings("unused")
    void removeGenerateReportFormSubmitListener(ReportLocalSaveFormSubmitListener l);

    void addSavePathChangeListener(SavePathChangeListener l);

    @SuppressWarnings("unused")
    void removeSavePathChangeListener(SavePathChangeListener l);

    void addDateRangeChangeListener(DateRangeChangeListener l);

    @SuppressWarnings("unused")
    void removeDateRangeChangeListener(DateRangeChangeListener l);
}
