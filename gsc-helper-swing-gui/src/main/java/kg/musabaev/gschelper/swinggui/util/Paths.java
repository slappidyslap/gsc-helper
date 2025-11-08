package kg.musabaev.gschelper.swinggui.util;

import java.nio.file.Path;

public class Paths {

    public static final Path USER_HOME = java.nio.file.Paths.get(System.getProperty("user.home"));

    public static final Path APP_HOME = paths(USER_HOME, ".gsc-helper");

    public static final Path DATA_STORE_FOLDER = paths(APP_HOME, "tokens");

    public static final Path LOGS_FOLDER = paths(APP_HOME, "logs");

    public static Path paths(Path first, String... child) {
        return java.nio.file.Paths.get(first.toString(), child);
    }

    private Paths() {
    }
}
