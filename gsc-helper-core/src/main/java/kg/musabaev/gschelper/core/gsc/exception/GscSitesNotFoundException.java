package kg.musabaev.gschelper.core.gsc.exception;

import static kg.musabaev.gschelper.core.util.Constants.GSC_SITES_NOT_FOUNT_EXCEPTION;

public class GscSitesNotFoundException extends RuntimeException {
    public GscSitesNotFoundException() {
        super(GSC_SITES_NOT_FOUNT_EXCEPTION);
    }
}
