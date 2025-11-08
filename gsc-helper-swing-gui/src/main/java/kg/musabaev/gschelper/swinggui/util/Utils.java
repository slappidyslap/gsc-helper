package kg.musabaev.gschelper.swinggui.util;

import java.nio.file.Path;
import java.nio.file.Paths;

public class Utils {

    public static Path paths(Path first, String... child) {
        return Paths.get(first.toString(), child);
    }

    private Utils() {
    }
}
