package kg.seo.musabaev.gsc;

import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.services.searchconsole.v1.SearchConsole;
import com.google.api.services.searchconsole.v1.model.*;
import kg.seo.musabaev.gsc.domain.GscResourceType;
import kg.seo.musabaev.gsc.domain.SiteMetrics;
import kg.seo.musabaev.gsc.exception.GscApiException;
import kg.seo.musabaev.gsc.exception.GscSitesNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import static com.google.api.client.util.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkArgument;

/**
 * Сервисный класс-обёртка для взаимодействия с Google Search Console API.
 * <p>
 * Использует {@link GscAuthenticator} для авторизации и выполнения запросов.
 * Предоставляет удобные методы для получения списка сайтов, аналитики и извлечения метрик,
 * обрабатывая ошибки и преобразуя данные в бизнес-объекты.
 * </p>
 */
public class GscService {

    private static final Logger log = LoggerFactory.getLogger(GscService.class);

    private final SearchConsole searchConsole;

    public GscService(GscAuthenticator gscAuthenticator) {
        checkNotNull(gscAuthenticator);
        this.searchConsole = gscAuthenticator.build();
    }

    /**
     * Получает список сайтов, связанных с аккаунтом GSC.
     *
     * @return список сайтов {@link WmxSite}
     * @throws GscSitesNotFoundException если список сайтов пуст
     * @throws GscApiException           при ошибках взаимодействия с API
     */
    public List<WmxSite> getSites() {
        log.info("Запрос списка сайтов из GSC...");

        try {
            SitesListResponse response = searchConsole.sites().list().execute();
            List<WmxSite> sites = response.getSiteEntry();

            checkNotNull(sites);

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
     * Запрашивает метрики для указанного сайта за определённый период.
     *
     * @param siteUrl   URL сайта
     * @param startDate дата начала периода
     * @param endDate   дата окончания периода
     * @return ответ {@link SearchAnalyticsQueryResponse} с метриками
     *         или {@code null}, если сайт не подтверждён (HTTP 403)
     * @throws NullPointerException если параметры {@code siteUrl}, {@code startDate} или {@code endDate} равны {@code null}
     * @throws GscApiException      при ошибках API
     */
    public SearchAnalyticsQueryResponse getAnalytics(
            String siteUrl,
            LocalDate startDate,
            LocalDate endDate) {
        checkNotNull(siteUrl);
        checkNotNull(startDate);
        checkNotNull(endDate);

        log.info("Запрос аналитики для сайта: {} за период {} - {}", siteUrl, startDate, endDate);

        SearchAnalyticsQueryRequest request = new SearchAnalyticsQueryRequest()
                .setStartDate(startDate.toString())
                .setEndDate(endDate.toString());

        try {
            SearchAnalyticsQueryResponse response = searchConsole.searchanalytics()
                    .query(siteUrl, request)
                    .execute();

            log.info("Аналитика успешно получена для сайта {}. Ответ:\n{}",
                    siteUrl, response.toPrettyString());

            return response;
        } catch (GoogleJsonResponseException e) {
            if (e.getStatusCode() == 403) {
                log.warn("Сайт {} не подтвержден (403 Forbidden)", siteUrl);
                return null;
            }
            log.error("Ошибка GSC API для сайта {}. HTTP Status Code: {}. Сообщение: {}",
                    siteUrl, e.getStatusCode(), e.getStatusMessage(), e);
            throw new GscApiException(e);
        } catch (IOException e) {
            log.error("Ошибка ввода-вывода при запросе метрик сайта: {}", siteUrl, e);
            throw new GscApiException(e);
        }
    }

    /**
     * Извлекает основные метрики сайта из ответа GSC API.
     *
     * @param siteUrl  URL сайта, для которого извлекаются метрики
     * @param response ответ {@link SearchAnalyticsQueryResponse} от API
     * @return объект {@link SiteMetrics} с ключевыми метриками сайта
     * @throws NullPointerException     если {@code siteUrl}, {@code response} или {@code response.getRows()} равны {@code null}
     * @throws IllegalArgumentException если ответ не содержит данных
     */
    public SiteMetrics getMetrics(String siteUrl, SearchAnalyticsQueryResponse response) {
        checkNotNull(siteUrl);
        checkNotNull(response);
        checkNotNull(response.getRows());
        checkArgument(!response.getRows().isEmpty());

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
     * Определяет тип ресурса GSC: доменный ресурс или с префиксом URL.
     *
     * @param siteUrl URL сайта
     * @return тип ресурса {@link GscResourceType}
     */
    private GscResourceType getResourceType(String siteUrl) {
        return siteUrl.contains("sc-domain:") ?
                GscResourceType.DOMAIN :
                GscResourceType.WITH_PREFIX;
    }

    /**
     * Извлекает чистый URL сайта без префикса GSC.
     * Например: {@code sc-domain:https://httpbin.org/} -> {@code `https://httpbin.org/`}
     *
     * @param siteUrl URL сайта с возможным префиксом
     * @return URL без префикса.
     */
    private String getCleanUrl(String siteUrl) {
        if (siteUrl.contains("sc-domain:"))
            return siteUrl.substring("sc-domain:".length());
        return siteUrl;
    }

    /**
     * Округляет число до одного знака после запятой.
     *
     * @param value число для округления
     * @return округлённое значение с одним знаком после запятой
     */
    private double roundToOneDecimal(double value) {
        return Math.round(value * 10.0) / 10.0;
    }
}
