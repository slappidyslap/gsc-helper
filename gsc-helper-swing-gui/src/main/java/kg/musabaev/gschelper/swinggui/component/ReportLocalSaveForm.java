package kg.musabaev.gschelper.swinggui.component;

import kg.musabaev.gschelper.swinggui.component.dialog.ExceptionDialog;
import kg.musabaev.gschelper.swinggui.component.dialog.WarningDialog;
import kg.musabaev.gschelper.swinggui.listener.DateRangeChangeListener;
import kg.musabaev.gschelper.swinggui.listener.ReportLocalSaveFormSubmitListener;
import kg.musabaev.gschelper.swinggui.listener.SavePathChangeListener;
import kg.musabaev.gschelper.swinggui.view.contract.ReportLocalSavePresenterViewContract;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

public class ReportLocalSaveForm extends JPanel implements ReportLocalSavePresenterViewContract {

    private final JLabel dateRangePickerLabel;
    private final DateRangePickerInput dateRangePickerInput;

    private final JLabel savePathPickerLabel;
    private final SavePathPickerInput savePathPickerInput;

    private final JButton submitButton;

    private final List<ReportLocalSaveFormSubmitListener> listeners;

    public ReportLocalSaveForm() {
        this.dateRangePickerLabel = new JLabel();
        this.dateRangePickerInput = new DateRangePickerInput();
        this.savePathPickerLabel = new JLabel();
        this.savePathPickerInput = new SavePathPickerInput();
        this.submitButton = new JButton();
        this.listeners = new ArrayList<>();
        setupUi();
    }

    private void setupUi() {
        super.setLayout(new MigLayout(
            /*layoutConstraints*/ "wrap 2",
            /*colConstraints*/ "[][grow, 30:400:n]",
            /*rowConstraints*/ "[][][grow][]"));
        setupDateRangePickerInput();
        setupFileChooserInput();
        setupVerticalSpace();
        setupSubmitButton();
    }

    private void setupDateRangePickerInput() {
        dateRangePickerLabel.setText("Диапазон дат:");

        super.add(dateRangePickerLabel);
        super.add(dateRangePickerInput, "growx");
    }

    private void setupFileChooserInput() {
        savePathPickerLabel.setText("Путь сохранения:");

        super.add(savePathPickerLabel);
        super.add(savePathPickerInput, "growx");
    }

    private void setupVerticalSpace() {
        super.add(new JPanel() {{
            setPreferredSize(new Dimension(0, 0));
            setMinimumSize(new Dimension(0, 0));
        }}, "wrap");
    }

    private void setupSubmitButton() {
        submitButton.setText("Принять");
        submitButton.addActionListener(e ->
            fireGenerateReportFormSubmit(
                dateRangePickerInput.startDate(),
                dateRangePickerInput.endDate(),
                savePathPickerInput.savePath()));

        super.add(submitButton, "span 2 4, growx");
    }


    // ========= Методы для изменения данных =========

    @Override
    public void setSavePath(Path savePath) {
        savePathPickerInput().setSavePath(savePath);
    }

    @Override
    public void setSuggestedFilename(String suggestedFilename) {
        savePathPickerInput().setSuggestedFilename(suggestedFilename);
    }

    // ========= Методы для управления UI состоянием =========

    @Override
    public void disableSubmitButton() {
        if (submitButton().isEnabled())
            submitButton().setEnabled(false);
        else
            throw new IllegalStateException("Кнопка подтверждения в форме уже выключен");
    }

    @Override
    public void enableSubmitButton() {
        if (submitButton().isEnabled())
            throw new IllegalStateException("Кнопка подтверждения в форме уже включен");
        else
            submitButton().setEnabled(true);
    }

    // ========= Методы для отображения диалоговых окон =========

    @Override
    public void showWarningDialog(String message) {
        WarningDialog.show(message);
    }

    @Override
    public void showExceptionDialog(Exception e) {
        ExceptionDialog.show(e);
    }

    // ========= Методы для работы со слушателями =========

    @Override
    public void addGenerateReportFormSubmitListener(ReportLocalSaveFormSubmitListener l) {
        synchronized (listeners) {
            listeners.add(checkNotNull(l));
        }
    }

    @Override
    public void removeGenerateReportFormSubmitListener(ReportLocalSaveFormSubmitListener l) {
        synchronized (listeners) {
            listeners.add(checkNotNull(l));
        }
    }

    public void fireGenerateReportFormSubmit(LocalDate startDate, LocalDate endDate, Path savePath) {
        synchronized (listeners) {
            for (ReportLocalSaveFormSubmitListener l : listeners) {
                l.formSubmitted(startDate, endDate, savePath);
            }
        }
    }

    @Override
    public void addDateRangeChangeListener(DateRangeChangeListener l) {
        dateRangePickerInput().addDateRangeChangeListener(l);
    }

    @Override
    public void removeDateRangeChangeListener(DateRangeChangeListener l) {
        dateRangePickerInput().removeDateRangeChangeListener(l);
    }

    @Override
    public void addSavePathChangeListener(SavePathChangeListener l) {
        savePathPickerInput().addSavePathChangeListener(l);
    }

    @Override
    public void removeSavePathChangeListener(SavePathChangeListener l) {
        savePathPickerInput().removeSavePathChangeListener(l);
    }

    // ========= Методы для обращения к дочерним компонентам =========

    public JLabel dateRangePickerLabel() {
        return dateRangePickerLabel;
    }

    public DateRangePickerInput dateRangePickerInput() {
        return dateRangePickerInput;
    }

    public JLabel savePathPickerLabel() {
        return savePathPickerLabel;
    }

    public SavePathPickerInput savePathPickerInput() {
        return savePathPickerInput;
    }

    public JButton submitButton() {
        return submitButton;
    }
}
