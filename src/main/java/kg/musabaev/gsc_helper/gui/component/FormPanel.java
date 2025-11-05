package kg.musabaev.gsc_helper.gui.component;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;

public class FormPanel extends JPanel {
    private final DateRangePicker dateRangePicker;
    private final FileChooser fileChooser;

    public FormPanel() {
        this.dateRangePicker = new DateRangePicker();
        this.fileChooser = new FileChooser();
        setupUi();
    }

    private void setupUi() {
        super.setLayout(new MigLayout("wrap 1, fillx", "[grow, fill]", "[]10[]10[]"));
        super.add(dateRangePicker, "growx");
        super.add(fileChooser, "growx");
    }
}
