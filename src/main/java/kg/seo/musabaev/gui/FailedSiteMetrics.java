package kg.seo.musabaev.gui;

import kg.seo.musabaev.gsc.domain.GscResourceType;

import java.util.Objects;

public class FailedSiteMetrics {

    private final GscResourceType type;
    private final String url;
    private final Throwable throwable;

    public FailedSiteMetrics(GscResourceType type, String url, Throwable throwable) {
        this.type = type;
        this.url = url;
        this.throwable = throwable;
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
