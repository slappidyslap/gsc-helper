package kg.musabaev.gschelper.swinggui.presenter;

import kg.musabaev.gschelper.api.report.ReportService;
import kg.musabaev.gschelper.core.gsc.exception.CredentialsFileNotFoundException;
import kg.musabaev.gschelper.core.gsc.exception.GscSitesNotFoundException;
import kg.musabaev.gschelper.core.table.exception.LocalFileNotFoundException;
import kg.musabaev.gschelper.core.table.output.file.local.TableDataOutputLocalFileSaver;
import kg.musabaev.gschelper.swinggui.listener.DateRangeChangeListener;
import kg.musabaev.gschelper.swinggui.listener.MenuBarItemsClickListener;
import kg.musabaev.gschelper.swinggui.listener.ReportLocalSaveFormSubmitListener;
import kg.musabaev.gschelper.swinggui.listener.SavePathChangeListener;
import kg.musabaev.gschelper.swinggui.model.ReportLocalSaveModel;
import kg.musabaev.gschelper.swinggui.util.XlsxFiles;
import kg.musabaev.gschelper.swinggui.view.ReportLocalSavePresenterViewContract;

import java.nio.file.Path;
import java.time.LocalDate;
import java.util.Optional;
import java.util.regex.Matcher;

public class ReportLocalSavePresenter {

    private final ReportLocalSavePresenterViewContract view;
    private final ReportLocalSaveModel model;
    private final MenuBarItemsClickListener menuBarItemsClickListener;
    private final ReportService reportService;

    public ReportLocalSavePresenter(
        ReportLocalSavePresenterViewContract view,
        ReportLocalSaveModel model,
        MenuBarItemsClickListener menuBarItemsClickListener,
        ReportService reportService
    ) {
        this.view = view;
        this.model = model;
        this.menuBarItemsClickListener = menuBarItemsClickListener;
        this.reportService = reportService;

        attachListeners();
    }

    public void start() {
        view.showFrame();
    }

    private void attachListeners() {
        view.setMenuBarItemsClickListener(menuBarItemsClickListener); // делегируем обработка событий сервису
        view.addGenerateReportFormSubmitListener(this::onGenerateReportFormSubmit);
        view.addDateRangeChangeListener(this::onDateRangeChange);
        view.addSavePathChangeListener(this::onSavePathChange);
    }

    // ========= Методы - реализации слушателей =========

    /**
     * Реализация слушателя {@link ReportLocalSaveFormSubmitListener}.
     * <p>
     * При подтверждении формы, выключает кнопку формы на время,
     * генерирует отчет и сохраняет по пути {@code savePath}
     *
     * @param startDate дата начала отчетного периода
     * @param endDate   дата конца отчетного периода
     * @param savePath  путь, по которому нужно сохранить сгенерированный отчет
     */
    private void onGenerateReportFormSubmit(LocalDate startDate, LocalDate endDate, Path savePath) {
        disableFormButtonWhile(() -> {
            byte[] data = reportService.generateReport(startDate, endDate);
            reportService.processReportOutput(
                new TableDataOutputLocalFileSaver.Config(savePath.toFile()), data);
        });
    }

    /**
     * Реализация слушателя {@link DateRangeChangeListener}.
     * <p>
     * При изменении даты, изменяет обновляем модель
     * и при определенных условиях обновляем и путь к сохранению {@link #updateSavePathOnDateRangeChange(String)}}
     *
     * @param startDate          дата начала отчетного периода
     * @param endDate            дата конца отчетного периода
     * @param formattedDateRange отформатированный диапазон дат
     */
    private void onDateRangeChange(LocalDate startDate, LocalDate endDate, String formattedDateRange) {
        model.setStartDate(startDate);
        model.setEndDate(endDate);
        view.setSuggestedFilename(formattedDateRange);

        Optional<Path> updatedSavePath = updateSavePathOnDateRangeChange(formattedDateRange);
        updatedSavePath.ifPresent(sp -> {
            model.setSavePath(sp);
            view.setSavePath(sp);
        });
    }

    /**
     * Реализация слушателя {@link SavePathChangeListener}.
     * <p>
     * При изменении даты, изменяет обновляем модель
     *
     * @param savePath путь к сохранению
     */
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
        // путь к сохранению, но название файла не стандартное,
        // (стандартное название смотрите XlsxFiles.FILENAME_PATTERN),
        // то тогда тоже не меняем название файла
        if (!matcher.matches()) return Optional.empty();

        // Если текст в JTextField есть/если пользователь выбрал
        // путь к сохранению, и название файла совпадает
        // к RegEx XlsxFiles.FILENAME_PATTERN, то меняем название файла
        String updatedFilename = filename.replace(matcher.group(1), formattedDateString);
        Path updatedSavePath = path.resolveSibling(updatedFilename);
        return Optional.of(updatedSavePath);
    }

    /**
     * Отключает кнопку отправки формы на время выполнения {@code runnable}.
     * После завершения выполнения действия (успешного или с исключением)
     * кнопка отправки становится активной.
     *
     * @param runnable runnable
     */
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

    /**
     * Отображает для определённых исключений нормальное диалоговое окно с одним сообщением.
     * Для других исключений отображает диалоговое окно со stack trace.
     * Важно: обрабатывает исключения из других сервисов. Не обрабатывает исключения из Swing
     *
     * @param e пойманное исключение
     */
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
