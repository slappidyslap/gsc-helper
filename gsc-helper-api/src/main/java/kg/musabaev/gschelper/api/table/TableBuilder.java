package kg.musabaev.gschelper.api.table;

/**
 * Интерфейс для построения табличных данных с минимальным функционалом.
 */
public interface TableBuilder {

    /**
     * Добавляет в самую первую строку заголовки - названия колонок.
     *
     * @param headers массив строк с названиями колонок
     */
    void createHeader(String... headers);

    /**
     * Добавляет строку данных в таблицу.
     *
     * @param values значения ячеек строки; могут быть разных типов (например, String, Integer, Double)
     */
    void addRow(Object... values);

    /**
     * Формирует выходное представление таблицы в виде массива байт.
     *
     * @return массив байт с данными таблицы
     */
    byte[] build();
}
