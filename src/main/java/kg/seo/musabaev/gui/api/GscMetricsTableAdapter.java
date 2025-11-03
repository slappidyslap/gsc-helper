package kg.seo.musabaev.gui.api;

import kg.seo.musabaev.api.table.TableBuilder;
import kg.seo.musabaev.gui.api.domain.SiteMetricsList;

/**
 * Интерфейс для преобразования Google Search Console метрик в табличный формат.
 */
public interface GscMetricsTableAdapter {

    /**
     * Возвращает связанный {@link TableBuilder}, используемый для формирования табличных данных.
     *
     * @return объект {@link TableBuilder}
     */
    TableBuilder getTableBuilder();

    /**
     * Преобразует объект {@link SiteMetricsList} в табличный формат
     * и возвращает байтовый массив данных.
     *
     * @return байтовый массив табличных данных.
     */
    byte[] process(SiteMetricsList metrics);
}
