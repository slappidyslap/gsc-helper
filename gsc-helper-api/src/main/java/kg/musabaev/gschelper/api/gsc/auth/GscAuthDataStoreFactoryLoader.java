package kg.musabaev.gschelper.api.gsc.auth;

import com.google.api.client.util.store.DataStoreFactory;
import com.google.api.client.util.store.FileDataStoreFactory;

/**
 * Интерфейс для загрузки DataStoreFactory, где сохраняются учетные данные доступа
 * авторизованного пользователя (токены OAuth 2.0, refresh tokens и прочие данные аутентификации).
 */
public interface GscAuthDataStoreFactoryLoader {

    /**
     * Загружает фабрику {@link DataStoreFactory}
     *
     * @return реализацию {@link DataStoreFactory}
     */
    DataStoreFactory load();
}
