package kg.musabaev.gschelper.core.report;

import kg.musabaev.gschelper.api.adapter.GscMetricsTableAdapter;
import kg.musabaev.gschelper.api.gsc.collector.BaseGscMetricsBetweenDateCollector;
import kg.musabaev.gschelper.api.gsc.collector.domain.SiteMetricsList;
import kg.musabaev.gschelper.api.report.ReportService;
import kg.musabaev.gschelper.api.table.output.OutputProcessorConfig;
import kg.musabaev.gschelper.api.table.output.TableDataOutputProcessor;
import kg.musabaev.gschelper.core.table.output.TableDataOutputProcessorFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Сервис для генерации отчетов GSC в табличном формате (xlsx, csv, json) и обработки после.
 */
public class ReportServiceImpl implements ReportService {

    private static final Logger log = LoggerFactory.getLogger(ReportServiceImpl.class);

    private final BaseGscMetricsBetweenDateCollector metricsCollector;
    private final GscMetricsTableAdapter gscXlsxAdapter;

    public ReportServiceImpl(
        BaseGscMetricsBetweenDateCollector metricsCollector,
        GscMetricsTableAdapter gscXlsxAdapter) {
        checkNotNull(metricsCollector);
        checkNotNull(gscXlsxAdapter);
        this.metricsCollector = metricsCollector;
        this.gscXlsxAdapter = gscXlsxAdapter;
    }

    /**
     * Генерирует отчет в указанном табличном формате.
     *
     * @param startDate дата начала периода
     * @param endDate   дата окончания периода
     * @return байтовый массив табличных данных
     */
    @Override
    public byte[] generateReport(LocalDate startDate, LocalDate endDate) {
        checkNotNull(startDate);
        checkNotNull(endDate);

        log.info("Начата генерация отчета GSC за период {} - {}", startDate, endDate);

        SiteMetricsList collectedMetrics = metricsCollector
            .collectBetweenDate(startDate, endDate);
        metricsCollector.collect();

        byte[] report = gscXlsxAdapter.adapt(collectedMetrics);

        log.info("Отчет успешно сгенерирован");

        return report;
    }

    /**
     * Выполняет обработку байтового массив данных.
     *
     * <p>Тип обработки определяется конфигурацией {@link OutputProcessorConfig}</p>
     *
     * <p>Конфигурация обработки можно создать с помощью {@link TableDataOutputProcessorFactory}</p>
     *
     * @param config     конфигурация обработчика выходных данных
     * @param reportData байтовый массив табличного отчёта для обработки
     */
    @Override
    public void processReportOutput(OutputProcessorConfig config, byte[] reportData) {
        log.info("Начата обработка байтового массива данных отчета");
        checkNotNull(config);
        checkNotNull(reportData);

        TableDataOutputProcessor outputProcessor = TableDataOutputProcessorFactory.create(config);
        outputProcessor.process(reportData);
        log.info("Отчет был обработан реализацией {} - {}",
            TableDataOutputProcessor.class.getName(), outputProcessor.getClass().getName());
    }
}
