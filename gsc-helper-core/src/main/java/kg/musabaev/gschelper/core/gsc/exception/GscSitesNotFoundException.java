package kg.musabaev.gschelper.core.gsc.exception;

import static kg.musabaev.gschelper.core.util.ExceptionMessages.GSC_SITES_NOT_FOUNT_EXCEPTION;

public class GscSitesNotFoundException extends RuntimeException {
    public GscSitesNotFoundException() {
        super(GSC_SITES_NOT_FOUNT_EXCEPTION);
    }
}
