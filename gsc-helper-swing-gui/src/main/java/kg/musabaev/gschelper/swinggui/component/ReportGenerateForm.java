package kg.musabaev.gschelper.swinggui.component;

import kg.musabaev.gschelper.swinggui.listener.ReportGenerateFormSubmitListener;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

public class ReportGenerateForm extends JPanel {

    private final JLabel dateRangePickerLabel;
    private final DateRangePickerInput dateRangePickerInput;

    private final JLabel savePathPickerLabel;
    private final SavePathPickerInput savePathPickerInput;

    private final JButton submitButton;

    private final List<ReportGenerateFormSubmitListener> listeners;

    public ReportGenerateForm() {
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

    // ========= Методы для работы со слушателями в дочерних компонентах =========

    public void addGenerateReportFormSubmitListener(ReportGenerateFormSubmitListener l) {
        synchronized (listeners) {
            listeners.add(checkNotNull(l));
        }
    }

    public void removeGenerateReportFormSubmitListener(ReportGenerateFormSubmitListener l) {
        synchronized (listeners) {
            listeners.add(checkNotNull(l));
        }
    }

    public void fireGenerateReportFormSubmit(LocalDate startDate, LocalDate endDate, Path savePath) {
        synchronized (listeners) {
            for (ReportGenerateFormSubmitListener l : listeners) {
                l.formSubmitted(startDate, endDate, savePath);
            }
        }
    }
}
