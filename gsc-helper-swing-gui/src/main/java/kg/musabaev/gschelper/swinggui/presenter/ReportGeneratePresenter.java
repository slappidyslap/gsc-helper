package kg.musabaev.gschelper.swinggui.presenter;

import kg.musabaev.gschelper.api.report.ReportService;
import kg.musabaev.gschelper.core.gsc.exception.CredentialsFileNotFoundException;
import kg.musabaev.gschelper.core.gsc.exception.GscSitesNotFoundException;
import kg.musabaev.gschelper.core.table.exception.LocalFileNotFoundException;
import kg.musabaev.gschelper.swinggui.model.ReportGenerateModel;
import kg.musabaev.gschelper.swinggui.util.XlsxFiles;
import kg.musabaev.gschelper.swinggui.view.ReportGenerateView;

import java.nio.file.Path;
import java.time.LocalDate;
import java.util.Optional;
import java.util.regex.Matcher;

public class ReportGeneratePresenter {

    private final ReportGenerateView view;
    private final ReportGenerateModel model;
    private final ReportService reportService;

    public ReportGeneratePresenter(
        ReportGenerateView view,
        ReportGenerateModel model,
        ReportService reportService
    ) {
        this.view = view;
        this.model = model;
        this.reportService = reportService;

        attachListeners();
    }

    private void attachListeners() {
        view.addGenerateReportFormSubmitListener(this::onGenerateReportFormSubmit);
        view.addDateRangeChangeListener(this::onDateRangeChange);
        view.addSavePathChangeListener(this::onSavePathChange);
    }

    // ========= Методы - реализации слушателей =========

    private void onGenerateReportFormSubmit(LocalDate startDate, LocalDate endDate, Path savePath) {
//        disableFormButtonWhile(() -> {
//            byte[] data = reportService.generateReport(startDate, endDate);
//            reportService.processReportOutput(
//                new TableDataOutputLocalFileSaver.Config(savePath.toFile()), data);
//        });
    }

    private void onDateRangeChange(LocalDate startDate, LocalDate endDate, String formattedDateString) {
        model.setStartDate(startDate);
        model.setEndDate(endDate);

        Optional<Path> updatedSavePath = updateSavePathOnDateRangeChange(formattedDateString);
        updatedSavePath.ifPresent(sp -> {
            model.setSavePath(sp);
            view.setSavePath(sp);
        });
    }

    private void onSavePathChange(Path savePath) {
        model.setSavePath(savePath);
    }

    // ========= Дополнительные методы к реализациям слушателей =========

    private Optional<Path> updateSavePathOnDateRangeChange(String formattedDateString) {
        // Если текста в JTextField нет/если пользователь не еще
        // выбрал путь к сохранению, то не меняем название файла
        Path path = model.savePath();
        if (path == null) return Optional.empty();

        String filename = path.getFileName().toString();
        Matcher matcher = XlsxFiles.FILENAME_PATTERN.matcher(filename);
        // Если текст в JTextField есть/если пользователь выбрал
        // путь к сохранению, но название файла кастомное
        // (оригинальный смотрите XlsxFiles.FILENAME_PATTERN),
        // то тогда тоже не меняем кастомное название файла
        if (!matcher.matches()) return Optional.empty();

        // Если текст в JTextField есть/если пользователь выбрал
        // путь к сохранению, и название файла матчится
        // к regex XlsxFiles.FILENAME_PATTERN, то меняем название файла
        String updatedFilename = filename.replace(matcher.group(1), formattedDateString);
        Path updatedSavePath = path.resolveSibling(updatedFilename);
        return Optional.of(updatedSavePath);
    }

    private void disableFormButtonWhile(Runnable runnable) {
        try {
            view.disableSubmitButton();

            runnable.run();

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
