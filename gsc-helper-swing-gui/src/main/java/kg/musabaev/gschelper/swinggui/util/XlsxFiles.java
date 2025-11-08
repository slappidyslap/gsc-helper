package kg.musabaev.gschelper.swinggui.util;

import java.util.regex.Pattern;

public class XlsxFiles {

    public static final String FILENAME_TEMPLATE = "Отчет GSC за период %s.xlsx";

    public static final String NULL_FILENAME_TEMPLATE = "Отчет GSC за период (период).xlsx";

    public static final Pattern FILENAME_PATTERN = Pattern.compile(".*Отчет GSC за период (.*)\\.xlsx$");

}
