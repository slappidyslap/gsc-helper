package kg.musabaev.gschelper.core.gsc;

import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.services.searchconsole.v1.SearchConsole;
import com.google.api.services.searchconsole.v1.model.*;
import kg.musabaev.gschelper.api.gsc.GscApiBuilder;
import kg.musabaev.gschelper.api.gsc.GscService;
import kg.musabaev.gschelper.api.gsc.domain.GscAnalyticsResponse;
import kg.musabaev.gschelper.api.gsc.domain.GscResourceType;
import kg.musabaev.gschelper.api.gsc.domain.SiteMetrics;
import kg.musabaev.gschelper.core.gsc.exception.GscApiException;
import kg.musabaev.gschelper.core.gsc.exception.GscSitesNotFoundException;
import kg.musabaev.gschelper.core.gsc.exception.MetricsNotFoundException;
import kg.musabaev.gschelper.core.gsc.exception.SiteNotVerifiedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Сервисный класс-обёртка для взаимодействия с Google Search Console API.
 * Сервис жестко привязан к авторизованному пользователю в {@link GscApiBuilder}.
 * Если нужно достать метрики сайтов подтвержденные другим пользователем,
 * нужно использовать другой объект {@link GscApiBuilder}
 * <p>
 * Использует объект {@link SearchConsole} созданный {@link GscApiBuilder} для выполнения запросов.
 * </p>
 */
public class GscServiceImpl implements GscService {

    private static final Logger log = LoggerFactory.getLogger(GscServiceImpl.class);

    private final SearchConsole searchConsole;

    public GscServiceImpl(GscApiBuilder gscApiBuilder) {
        checkNotNull(gscApiBuilder);
        this.searchConsole = gscApiBuilder.build();
    }

    /**
     * Получает список сайтов, связанных с аккаунтом GSC.
     *
     * @return список сайтов {@link WmxSite}
     * @throws NullPointerException      если {@code response.getSiteEntry()} равны {@code null}
     * @throws GscSitesNotFoundException если список сайтов пуст
     * @throws GscApiException           при ошибках взаимодействия с API
     */
    @Override
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
     * @return объект {@link GscAnalyticsResponse} с ответом от API и ссылкой на сайт
     * @throws NullPointerException если параметры {@code siteUrl}, {@code startDate} или {@code endDate} равны {@code null}
     * @throws SiteNotVerifiedException если сайт не подтвержден
     * @throws GscApiException      при ошибках с API или IO
     */
    @Override
    public GscAnalyticsResponse getAnalytics(
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

            return new GscAnalyticsResponse(response, siteUrl);
        } catch (GoogleJsonResponseException e) {
            if (e.getStatusCode() == 403) {
                log.warn("Сайт {} не подтвержден (403 Forbidden)", siteUrl);
                throw new SiteNotVerifiedException(
                    getResourceType(siteUrl), getCleanUrl(siteUrl));
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
     * @param response объект {@link GscAnalyticsResponse} с ответом от API и ссылкой на сайт
     * @return объект {@link SiteMetrics} с ключевыми метриками сайта
     * @throws NullPointerException если {@code siteUrl}, {@code response} или {@code response.getRows()} равны {@code null}
     * @throws MetricsNotFoundException если метрики сайта не найдены
     */
    @Override
    public SiteMetrics getMetrics(GscAnalyticsResponse response) {
        String siteUrl = response.siteUrl();
        SearchAnalyticsQueryResponse analytics = response.analytics();
        checkNotNull(siteUrl);
        checkNotNull(response);

        if (analytics.getRows() == null) {
            log.error("Метрики сайта {} не найдены", siteUrl);
            throw new MetricsNotFoundException(siteUrl);
        }

        log.info("Извлечение метрик для сайта: {}", siteUrl);

        List<ApiDataRow> rows = analytics.getRows();

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
    @Override
    public GscResourceType getResourceType(String siteUrl) {
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
    @Override
    public String getCleanUrl(String siteUrl) {
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
