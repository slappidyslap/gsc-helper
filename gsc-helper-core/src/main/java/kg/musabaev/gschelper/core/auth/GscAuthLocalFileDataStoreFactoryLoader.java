package kg.musabaev.gschelper.core.auth;

import com.google.api.client.util.store.DataStoreFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import kg.musabaev.gschelper.api.gsc.auth.GscAuthDataStoreFactoryLoader;
import kg.musabaev.gschelper.core.gsc.exception.GscApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;

/**
 * Реализация {@link GscAuthDataStoreFactoryLoader}, которая загружает
 * директорию для сохранения учетных данных пользователя
 * (токены OAuth 2.0, refresh tokens и прочие данные аутентификации).
 */
public class GscAuthLocalFileDataStoreFactoryLoader implements GscAuthDataStoreFactoryLoader {

    private static final Logger log = LoggerFactory.getLogger(GscAuthLocalFileDataStoreFactoryLoader.class);

    private final Path pathSavingDataStore;

    public GscAuthLocalFileDataStoreFactoryLoader(Path pathSavingDataStore) {
        this.pathSavingDataStore = pathSavingDataStore;
    }

    /**
     * Загружает фабрику {@link DataStoreFactory}, используемую Google API Client
     * для сохранения токенов и других данных аутентификации.
     *
     * @return экземпляр {@link FileDataStoreFactory}
     * @throws GscApiException если произошла ошибка ввода-вывода при создании фабрики
     */
    @Override
    public DataStoreFactory load() {
        try {
            log.info("Попытка загрузить FileDataStoreFactory из пути: {}", pathSavingDataStore);
            FileDataStoreFactory factory = new FileDataStoreFactory(pathSavingDataStore.toFile());
            log.info("FileDataStoreFactory успешно создан: {}", factory.getDataDirectory().getAbsolutePath());
            return factory;
        } catch (IOException e) {
            log.error("Ошибка ввода-вывода при создании FileDataStoreFactory по пути: {}", pathSavingDataStore, e);
            throw new GscApiException(e);
        }
    }
}
