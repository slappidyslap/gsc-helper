package kg.musabaev.gsc_helper.api.gsc.exception;

import static kg.musabaev.gsc_helper.util.Constants.GSC_SITES_NOT_FOUNT_EXCEPTION;

public class GscSitesNotFoundException extends RuntimeException {
    public GscSitesNotFoundException() {
        super(GSC_SITES_NOT_FOUNT_EXCEPTION);
    }
}
