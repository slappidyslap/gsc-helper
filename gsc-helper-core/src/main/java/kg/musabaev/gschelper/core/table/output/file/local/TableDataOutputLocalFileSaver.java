package kg.musabaev.gschelper.core.table.output.file.local;

import kg.musabaev.gschelper.api.table.output.OutputProcessorConfig;
import kg.musabaev.gschelper.api.table.output.TableDataOutputProcessor;
import kg.musabaev.gschelper.core.table.exception.LocalFileNotFoundException;
import kg.musabaev.gschelper.core.table.exception.TableDataOutputProcessException;
import kg.musabaev.gschelper.core.util.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Реализация {@link TableDataOutputProcessor}, которая сохраняет
 * выходные данные таблицы в локальный файл указанный в {@link Config}.
 */
public class TableDataOutputLocalFileSaver implements TableDataOutputProcessor {

    private static final Logger log = LoggerFactory.getLogger(TableDataOutputLocalFileSaver.class);

    private final Config config;

    public TableDataOutputLocalFileSaver(Config config) {
        checkNotNull(config);
        this.config = config;
    }

    /**
     * Сохраняет байтовый массив данных в указанный в {@link Config#savePath} файл.
     *
     * @param data байтовый массив с данными таблицы
     */
    @Override
    public void process(byte[] data) {
        File savePath = config.savePath;
        try (FileOutputStream out = new FileOutputStream(savePath)) {
            out.write(data);
        } catch (FileNotFoundException e) {
            log.error(Constants.LOCAL_FILE_NOT_FOUND_EXCEPTION + "{}", savePath, e);
            throw new LocalFileNotFoundException(savePath);
        } catch (IOException e) {
            log.error("Ошибка ввода-вывода при обработке выходных данных таблицы", e);
            throw new TableDataOutputProcessException(e);
        }
    }

    /**
     * Возвращает конфиг к данному обработчику
     *
     * @return объект {@link OutputProcessorConfig}
     */
    @Override
    public OutputProcessorConfig getConfig() {
        return config;
    }

    /**
     * Конфигурация к обработчику {@link TableDataOutputLocalFileSaver}
     */
    public static class Config implements OutputProcessorConfig {

        private final File savePath;

        public Config(File savePath) {
            checkNotNull(savePath);
            this.savePath = savePath;
        }

        @Override
        public String toString() {
            return "Config{" +
                "savePath=" + savePath +
                '}';
        }

        @Override
        public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass()) return false;
            Config that = (Config) o;
            return Objects.equals(savePath, that.savePath);
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(savePath);
        }

        public File savePath() {
            return savePath;
        }
    }
}
