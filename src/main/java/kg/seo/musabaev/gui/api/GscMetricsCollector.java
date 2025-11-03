package kg.seo.musabaev.gui.api;

import kg.seo.musabaev.gui.api.domain.FailedSiteMetrics;
import kg.seo.musabaev.gui.api.domain.SiteMetricsList;

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
