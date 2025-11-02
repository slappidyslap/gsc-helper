package kg.seo.musabaev.searchconsole;

import static kg.seo.musabaev.util.Constants.GSC_SITES_NOT_FOUNT_EXCEPTION;

public class GscSitesNotFoundException extends RuntimeException {
    public GscSitesNotFoundException() {
        super(GSC_SITES_NOT_FOUNT_EXCEPTION);
    }
}
