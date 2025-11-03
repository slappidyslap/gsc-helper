package kg.musabaev.gsc_helper.api.gsc;

import com.google.api.services.searchconsole.v1.model.WmxSite;
import kg.musabaev.gsc_helper.api.gsc.domain.GscAnalyticsResponse;
import kg.musabaev.gsc_helper.api.gsc.domain.GscResourceType;
import kg.musabaev.gsc_helper.api.gsc.domain.SiteMetrics;

import java.time.LocalDate;
import java.util.List;

/**
 * Интерфейс для взаимодействия с Google Search Console API.
 */
public interface GscService {

    /**
     * Получает список сайтов, связанных с аккаунтом GSC.
     *
     * @return список сайтов {@link WmxSite}
     */
    List<WmxSite> getSites();

    /**
     * Запрашивает метрики для указанного сайта за определённый период.
     *
     * @param siteUrl   URL сайта
     * @param startDate дата начала периода
     * @param endDate   дата окончания периода
     * @return ответ {@link GscAnalyticsResponse} с метриками
     */
    GscAnalyticsResponse getAnalytics(
            String siteUrl,
            LocalDate startDate,
            LocalDate endDate);

    /**
     * Извлекает основные метрики сайта из ответа.
     *
     * @param response объект {@link GscAnalyticsResponse} с ответом от API и ссылкой на сайт
     * @return объект {@link SiteMetrics} с метриками сайта
     */
    SiteMetrics getMetrics(GscAnalyticsResponse response);

    /**
     * Определяет тип ресурса GSC: доменный ресурс или с префиксом URL.
     *
     * @param siteUrl URL сайта
     * @return тип ресурса {@link GscResourceType}
     */
    GscResourceType getResourceType(String siteUrl);

    /**
     * Извлекает чистый URL сайта без префикса GSC.
     * Например: {@code sc-domain:https://httpbin.org/} -> {@code `https://httpbin.org/`}
     *
     * @param siteUrl URL сайта с возможным префиксом
     * @return URL без префикса.
     */
    String getCleanUrl(String siteUrl);
}
