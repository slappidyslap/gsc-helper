package kg.musabaev.gschelper.swinggui.component;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import kg.musabaev.gschelper.swinggui.exception.DateRangeValidationException;
import kg.musabaev.gschelper.swinggui.listener.DateRangeChangeListener;
import raven.datetime.component.date.DatePicker;

import javax.swing.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;
import static raven.datetime.component.date.DatePicker.DateSelectionMode.BETWEEN_DATE_SELECTED;

public class DateRangePickerInput extends JTextField {

    private final DatePicker datePicker;
    private final List<DateRangeChangeListener> dateRangeChangeListeners;

    public DateRangePickerInput() {
        this.datePicker = new DatePicker();
        this.dateRangeChangeListeners = new ArrayList<>();

        setupUi();
        setupListeners();
    }

    public LocalDate startDate() {
        if (datePicker.getSelectedDateRange() == null)
            throw new DateRangeValidationException("Диапазон дат не указан");

        return datePicker.getSelectedDateRange()[0];
    }

    public LocalDate endDate() {
        if (datePicker.getSelectedDateRange() == null)
            throw new DateRangeValidationException("Диапазон дат не указан");

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
            fireDateRangeChange(startDate(), endDate()));
    }

    public void addDateRangeChangeListener(DateRangeChangeListener l) {
        synchronized (dateRangeChangeListeners) {
            dateRangeChangeListeners.add(checkNotNull(l));
        }
    }

    public void removeDateRangeChangeListener(DateRangeChangeListener l) {
        synchronized (dateRangeChangeListeners) {
            dateRangeChangeListeners.remove(checkNotNull(l));
        }
    }

    public void fireDateRangeChange(LocalDate startDate, LocalDate endDate) {
        synchronized (dateRangeChangeListeners) {
            for (DateRangeChangeListener l : dateRangeChangeListeners) {
                l.dateRangeChanged(startDate, endDate, super.getText());
            }
        }
    }
}
