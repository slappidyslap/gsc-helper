package kg.musabaev.gschelper.api.report;

import kg.musabaev.gschelper.api.table.output.OutputProcessorConfig;

import java.time.LocalDate;

/**
 * Интерфейс для генерации отчетов GSC в табличном формате (xlsx, csv, json) и обработки после.
 */
public interface ReportService {

    /**
     * Генерирует отчет в указанном табличном формате.
     *
     * @param startDate дата начала периода
     * @param endDate   дата окончания периода
     * @return байтовый массив табличных данных
     */
    byte[] generateReport(LocalDate startDate, LocalDate endDate);

    /**
     * Выполняет обработку байтового массив данных.
     *
     * <p>Тип обработки определяется конфигурацией {@link OutputProcessorConfig}</p>
     *
     * @param config     конфигурация обработчика выходных данных
     * @param reportData байтовый массив табличного отчёта для обработки
     */
    void processReportOutput(OutputProcessorConfig config, byte[] reportData);
}
