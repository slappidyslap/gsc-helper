package kg.seo.musabaev.excel;

import kg.seo.musabaev.searchconsole.SiteMetrics;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.stream.IntStream;

/**
 * Класс для создания Excel отчетов с метриками сайтов из GSC
 */
@Deprecated
public class ExcelReportBuilder {

    private static final Logger log = LoggerFactory.getLogger(ExcelReportBuilder.class);
    private static final String SHEET_NAME = "Метрики";
    private static final int COLUMNS_COUNT = 6;
    private final Workbook workbook;
    private final Sheet sheet;
    private int currentRow;

    public ExcelReportBuilder() {
        this.workbook = new XSSFWorkbook();
        this.sheet = workbook.createSheet(SHEET_NAME);
        this.currentRow = 0;
        log.info("Создание новой Excel документа...");
        initHeader();
    }

    /**
     * Создает заголовок таблицы
     */
    private void initHeader() {
        Row headerRow = sheet.createRow(currentRow++);
        headerRow.createCell(0).setCellValue("Тип ресурса");
        headerRow.createCell(1).setCellValue("URL");
        headerRow.createCell(2).setCellValue("Клики");
        headerRow.createCell(3).setCellValue("Показы");
        headerRow.createCell(4).setCellValue("CTR");
        headerRow.createCell(5).setCellValue("Средняя позиция");

        log.info("Заголовок таблицы создан");
    }

    /**
     * Добавляет данные одного сайта в таблицу
     *
     * @param metrics метрики сайта
     */
    public void addSiteMetrics(SiteMetrics metrics) {
        Row row = sheet.createRow(currentRow++);
        row.createCell(0).setCellValue(metrics.type().rus());
        row.createCell(1).setCellValue(metrics.url());
        row.createCell(2).setCellValue(metrics.clicks());
        row.createCell(3).setCellValue(metrics.impressions());
        row.createCell(4).setCellValue(metrics.ctr());
        row.createCell(5).setCellValue(metrics.avgPosition());

        log.info("Метрики для сайта {} успешно добавлены:\n{}", metrics.url(), metrics);
    }

    /**
     * Сохраняет Excel файл по указанному пути
     *
     * @param file путь для сохранения файла
     * @throws ExcelReportSaveException если файл или папка не существует
     * @throws ExcelReportException если произошла ошибка при сохранении
     */
    public void save(File file) {
        try (FileOutputStream fileOut = new FileOutputStream(file)) {
            workbook.write(fileOut);
            log.info("Excel файл успешно сохранен: {}", file.getAbsolutePath());

        } catch (FileNotFoundException e) {
            log.warn("Файл или папка не существует: {}", file.getAbsolutePath(), e);
            throw new ExcelReportSaveException(file);
        } catch (IOException e) {
            log.error("Ошибка ввода-вывода при сохранении Excel файла", e);
            throw new ExcelReportException(e);
        } finally {
            closeWorkbook();
        }
        log.info("Сохранение Excel файла: {}", file.getAbsolutePath());
    }

    /**
     * Закрывает workbook и освобождает ресурсы
     */
    private void closeWorkbook() {
        try {
            workbook.close();
            log.debug("Apache POI Workbook закрыт");
        } catch (IOException e) {
            log.warn("Ошибка при закрытии workbook", e);
            throw new ExcelReportException(e);
        }
    }

    /**
     * Возвращает текущее количество строк с данными (без учета заголовка)
     */
    public int getDataRowCount() {
        return currentRow - 1;
    }

    /**
     * Автоматически подбирает ширину столбцов
     */
    public void autoSizeColumns() {
        log.debug("Автоматическая настройка ширины столбцов...");
        IntStream.range(0, COLUMNS_COUNT).forEach(sheet::autoSizeColumn);
        log.debug("Ширина столбцов настроена");
    }
}