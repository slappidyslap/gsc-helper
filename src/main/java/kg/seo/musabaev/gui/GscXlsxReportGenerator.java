package kg.seo.musabaev.gui;

import com.google.api.services.searchconsole.v1.model.SearchAnalyticsQueryResponse;
import com.google.api.services.searchconsole.v1.model.WmxSite;
import kg.seo.musabaev.searchconsole.GoogleSearchConsole;
import kg.seo.musabaev.searchconsole.SearchConsoleService;
import kg.seo.musabaev.searchconsole.SiteMetrics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Генератор отчетов в формате xlsx (Excel) по метрикам сайтов из Google Search Console
 */
public class GscXlsxReportGenerator {

    private static final Logger log = LoggerFactory.getLogger(GscXlsxReportGenerator.class);

    private final SearchConsoleService gsc;
    private final GscMetricsXlsxTableAdapter xlsx;

    public GscXlsxReportGenerator(GoogleSearchConsole searchConsole) {
        this.gsc = new SearchConsoleService(searchConsole);
        this.xlsx = new GscMetricsXlsxTableAdapter();
    }

    /**
     * Запускает процесс извлечение метрик, генерации отчета и сохранения в xlsx файл
     */
    public CompletableFuture<Void> generateAndSave(LocalDate startDate, LocalDate endDate, String savePath) {
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
        log.info("Получено сайтов для обработки: {}", sites.size());

        int processedCount = 0;
        int skippedCount = 0;

        for (WmxSite site : sites) {
            String siteUrl = site.getSiteUrl();
            log.info("Обработка сайта: {}", siteUrl);

            try {
                SearchAnalyticsQueryResponse response = gsc.getAnalytics(
                        siteUrl,
                        startDate.toString(),
                        endDate.toString());

                if (response == null) {
                    log.warn("Сайт {} пропущен - не подтвержден", siteUrl);
                    skippedCount++;
                    continue;
                }

                SiteMetrics metrics = gsc.getMetrics(siteUrl, response);
                xlsx.addSiteMetrics(metrics);

                processedCount++;
                log.info("Сайт {} успешно обработан ({}/{})",
                        siteUrl, processedCount, sites.size());
            } catch (Exception e) {
                log.error("Ошибка при обработке сайта: {}", siteUrl, e);
                skippedCount++;
            }
        }
        xlsx.autoSizeColumns();
        log.info("Генерация отчета завершена. Обработано: {}, Пропущено: {}",
                processedCount, skippedCount);
    }

    /**
     * Сохраняет сгенерированный отчет в xlsx файл
     *
     * @param savePath путь для сохранения файла
     */
    public void save(String savePath) {
        File file = new File(savePath);
        log.info("Сохранение отчета в файл: {}", file.getAbsolutePath());
        xlsx.save(file);
    }
}