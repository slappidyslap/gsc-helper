package kg.musabaev.gschelper.swinggui.listener.impl;

import kg.musabaev.gschelper.swinggui.listener.ReportGenerateFormListener;
import kg.musabaev.gschelper.swinggui.presenter.ReportGeneratePresenter;

import java.io.File;
import java.time.LocalDate;

public class ReportGenerateFormListenerImpl implements ReportGenerateFormListener {

    private final ReportGeneratePresenter presenter;

    public ReportGenerateFormListenerImpl(ReportGeneratePresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void generateReportClicked(LocalDate startDate, LocalDate endDate, File savePath) {
        presenter.onClickGenerateReport(startDate, endDate, savePath);
    }
}
