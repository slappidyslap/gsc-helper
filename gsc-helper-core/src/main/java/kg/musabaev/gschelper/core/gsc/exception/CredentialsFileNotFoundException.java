package kg.musabaev.gschelper.core.gsc.exception;

import static kg.musabaev.gschelper.core.util.ExceptionMessages.CREDENTIALS_FILE_NOT_FOUND_EXCEPTION;

public class CredentialsFileNotFoundException extends RuntimeException {
    public CredentialsFileNotFoundException() {
        super(CREDENTIALS_FILE_NOT_FOUND_EXCEPTION);
    }
}
