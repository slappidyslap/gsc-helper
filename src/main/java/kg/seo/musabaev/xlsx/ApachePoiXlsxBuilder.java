package kg.seo.musabaev.xlsx;

import kg.seo.musabaev.api.TableBuilder;
import kg.seo.musabaev.api.exception.TableBuilderException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.IntStream;

/**
 * Класс для создания xlsx файлов с помощью Apache POI с минимальным функционалом
 */
public class ApachePoiXlsxBuilder implements TableBuilder {

    private static final Logger log = LoggerFactory.getLogger(ApachePoiXlsxBuilder.class);

    private final Workbook workbook;
    private final Sheet sheet;
    private int currentRow;

    public ApachePoiXlsxBuilder(String sheetName) {
        this.workbook = new XSSFWorkbook();
        this.sheet = workbook.createSheet(sheetName);
        this.currentRow = 0;
    }

    /**
     * Создает заголовок таблицы
     *
     * @param headers массив названий колонок
     */
    @Override
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
    @Override
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
                log.error("Недопустимое значение передано в addRow: {}", value);
                throw new TableBuilderException("Illegal argument: " + value);
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
     * Автоматически подбирает ширину столбцов
     */
    public void autoSizeColumns(int columnCount) {
        IntStream.range(0, columnCount).forEach(sheet::autoSizeColumn);
    }

    /**
     * Формирует выходное представление таблицы в виде массива байт.
     *
     * @return массив байт с данными таблицы
     */
    @Override
    public byte[] build() {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            workbook.write(out);
            closeWorkbook();
            return out.toByteArray();
        } catch (IOException e) {
            log.error("Ошибка ввода-вывода при преобразования xlsx файла в байтовый массив", e);
            throw new TableBuilderException(e);
        }
    }

    /**
     * Закрывает workbook и освобождает ресурсы
     */
    private void closeWorkbook() {
        try {
            workbook.close();
        } catch (IOException e) {
            throw new TableBuilderException(e);
        }
    }

    /**
     * Сохраняет Excel файл по указанному пути
     *
     * @param file путь для сохранения файла
     * @throws TableBuilderException если файл не найден или папка не существует
     * @throws TableBuilderException если произошла ошибка при сохранении
     */
    @Deprecated
    public void save(File file) {
    }

    /**
     * Возвращает текущее количество строк с данными (без учета заголовка)
     */
    public int getRowCount() {
        return currentRow - 1;
    }
}