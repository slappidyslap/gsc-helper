package kg.seo.musabaev.api;

import kg.seo.musabaev.util.Constants;
import kg.seo.musabaev.api.exception.LocalFileNotFoundException;
import kg.seo.musabaev.api.exception.TableBuilderException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Реализация {@link TableDataOutputProcessor}, которая сохраняет
 * выходные данные таблицы в локальный файл.
 */
public class TableDataLocalFileSaver implements TableDataOutputProcessor {

    private static final Logger log = LoggerFactory.getLogger(TableDataLocalFileSaver.class);

    private final TableBuilder builder;
    private File savePath;

    /**
     * Конструктор с привязкой к конкретному билдеру таблицы.
     *
     * @param builder билдер, который формирует табличные данные
     */
    public TableDataLocalFileSaver(TableBuilder builder) {
        checkNotNull(builder);
        this.builder = builder;
    }

    /**
     * Возвращает текущий путь для сохранения файла.
     *
     * @return путь сохранения файла
     */
    public File savePath() {
        return savePath;
    }

    /**
     * Устанавливает путь для сохранения выходных данных.
     *
     * @param savePath путь для сохранения файла
     */
    public void setSavePath(File savePath) {
        this.savePath = savePath;
    }

    /**
     * Возвращает связанный билдер таблицы.
     *
     * @return объект {@link TableBuilder}
     */
    @Override
    public TableBuilder getTableBuilder() {
        return builder;
    }

    /**
     * Обрабатывает выходные данные в виде массива байт,
     * записывая их в файл по пути {@link #savePath}.
     *
     * @param data байтовый массив с данными таблицы
     */
    @Override
    public void processTableDataOutput(byte[] data) {
        try (FileOutputStream out = new FileOutputStream(savePath)) {
            out.write(data);
        } catch (FileNotFoundException e) {
            log.error(Constants.LOCAL_FILE_NOT_FOUND_EXCEPTION + "{}", savePath, e);
            throw new LocalFileNotFoundException(savePath);
        } catch (IOException e) {
            log.error("Ошибка ввода-вывода при сохранении Excel файла", e);
            throw new TableBuilderException(e);
        }
    }

    /**
     * Сохраняет переданные данные в указанный файл.
     *
     * @param file файл куда будут сохранены данные таблицы
     * @param data байтовый массив с данными таблицы
     * @throws NullPointerException если file или data - null
     */
    public void save(File file, byte[] data) {
        checkNotNull(data);
        checkNotNull(file);
        setSavePath(file);
        processTableDataOutput(data);
    }
}
