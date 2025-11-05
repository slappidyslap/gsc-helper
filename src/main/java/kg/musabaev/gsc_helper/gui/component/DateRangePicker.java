package kg.musabaev.gsc_helper.gui.component;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import raven.datetime.component.date.DatePicker;

import javax.swing.*;

import static raven.datetime.component.date.DatePicker.DateSelectionMode.BETWEEN_DATE_SELECTED;

public class DateRangePicker extends BaseInput {

    private final JLabel label;
    private final JTextField textField;
    private final DatePicker datePicker;

    public DateRangePicker() {
        this.label = new JLabel();
        this.textField = new JTextField();
        this.datePicker = new DatePicker();
        setupUi();
    }

    @Override
    protected void setupUi() {
        super.setupUi();

        label.setText("Диапазон дат:");
        datePicker.setDateSelectionMode(BETWEEN_DATE_SELECTED);
        datePicker.setUsePanelOption(true);
        datePicker.setCloseAfterSelected(true);
        datePicker.setEditor(textField);
        datePicker.setEditorIcon(new FlatSVGIcon("date-range-picker-icon.svg", 0.7F));

        super.add(label);
        super.add(textField);
    }
}
