package kg.musabaev.gschelper.core.adapter;

import kg.musabaev.gschelper.api.adapter.GscMetricsTableAdapter;
import kg.musabaev.gschelper.api.gsc.collector.domain.FailedSiteMetrics;
import kg.musabaev.gschelper.api.gsc.collector.domain.SiteMetricsList;
import kg.musabaev.gschelper.api.gsc.domain.SiteMetrics;
import kg.musabaev.gschelper.api.table.TableBuilder;
import kg.musabaev.gschelper.api.table.xlsx.XlsxTableBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Преобразователь Google Search Console метрик в формат для xlsx файла.
 */
public class GscMetricsXlsxTableAdapter implements GscMetricsTableAdapter {

    private static final Logger log = LoggerFactory.getLogger(GscMetricsXlsxTableAdapter.class);

    private final XlsxTableBuilder xlsxBuilder;

    public GscMetricsXlsxTableAdapter(XlsxTableBuilder xlsxBuilder) {
        this.xlsxBuilder = xlsxBuilder;
    }

    /**
     * Добавляет названия колонок
     */
    public void addHeaders() {
        xlsxBuilder.createHeader(
            "Тип ресурса",
            "URL",
            "Клики",
            "Показы",
            "CTR",
            "Средняя позиция");
        log.info("Названия колонок добавлены в xlsx таблицу");
    }

    /**
     * Добавляет метрики одного сайта в xlsx builder.
     *
     * @param metrics метрики сайта
     */
    public void addSiteMetrics(SiteMetrics metrics) {
        xlsxBuilder.addRow(
            metrics.type().rus(),
            metrics.url(),
            metrics.clicks(),
            metrics.impressions(),
            metrics.ctr(),
            metrics.avgPosition());
        log.info("Метрики для сайта {} успешно добавлены в xlsx таблицу", metrics.url());
    }

    /**
     * Добавляет метрики нескольких сайтов в xlsx builder.
     *
     * @param metricsList список метрик сайтов
     */
    public void addAllSiteMetrics(List<SiteMetrics> metricsList) {
        metricsList.forEach(this::addSiteMetrics);
        log.info("Все метрики для {} сайтов добавлены в xlsx таблицу", metricsList.size());
    }

    /**
     * Добавляет детали об ошибке,
     * возникшей при получении метрик сайта, в xlsx файла.
     *
     * @param failedSite детали об ошибке
     */
    public void addFailedSiteMetrics(FailedSiteMetrics failedSite) {
        xlsxBuilder.addRow(
            failedSite.type().rus(),
            failedSite.url(),
            failedSite.throwable());
        log.info("Детали об ошибке, возникшее при получении метрик сайта {} успешно добавлены в xlsx таблицу",
            failedSite.url());
    }

    /**
     * Добавляет несколько деталей об ошибке,
     * возникшее при получении метрик сайта, в xlsx builder.
     *
     * @param failedSiteMetrics список деталей об ошибке
     */
    public void addAllFailedSiteMetrics(List<FailedSiteMetrics> failedSiteMetrics) {
        failedSiteMetrics.forEach(this::addFailedSiteMetrics);
        log.info("Все детали об ошибке для {} сайтов добавлены в xlsx таблицу", failedSiteMetrics.size());
    }

    /**
     * Автоматически подбирает ширину столбцов.
     */
    public void autoSizeColumns() {
        xlsxBuilder.autoSizeColumns();
        log.info("Все колонки xlsx таблицы выровнены по ширине");
    }

    /**
     * Возвращает связанный {@link TableBuilder}, используемый для формирования табличных данных.
     *
     * @return объект {@link TableBuilder}
     */
    @Override
    public TableBuilder getTableBuilder() {
        return xlsxBuilder;
    }

    /**
     * Преобразует объект {@link SiteMetricsList} в формат для xlsx файла
     * и возвращает байтовый массив xlsx файл.
     *
     * @return байтовый массив xlsx сформированного xlsx файла.
     */
    @Override
    public byte[] adapt(SiteMetricsList allSitesMetrics) {
        log.info("Начата обработка метрик из GSC в xlsx файл");

        addHeaders();
        addAllSiteMetrics(allSitesMetrics.metrics());
        addAllFailedSiteMetrics(allSitesMetrics.failedSites());

        autoSizeColumns();

        byte[] builtXlsxFile = xlsxBuilder.build();

        log.info("Все метрики из GSC добавлены в xlsx файл");

        return builtXlsxFile;
    }
}
