package kg.musabaev.gschelper.swinggui.component;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import kg.musabaev.gschelper.swinggui.model.ReportGenerateFormModel;
import raven.datetime.component.date.DatePicker;

import javax.swing.*;
import java.time.LocalDate;

import static raven.datetime.component.date.DatePicker.DateSelectionMode.BETWEEN_DATE_SELECTED;

public class DateRangePickerInput extends JTextField {

    private final DatePicker datePicker;
    private final ReportGenerateFormModel model;

    public DateRangePickerInput(ReportGenerateFormModel model) {
        this.model = model;

        this.datePicker = new DatePicker();

        setupUi();
        setupListeners();
    }

    public LocalDate startDate() {
        return datePicker.getSelectedDateRange()[0];
    }

    public LocalDate endDate() {
        return datePicker.getSelectedDateRange()[1];
    }

    protected void setupUi() {
        super.setEditable(false);

        datePicker.setDateSelectionMode(BETWEEN_DATE_SELECTED);
        datePicker.setUsePanelOption(true);
        datePicker.setCloseAfterSelected(true);
        datePicker.setEditor(this);
        datePicker.setEditorIcon(new FlatSVGIcon("date-range-picker-icon.svg", 0.7F));
    }

    private void setupListeners() {
        // Компонент DatePicker из сторонней библиотеки
        // не вставляет новое значение в model (что абсолютно нормально).
        // Это строчка это исправляет
        datePicker.addDateSelectionListener(dateEvent ->
            model.setDateRange(super.getText()));
        // model -> TextField.text (dateRange)
        model.addPropertyChangeListener(event -> {
            if (model.DATE_RANGE_FIELD_NAME.equals(event.getPropertyName()))
                super.setText(((String) event.getNewValue()));
        });
    }

    public ReportGenerateFormModel model() {
        return model;
    }
}
