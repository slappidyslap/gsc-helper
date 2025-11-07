package kg.musabaev.gschelper.swinggui.listener;

import java.io.File;
import java.time.LocalDate;

public interface ReportGenerateFormListener {

    void generateReportClicked(LocalDate startDate, LocalDate endDate, File savePath);
}
