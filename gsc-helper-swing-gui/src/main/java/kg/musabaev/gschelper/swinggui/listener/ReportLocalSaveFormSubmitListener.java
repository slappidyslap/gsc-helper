package kg.musabaev.gschelper.swinggui.listener;

import kg.musabaev.gschelper.swinggui.component.ReportLocalSaveForm;

import java.nio.file.Path;
import java.time.LocalDate;

/**
 * Слушатель нажатие кнопки подтверждения в форме {@link ReportLocalSaveForm}
 */
@FunctionalInterface
public interface ReportLocalSaveFormSubmitListener {

    /**
     * @param startDate дата начала отчетного периода
     * @param endDate дата конца отчетного периода
     * @param savePath путь, по которому нужно сохранить сгенерированный отчет
     */
    void formSubmitted(LocalDate startDate, LocalDate endDate, Path savePath);
}
