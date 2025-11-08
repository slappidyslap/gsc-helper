package kg.musabaev.gschelper.swinggui.component;

import kg.musabaev.gschelper.swinggui.listener.ReportGenerateFormListener;
import kg.musabaev.gschelper.swinggui.model.ReportGenerateFormModel;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;

import static com.google.common.base.Preconditions.checkNotNull;

public class ReportGenerateForm extends JPanel {

    private final ReportGenerateFormModel model;

    private final JLabel dateRangePickerLabel;
    private final DateRangePickerInput dateRangePickerInput;

    private final JLabel fileChooserLabel;
    private final FileChooserInput fileChooserInput;

    private final JButton submitButton;
    private ReportGenerateFormListener listener;

    public ReportGenerateForm(ReportGenerateFormModel model) {
        this.model = model;

        this.dateRangePickerLabel = new JLabel();
        this.dateRangePickerInput = new DateRangePickerInput(model);
        this.fileChooserLabel = new JLabel();
        this.fileChooserInput = new FileChooserInput(model);
        this.submitButton = new JButton();
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
        super.add(fileChooserInput, "growx");
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
            listener.generateReportClicked(
                dateRangePickerInput.startDate(),
                dateRangePickerInput.endDate(),
                fileChooserInput.savePath()));

        super.add(submitButton, "span 2 4, growx");
    }

    public JButton submitButton() {
        return submitButton;
    }

    public void setListener(ReportGenerateFormListener listener) {
        checkNotNull(listener);
        this.listener = listener;
    }
}
