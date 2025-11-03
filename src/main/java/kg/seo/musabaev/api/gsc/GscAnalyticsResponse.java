package kg.seo.musabaev.api.gsc;

import com.google.api.services.searchconsole.v1.model.SearchAnalyticsQueryResponse;

import java.util.Objects;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * DTO с метриками сайта и ссылкой на сайт
 */
public class GscAnalyticsResponse {

    private final SearchAnalyticsQueryResponse analytics;
    private final String siteUrl;

    public GscAnalyticsResponse(SearchAnalyticsQueryResponse analytics, String siteUrl) {
        this.analytics = checkNotNull(analytics);
        this.siteUrl = checkNotNull(siteUrl);
    }

    public SearchAnalyticsQueryResponse analytics() {
        return analytics;
    }

    public String siteUrl() {
        return siteUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        GscAnalyticsResponse that = (GscAnalyticsResponse) o;
        return Objects.equals(siteUrl, that.siteUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(siteUrl);
    }

    @Override
    public String toString() {
        return "GscAnalyticsResponse{" +
                "analytics=" + analytics +
                ", siteUrl='" + siteUrl + '\'' +
                '}';
    }
}
