package kg.musabaev.gschelper.core.table.exception;

import java.io.File;

import static com.google.common.base.Preconditions.checkNotNull;
import static kg.musabaev.gschelper.core.util.ExceptionMessages.LOCAL_FILE_NOT_FOUND_EXCEPTION;

public class LocalFileNotFoundException extends RuntimeException {
    public LocalFileNotFoundException(File f) {
        super(LOCAL_FILE_NOT_FOUND_EXCEPTION + checkNotNull(f).getAbsolutePath());
    }
}
