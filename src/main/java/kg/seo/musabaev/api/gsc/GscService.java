package kg.seo.musabaev.api.gsc;

import com.google.api.services.searchconsole.v1.model.SearchAnalyticsQueryResponse;
import com.google.api.services.searchconsole.v1.model.WmxSite;
import kg.seo.musabaev.gsc.domain.SiteMetrics;

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
     * @return ответ {@link SearchAnalyticsQueryResponse} с метриками
     */
    SearchAnalyticsQueryResponse getAnalytics(
            String siteUrl,
            LocalDate startDate,
            LocalDate endDate);

    /**
     * Извлекает основные метрики сайта из ответа.
     *
     * @param siteUrl  URL сайта, для которого извлекаются метрики
     * @param response ответ {@link SearchAnalyticsQueryResponse} от API
     * @return объект {@link SiteMetrics} с метриками сайта
     */
    SiteMetrics getMetrics(String siteUrl, SearchAnalyticsQueryResponse response);
}
