package kg.musabaev.gsc_helper.core.table.exception;

import static com.google.common.base.Preconditions.checkNotNull;

public class TableBuilderException extends RuntimeException {
    public TableBuilderException(Throwable cause) {
        super(checkNotNull(cause));
    }

    public TableBuilderException(String message) {
        super(checkNotNull(message));
    }
}
