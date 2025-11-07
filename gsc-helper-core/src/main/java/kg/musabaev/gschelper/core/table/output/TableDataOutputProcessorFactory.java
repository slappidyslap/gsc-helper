package kg.musabaev.gschelper.core.table.output;

import kg.musabaev.gschelper.api.table.output.OutputProcessorConfig;
import kg.musabaev.gschelper.api.table.output.TableDataOutputProcessor;
import kg.musabaev.gschelper.core.table.output.file.local.TableDataOutputLocalFileSaver;

/**
 * Фабрика для создания экземпляров {@link TableDataOutputProcessor}
 * на основе типа переданного {@link OutputProcessorConfig}.
 */
public class TableDataOutputProcessorFactory {

    private TableDataOutputProcessorFactory() {
    }

    /**
     * Создаёт экземпляр {@link TableDataOutputProcessor}
     * в зависимости от типа конфигурации.
     *
     * @param config объект конфигурации, определяющий тип обработчика
     * @return экземпляр {@link TableDataOutputProcessor},
     * соответствующий типу конфигурации
     */
    public static TableDataOutputProcessor create(OutputProcessorConfig config) {
        if (config instanceof TableDataOutputLocalFileSaver.Config)
            return new TableDataOutputLocalFileSaver((TableDataOutputLocalFileSaver.Config) config);
        else
            throw new IllegalStateException("Фабрика для такого конфига не настроена");
    }
}
