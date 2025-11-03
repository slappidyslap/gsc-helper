package kg.musabaev.gsc_helper.core.table.output;

import kg.musabaev.gsc_helper.api.table.output.OutputProcessorConfig;
import kg.musabaev.gsc_helper.api.table.output.TableDataOutputProcessor;
import kg.musabaev.gsc_helper.core.table.output.email.TestTableDataOutputEmailSender;
import kg.musabaev.gsc_helper.core.table.output.file.local.TableDataOutputLocalFileSaver;

/**
 * Фабрика для создания экземпляров {@link TableDataOutputProcessor}
 * на основе типа переданного {@link OutputProcessorConfig}.
 */
public class TableDataOutputProcessorFactory {

    /**
     * Создаёт экземпляр {@link TableDataOutputProcessor}
     * в зависимости от типа конфигурации.
     *
     * @param config объект конфигурации, определяющий тип обработчика
     * @return экземпляр {@link TableDataOutputProcessor},
     *         соответствующий типу конфигурации
     */
    public static TableDataOutputProcessor create(OutputProcessorConfig config) {
        if (config instanceof TableDataOutputLocalFileSaver.Config)
            return new TableDataOutputLocalFileSaver((TableDataOutputLocalFileSaver.Config) config);
        else if (config instanceof TestTableDataOutputEmailSender.Config)
            return new TestTableDataOutputEmailSender((TestTableDataOutputEmailSender.Config) config);
        else
            throw new IllegalStateException("Фабрика для такого конфига не настроена");
    }

    private TableDataOutputProcessorFactory() {
    }
}
