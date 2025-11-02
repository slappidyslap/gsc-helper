package kg.seo.musabaev;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.List;

/**
 * Адаптер для преобразования GSC метрик в формат для Excel
 */
public class GscMetricsExcelReportAdapter {

    private static final Logger log = LoggerFactory.getLogger(GscMetricsExcelReportAdapter.class);

    private final ExcelBuilder excelBuilder;

    public GscMetricsExcelReportAdapter() {
        this.excelBuilder = new ExcelBuilder("Метрики");
        excelBuilder.createHeader(
                "Тип ресурса",
                "URL",
                "Клики",
                "Показы",
                "CTR",
                "Средняя позиция");
        log.debug("GscMetricsExcelReportAdapter инициализирован");
    }

    /**
     * Добавляет метрики одного сайта в Excel
     *
     * @param metrics метрики сайта
     */
    public void addSiteMetrics(SiteMetrics metrics) {
        log.debug("Добавление метрик для сайта: {}", metrics.url());

        excelBuilder.addRow(
                metrics.type().rus(),
                metrics.url(),
                metrics.clicks(),
                metrics.impressions(),
                metrics.ctr(),
                metrics.avgPosition()
        );

        log.debug("Метрики для сайта {} успешно добавлены", metrics.url());
    }

    /**
     * Добавляет метрики нескольких сайтов в Excel
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
        excelBuilder.autoSizeColumns(excelBuilder.getRowCount());
    }

    /**
     * Сохраняет Excel файл по указанному пути
     *
     * @param file путь для сохранения файла
     * @throws ExcelReportException если файл не найден или папка не существует
     * @throws ExcelReportException если произошла ошибка при сохранении
     */
    public void save(File file) {
        excelBuilder.save(file);
    }

}