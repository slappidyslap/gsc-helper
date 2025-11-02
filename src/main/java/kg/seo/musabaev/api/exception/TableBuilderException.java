package kg.seo.musabaev.api.exception;

public class TableBuilderException extends RuntimeException {
    public TableBuilderException(Throwable cause) {
        super(cause);
    }

    public TableBuilderException(String message) {
        super(message);
    }
}
