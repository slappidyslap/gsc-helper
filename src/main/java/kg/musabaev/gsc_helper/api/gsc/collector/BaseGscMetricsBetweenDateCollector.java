package kg.musabaev.gsc_helper.api.gsc.collector;

import kg.musabaev.gsc_helper.api.gsc.collector.domain.SiteMetricsList;
import org.jetbrains.annotations.ApiStatus;

import java.time.LocalDate;

/**
 * Абстрактный класс для сбора метрик всех сайтов из Google Search Console API за указанный период.
 */
public abstract class BaseGscMetricsBetweenDateCollector implements GscMetricsCollector {

    private LocalDate startDate = LocalDate.now();
    private LocalDate endDate = LocalDate.now();

    /**
     * Собирает метрики всех сайтов за указанный период.
     *
     * @param startDate дата начала периода
     * @param endDate дата окончания периода
     * @return результат сбора метрик. Объект {@link SiteMetricsList}
     */
    public abstract SiteMetricsList collectBetweenDate(LocalDate startDate, LocalDate endDate);

    /**
     * Собирает метрики всех сайтов.
     *
     * <p>Лучше использовать {@link #collectBetweenDate(LocalDate, LocalDate)}.</p>
     *
     * @return результат сбора метрик. Объект {@link SiteMetricsList}
     */
    @ApiStatus.Internal
    @Override
    public final SiteMetricsList collect() {
        return collectBetweenDate(startDate, endDate);
    }

    public final LocalDate startDate() {
        return startDate;
    }

    public final void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public final LocalDate endDate() {
        return endDate;
    }

    public final void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
}
