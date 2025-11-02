package kg.seo.musabaev;

import static kg.seo.musabaev.Constants.GSC_SITES_NOT_FOUNT_EXCEPTION;

public class GscSitesNotFoundException extends RuntimeException {
    public GscSitesNotFoundException() {
        super(GSC_SITES_NOT_FOUNT_EXCEPTION);
    }
}
