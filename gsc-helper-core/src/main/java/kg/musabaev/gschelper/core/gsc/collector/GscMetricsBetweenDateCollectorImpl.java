package kg.musabaev.gschelper.core.gsc.collector;

import com.google.api.services.searchconsole.v1.model.WmxSite;
import kg.musabaev.gschelper.api.gsc.GscApiBuilder;
import kg.musabaev.gschelper.api.gsc.GscService;
import kg.musabaev.gschelper.api.gsc.collector.BaseGscMetricsBetweenDateCollector;
import kg.musabaev.gschelper.api.gsc.collector.domain.FailedSiteMetrics;
import kg.musabaev.gschelper.api.gsc.collector.domain.SiteMetricsList;
import kg.musabaev.gschelper.api.gsc.domain.GscAnalyticsResponse;
import kg.musabaev.gschelper.api.gsc.domain.GscResourceType;
import kg.musabaev.gschelper.api.gsc.domain.SiteMetrics;
import kg.musabaev.gschelper.core.gsc.GscServiceImpl;
import kg.musabaev.gschelper.core.gsc.exception.SiteNotVerifiedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Сервис для сбора метрик всех сайтов из Google Search Console API за указанный период.
 */
public class GscMetricsBetweenDateCollectorImpl extends BaseGscMetricsBetweenDateCollector {

    private static final Logger log = LoggerFactory.getLogger(GscMetricsBetweenDateCollectorImpl.class);

    private final GscService gsc;

    public GscMetricsBetweenDateCollectorImpl(GscApiBuilder gscApiBuilder) {
        super();
        checkNotNull(gscApiBuilder);
        this.gsc = new GscServiceImpl(gscApiBuilder);
    }

    /**
     * Собирает метрики всех сайтов за указанный период.
     * Обрабатывает все ошибки и ложит их в контейнер {@link FailedSiteMetrics}.
     *
     * @param startDate дата начала периода
     * @param endDate дата окончания периода
     * @return результат сбора метрик. Объект {@link SiteMetricsList}
     */
    @Override
    public SiteMetricsList collectBetweenDate(LocalDate startDate, LocalDate endDate) {
        checkNotNull(startDate);
        checkNotNull(endDate);
        super.setStartDate(startDate);
        super.setEndDate(endDate);

        log.info("Начат сбор метрик GSC за период {} - {}", startDate, endDate);

        List<WmxSite> sites = gsc.getSites();
        log.info("Получено {} сайтов для обработки", sites.size());

        List<SiteMetrics> collectedMetrics = new ArrayList<>();
        List<FailedSiteMetrics> failedSites = new ArrayList<>();

        for (WmxSite site : sites) {
            String siteUrl = site.getSiteUrl();
            log.info("Обработка сайта: {}", siteUrl);

            try {
                GscAnalyticsResponse response = gsc.getAnalytics(
                        siteUrl,
                        startDate,
                        endDate);

                if (response == null) {
                    log.warn("Сайт {} пропущен - не подтвержден", siteUrl);
                    failedSites.add(
                            createFailedSiteMetrics(siteUrl,
                                    createSiteNotVerifiedException(siteUrl)));
                    continue;
                }

                SiteMetrics metrics = gsc.getMetrics(response);
                collectedMetrics.add(metrics);

                log.info("Сайт {} успешно обработан ({}/{})",
                        siteUrl, collectedMetrics.size(), sites.size());

            } catch (Exception e) { // FIXME Перехватывает все исключения. globalExceptionHandler не успевает обработать
                log.error("Ошибка при обработке сайта: {}", siteUrl, e);
                failedSites.add(createFailedSiteMetrics(siteUrl, e));
            }
        }

        SiteMetricsList result = new SiteMetricsList(
                collectedMetrics,
                failedSites);

        log.info("Сбор метрик завершен. Всего: {}, обработано: {}, ошибок: {}",
                sites.size(),
                collectedMetrics.size(),
                failedSites.size());

        return result;
    }

    /**
     * Создает объект {@link FailedSiteMetrics}
     * @param siteUrl ссылка на сайт
     * @param t исключение, которое произошло при сборке метрик сайта
     * @return объект {@link FailedSiteMetrics}
     */
    private FailedSiteMetrics createFailedSiteMetrics(String siteUrl, Throwable t) {
        GscResourceType type = gsc.getResourceType(siteUrl);
        String cleanUrl = gsc.getCleanUrl(siteUrl);
        return new FailedSiteMetrics(type, cleanUrl, t);
    }

    /**
     * Создает исключение {@link SiteNotVerifiedException}
     * @param siteUrl ссылка на сайт
     * @return исключение {@link SiteNotVerifiedException}
     */
    private SiteNotVerifiedException createSiteNotVerifiedException(String siteUrl) {
        GscResourceType type = gsc.getResourceType(siteUrl);
        String cleanUrl = gsc.getCleanUrl(siteUrl);
        return new SiteNotVerifiedException(type, cleanUrl);
    }
}
