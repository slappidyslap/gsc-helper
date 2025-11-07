package kg.musabaev.gschelper.swinggui.exception;

import kg.musabaev.gschelper.swinggui.component.ExceptionDialog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;

public class AwtEventQueueExceptionHandler extends EventQueue {

    private static final Logger log = LoggerFactory.getLogger(AwtEventQueueExceptionHandler.class);

    @Override
    protected void dispatchEvent(AWTEvent newEvent) {
        try {
            super.dispatchEvent(newEvent);
        } catch (Throwable t) {
            log.error(t.getMessage(), t);
            ExceptionDialog.show(t);
            super.dispatchEvent(newEvent);
        }
    }
}
