package kg.musabaev.gsc_helper.api.gsc;

import com.google.api.services.searchconsole.v1.SearchConsole;

/**
 * Интерфейс для инициализации {@link SearchConsole} для работы с его API.
 */
public interface GscApiBuilder {

    /**
     * Инициализирует и возвращает {@link SearchConsole} для работы с его API.
     *
     * @return инициализированный {@link SearchConsole}
     */
    SearchConsole build();
}
