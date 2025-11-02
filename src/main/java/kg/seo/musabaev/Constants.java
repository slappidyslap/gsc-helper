package kg.seo.musabaev;

import java.io.File;

public class Constants {

    public static final File APP_HOME = new File(
            System.getProperty("user.home") + File.separator +
                    ".gsc-helper" + File.separator);

    public static final String GSC_SITES_NOT_FOUNT_EXCEPTION =
            "Сайты из GSC для генерации отчета не существуют. " +
            "Скорее всего, вы авторизовались через не тот аккаунт.";

    public static final String EXCEL_REPORT_SAVE_EXCEPTION =
            "Не удалось найти путь для сохранения: ";

    public static final String CREDENTIALS_FILE_NOT_FOUND_EXCEPTION =
            "Файл credentials.json не найден";
}
