package kg.musabaev.gschelper.swinggui;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.lang.System.currentTimeMillis;

public class Main {
    private static final Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        long startTime = currentTimeMillis();
        Gui2 gui = new Gui2();
        gui.showFrame();
        long duration = currentTimeMillis() - startTime;
        log.info("Программа запущено за {} секунд", duration / 1000.0);
    }
}
