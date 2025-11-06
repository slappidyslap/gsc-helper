package kg.musabaev.gschelper.swinggui.component;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import raven.datetime.component.date.DatePicker;

import javax.swing.*;

import static raven.datetime.component.date.DatePicker.DateSelectionMode.BETWEEN_DATE_SELECTED;

public class DateRangePicker extends JTextField {

    private final DatePicker datePicker;

    public DateRangePicker() {
        this.datePicker = new DatePicker();
        setupUi();
    }

    protected void setupUi() {
        super.setEditable(false);

        datePicker.setDateSelectionMode(BETWEEN_DATE_SELECTED);
        datePicker.setUsePanelOption(true);
        datePicker.setCloseAfterSelected(true);
        datePicker.setEditor(this);
        datePicker.setEditorIcon(new FlatSVGIcon("date-range-picker-icon.svg", 0.7F));
    }
}
