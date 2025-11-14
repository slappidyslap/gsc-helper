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
 * Реализация {@link GscAuthCredentialsLoader},
 * которая сразу возвращает заданный {@link #is} для
 * аутентификации с Google Search Console API.
 */
public class GscAuthInputStreamCredentialsLoader implements GscAuthCredentialsLoader {

    private static final Logger log = LoggerFactory.getLogger(GscAuthInputStreamCredentialsLoader.class);

    private final InputStream is;

    public GscAuthInputStreamCredentialsLoader(InputStream is) {
        this.is = is;
    }

    /**
     * Загружает файл учетных данных в виде {@link InputStream}.
     *
     * @return {@link InputStream} для чтения содержимого файла credentials.json
     */
    @Override
    public InputStream load() {
        if (is == null) {
            log.error("Файл учетных данных не найден");
            throw new CredentialsFileNotFoundException();
        }
        log.info("Учетные данные успешно загружены из из InputStream");
        return is;
    }
}
