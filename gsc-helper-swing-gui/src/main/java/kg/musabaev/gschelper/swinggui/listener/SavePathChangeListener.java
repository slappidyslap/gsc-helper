package kg.musabaev.gschelper.swinggui.listener;

import java.nio.file.Path;
import java.util.EventListener;

@FunctionalInterface
public interface SavePathChangeListener extends EventListener {

    void savePathChanged(Path savePath);
}
