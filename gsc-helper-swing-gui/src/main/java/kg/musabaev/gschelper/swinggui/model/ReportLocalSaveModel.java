package kg.musabaev.gschelper.swinggui.model;

import java.nio.file.Path;
import java.time.LocalDate;

import static com.google.common.base.Preconditions.checkNotNull;

public class ReportLocalSaveModel {

    private Path savePath;
    private LocalDate startDate;
    private LocalDate endDate;

    public Path savePath() {
        return savePath;
    }

    public void setSavePath(Path savePath) {
        this.savePath = checkNotNull(savePath);
    }

    @SuppressWarnings("unused")
    public LocalDate startDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = checkNotNull(startDate);
    }

    @SuppressWarnings("unused")
    public LocalDate endDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = checkNotNull(endDate);
    }
}
