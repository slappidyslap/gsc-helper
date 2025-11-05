package kg.musabaev.gsc_helper.gui.component;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;

public abstract class BaseInput extends JPanel {

    protected void setupUi() {
        super.setLayout(new MigLayout(
                /* layoutConstraints */ "insets 0, fillx",
                /* colConstraints */ "[right]10[grow,fill][pref!]",
                /* rowConstraints */ "[]"));
    }

}
