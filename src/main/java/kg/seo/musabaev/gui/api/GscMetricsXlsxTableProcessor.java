package kg.seo.musabaev.gui.api;

import kg.seo.musabaev.api.table.XlsxTableBuilder;
import kg.seo.musabaev.gui.api.domain.SiteMetricsList;

/**
 * Интерфейс для преобразования Google Search Console метрик в формат для xlsx файла.
 */
public interface GscMetricsXlsxTableProcessor {

    /**
     * Возвращает связанный {@link XlsxTableBuilder}, используемый для формирования табличных данных.
     *
     * @return объект {@link XlsxTableBuilder}
     */
    XlsxTableBuilder getXlsxTableBuilder();

    /**
     * Преобразует объект {@link SiteMetricsList} в формат для xlsx файла
     * и возвращает байтовый массив xlsx файл.
     *
     * @return байтовый массив xlsx сформированного xlsx файла.
     */
    byte[] process(SiteMetricsList metrics);
}
