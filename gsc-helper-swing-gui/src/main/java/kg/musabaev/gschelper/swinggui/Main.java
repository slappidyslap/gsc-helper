package kg.musabaev.gschelper.swinggui;

import kg.musabaev.gschelper.api.report.ReportService;
import kg.musabaev.gschelper.core.adapter.GscMetricsXlsxTableAdapter;
import kg.musabaev.gschelper.core.auth.GscAuthLocalFileCredentialsLoader;
import kg.musabaev.gschelper.core.auth.GscAuthLocalFileDataStoreFactoryLoader;
import kg.musabaev.gschelper.core.gsc.GscApiAuthorizationCodeFlowBuilder;
import kg.musabaev.gschelper.core.gsc.collector.GscMetricsBetweenDateCollectorImpl;
import kg.musabaev.gschelper.core.report.ReportServiceImpl;
import kg.musabaev.gschelper.core.table.xlsx.ApachePoiXlsxBuilder;
import kg.musabaev.gschelper.swinggui.exception.GlobalExceptionHandler;
import kg.musabaev.gschelper.swinggui.listener.impl.MenuBarItemsClickService;
import kg.musabaev.gschelper.swinggui.model.ReportLocalSaveModel;
import kg.musabaev.gschelper.swinggui.presenter.ReportLocalSavePresenter;
import kg.musabaev.gschelper.swinggui.util.Constants;
import kg.musabaev.gschelper.swinggui.util.Paths;
import kg.musabaev.gschelper.swinggui.view.ReportLocalSaveView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URISyntaxException;
import java.nio.file.Path;

import static java.lang.System.currentTimeMillis;

public class Main {

    private static final Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        evaluateDuration(() -> {
            setupThreadSettings();

            ReportLocalSavePresenter presenter = buildReportLocalSavePresenter();
            presenter.start();
        });
    }

    private static void evaluateDuration(Runnable runnable) {
        long startTime = currentTimeMillis();

        runnable.run();

        long duration = currentTimeMillis() - startTime;
        log.info("Программа запущено за {} секунд", duration / 1000.0);
    }

    private static void setupThreadSettings() {
        Thread.currentThread().setName("Main");
        Thread.setDefaultUncaughtExceptionHandler(new GlobalExceptionHandler());
    }

    @SuppressWarnings("ALL")
    private static Path getPathToGoogleCredentials() {
        try {
            return java.nio.file.Paths.get(
                    Main.class.getClassLoader().getResource("credentials.json").toURI());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

    }

    private static ReportService buildReportService() {
        return new ReportServiceImpl(
            new GscMetricsBetweenDateCollectorImpl(
                new GscApiAuthorizationCodeFlowBuilder(
                    Constants.APP_NAME,
                    new GscAuthLocalFileDataStoreFactoryLoader(Paths.DATA_STORE_FOLDER),
                    new GscAuthLocalFileCredentialsLoader(getPathToGoogleCredentials()))),
            new GscMetricsXlsxTableAdapter(
                new ApachePoiXlsxBuilder("Метрики")));
    }

    private static ReportLocalSavePresenter buildReportLocalSavePresenter() {
        ReportLocalSaveView view = new ReportLocalSaveView();
        ReportLocalSaveModel model = new ReportLocalSaveModel();
        return new ReportLocalSavePresenter(
            view,
            model,
            new MenuBarItemsClickService(view, model),
            buildReportService());
    }
}
