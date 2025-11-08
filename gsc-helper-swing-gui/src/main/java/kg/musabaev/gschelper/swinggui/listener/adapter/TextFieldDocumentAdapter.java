package kg.musabaev.gschelper.swinggui.listener.adapter;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

@FunctionalInterface
public interface TextFieldDocumentAdapter extends DocumentListener {

    void valueUpdated(DocumentEvent e);

    @Override
    default void insertUpdate(DocumentEvent e) {
        valueUpdated(e);
    }

    @Override
    default void removeUpdate(DocumentEvent e) {
    }

    @Override
    default void changedUpdate(DocumentEvent e) {
    }
}
