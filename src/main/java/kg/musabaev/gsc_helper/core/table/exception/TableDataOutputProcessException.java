package kg.musabaev.gsc_helper.core.table.exception;

public class TableDataOutputProcessException extends RuntimeException {
    public TableDataOutputProcessException(Throwable cause) {
        super(cause);
    }

    public TableDataOutputProcessException(String message) {
        super(message);
    }
}
