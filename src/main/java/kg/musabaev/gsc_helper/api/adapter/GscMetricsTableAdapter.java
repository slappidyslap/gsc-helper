package kg.musabaev.gsc_helper.api.adapter;

import kg.musabaev.gsc_helper.api.table.TableBuilder;
import kg.musabaev.gsc_helper.api.gsc.collector.domain.SiteMetricsList;

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
    byte[] adapt(SiteMetricsList metrics);
}
