package kg.musabaev.gschelper.api.gsc.auth;

import java.io.InputStream;

/**
 * Интерфейс для загрузки учетных данных доступа (credentials) для
 * аутентификации с Google Search Console API.
 */
public interface GscAuthCredentialsLoader {

    /**
     * Загружает учетные данные доступа в виде {@link InputStream}.
     *
     * @return {@link InputStream} для чтения учетных данных доступа
     */
    InputStream load();
}
