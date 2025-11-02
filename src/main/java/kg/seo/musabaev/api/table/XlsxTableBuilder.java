package kg.seo.musabaev.api.table;

public interface XlsxTableBuilder extends TableBuilder {

    /**
     * Автоматически подбирает ширину столбцов, учитывая максимально длинную строку
     */
    void autoSizeColumns();
}
