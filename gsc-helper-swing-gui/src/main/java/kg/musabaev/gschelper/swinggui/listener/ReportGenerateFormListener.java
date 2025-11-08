package kg.musabaev.gschelper.swinggui.listener;

import kg.musabaev.gschelper.swinggui.component.ReportGenerateForm;
import kg.musabaev.gschelper.swinggui.presenter.ReportGeneratePresenter;

import java.io.File;
import java.time.LocalDate;

/**
 * Слушатель нажатие кнопки подтверждения в форме {@link ReportGenerateForm}
 */
public interface ReportGenerateFormListener {

    /**
     * Обрабатывает нажатие на кнопку подтверждения в форме {@link ReportGenerateForm}.
     * <p>
     * Передает выбранные пользователем параметры в метод
     * {@link ReportGeneratePresenter#onClickGenerateReport(LocalDate, LocalDate, File)}.
     *
     * @param startDate дата начала отчетного периода
     * @param endDate дата конца отчетного периода
     * @param savePath путь, по которому нужно сохранить сгенерированный отчет
     */
    void generateReportClicked(LocalDate startDate, LocalDate endDate, File savePath);
}
