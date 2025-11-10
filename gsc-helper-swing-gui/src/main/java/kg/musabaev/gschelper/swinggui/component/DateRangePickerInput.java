package kg.musabaev.gschelper.swinggui.component;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import kg.musabaev.gschelper.swinggui.listener.DateRangeChangeListener;
import raven.datetime.component.date.DatePicker;

import javax.swing.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;
import static raven.datetime.component.date.DatePicker.DateSelectionMode.BETWEEN_DATE_SELECTED;

@SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
public class DateRangePickerInput extends JTextField {

    private final DatePicker datePicker;
    private final List<DateRangeChangeListener> listeners;

    public DateRangePickerInput() {
        this.datePicker = new DatePicker();
        this.listeners = new ArrayList<>();

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
        /*// Компонент DatePicker из сторонней библиотеки
        // не вставляет новое значение в model (что абсолютно нормально).
        // Это строчка это исправляет
        datePicker.addDateSelectionListener(dateEvent ->
            model.setDateRange(super.getText()));
        // model -> TextField.text (dateRange)
        model.addPropertyChangeListener(event -> {
            if (model.DATE_RANGE_FIELD_NAME.equals(event.getPropertyName()))
                super.setText(((String) event.getNewValue()));
        });*/
    }

    public void addListener(DateRangeChangeListener l) {
        synchronized (listeners) {
            listeners.add(checkNotNull(l));
        }
    }

    public void removeListener(DateRangeChangeListener l) {
        synchronized (listeners) {
            listeners.remove(checkNotNull(l));
        }
    }
}
