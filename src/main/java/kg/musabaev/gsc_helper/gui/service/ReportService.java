package kg.musabaev.gsc_helper.gui.service;

import kg.musabaev.gsc_helper.api.gsc.collector.GscMetricsCollector;
import kg.musabaev.gsc_helper.api.adapter.GscMetricsTableAdapter;
import kg.musabaev.gsc_helper.api.gsc.collector.domain.SiteMetricsList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Сервис для генерации отчетов GSC в табличном формате (xlsx, csv, json)
 */
public class ReportService {

    private static final Logger log = LoggerFactory.getLogger(ReportService.class);

    private final GscMetricsCollector metricsCollector;
    private final GscMetricsTableAdapter gscXlsxAdapter;

    public ReportService(
            GscMetricsCollector metricsCollector,
            GscMetricsTableAdapter gscXlsxAdapter) {
        checkNotNull(metricsCollector);
        checkNotNull(gscXlsxAdapter);
        this.metricsCollector = metricsCollector;
        this.gscXlsxAdapter = gscXlsxAdapter;
    }

    /**
     * Генерирует отчет в указанном табличном формате
     *
     * @param startDate дата начала периода
     * @param endDate дата окончания периода
     * @return байтовый массив табличных данных
     */
    public byte[] generateReport(LocalDate startDate, LocalDate endDate) {
        log.info("Начата генерация отчета GSC за период {} - {}", startDate, endDate);

        SiteMetricsList collectedMetrics = metricsCollector.collect();
        byte[] report = gscXlsxAdapter.process(collectedMetrics);

        // saver todo

        log.info("Отчет успешно сгенерирован");

        return report;
    }
}