package kg.musabaev.gschelper.swinggui.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class ReportGenerateFormModel {

    public final String DATE_RANGE_FIELD_NAME = "dateRange";
    public final String SAVE_PATH_FIELD_NAME = "savePath";

    private String dateRange = "";
    private String savePath = "";

    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    public void setDateRange(String dateRange) {
        String old = this.dateRange;
        this.dateRange = dateRange;
        pcs.firePropertyChange(DATE_RANGE_FIELD_NAME, old, dateRange);
    }

    public void setSavePath(String savePath) {
        String old = this.savePath;
        this.savePath = savePath;
        pcs.firePropertyChange(SAVE_PATH_FIELD_NAME, old, savePath);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(listener);
    }

    public String dateRange() { return dateRange; }
    public String savePath() { return savePath; }
}
