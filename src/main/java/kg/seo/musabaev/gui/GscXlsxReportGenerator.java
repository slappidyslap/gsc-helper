package kg.seo.musabaev.gui;

import com.google.api.services.searchconsole.v1.model.SearchAnalyticsQueryResponse;
import com.google.api.services.searchconsole.v1.model.WmxSite;
import kg.seo.musabaev.api.gsc.GscAnalyticsResponse;
import kg.seo.musabaev.api.gsc.GscApiBuilder;
import kg.seo.musabaev.api.gsc.GscService;
import kg.seo.musabaev.gsc.GscServiceImpl;
import kg.seo.musabaev.gsc.domain.SiteMetrics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Генератор отчетов в формате xlsx (Excel) по метрикам сайтов из Google Search Console
 */
@Deprecated
public class GscXlsxReportGenerator {

    private static final Logger log = LoggerFactory.getLogger(GscXlsxReportGenerator.class);

    private final GscService gsc;
    private final GscMetricsXlsxTableAdapter xlsx;

    public GscXlsxReportGenerator(GscApiBuilder gscApiBuilder) {
        this.gsc = new GscServiceImpl(gscApiBuilder);
        this.xlsx = new GscMetricsXlsxTableAdapter();
    }

    /**
     * Запускает процесс извлечение метрик, генерации отчета и сохранения в xlsx файл
     */
    public CompletableFuture<Void> generateAndSave(
            LocalDate startDate, LocalDate endDate, String savePath) {
        return CompletableFuture.runAsync(() -> {
            fetchMetricsAndGenerateReport(startDate, endDate);
            save(savePath);
        });
    }

    /**
     * Запускает процесс извлечение метрик и генерации отчета
     */
    public void fetchMetricsAndGenerateReport(LocalDate startDate, LocalDate endDate) {
        log.info("Начата генерация отчета GSC за период {} - {}", startDate, endDate);

        List<WmxSite> sites = gsc.getSites();
        log.info("Получено {} сайтов для обработки", sites.size());

        int processedCount = 0;
        int skippedCount = 0;
        for (WmxSite site : sites) {
            String siteUrl = site.getSiteUrl();
            log.info("Обработка сайта: {}", siteUrl);
            try {
                GscAnalyticsResponse response = gsc.getAnalytics(
                        siteUrl,
                        startDate,
                        endDate);
                if (response == null) {
                    log.warn("Сайт {} пропущен - не подтвержден", siteUrl);
                    skippedCount++;
                    continue;
                }

                SiteMetrics metrics = gsc.getMetrics(response);
                xlsx.addSiteMetrics(metrics);

                processedCount++;
                log.info("Сайт {} успешно обработан ({}/{})",
                        siteUrl, processedCount, sites.size());
            } catch (Exception e) { // FIXME Перехватывает все исключения. globalExceptionHandler не успевает обрабоать
                log.error("Ошибка при обработке сайта: {}", siteUrl, e);
                skippedCount++;
            }
        }
        xlsx.autoSizeColumns();

        log.info("Генерация отчета завершена. Всего: {}, обработано: {}, пропущено: {}",
                sites.size(), processedCount, skippedCount);
    }

    /**
     * Сохраняет сгенерированный отчет в xlsx файл
     *
     * @param savePath путь для сохранения файла
     */
    public void save(String savePath) {
        File file = new File(savePath);
        xlsx.save(file);
        log.info("Отчета в формате xlsx сохранен в: {}", file.getAbsolutePath());
    }
}