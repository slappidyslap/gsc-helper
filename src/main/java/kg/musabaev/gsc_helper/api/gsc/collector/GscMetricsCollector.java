package kg.musabaev.gsc_helper.api.gsc.collector;

import kg.musabaev.gsc_helper.api.gsc.collector.domain.FailedSiteMetrics;
import kg.musabaev.gsc_helper.api.gsc.collector.domain.SiteMetricsList;

/**
 * Интерфейс для сбора метрик всех сайтов из Google Search Console API.
 */
public interface GscMetricsCollector {

    /**
     * Собирает метрики всех сайтов.
     * Обрабатывает все ошибки и ложит их в контейнер {@link FailedSiteMetrics}.
     *
     * @return результат сбора метрик. Объект {@link SiteMetricsList}
     */
    SiteMetricsList collect();
}
