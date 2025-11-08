package kg.musabaev.gschelper.core.auth;

import kg.musabaev.gschelper.api.gsc.auth.GscAuthCredentialsLoader;
import kg.musabaev.gschelper.core.gsc.exception.CredentialsFileNotFoundException;
import kg.musabaev.gschelper.core.gsc.exception.GscApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Реализация {@link GscAuthCredentialsLoader}, которая загружает
 * локальный файл учетных данных (credentials.json) для
 * аутентификации с Google Search Console API.
 */
public class GscAuthLocalFileCredentialsLoader implements GscAuthCredentialsLoader {

    private static final Logger log = LoggerFactory.getLogger(GscAuthLocalFileCredentialsLoader.class);

    private final Path pathCredentialsFile;

    public GscAuthLocalFileCredentialsLoader(Path pathCredentialsFile) {
        this.pathCredentialsFile = pathCredentialsFile;
    }

    /**
     * Загружает файл учетных данных в виде {@link InputStream}.
     *
     * @return {@link InputStream} для чтения содержимого файла credentials.json
     * @throws GscApiException если файл не найден или произошла ошибка ввода-вывода
     */
    @Override
    public InputStream load() {
        try {
            if (!Files.exists(pathCredentialsFile)) {
                log.error("Файл учетных данных не найден по пути: {}", pathCredentialsFile);
                throw new CredentialsFileNotFoundException();
            }
            log.info("Загрузка учетных данных из файла: {}", pathCredentialsFile);
            InputStream inputStream = Files.newInputStream(pathCredentialsFile);
            log.info("Учетные данные успешно загружены из файла {}", pathCredentialsFile);
            return inputStream;
        } catch (IOException e) {
            log.error("Ошибка ввода-вывода при чтении файла учетных данных: {}", pathCredentialsFile, e);
            throw new GscApiException(e);
        }
    }
}
