package kg.musabaev.gschelper.swinggui.view;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.fonts.roboto.FlatRobotoFont;
import kg.musabaev.gschelper.swinggui.component.dialog.ExceptionDialog;
import kg.musabaev.gschelper.swinggui.component.MenuBar;
import kg.musabaev.gschelper.swinggui.component.ReportGenerateForm;
import kg.musabaev.gschelper.swinggui.component.dialog.WarningDialog;
import kg.musabaev.gschelper.swinggui.exception.AwtEventQueueExceptionHandler;
import kg.musabaev.gschelper.swinggui.exception.GlobalExceptionHandler;
import kg.musabaev.gschelper.swinggui.listener.DateRangeChangeListener;
import kg.musabaev.gschelper.swinggui.listener.ReportGenerateFormSubmitListener;
import kg.musabaev.gschelper.swinggui.listener.SavePathChangeListener;
import kg.musabaev.gschelper.swinggui.listener.impl.MenuBarListenerImpl;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.time.LocalDate;

import static com.google.common.base.Preconditions.checkNotNull;

public class ReportGenerateView extends JFrame {

    private final JMenuBar menuBar;
    private final ReportGenerateForm form;

    public ReportGenerateView() {
        setupLaf();

        this.menuBar = new MenuBar();
        this.form = new ReportGenerateForm();

        setupAppIcon();
        setupMenuBar();
        setupUi();
        setupUncaughtExceptionHandler();
    }

    private void setupUi() {
        super.setTitle("GSC Helper");
        super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        super.setSize(650, 200);
        super.setLocationRelativeTo(null);

        super.add(form);
    }

    public void showFrame() {
        SwingUtilities.invokeLater(() -> {
            super.pack();
            SwingUtilities.updateComponentTreeUI(this);
            super.setVisible(true);
            super.getContentPane().requestFocusInWindow();
        });
    }

    private void setupLaf() {
        FlatDarkLaf.setup();
        FlatRobotoFont.install();
        UIManager.put("defaultFont", new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 15));
        UIManager.put("TextComponent.arc", 7);
        UIManager.put("Button.arc", 7);
    }

    private void setupAppIcon() {
        Image icon = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/icon.png"));
        super.setIconImage(icon);
    }

    private void setupMenuBar() {
        MenuBar menuBar = new MenuBar();
        menuBar.setListener(new MenuBarListenerImpl(this));
        super.setJMenuBar(menuBar);
    }

    /*public void setReportGeneratePresenter(ReportGeneratePresenter presenter) {
        checkNotNull(presenter);
        this.presenter = presenter;
    }*/

    private void setupUncaughtExceptionHandler() {
        Thread.setDefaultUncaughtExceptionHandler(
            new GlobalExceptionHandler());
        Toolkit.getDefaultToolkit().getSystemEventQueue().push(
            new AwtEventQueueExceptionHandler());
    }

    public void onClickGenerateReport(LocalDate startDate, LocalDate endDate, File savePath) {
        checkNotNull(startDate);
        checkNotNull(endDate);
        checkNotNull(savePath);
//        presenter.onClickGenerateReport(startDate, endDate, savePath);
    }

    // ========= Методы для работы с модальными окнами =========

    public void showWarningDialog(String message) {
        WarningDialog.show(message);
    }

    public void showExceptionDialog(Exception e) {
        ExceptionDialog.show(e);
    }

    // ========= Методы для работы с дочерними компонентами =========

    public void disableSubmitButton() {
        if (form.submitButton().isEnabled())
            form.submitButton().setEnabled(false);
        else
            throw new IllegalStateException("Кнопка подтверждения в форме уже выключен");
    }

    public void enableSubmitButton() {
        if (form.submitButton().isEnabled())
            throw new IllegalStateException("Кнопка подтверждения в форме уже включен");
        else
            form.submitButton().setEnabled(true);
    }

    // ========= Методы для работы со слушателями в дочерних компонентах =========

    public void addGenerateReportFormSubmitListener(ReportGenerateFormSubmitListener l) {
        form.addGenerateReportFormSubmitListener(l);
    }

    public void removeGenerateReportFormSubmitListener(ReportGenerateFormSubmitListener l) {
        form.removeGenerateReportFormSubmitListener(l);
    }

    public void addSavePathChangeListener(SavePathChangeListener l) {
        form.savePathPickerInput().addSavePathChangeListener(l);
    }

    public void removeSavePathChangeListener(SavePathChangeListener l) {
        form.savePathPickerInput().removeSavePathChangeListener(l);
    }

    public void addDateRangeChangeListener(DateRangeChangeListener l) {
        form.dateRangePickerInput().addDateRangeChangeListener(l);
    }

    public void removeDateRangeChangeListener(DateRangeChangeListener l) {
        form.dateRangePickerInput().removeDateRangeChangeListener(l);
    }
}
