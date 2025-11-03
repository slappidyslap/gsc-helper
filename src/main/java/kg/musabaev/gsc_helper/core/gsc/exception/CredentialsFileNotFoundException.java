package kg.musabaev.gsc_helper.core.gsc.exception;

import static kg.musabaev.gsc_helper.util.Constants.CREDENTIALS_FILE_NOT_FOUND_EXCEPTION;

public class CredentialsFileNotFoundException extends RuntimeException {
    public CredentialsFileNotFoundException() {
        super(CREDENTIALS_FILE_NOT_FOUND_EXCEPTION);
    }
}
