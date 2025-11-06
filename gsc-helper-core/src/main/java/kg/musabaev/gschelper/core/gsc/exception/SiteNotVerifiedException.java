package kg.musabaev.gschelper.core.gsc.exception;

import kg.musabaev.gschelper.api.gsc.domain.GscResourceType;

import static com.google.common.base.Preconditions.checkNotNull;

public class SiteNotVerifiedException extends RuntimeException {
    public SiteNotVerifiedException(GscResourceType type, String siteUrl) {
        super("Сайт" + checkNotNull(siteUrl) + " типа '" + checkNotNull(type).rus() + "' не подтвержден");
    }
}
