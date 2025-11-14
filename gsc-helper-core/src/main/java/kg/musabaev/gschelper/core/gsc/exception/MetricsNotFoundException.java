package kg.musabaev.gschelper.core.gsc.exception;

public class MetricsNotFoundException extends RuntimeException {
    public MetricsNotFoundException(String siteUrl) {
        super(
            "Метрики для сайта " + siteUrl + " не найдены." +
            "Скорее всего, сайт был добавлен в Google Search Console позже");
    }
}
