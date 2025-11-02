package kg.seo.musabaev;

import java.io.File;

import static kg.seo.musabaev.Constants.EXCEL_REPORT_SAVE_EXCEPTION;

public class ExcelReportSaveException extends RuntimeException {
    public ExcelReportSaveException(File f) {
        super(EXCEL_REPORT_SAVE_EXCEPTION + f.getAbsolutePath());
    }
}
