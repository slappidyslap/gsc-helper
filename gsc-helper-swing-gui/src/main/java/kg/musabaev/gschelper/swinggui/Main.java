package kg.musabaev.gschelper.swinggui;

import kg.musabaev.gschelper.api.report.ReportService;
import kg.musabaev.gschelper.core.adapter.GscMetricsXlsxTableAdapter;
import kg.musabaev.gschelper.core.auth.GscAuthLocalFileCredentialsLoader;
import kg.musabaev.gschelper.core.auth.GscAuthLocalFileDataStoreFactoryLoader;
import kg.musabaev.gschelper.core.gsc.GscApiAuthorizationCodeFlowBuilder;
import kg.musabaev.gschelper.core.gsc.collector.GscMetricsBetweenDateCollectorImpl;
import kg.musabaev.gschelper.core.report.ReportServiceImpl;
import kg.musabaev.gschelper.core.table.xlsx.ApachePoiXlsxBuilder;
import kg.musabaev.gschelper.swinggui.model.ReportGenerateFormModel;
import kg.musabaev.gschelper.swinggui.model.ReportGenerateModel;
import kg.musabaev.gschelper.swinggui.presenter.ReportGeneratePresenter;
import kg.musabaev.gschelper.swinggui.util.Constants;
import kg.musabaev.gschelper.swinggui.util.Paths;
import kg.musabaev.gschelper.swinggui.view.ReportGenerateView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URISyntaxException;
import java.nio.file.Path;

import static java.lang.System.currentTimeMillis;

public class Main {
    private static final Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        Thread.currentThread().setName("Main");

        long startTime = currentTimeMillis();

        ReportGenerateView gui = buildReportView();
        gui.showFrame();

        long duration = currentTimeMillis() - startTime;
        log.info("Программа запущено за {} секунд", duration / 1000.0);
    }

    private static ReportService buildReportService() {
        Path credentialsFile;
        try {
            credentialsFile = java.nio.file.Paths.get(Main.class.getClassLoader().getResource("credentials.json").toURI());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        return new ReportServiceImpl(
            new GscMetricsBetweenDateCollectorImpl(
                new GscApiAuthorizationCodeFlowBuilder(
                    Constants.APP_NAME,
                    new GscAuthLocalFileDataStoreFactoryLoader(Paths.DATA_STORE_FOLDER),
                    new GscAuthLocalFileCredentialsLoader(credentialsFile) // credentials.json
                )),
            new GscMetricsXlsxTableAdapter(
                new ApachePoiXlsxBuilder("Метрики")));
    }

    private static ReportGenerateView buildReportView() {
        ReportGenerateView view = new ReportGenerateView();
        ReportGeneratePresenter presenter = new ReportGeneratePresenter(
            view,
            new ReportGenerateModel(),
            buildReportService());
//        view.setReportGeneratePresenter(presenter);
        return view;
    }
}
