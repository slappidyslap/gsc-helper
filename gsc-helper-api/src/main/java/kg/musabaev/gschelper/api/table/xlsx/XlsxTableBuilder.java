package kg.musabaev.gschelper.api.table.xlsx;

import kg.musabaev.gschelper.api.table.TableBuilder;

public interface XlsxTableBuilder extends TableBuilder {

    /**
     * Автоматически подбирает ширину столбцов, учитывая максимально длинную строку
     */
    void autoSizeColumns();
}
