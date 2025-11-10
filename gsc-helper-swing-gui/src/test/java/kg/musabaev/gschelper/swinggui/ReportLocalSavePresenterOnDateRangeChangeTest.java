package kg.musabaev.gschelper.swinggui;

import kg.musabaev.gschelper.api.report.ReportService;
import kg.musabaev.gschelper.swinggui.listener.DateRangeChangeListener;
import kg.musabaev.gschelper.swinggui.model.ReportLocalSaveModel;
import kg.musabaev.gschelper.swinggui.presenter.ReportLocalSavePresenter;
import kg.musabaev.gschelper.swinggui.util.XlsxFiles;
import kg.musabaev.gschelper.swinggui.view.contract.ReportLocalSavePresenterViewContract;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.Period;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@SuppressWarnings("unused")
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class ReportLocalSavePresenterOnDateRangeChangeTest {

    @Mock
    private ReportLocalSavePresenterViewContract view;
    @Mock
    private ReportLocalSaveModel model;
    @Mock
    private ReportService service;

    @InjectMocks
    private ReportLocalSavePresenter presenter;

    @Captor
    private ArgumentCaptor<DateRangeChangeListener> dateRangeChangeListenerCaptor;
    @Captor
    private ArgumentCaptor<Path> savePathCaptor;

    private LocalDate startDate;
    private LocalDate endDate;
    private String formattedDateRange;

    @BeforeEach
    void beforeEach() {
        doNothing().when(view).addGenerateReportFormSubmitListener(any());
        doNothing().when(view).addDateRangeChangeListener(any());
        doNothing().when(view).addSavePathChangeListener(any());

        verify(view).addDateRangeChangeListener(dateRangeChangeListenerCaptor.capture());

        startDate = LocalDate.now();
        endDate = LocalDate.now().plus(Period.ofMonths(3));
        formattedDateRange = startDate + " - " + endDate;
    }

    @Test
    public void shouldNotChangeFilename_WhenSavePathDontPicked() {
        fireDateRangeChangeWithCaptor();

        verify(model).setStartDate(eq(startDate));
        verify(model).setEndDate(eq(endDate));
        verify(model, times(0)).setSavePath(any());
        verify(view, times(0)).setSavePath(any());
    }

    @Test
    public void shouldNotChangeFilename_WhenSavePathIsCustom() {
        Path savePath = Paths.get("/home/xlsx/Отчет Google Search Console за 1 год. Проект - GSC Helper");
        when(model.savePath()).thenReturn(savePath);

        fireDateRangeChangeWithCaptor();

        verify(model).setStartDate(eq(startDate));
        verify(model).setEndDate(eq(endDate));
        verify(model, times(0)).setSavePath(any());
        verify(view, times(0)).setSavePath(any());
    }

    @Test
    public void shouldChangeFilename_WhenSavePathPicked() {
        Path savePath = Paths.get("/home/xlsx/" + XlsxFiles.NULL_FILENAME_TEMPLATE);
        when(model.savePath()).thenReturn(savePath);

        fireDateRangeChangeWithCaptor();

        verify(model).setStartDate(eq(startDate));
        verify(model).setEndDate(eq(endDate));
        verify(model, times(1)).setSavePath(any());
        verify(view, times(1)).setSavePath(any());

        verify(model).setSavePath(savePathCaptor.capture());
        Path updatedSavePath = savePathCaptor.getValue();
        String expectedFilename = format(XlsxFiles.FILENAME_TEMPLATE, formattedDateRange);
        String actualFilename = updatedSavePath.getFileName().toString();
        assertThat(actualFilename).isEqualTo(expectedFilename);
    }

    private void fireDateRangeChangeWithCaptor() {
        DateRangeChangeListener listener = dateRangeChangeListenerCaptor.getValue();
        listener.dateRangeChanged(
            startDate,
            endDate,
            formattedDateRange);
    }
}
