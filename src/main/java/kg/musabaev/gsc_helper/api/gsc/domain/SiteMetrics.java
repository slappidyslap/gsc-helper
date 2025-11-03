package kg.musabaev.gsc_helper.api.gsc.domain;

import java.util.Objects;

import static com.google.common.base.Preconditions.checkNotNull;

public class SiteMetrics {

    private final GscResourceType type;
    private final String url;
    private final double clicks;
    private final double impressions;
    private final double ctr;
    private final double avgPosition;

    public SiteMetrics(GscResourceType type, String url, double clicks, double impressions, double ctr, double avgPosition) {
        this.type = checkNotNull(type);
        this.url = checkNotNull(url);
        this.clicks = clicks;
        this.impressions = impressions;
        this.ctr = ctr;
        this.avgPosition = avgPosition;
    }

    public GscResourceType type() {
        return type;
    }

    public String url() {
        return url;
    }

    public double clicks() {
        return clicks;
    }

    public double impressions() {
        return impressions;
    }

    public double ctr() {
        return ctr;
    }

    public double avgPosition() {
        return avgPosition;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        SiteMetrics that = (SiteMetrics) o;
        return type == that.type && Objects.equals(url, that.url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, url);
    }

    @Override
    public String toString() {
        return "SiteMetrics{" +
                "type=" + type +
                ", url='" + url + '\'' +
                ", clicks=" + clicks +
                ", impressions=" + impressions +
                ", ctr=" + ctr +
                ", avgPosition=" + avgPosition +
                '}';
    }
}
