package kg.seo.musabaev.gsc.exception;

import static kg.seo.musabaev.util.Constants.CREDENTIALS_FILE_NOT_FOUND_EXCEPTION;

public class CredentialsFileNotFoundException extends RuntimeException {
    public CredentialsFileNotFoundException() {
        super(CREDENTIALS_FILE_NOT_FOUND_EXCEPTION);
    }
}
