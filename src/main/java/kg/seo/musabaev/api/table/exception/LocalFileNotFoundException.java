package kg.seo.musabaev.api.table.exception;

import java.io.File;

import static kg.seo.musabaev.util.Constants.LOCAL_FILE_NOT_FOUND_EXCEPTION;

public class LocalFileNotFoundException extends RuntimeException {
    public LocalFileNotFoundException(File f) {
        super(LOCAL_FILE_NOT_FOUND_EXCEPTION + f.getAbsolutePath());
    }
}
