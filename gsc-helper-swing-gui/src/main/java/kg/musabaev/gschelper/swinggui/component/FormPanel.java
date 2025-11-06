package kg.musabaev.gschelper.swinggui.component;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;

public class FormPanel extends JPanel {
    private final JLabel dateRangePickerLabel;
    private final DateRangePicker dateRangePicker;

    private final JLabel fileChooserLabel;
    private final FileChooser fileChooser;

    private final JButton submit;

    public FormPanel() {
        this.dateRangePickerLabel = new JLabel();
        this.dateRangePicker = new DateRangePicker();
        this.fileChooserLabel = new JLabel();
        this.fileChooser = new FileChooser();
        this.submit = new JButton();
        setupUi();
        setupListeners();
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
        submit.setText("Принять");

        super.add(submit, "span 2 4, growx");
    }

    private void setupListeners() {

    }
}
