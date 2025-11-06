package kg.musabaev.gschelper.api.table.output;

/**
 * Интерфейс для обработки выходных данных табличного отчёта.
 */
public interface TableDataOutputProcessor {

    /**
     * Возвращает конфиг к данному обработчику
     * @return объект {@link OutputProcessorConfig}
     */
    OutputProcessorConfig getConfig();

    /**
     * Выполняет обработку выходных данных отчёта.
     * Например, сохранить их в файл или передать другому сервису.
     *
     * @param data данные таблицы в формате массива байтов
     * </p>
     */
    void process(byte[] data);
}
