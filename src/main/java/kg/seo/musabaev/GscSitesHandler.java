package kg.seo.musabaev;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.Preconditions;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.searchconsole.v1.SearchConsole;
import com.google.api.services.searchconsole.v1.model.*;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.IntStream;

import static java.util.Collections.singleton;
import static kg.seo.musabaev.Constants.APP_HOME;

public class GscSitesHandler {

    private final Logger log = LoggerFactory.getLogger(GscSitesHandler.class);
    private final Workbook workbook = new XSSFWorkbook();
    private final String startDate;
    private final String endDate;

    public GscSitesHandler(LocalDate startDate, LocalDate endDate) {
        this.startDate = startDate.toString();
        this.endDate = endDate.toString();
    }

    public CompletableFuture<Void> start() {
        log.info("Начат сбор данных");
        return CompletableFuture.runAsync(() -> {
            SearchConsole searchConsole; // По идее это только сервис Google
            try {
                searchConsole = buildGoogle();
            } catch (CredentialsFileNotFoundException e) {
                throw e;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            log.debug("Создаем Excel файл и заполняем header...");
            Sheet sheet = workbook.createSheet("Метрики");
            Row headerRow = sheet.createRow(0);
            fillHeader(headerRow);
            log.debug("Файл Excel создан и header заполнен");

            SitesListResponse sitesResp;
            try {
                sitesResp = searchConsole.sites().list().execute();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            List<WmxSite> sites;
            try {
                sites = sitesResp.getSiteEntry();
                Preconditions.checkNotNull(sites);
            } catch (NullPointerException e) {
                // Пока не ясно, еще при каких обстоятельствах
                // происходит такое исключение, кроме отсутствия сайтов
                throw new GscSitesNotFoundException();
            }

            log.info("Все сайты были извлечены. Всего сайтов: {}", sites.size());

            int i = 1;
            for (WmxSite site : sites) {
                final String SITE_URL = site.getSiteUrl();

                log.info("Обработка сайта {}...", SITE_URL);

                SearchAnalyticsQueryRequest queryReq = new SearchAnalyticsQueryRequest()
                        .setStartDate(startDate)
                        .setEndDate(endDate);
                SearchAnalyticsQueryResponse queryResp = null;
                try {
                    log.debug("Запрашиваем метрики из GSC...");
                    queryResp = searchConsole.searchanalytics()
                            .query(SITE_URL, queryReq)
                            .execute();
                    log.debug("Метрики были получены. Ответ от GSC:\n{}", queryResp.toPrettyString());
                } catch (GoogleJsonResponseException e) {
                    if (e.getStatusCode() == 403) {
                        log.info("Данный сайт еще потвержден");
                        continue;
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

                log.debug("Вставляем новые данные...");
                Row siteDataRow = sheet.createRow(i);
                // Доменные ресурсы отличают от ресурсов с префиксом в URL по строке sc-domain
                // Если он присутствует, то ресурс 1, иначе 2
                GscResourceType type = SITE_URL.contains("sc-domain")
                        ? GscResourceType.DOMAIN
                        : GscResourceType.WITH_PREFIX;
                siteDataRow.createCell(0).setCellValue(type.rus);
                log.debug("Вставил тип ресурса: {}", type.rus);

                String url = type == GscResourceType.DOMAIN ? SITE_URL.substring(10) : SITE_URL;
                siteDataRow.createCell(1).setCellValue(url);
                log.debug("Вставил ссылку: {}", url);

                List<ApiDataRow> rows = queryResp.getRows();
                Preconditions.checkNotNull(rows);
                Preconditions.checkArgument(!rows.isEmpty());

                ApiDataRow row = rows.get(0);
                Row dataRow = sheet.getRow(i);

                double clicks = row.getClicks();
                dataRow.createCell(2).setCellValue(clicks);
                log.debug("Вставил клики: {}", clicks);

                double impressions = row.getImpressions();
                dataRow.createCell(3).setCellValue(impressions);
                log.debug("Вставил показы: {}", clicks);

                // там изначально поделен на 100 поэтому умножение на 100 делается
                // последующие десятки это, чтобы округлить число с плавающей точкой
                double ctr = Math.round((row.getCtr() * 100) * 10d) / 10d;
                dataRow.createCell(4).setCellValue(ctr);
                log.debug("Вставил CTR: {}", clicks);

                double avgPos = Math.round(row.getPosition() * 10d) / 10d;
                log.debug("Вставил среднию позицию: {}", clicks);
                dataRow.createCell(5).setCellValue(avgPos);

                i++;

                log.info("Сайт {} обработан", SITE_URL);
            }
            IntStream.range(0, 6).forEach(sheet::autoSizeColumn);
            log.debug("Ширина столбцов установлена автоматически");
        });
    }

    public void saveExcelFile(File path) throws IOException {
        log.debug("Сохранение Excel файла...");

        try {
            FileOutputStream fileOut = new FileOutputStream(path);
            workbook.write(fileOut);
            fileOut.close();
            workbook.close();
            log.info("Excel файл успешно сохранен по пути: {}", path);
        } catch (FileNotFoundException e) {
            log.info("Папка для сохранения не был выбран");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private SearchConsole buildGoogle() throws GeneralSecurityException, IOException, CredentialsFileNotFoundException {
        log.info("Выполняется вход в Google аккаунт...");

        final String APPLICATION_NAME = "GSC Helper";
        final HttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
        final String CREDENTIALS_FILE_PATH = new File(APP_HOME, "credentials.json").getAbsolutePath();

        InputStream in;
        try {
            in = Files.newInputStream(Paths.get(CREDENTIALS_FILE_PATH));
            Preconditions.checkNotNull(in);
        } catch (NullPointerException e) {
            log.info("Файл credentials.json не найден");
            throw new CredentialsFileNotFoundException();
        }

        GoogleClientSecrets clientSecrets =
                GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT,
                JSON_FACTORY,
                clientSecrets,
                singleton("https://www.googleapis.com/auth/webmasters.readonly")
        )
                .setDataStoreFactory(new FileDataStoreFactory(new File(APP_HOME, "tokens")))
                .setAccessType("offline")
                .build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(-1).build();
        Credential credential = new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");

        log.info("Вход в Google аккаунт выполнен");

        return new SearchConsole.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                JSON_FACTORY,
                credential
        ).setApplicationName(APPLICATION_NAME).build();
    }

    private void fillHeader(Row headerRow) {
        headerRow.createCell(0).setCellValue("Тип ресурса");
        headerRow.createCell(1).setCellValue("URL");
        headerRow.createCell(2).setCellValue("Клики");
        headerRow.createCell(3).setCellValue("Показы");
        headerRow.createCell(4).setCellValue("CTR");
        headerRow.createCell(5).setCellValue("Средняя позиция");
    }
}
