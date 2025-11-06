package kg.musabaev.gschelper.api.gsc.collector.domain;

import kg.musabaev.gschelper.api.gsc.domain.GscResourceType;

import java.util.Objects;

import static com.google.common.base.Preconditions.checkNotNull;

public class FailedSiteMetrics {

    private final GscResourceType type;
    private final String url;
    private final Throwable throwable;

    public FailedSiteMetrics(GscResourceType type, String url, Throwable throwable) {
        this.type = checkNotNull(type);
        this.url = checkNotNull(url);
        this.throwable = checkNotNull(throwable);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        FailedSiteMetrics that = (FailedSiteMetrics) o;
        return type == that.type && Objects.equals(url, that.url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, url);
    }

    @Override
    public String toString() {
        return "FailedSiteMetrics{" +
                "type=" + type +
                ", url='" + url + '\'' +
                ", throwable=" + throwable +
                '}';
    }

    public GscResourceType type() {
        return type;
    }

    public String url() {
        return url;
    }

    public Throwable throwable() {
        return throwable;
    }
}
