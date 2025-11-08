package kg.musabaev.gschelper.swinggui.util;

import java.nio.file.Path;
import java.nio.file.Paths;

import static kg.musabaev.gschelper.swinggui.util.Utils.paths;

public class Constants {

    public static final String APP_NAME = "GSC Helper";

    public static final Path USER_HOME = Paths.get(System.getProperty("user.home"));

    public static final Path APP_HOME = paths(USER_HOME, ".gsc-helper");

    public static final Path DATA_STORE_FOLDER = paths(APP_HOME, "tokens");

    public static final Path LOGS_FOLDER = paths(APP_HOME, "logs");

    private Constants() {
    }
}
