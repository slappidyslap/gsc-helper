package kg.musabaev.gschelper.core.table.exception;

public class TableDataOutputProcessException extends RuntimeException {
    public TableDataOutputProcessException(Throwable cause) {
        super(cause);
    }

    public TableDataOutputProcessException(String message) {
        super(message);
    }
}
