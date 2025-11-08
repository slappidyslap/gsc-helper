package kg.musabaev.gschelper.swinggui.listener.impl;

import kg.musabaev.gschelper.swinggui.listener.ReportGenerateFormListener;
import kg.musabaev.gschelper.swinggui.presenter.ReportGeneratePresenter;
import kg.musabaev.gschelper.swinggui.component.ReportGenerateForm;
import kg.musabaev.gschelper.swinggui.view.ReportGenerateView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.time.LocalDate;

/**
 * Реализация {@link ReportGenerateFormListener}, отвечающая за обработку действий пользователя
 * в форме генерации отчета (ReportGenerateView).
 */
public class ReportGenerateFormListenerImpl implements ReportGenerateFormListener {

    private static final Logger log = LoggerFactory.getLogger(ReportGenerateFormListenerImpl.class);
    private final ReportGenerateView view;

    public ReportGenerateFormListenerImpl(ReportGenerateView view) {
        this.view = view;
    }

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
    @Override
    public void generateReportClicked(LocalDate startDate, LocalDate endDate, File savePath) {
        view.onClickGenerateReport(startDate, endDate, savePath);
        log.info(
            "Запрос на создания отчета за период {} - {} отправлена." +
                "Отчет будет сохранен в {}",
            savePath, endDate, savePath);
    }
}
