package kg.musabaev.gsc_helper.api.gsc.collector.domain;

import kg.musabaev.gsc_helper.api.gsc.domain.SiteMetrics;

import java.util.List;
import java.util.Objects;

import static com.google.common.base.Preconditions.checkNotNull;

public class SiteMetricsList {

    private final List<SiteMetrics> metrics;
    private final List<FailedSiteMetrics> failedSites;

    public SiteMetricsList(List<SiteMetrics> metrics, List<FailedSiteMetrics> failedSites) {
        this.metrics = checkNotNull(metrics);
        this.failedSites = checkNotNull(failedSites);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        SiteMetricsList that = (SiteMetricsList) o;
        return Objects.equals(metrics, that.metrics) && Objects.equals(failedSites, that.failedSites);
    }

    @Override
    public int hashCode() {
        return Objects.hash(metrics, failedSites);
    }

    @Override
    public String toString() {
        return "SiteMetricsList{" +
                "metrics=" + metrics +
                ", failedSites=" + failedSites +
                '}';
    }

    public List<SiteMetrics> metrics() {
        return metrics;
    }

    public List<FailedSiteMetrics> failedSites() {
        return failedSites;
    }
}
