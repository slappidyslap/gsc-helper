package kg.seo.musabaev.gui.api;

import kg.seo.musabaev.api.table.XlsxTableBuilder;
import kg.seo.musabaev.gsc.domain.SiteMetrics;
import kg.seo.musabaev.gui.api.domain.FailedSiteMetrics;
import kg.seo.musabaev.gui.api.domain.SiteMetricsList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Преобразователь Google Search Console метрик в формат для xlsx файла
 */
public class GscMetricsXlsxTableProcessorImpl implements GscMetricsXlsxTableProcessor {

    private static final Logger log = LoggerFactory.getLogger(GscMetricsXlsxTableProcessorImpl.class);

    private final XlsxTableBuilder xlsxBuilder;

    public GscMetricsXlsxTableProcessorImpl(XlsxTableBuilder xlsxBuilder) {
        this.xlsxBuilder = xlsxBuilder;
        log.info("Начата обработка метрик GSC в xlsx таблицу");
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
     * Добавляет метрики одного сайта в xlsx builder
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
     * Добавляет метрики нескольких сайтов в xlsx builder
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
     * возникшее при получении метрик сайта, в xlsx builder
     *
     * @param failedSiteMetrics список деталей об ошибке
     */
    public void addAllFailedSiteMetrics(List<FailedSiteMetrics> failedSiteMetrics) {
        failedSiteMetrics.forEach(this::addFailedSiteMetrics);
        log.info("Все детали об ошибке для {} сайтов добавлены в xlsx таблицу", failedSiteMetrics.size());
    }

    /**
     * Автоматически подбирает ширину столбцов
     */
    public void autoSizeColumns() {
        xlsxBuilder.autoSizeColumns();
        log.info("Все колонки xlsx таблицы выровнены по ширине");
    }

    @Override
    public XlsxTableBuilder getXlsxTableBuilder() {
        return xlsxBuilder;
    }

    @Override
    public byte[] process(SiteMetricsList allSitesMetrics) {
        addAllSiteMetrics(allSitesMetrics.metrics());
        addAllFailedSiteMetrics(allSitesMetrics.failedSites());

        autoSizeColumns();

        return xlsxBuilder.build();
    }
}
