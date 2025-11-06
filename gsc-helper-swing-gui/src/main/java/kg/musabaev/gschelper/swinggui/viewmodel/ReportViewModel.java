package kg.musabaev.gschelper.swinggui.viewmodel;

import java.time.LocalDate;

public class ReportViewModel {

    private Boolean darkModeEnabled = true;
    private LocalDate startDate = null;
    private LocalDate endDate = null;
    private String xlsxFilename = "";

    public Boolean darkModeEnabled() {
        return darkModeEnabled;
    }

    public void toggleDarkModeEnabled() {
        this.darkModeEnabled = !darkModeEnabled;
    }
}
