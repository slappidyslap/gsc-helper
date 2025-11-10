package kg.musabaev.gschelper.swinggui.listener;

import kg.musabaev.gschelper.swinggui.component.DateRangePickerInput;

import java.time.LocalDate;
import java.util.EventListener;

/**
 * Слушатель изменения даты в {@link DateRangePickerInput}
 */
@FunctionalInterface
public interface DateRangeChangeListener extends EventListener {

    /**
     * @param startDate дата начала отчетного периода
     * @param endDate дата конца отчетного периода
     * @param formattedDateRange отформатированный диапазон дат
     */
    void dateRangeChanged(LocalDate startDate, LocalDate endDate, String formattedDateRange);
}
