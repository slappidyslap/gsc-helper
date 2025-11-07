package kg.musabaev.gschelper.api.adapter;

import kg.musabaev.gschelper.api.gsc.collector.domain.SiteMetricsList;
import kg.musabaev.gschelper.api.table.TableBuilder;

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
