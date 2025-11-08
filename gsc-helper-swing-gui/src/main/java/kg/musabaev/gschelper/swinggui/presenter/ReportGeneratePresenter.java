package kg.musabaev.gschelper.swinggui.presenter;

import kg.musabaev.gschelper.api.report.ReportService;
import kg.musabaev.gschelper.core.gsc.exception.CredentialsFileNotFoundException;
import kg.musabaev.gschelper.core.gsc.exception.GscSitesNotFoundException;
import kg.musabaev.gschelper.core.table.exception.LocalFileNotFoundException;
import kg.musabaev.gschelper.core.table.output.file.local.TableDataOutputLocalFileSaver;
import kg.musabaev.gschelper.swinggui.view.ReportGenerateView;

import java.io.File;
import java.time.LocalDate;

public class ReportGeneratePresenter {

    private final ReportGenerateView view;
    private final ReportService reportService;

    public ReportGeneratePresenter(ReportGenerateView view, ReportService reportService) {
        this.view = view;
        this.reportService = reportService;
    }

    public void onClickGenerateReport(LocalDate startDate, LocalDate endDate, File savePath) {
        try {
            view.disableSubmitButton();
            byte[] data = reportService.generateReport(startDate, endDate);
            reportService.processReportOutput(
                new TableDataOutputLocalFileSaver.Config(savePath), data);
        } catch (Exception e) {
            handleException(e);
        } finally {
            view.enableSubmitButton();
        }
    }

    public void handleException(Exception e) {
        if (e instanceof LocalFileNotFoundException ||
            e instanceof CredentialsFileNotFoundException ||
            e instanceof GscSitesNotFoundException) {
            view.showWarningDialog(e.getMessage());
        } else {
            view.showExceptionDialog(e);
        }
    }
}
