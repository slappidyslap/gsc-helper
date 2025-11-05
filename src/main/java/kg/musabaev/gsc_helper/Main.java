package kg.musabaev.gsc_helper;

import kg.musabaev.gsc_helper.gui.Gui;
import kg.musabaev.gsc_helper.gui.Gui2;
import kg.musabaev.gsc_helper.gui.Gui3;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.lang.System.currentTimeMillis;

public class Main {
    private static final Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        long startTime = currentTimeMillis();
        Gui3 gui = new Gui3();
        gui.showFrame();
        long duration = currentTimeMillis() - startTime;
        log.info("Программа запущено за {} секунд", duration / 1000.0);
    }
}
