package kg.musabaev.gschelper.swinggui.listener;

import kg.musabaev.gschelper.swinggui.component.ReportGenerateForm;

import java.nio.file.Path;
import java.time.LocalDate;

/**
 * Слушатель нажатие кнопки подтверждения в форме {@link ReportGenerateForm}
 */
public interface ReportGenerateFormSubmitListener {

    /**
     * Обрабатывает нажатие на кнопку подтверждения в форме {@link ReportGenerateForm}.
     * <p>
     *
     * @param startDate дата начала отчетного периода
     * @param endDate дата конца отчетного периода
     * @param savePath путь, по которому нужно сохранить сгенерированный отчет
     */
    void formSubmitted(LocalDate startDate, LocalDate endDate, Path savePath);
}
