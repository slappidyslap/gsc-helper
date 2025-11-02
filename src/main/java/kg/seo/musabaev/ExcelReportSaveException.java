package kg.seo.musabaev;

import java.io.File;

public class ExcelReportSaveException extends RuntimeException {
    public ExcelReportSaveException(File f) {
        super("Не удалось найти путь для сохранения: " + f.getAbsolutePath());
    }
}
