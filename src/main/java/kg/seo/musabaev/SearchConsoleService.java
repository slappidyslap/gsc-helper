package kg.seo.musabaev;

import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.services.searchconsole.v1.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

import static com.google.api.client.util.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkArgument;
import static java.lang.String.format;

/**
 * Сервис для работы с Google Search Console API
 */
public class SearchConsoleService {

    private static final Logger log = LoggerFactory.getLogger(SearchConsoleService.class);
    private final GoogleSearchConsole searchConsole;

    public SearchConsoleService(GoogleSearchConsole searchConsole) {
        this.searchConsole = checkNotNull(searchConsole, "searchConsole не может быть null");
    }

    /**
     * Получает список всех сайтов из Google Search Console
     *
     * @return список сайтов
     * @throws GscSitesNotFoundException если сайты не найдены
     * @throws GscApiException           при ошибке API
     */
    public List<WmxSite> getSites() {
        log.info("Запрос списка сайтов из GSC...");

        try {
            SitesListResponse response = searchConsole.sites().list().execute();
            List<WmxSite> sites = response.getSiteEntry();

            checkNotNull(sites, "Список сайтов пуст");

            log.info("Успешно получено {} сайтов из GSC", sites.size());

            return sites;
        } catch (NullPointerException e) {
            log.warn("Сайты не найдены в ответе GSC", e);
            throw new GscSitesNotFoundException();
        } catch (IOException e) {
            log.error("Ошибка ввода-вывода при запросе списка сайтов", e);
            throw new GscApiException(e);
        }
    }

    /**
     * Получает аналитические данные для конкретного сайта
     *
     * @param siteUrl   URL сайта
     * @param startDate дата начала периода (формат: yyyy-MM-dd)
     * @param endDate   дата окончания периода (формат: yyyy-MM-dd)
     * @return ответ с аналитическими данными или null если сайт не подтвержден
     * @throws GscApiException при ошибке API
     */
    public SearchAnalyticsQueryResponse getAnalytics(
            String siteUrl,
            String startDate,
            String endDate) {
        checkNotNull(siteUrl, "siteUrl не может быть null");
        checkNotNull(startDate, "startDate не может быть null");
        checkNotNull(endDate, "endDate не может быть null");

        log.info("Запрос аналитики для сайта: {} за период {} - {}",
                siteUrl, startDate, endDate);

        SearchAnalyticsQueryRequest request = new SearchAnalyticsQueryRequest()
                .setStartDate(startDate)
                .setEndDate(endDate);

        try {
            SearchAnalyticsQueryResponse response = searchConsole.searchAnalytics()
                    .query(siteUrl, request)
                    .execute();

            log.info("Аналитика успешно получена для сайта: {}", siteUrl);
            log.info("Ответ от GSC API:\n{}", response.toPrettyString());

            return response;
        } catch (GoogleJsonResponseException e) {
            if (e.getStatusCode() == 403) {
                log.warn("Сайт {} не подтвержден (403 Forbidden)", siteUrl);
                return null;
            }
            log.error("Ошибка Google API для сайта {}. Код: {}, Сообщение: {}",
                    siteUrl, e.getStatusCode(), e.getStatusMessage(), e);
            throw new GscApiException(e);
        } catch (IOException e) {
            log.error("Ошибка ввода-вывода при запросе аналитики для сайта: {}", siteUrl, e);
            throw new GscApiException(e);
        }
    }

    /**
     * Извлекает метрики сайта из ответа GSC
     *
     * @param siteUrl  URL сайта
     * @param response ответ от Google Search Console
     * @return объект с метриками сайта
     */
    public SiteMetrics getMetrics(String siteUrl, SearchAnalyticsQueryResponse response) {
        checkNotNull(siteUrl, "siteUrl не может быть null");
        checkNotNull(response, "response не может быть null");
        checkNotNull(response.getRows(), "Строки данных отсутствуют в ответе");
        checkArgument(!response.getRows().isEmpty(), "Ответ не содержит данных");

        log.info("Извлечение метрик для сайта: {}", siteUrl);

        List<ApiDataRow> rows = response.getRows();

        ApiDataRow dataRow = rows.get(0);
        GscResourceType resourceType = getResourceType(siteUrl);
        String cleanUrl = getCleanUrl(siteUrl);
        double clicks = dataRow.getClicks();
        double impressions = dataRow.getImpressions();
        double ctr = roundToOneDecimal(dataRow.getCtr() * 100);
        double avgPosition = roundToOneDecimal(dataRow.getPosition());

        log.info("Метрики извлечены - Клики: {}, Показы: {}, CTR: {}%, Позиция: {}",
                clicks, impressions, ctr, avgPosition);

        return new SiteMetrics(
                resourceType,
                cleanUrl,
                clicks,
                impressions,
                ctr,
                avgPosition
        );
    }

    /**
     * Определяет тип ресурса GSC (доменный или с префиксом)
     *
     * @see GscResourceType
     */
    private GscResourceType getResourceType(String siteUrl) {
        return siteUrl.contains("sc-domain:") ?
                GscResourceType.DOMAIN :
                GscResourceType.WITH_PREFIX;
    }

    /**
     * Извлекает чистый URL без служебных префиксов GSC
     */
    private String getCleanUrl(String siteUrl) {
        if (siteUrl.contains("sc-domain:"))
            return siteUrl.substring("sc-domain:".length());
        return siteUrl;
    }

    /**
     * Округляет число до одного знака после запятой
     */
    private double roundToOneDecimal(double value) {
        return Math.round(value * 10.0) / 10.0;
    }
}