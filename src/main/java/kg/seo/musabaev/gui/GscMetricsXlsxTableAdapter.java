package kg.seo.musabaev.gui;

import kg.seo.musabaev.api.table.TableDataLocalFileSaver;
import kg.seo.musabaev.api.table.exception.TableBuilderException;
import kg.seo.musabaev.api.table.XlsxTableBuilder;
import kg.seo.musabaev.gsc.domain.SiteMetrics;
import kg.seo.musabaev.xlsx.ApachePoiXlsxBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.List;

/**
 * Адаптер для преобразования Google Search Console метрик в формат для xlsx файла
 */
@Deprecated
public class GscMetricsXlsxTableAdapter {

    private static final Logger log = LoggerFactory.getLogger(GscMetricsXlsxTableAdapter.class);

    private final XlsxTableBuilder xlsxBuilder;
    private final TableDataLocalFileSaver fileSaver;

    public GscMetricsXlsxTableAdapter() {
        this.xlsxBuilder = new ApachePoiXlsxBuilder("Метрики");
        this.fileSaver = new TableDataLocalFileSaver(xlsxBuilder);
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
        log.info("Всех метрики для {} сайтов добавлены в xlsx таблицу", metricsList.size());
    }

    /**
     * Автоматически подбирает ширину столбцов
     */
    public void autoSizeColumns() {
        xlsxBuilder.autoSizeColumns();
        log.info("Все колонки xlsx таблицы выровнены по ширине");
    }

    /**
     * Сохраняет xlsx файл по указанному пути
     *
     * @param savePath путь для сохранения файла
     * @throws kg.seo.musabaev.api.table.exception.LocalFileNotFoundException если файл не найден или папка не существует
     * @throws TableBuilderException если произошла ошибка при сохранении
     */
    public void save(File savePath) {
        fileSaver.save(savePath, xlsxBuilder.build());
        log.info("Таблица в формате xlsx сохранена в {}", savePath.getAbsolutePath());
    }
}
