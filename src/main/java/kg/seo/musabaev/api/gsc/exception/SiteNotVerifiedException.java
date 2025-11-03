package kg.seo.musabaev.api.gsc.exception;

import kg.seo.musabaev.gsc.domain.GscResourceType;

public class SiteNotVerifiedException extends RuntimeException {
    public SiteNotVerifiedException(GscResourceType type, String siteUrl) {
        super("Сайт" + siteUrl + " типа '" + type.rus() + "' не подтвержден");
    }
}
