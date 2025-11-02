package kg.seo.musabaev;

import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.services.searchconsole.v1.model.*;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.IntStream;

import static com.google.api.client.util.Preconditions.checkArgument;
import static com.google.api.client.util.Preconditions.checkNotNull;

public class GscSitesHandler {

    private final Logger log = LoggerFactory.getLogger(GscSitesHandler.class);
    private final Workbook workbook = new XSSFWorkbook();
    private final GoogleSearchConsole searchConsole = new GoogleSearchConsole();
    private final String startDate;
    private final String endDate;

    public GscSitesHandler(LocalDate startDate, LocalDate endDate) {
        this.startDate = startDate.toString();
        this.endDate = endDate.toString();
    }

    public CompletableFuture<Void> start() {
        log.info("Начат сбор данных");
        return CompletableFuture.runAsync(() -> {
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
                checkNotNull(sites);
            } catch (NullPointerException e) {
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
                    queryResp = searchConsole.searchAnalytics()
                            .query(SITE_URL, queryReq)
                            .execute();
                    log.debug("Метрики были получены. Ответ от GSC:\n{}", queryResp.toPrettyString());
                } catch (GoogleJsonResponseException e) {
                    if (e.getStatusCode() == 403) {
                        log.info("Данный сайт еще подтвержден");
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
                checkNotNull(rows);
                checkArgument(!rows.isEmpty());

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

    public void saveExcelFile(File path) {
        log.debug("Сохранение Excel файла...");

        try {
            FileOutputStream fileOut = new FileOutputStream(path);
            workbook.write(fileOut);
            fileOut.close();
            workbook.close();
            log.info("Excel файл успешно сохранен по пути: {}", path);
        } catch (FileNotFoundException e) {
            log.info("Папка для сохранения не был выбран");
        } catch (IOException e) {
            log.error("Общее исключение при сохранении Excel файла", e);
            throw new RuntimeException(e);
        }
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
