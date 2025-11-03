package kg.musabaev.gsc_helper.api.table.output;

import kg.musabaev.gsc_helper.api.table.TableBuilder;

/**
 * Интерфейс для обработки выходных данных табличного отчёта.
 * <p>
 * Объект, реализующий этот интерфейс, предоставляет доступ к построителю таблицы
 * и умеет обработать эти данные таблицы, например, сохранить их в файл или отправить по сети.
 * </p>
 */
public interface TableDataOutputProcessor {

    /**
     * Возвращает связанный {@link TableBuilder}, используемый для формирования табличных данных.
     *
     * @return объект {@link TableBuilder}
     */
    TableBuilder getTableBuilder();

    /**
     * Выполняет обработку выходных данных отчёта.
     * Например, сохранить их в файл или передать другому сервису.
     *
     * @param data данные таблицы в формате массива байтов
     * </p>
     */
    void process(byte[] data);
}
