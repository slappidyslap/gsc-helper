package kg.musabaev.gschelper.core.gsc;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.searchconsole.v1.SearchConsole;
import kg.musabaev.gschelper.api.gsc.GscApiBuilder;
import kg.musabaev.gschelper.api.gsc.auth.GscAuthCredentialsLoader;
import kg.musabaev.gschelper.api.gsc.auth.GscAuthDataStoreFactoryLoader;
import kg.musabaev.gschelper.core.gsc.exception.CredentialsFileNotFoundException;
import kg.musabaev.gschelper.core.gsc.exception.GscApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;

import static java.util.Collections.singleton;

/**
 * Класс-обёртка для авторизации Google Search Console API через flow
 * <a
 * href="https://auth0.com/docs/get-started/authentication-and-authorization-flow/authorization-code-flow"
 * >
 * Authorization Code
 * </a>
 * При инициализации требуют загрузчик учетных данных доступы (credentials)
 * и загрузчик DataStoreFactory
 */
public class GscApiAuthorizationCodeFlowBuilder implements GscApiBuilder {

    private final Logger log = LoggerFactory.getLogger(GscApiAuthorizationCodeFlowBuilder.class);

    private final GscAuthDataStoreFactoryLoader dataStoreFactoryLoader;
    private final GscAuthCredentialsLoader credentialsLoader;

    private final String APPLICATION_NAME;
    private final HttpTransport HTTP_TRANSPORT;
    private final JsonFactory JSON_FACTORY;

    public GscApiAuthorizationCodeFlowBuilder(
        String applicationName,
        GscAuthDataStoreFactoryLoader dataStoreFactoryLoader,
        GscAuthCredentialsLoader credentialsLoader
    ) {
        this.dataStoreFactoryLoader = dataStoreFactoryLoader;
        this.credentialsLoader = credentialsLoader;

        String className = GscApiAuthorizationCodeFlowBuilder.class.getName();
        try {
            APPLICATION_NAME = applicationName;
            HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
            JSON_FACTORY = GsonFactory.getDefaultInstance();
        } catch (GeneralSecurityException e) {
            log.error("Ошибка типа GeneralSecurityException при инициализации {}",
                className, e);
            throw new GscApiException(e);
        } catch (IOException e) {
            log.error("Ошибка ввода-вывода при инициализации {}",
                className, e);
            throw new GscApiException(e);
        }
    }

    /**
     * Инициализирует и возвращает объект клиента Search Console.
     *
     * @return инициализированный {@link SearchConsole} клиент
     */
    @Override
    public SearchConsole build() {
        log.info("Выполняется вход в Google аккаунт...");

        GoogleClientSecrets secrets = buildClientSecrets();
        log.info("GoogleClientSecrets создан");
        GoogleAuthorizationCodeFlow flow = buildAuthorizationCodeFlow(secrets);
        log.info("GoogleAuthorizationCodeFlow создан");
        LocalServerReceiver server = buildLocalServer();
        log.info("LocalServerReceiver создан");
        Credential credential = buildAuthorizationCodeInstalledApp(flow, server);
        log.info("AuthorizationCodeInstalledApp (Credential) создан");

        log.info("Вход в Google аккаунт выполнен");

        SearchConsole searchConsole = buildSearchConsole(credential);
        log.info("Объект SearchConsole для работы с GSC API создан");
        return searchConsole;
    }

    /**
     * Загружает клиентские секреты OAuth из файла credentials.json.
     *
     * @return {@link GoogleClientSecrets} для OAuth
     * @throws CredentialsFileNotFoundException если файл credentials.json не найден
     * @throws GscApiException                  при ошибках чтения файла
     */
    private GoogleClientSecrets buildClientSecrets() {
        InputStream in;
        try {
            return GoogleClientSecrets.load(
                JSON_FACTORY,
                new InputStreamReader(credentialsLoader.load()));
        } catch (IOException e) {
            log.error("Ошибка ввода-вывода при создании GoogleClientSecrets", e);
            throw new GscApiException(e);
        }
    }

    /**
     * Создаёт и настраивает {@link GoogleAuthorizationCodeFlow} для OAuth 2.0 авторизации.
     * Используется scope с правами только для чтения вебмастерских данных.
     *
     * @param secrets клиентские секреты OAuth
     * @return настроенный {@link GoogleAuthorizationCodeFlow}
     * @throws GscApiException при ошибках IO
     */
    private GoogleAuthorizationCodeFlow buildAuthorizationCodeFlow(
        GoogleClientSecrets secrets) {
        try {
            return new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT,
                JSON_FACTORY,
                secrets,
                singleton("https://www.googleapis.com/auth/webmasters.readonly") // scopes
            )
                .setDataStoreFactory(dataStoreFactoryLoader.load())
                .setAccessType("offline")
                .build();
        } catch (IOException e) {
            log.error("Ошибка ввода-вывода при создании GoogleAuthorizationCodeFlow", e);
            throw new GscApiException(e);
        }
    }

    /**
     * Создаёт локальный сервер для обработки OAuth авторизации.
     * Порт задается автоматически.
     *
     * @return {@link LocalServerReceiver}
     */
    private LocalServerReceiver buildLocalServer() {
        return new LocalServerReceiver.Builder()
            .setPort(-1)
            .build();
    }

    /**
     * Выполняет авторизацию пользователя через локальный сервер,
     * используя {@link AuthorizationCodeInstalledApp}.
     *
     * @param flow   объект {@link GoogleAuthorizationCodeFlow} для авторизации
     * @param server локальный сервер {@link LocalServerReceiver}
     * @return авторизационные данные {@link Credential}
     * @throws GscApiException при ошибках IO
     */
    private Credential buildAuthorizationCodeInstalledApp(
        GoogleAuthorizationCodeFlow flow,
        LocalServerReceiver server) {
        try {
            return new AuthorizationCodeInstalledApp(flow, server)
                .authorize("user");
        } catch (IOException e) {
            log.error("Ошибка ввода-вывода при создании " +
                "AuthorizationCodeInstalledApp (Credential)", e);
            throw new GscApiException(e);
        }
    }

    /**
     * Создаёт клиент GSC API с переданными учётными данными.
     *
     * @param credential авторизационные данные
     * @return клиент {@link SearchConsole}
     */
    private SearchConsole buildSearchConsole(Credential credential) {
        return new SearchConsole.Builder(
            HTTP_TRANSPORT,
            JSON_FACTORY,
            credential
        )
            .setApplicationName(APPLICATION_NAME)
            .build();
    }
}
