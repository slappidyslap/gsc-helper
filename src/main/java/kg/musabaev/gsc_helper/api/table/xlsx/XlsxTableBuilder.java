package kg.musabaev.gsc_helper.api.table.xlsx;

import kg.musabaev.gsc_helper.api.table.TableBuilder;

public interface XlsxTableBuilder extends TableBuilder {

    /**
     * Автоматически подбирает ширину столбцов, учитывая максимально длинную строку
     */
    void autoSizeColumns();
}
