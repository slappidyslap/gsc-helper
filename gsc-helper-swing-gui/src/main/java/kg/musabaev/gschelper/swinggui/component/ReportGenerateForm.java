package kg.musabaev.gschelper.swinggui.component;

import kg.musabaev.gschelper.swinggui.listener.DateRangeChangeListener;
import kg.musabaev.gschelper.swinggui.listener.ReportGenerateFormSubmitListener;
import kg.musabaev.gschelper.swinggui.listener.SavePathChangeListener;
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

    private final JLabel fileChooserLabel;
    private final SavePathPickerInput savePathPickerInput;

    private final JButton submitButton;

    private final List<ReportGenerateFormSubmitListener> listeners;

    public ReportGenerateForm() {
        this.dateRangePickerLabel = new JLabel();
        this.dateRangePickerInput = new DateRangePickerInput();
        this.fileChooserLabel = new JLabel();
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
        fileChooserLabel.setText("Путь сохранения:");

        super.add(fileChooserLabel);
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

    public JButton submitButton() {
        return submitButton;
    }

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

    public void addSavePathChangedListener(SavePathChangeListener l) {
        savePathPickerInput.addListener(l);
    }

    public void removeSavePathChangedListener(SavePathChangeListener l) {
        savePathPickerInput.removeListener(l);
    }

    public void addDateRangeChangedListener(DateRangeChangeListener l) {
        dateRangePickerInput.addListener(l);
    }

    public void removeDateRangeChangedListener(DateRangeChangeListener l) {
        dateRangePickerInput.removeListener(l);
    }
}
