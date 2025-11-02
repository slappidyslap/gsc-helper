package kg.seo.musabaev.searchconsole;

import static kg.seo.musabaev.util.Constants.CREDENTIALS_FILE_NOT_FOUND_EXCEPTION;

public class CredentialsFileNotFoundException extends RuntimeException {
    public CredentialsFileNotFoundException() {
        super(CREDENTIALS_FILE_NOT_FOUND_EXCEPTION);
    }
}
