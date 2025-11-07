package kg.musabaev.gschelper.swinggui.component;

import kg.musabaev.gschelper.swinggui.listener.ReportGenerateFormListener;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;

import static com.google.common.base.Preconditions.checkNotNull;

public class ReportGenerateForm extends JPanel {
    private final JLabel dateRangePickerLabel;
    private final DateRangePicker dateRangePicker;

    private final JLabel fileChooserLabel;
    private final FileChooser fileChooser;

    private final JButton submitButton;
    private ReportGenerateFormListener listener;

    public ReportGenerateForm() {
        this.dateRangePickerLabel = new JLabel();
        this.dateRangePicker = new DateRangePicker();
        this.fileChooserLabel = new JLabel();
        this.fileChooser = new FileChooser();
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
        super.add(dateRangePicker, "growx");
    }

    private void setupFileChooserInput() {
        fileChooserLabel.setText("Путь сохранения:");

        super.add(fileChooserLabel);
        super.add(fileChooser, "growx");
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
                dateRangePicker.startDate(),
                dateRangePicker.endDate(),
                fileChooser.savePath()));

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
