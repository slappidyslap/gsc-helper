package kg.seo.musabaev.gui;

import kg.seo.musabaev.api.TableDataLocalFileSaver;
import kg.seo.musabaev.xlsx.ApachePoiXlsxBuilder;
import kg.seo.musabaev.api.exception.TableBuilderException;
import kg.seo.musabaev.searchconsole.SiteMetrics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.List;

/**
 * Адаптер для преобразования Google Search Console метрик в формат для xlsx файла
 */
public class GscMetricsXlsxTableAdapter {

    private static final Logger log = LoggerFactory.getLogger(GscMetricsXlsxTableAdapter.class);

    private final ApachePoiXlsxBuilder xlsxBuilder;
    private final TableDataLocalFileSaver xlsxSaver;

    public GscMetricsXlsxTableAdapter() {
        this.xlsxBuilder = new ApachePoiXlsxBuilder("Метрики");
        this.xlsxSaver = new TableDataLocalFileSaver(xlsxBuilder);
        xlsxBuilder.createHeader(
                "Тип ресурса",
                "URL",
                "Клики",
                "Показы",
                "CTR",
                "Средняя позиция");
    }

    /**
     * Добавляет метрики одного сайта в xlsx builder
     *
     * @param metrics метрики сайта
     */
    public void addSiteMetrics(SiteMetrics metrics) {
        log.debug("Добавление метрик для сайта: {}", metrics.url());

        xlsxBuilder.addRow(
                metrics.type().rus(),
                metrics.url(),
                metrics.clicks(),
                metrics.impressions(),
                metrics.ctr(),
                metrics.avgPosition());

        log.debug("Метрики для сайта {} успешно добавлены", metrics.url());
    }

    /**
     * Добавляет метрики нескольких сайтов в xlsx builder
     *
     * @param metricsList список метрик сайтов
     */
    public void addAllSiteMetrics(List<SiteMetrics> metricsList) {
        log.info("Добавление метрик для {} сайтов", metricsList.size());
        metricsList.forEach(this::addSiteMetrics);
        log.info("Все метрики успешно добавлены");
    }

    /**
     * Автоматически подбирает ширину столбцов
     */
    public void autoSizeColumns() {
        xlsxBuilder.autoSizeColumns(xlsxBuilder.getRowCount());
    }

    /**
     * Сохраняет xlsx файл по указанному пути
     *
     * @param savePath путь для сохранения файла
     * @throws kg.seo.musabaev.api.exception.LocalFileNotFoundException если файл не найден или папка не существует
     * @throws TableBuilderException если произошла ошибка при сохранении
     */
    public void save(File savePath) {
        xlsxSaver.save(savePath, xlsxBuilder.build());
    }

}