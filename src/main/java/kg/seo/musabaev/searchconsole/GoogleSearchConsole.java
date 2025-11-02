package kg.seo.musabaev.searchconsole;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.searchconsole.v1.SearchConsole;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;

import static com.google.api.client.util.Preconditions.checkNotNull;
import static java.util.Collections.singleton;
import static kg.seo.musabaev.Constants.APP_HOME;

public class GoogleSearchConsole {

    private final Logger log = LoggerFactory.getLogger(GoogleSearchConsole.class);
    private final String APPLICATION_NAME;
    private final String CREDENTIALS_FILE_PATH;
    private final File GOOGLE_TOKENS_FILE;
    private final HttpTransport HTTP_TRANSPORT;
    private final JsonFactory JSON_FACTORY;
    private final SearchConsole searchConsole;

    public GoogleSearchConsole() {
        try {
            APPLICATION_NAME = "GSC Helper";
            CREDENTIALS_FILE_PATH = new File(APP_HOME, "credentials.json").getAbsolutePath();
            GOOGLE_TOKENS_FILE = new File(APP_HOME, "tokens");
            HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
            JSON_FACTORY = GsonFactory.getDefaultInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        searchConsole = build();
    }

    public SearchConsole.Sites sites() {
        return searchConsole.sites();
    }

    public SearchConsole.Searchanalytics searchAnalytics() {
        return searchConsole.searchanalytics();
    }

    private SearchConsole build() {
        log.info("Выполняется вход в Google аккаунт...");

        GoogleClientSecrets secrets = buildClientSecrets();
        log.info("GoogleClientSecrets создан");
        GoogleAuthorizationCodeFlow flow = buildAuthorizationCodeFlow(secrets);
        log.info("GoogleAuthorizationCodeFlow создан");
        LocalServerReceiver server = buildLocalServer();
        log.info("LocalServerReceiver создан");
        Credential credential = buildAuthorizationCodeInstalledApp(flow, server);
        log.info("AuthorizationCodeInstalledApp создан");

        log.info("Вход в Google аккаунт выполнен");

        SearchConsole searchConsole = buildSearchConsole(credential);
        log.info("SearchConsole создан");
        return searchConsole;
    }

    private GoogleClientSecrets buildClientSecrets() {
        InputStream in;
        try {
            in = Files.newInputStream(Paths.get(CREDENTIALS_FILE_PATH));
            checkNotNull(in);
            return GoogleClientSecrets.load(
                    JSON_FACTORY,
                    new InputStreamReader(in));
        } catch (NullPointerException e) {
            log.info("Файл credentials.json не найден");
            throw new CredentialsFileNotFoundException();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private GoogleAuthorizationCodeFlow buildAuthorizationCodeFlow(GoogleClientSecrets secrets) {
        try {
            return new GoogleAuthorizationCodeFlow.Builder(
                    HTTP_TRANSPORT,
                    JSON_FACTORY,
                    secrets,
                    singleton("https://www.googleapis.com/auth/webmasters.readonly"))
                    .setDataStoreFactory(new FileDataStoreFactory(GOOGLE_TOKENS_FILE))
                    .setAccessType("offline")
                    .build();
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при работе с папкой tokens", e);
        }
    }

    private LocalServerReceiver buildLocalServer() {
        return new LocalServerReceiver.Builder()
                .setPort(-1)
                .build();
    }

    private Credential buildAuthorizationCodeInstalledApp(
            GoogleAuthorizationCodeFlow flow,
            LocalServerReceiver server) {
        try {
            return new AuthorizationCodeInstalledApp(flow, server)
                    .authorize("user");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

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
