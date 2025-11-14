package kg.musabaev.gschelper.swinggui.view;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.fonts.roboto.FlatRobotoFont;
import kg.musabaev.gschelper.swinggui.component.MenuBar;
import kg.musabaev.gschelper.swinggui.component.ReportLocalSaveForm;
import kg.musabaev.gschelper.swinggui.component.dialog.ExceptionDialog;
import kg.musabaev.gschelper.swinggui.component.dialog.WarningDialog;
import kg.musabaev.gschelper.swinggui.exception.AwtEventQueueExceptionHandler;
import kg.musabaev.gschelper.swinggui.exception.GlobalExceptionHandler;
import kg.musabaev.gschelper.swinggui.listener.DateRangeChangeListener;
import kg.musabaev.gschelper.swinggui.listener.MenuBarItemsClickListener;
import kg.musabaev.gschelper.swinggui.listener.ReportLocalSaveFormSubmitListener;
import kg.musabaev.gschelper.swinggui.listener.SavePathChangeListener;
import kg.musabaev.gschelper.swinggui.util.Constants;

import javax.swing.*;
import java.awt.*;
import java.nio.file.Path;

import static javax.swing.SwingUtilities.invokeLater;

public class ReportLocalSaveView extends JFrame implements ReportLocalSavePresenterViewContract {

    private final MenuBar menuBar;
    private final ReportLocalSaveForm form;

    public ReportLocalSaveView() {
        setupLaf();

        this.menuBar = new MenuBar();
        this.form = new ReportLocalSaveForm();

        setupAppIcon();
        setupMenuBar();
        setupUi();
        setupUncaughtExceptionHandler();
    }

    private void setupUi() {
        super.setTitle(Constants.APP_NAME);
        super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        super.setSize(Constants.APP_WIDTH, Constants.APP_HEIGHT);
        super.setLocationRelativeTo(null);

        super.add(form);
    }

    private void setupLaf() {
        FlatLightLaf.setup();
        FlatRobotoFont.install();
        UIManager.put("defaultFont", new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 14));
        UIManager.put("TextComponent.arc", 7);
        UIManager.put("Button.arc", 7);
    }

    private void setupAppIcon() {
        Image icon = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/icon.png"));
        super.setIconImage(icon);
    }

    private void setupMenuBar() {
        super.setJMenuBar(menuBar);
    }

    private void setupUncaughtExceptionHandler() {
        Thread.setDefaultUncaughtExceptionHandler(
            new GlobalExceptionHandler());
        Toolkit.getDefaultToolkit().getSystemEventQueue().push(
            new AwtEventQueueExceptionHandler());
    }

    // ========= Методы для изменения данных =========

    @Override
    public void setSavePath(Path savePath) {
        invokeLater(() -> form.savePathPickerInput().setSavePath(savePath));
    }

    @Override
    public void setSuggestedFilename(String suggestedFilename) {
        invokeLater(() -> form.savePathPickerInput().setSuggestedFilename(suggestedFilename));
    }

    // ========= Методы для управления UI состоянием =========

    @Override
    public void showFrame() {
        invokeLater(() -> {
            super.pack();
            SwingUtilities.updateComponentTreeUI(this);
            super.setVisible(true);
            super.getContentPane().requestFocusInWindow();
        });
    }

    @Override
    public void disableSubmitButton() {
        invokeLater(() -> {
            if (form.submitButton().isEnabled())
                form.submitButton().setEnabled(false);
            else
                throw new IllegalStateException("Кнопка подтверждения в форме уже выключен");
        });
    }

    @Override
    public void enableSubmitButton() {
        invokeLater(() -> {
            if (form.submitButton().isEnabled())
                throw new IllegalStateException("Кнопка подтверждения в форме уже включен");
            else
                form.submitButton().setEnabled(true);
        });
    }

    // ========= Методы для отображения диалоговых окон =========

    @Override
    public void showWarningDialog(String message) {
        invokeLater(() -> WarningDialog.show(message));
    }

    @Override
    public void showExceptionDialog(Exception e) {
        invokeLater(() -> ExceptionDialog.show(e));
    }

    // ========= Методы для работы со слушателями в дочерних компонентах =========

    @Override
    public void setMenuBarItemsClickListener(MenuBarItemsClickListener menuBarItemsClickListener) {
        menuBar.setItemsClickListener(menuBarItemsClickListener);
    }

    @Override
    public void addGenerateReportFormSubmitListener(ReportLocalSaveFormSubmitListener l) {
        form.addGenerateReportFormSubmitListener(l);
    }

    @Override
    public void removeGenerateReportFormSubmitListener(ReportLocalSaveFormSubmitListener l) {
        form.removeGenerateReportFormSubmitListener(l);
    }

    @Override
    public void addSavePathChangeListener(SavePathChangeListener l) {
        form.savePathPickerInput().addSavePathChangeListener(l);
    }

    @Override
    public void removeSavePathChangeListener(SavePathChangeListener l) {
        form.savePathPickerInput().removeSavePathChangeListener(l);
    }

    @Override
    public void addDateRangeChangeListener(DateRangeChangeListener l) {
        form.dateRangePickerInput().addDateRangeChangeListener(l);
    }

    @Override
    public void removeDateRangeChangeListener(DateRangeChangeListener l) {
        form.dateRangePickerInput().removeDateRangeChangeListener(l);
    }
}
