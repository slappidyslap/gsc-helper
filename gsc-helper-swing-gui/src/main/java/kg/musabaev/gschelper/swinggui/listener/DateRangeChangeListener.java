package kg.musabaev.gschelper.swinggui.listener;

import java.time.LocalDate;
import java.util.EventListener;

@FunctionalInterface
public interface DateRangeChangeListener extends EventListener {

    void dateRangeChanged(LocalDate startDate, LocalDate endDate);
}
