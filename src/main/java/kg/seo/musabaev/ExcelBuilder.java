package kg.seo.musabaev;

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
import java.util.List;
import java.util.stream.IntStream;

/**
 * Универсальный класс для создания Excel файлов с минимальным функционалом
 */
public class ExcelBuilder {

    private static final Logger log = LoggerFactory.getLogger(ExcelBuilder.class);

    private final Workbook workbook;
    private final Sheet sheet;
    private int currentRow;

    public ExcelBuilder(String sheetName) {
        this.workbook = new XSSFWorkbook();
        this.sheet = workbook.createSheet(sheetName);
        this.currentRow = 0;
    }

    /**
     * Создает заголовок таблицы
     *
     * @param headers массив названий колонок
     */
    public void createHeader(String... headers) {
        Row headerRow = sheet.createRow(currentRow++);
        for (int i = 0; i < headers.length; i++) {
            headerRow.createCell(i).setCellValue(headers[i]);
        }
    }

    /**
     * Добавляет строку данных в таблицу
     *
     * @param values значения ячеек
     */
    public void addRow(Object... values) {
        Row dataRow = sheet.createRow(currentRow++);

        for (int i = 0; i < values.length; i++) {
            Object value = values[i];

            if (value instanceof Double) {
                dataRow.createCell(i).setCellValue((Double) value);
            } else if (value instanceof Integer) {
                dataRow.createCell(i).setCellValue((Integer) value);
            } else if (value instanceof String) {
                dataRow.createCell(i).setCellValue((String) value);
            } else {
                throw new IllegalArgumentException("Illegal argument: " + value);
            }
        }
    }

    /**
     * Добавляет несколько строк данных
     *
     * @param rows список массивов значений
     */
    public void addRows(List<Object[]> rows) {
        rows.forEach(this::addRow);
    }

    /**
     * Сохраняет Excel файл по указанному пути
     *
     * @param file путь для сохранения файла
     * @throws ExcelReportException если файл не найден или папка не существует
     * @throws ExcelReportException если произошла ошибка при сохранении
     */
    public void save(File file) {
        try (FileOutputStream fileOut = new FileOutputStream(file)) {
            workbook.write(fileOut);

        } catch (FileNotFoundException e) {
            log.error("Файл не найден или папка не существует: {}", file.getAbsolutePath(), e);
            throw new ExcelReportSaveException(file);
        } catch (IOException e) {
            log.error("Ошибка ввода-вывода при сохранении Excel файла", e);
            throw new ExcelReportException(e);
        } finally {
            closeWorkbook();
        }
    }

    /**
     * Закрывает workbook и освобождает ресурсы
     */
    private void closeWorkbook() {
        try {
            workbook.close();
        } catch (IOException e) {
            throw new ExcelReportException(e);
        }
    }

    /**
     * Автоматически подбирает ширину столбцов
     */
    public void autoSizeColumns(int columnCount) {
        IntStream.range(0, columnCount).forEach(sheet::autoSizeColumn);
    }

    /**
     * Возвращает текущее количество строк с данными (без учета заголовка)
     */
    public int getRowCount() {
        return currentRow - 1;
    }
}