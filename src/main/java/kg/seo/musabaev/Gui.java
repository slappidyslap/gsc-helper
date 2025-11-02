package kg.seo.musabaev;

import ch.qos.logback.classic.LoggerContext;
import com.formdev.flatlaf.fonts.roboto.FlatRobotoFont;
import com.formdev.flatlaf.themes.FlatMacLightLaf;
import de.milchreis.uibooster.UiBooster;
import de.milchreis.uibooster.model.UiBoosterOptions;
import net.miginfocom.swing.MigLayout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import raven.datetime.component.date.DatePicker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;

import static java.lang.String.format;
import static kg.seo.musabaev.Constants.APP_HOME;
import static raven.datetime.component.date.DatePicker.DateSelectionMode.BETWEEN_DATE_SELECTED;

public class Gui {

    private static final Logger log = LoggerFactory.getLogger(Gui.class);
    private static final UiBooster uiBooster = new UiBooster(
            new UiBoosterOptions(new FlatMacLightLaf(), null, null)
    );
    private final JFrame frame;
    private final JPanel formPanel;
    private final JLabel dateRangeLabel;
    private final JTextField dateRangeInput;

    private final JLabel savePathLabel;
    private final JTextField savePathInput;

    private final DatePicker datePicker;
    private final JButton submitButton;

    private LocalDate startDate;
    private LocalDate endDate;
    private String savePath;

    public Gui() {
        FlatMacLightLaf.setup();
        FlatRobotoFont.install();
        UIManager.put("defaultFont", new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 16));
        UIManager.put("TextComponent.arc", 7);
        UIManager.put("Button.arc", 7);
        Image icon = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/icon.png"));

        // Настройка компонентов
        frame = new JFrame();
        frame.setIconImage(icon);
        frame.setJMenuBar(buildMenuBar());
        frame.setTitle("GSC Helper");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(650, 200);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new MigLayout("ins 12 13 6 13", "[grow]", "[][grow][]"));

        formPanel =  new JPanel();
        formPanel.setLayout(new MigLayout("fillx, ins 2", "[][grow]"));

        dateRangeLabel = new JLabel();
        dateRangeLabel.setText("Диапазон дат:");

        dateRangeInput = new JTextField();
        datePicker = new DatePicker();
        datePicker.setDateSelectionMode(BETWEEN_DATE_SELECTED);
        datePicker.setUsePanelOption(true);
        datePicker.setCloseAfterSelected(true);
        datePicker.setEditor(dateRangeInput);

        savePathLabel = new JLabel("Путь сохранения отчета:");

        savePathInput = new JTextField();

        JToolBar toolBar = new JToolBar();
        JButton btn = new JButton("ы");
        toolBar.add(btn);
        btn.addActionListener(e -> {
            File file = uiBooster.showFileSelection(
                    format("Отчет GSC за период %s и %s.xlsx", startDate, endDate),
                    "Файл Excel", "xlsx");
            savePathInput.setText(file.getAbsolutePath());
        });
        savePathInput.putClientProperty("JTextField.trailingComponent", toolBar);

        submitButton = new JButton();
        submitButton.setText("Принять");

        // Настройка слушателей
        submitButton.addActionListener($ -> {
            SwingUtilities.invokeLater(() -> submitButton.setEnabled(false));
            try {
                startDate = datePicker.getSelectedDateRange()[0];
                endDate = datePicker.getSelectedDateRange()[1];
                savePath = savePathInput.getText();
            } catch (NullPointerException e) {
                showErrorDialog("Введите диапазон дат");
                return;
            } catch (Exception e) {
                showExceptionDialog(e);
                return;
            } finally {
                submitButton.setEnabled(true);
            }
            GoogleSearchConsole searchConsole = new GoogleSearchConsole();
            GscReportGenerator reportGenerator = new GscReportGenerator(searchConsole);
            reportGenerator.generateAndSave(startDate, endDate, savePath);
            /*gsc.start()
                    .thenRun(() -> SwingUtilities.invokeLater(() -> {
                        File dir = uiBooster.showFileSelection(format("Отчет GSC за период %s и %s.xlsx", startDate, endDate), "Файл Excel", "xlsx");
                        gsc.saveExcelFile(dir);
                        submitButton.setEnabled(true);
                    }))
                    .exceptionally(ex -> {
                        SwingUtilities.invokeLater(() -> {
                            if (ex.getCause() instanceof CredentialsFileNotFoundException) {
                                showErrorDialog("Файл credentials.json не найден");
                            } else if (ex.getCause() instanceof GscSitesNotFoundException) {
                                showErrorDialog("Сайты для обработки отсутствуют");
                            } else {
                                log.error("", ex);
                                showExceptionDialog(ex);
                            }
                            submitButton.setEnabled(true);
                        });
                        return null;
                    });
            submitButton.setEnabled(true);*/
        });
//        addFocusListenersToComponents(frame);

        // Добавляем компоненты
        formPanel.add(dateRangeLabel);
        formPanel.add(dateRangeInput, "growx, pushx, wrap");

        formPanel.add(savePathLabel);
        formPanel.add(savePathInput, "growx, pushx, wrap");

        frame.add(formPanel, "growx, wrap");
        frame.add(new JLabel(""), "grow, wrap");

        frame.add(submitButton, "growx, wrap");

        SwingUtilities.invokeLater(() -> {
            frame.setVisible(true);
            frame.getContentPane().requestFocusInWindow();
        });
    }

    private void showExceptionDialog(Throwable e) {
        uiBooster.showException(
                "Произошла ошибка",
                "Сообщение исключения",
                (Exception) e
        );
    }

    private void showErrorDialog(String s) {
        uiBooster.showErrorDialog(s, "Ошибка");
    }

    private void addFocusListenersToComponents(Container container) {
        for (Component component : container.getComponents()) {
            if (component instanceof Container) {
                addFocusListenersToComponents((Container) component);
            }
            component.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (!dateRangeInput.hasFocus()) return; // Если текстовое поле уже не в фокусе, выходим

                    // Проверяем, что клик был не по самому текстовому полю
                    if (component != dateRangeInput) {
                        dateRangeInput.setFocusable(false);
                        dateRangeInput.setFocusable(true);
                        dateRangeLabel.requestFocusInWindow();
                    }
                }
            });
        }
    }

    private JMenuBar buildMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Полезное");

        JMenuItem logsMenu = new JMenuItem("Открыть логи");
        logsMenu.addActionListener(e -> {
            LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
            String curTimestamp = context.getProperty("currentTimestamp");
            try {
                Desktop.getDesktop().open(new File(
                        System.getProperty("user.home") + File.separator +
                                ".gsc-helper" + File.separator +
                                "logs" + File.separator +
                                format("log-%s.txt", curTimestamp)));
            } catch (IOException ex) {
                log.error("", ex);
                showErrorDialog("Из-за неизвестных мне причин я не могу открыть файл лога");
            }
        });

        JMenuItem deleteFolderTokensMenu = new JMenuItem("Удалить папку tokens");
        deleteFolderTokensMenu.addActionListener(e -> {
            if (uiBooster.showConfirmDialog("Вы уверены?", "Удалить папку tokens")) {
                try {
                    Files.delete(Paths.get(APP_HOME.getAbsolutePath(), "tokens", "StoredCredential"));
                    Files.delete(Paths.get(APP_HOME.getAbsolutePath(), "tokens"));
                    log.info("Файл tokens был удален");
                } catch (IOException ex) {
                    log.error("", ex);
                    showErrorDialog("Из-за неизвестных мне причин я не могу удалить папку tokens");
                }
            }
        });

        menu.add(deleteFolderTokensMenu);
        menu.add(logsMenu);
        menuBar.add(menu);
        return menuBar;
    }

    public static void main(String[] args) {
        log.info("Программа запущена");
        /*PrintStream printStream = new PrintStream(new NullOutputStream()) {
            @Override
            public void println(String x) {
                Preconditions.checkNotNull(x);
                if (x.contains("https://accounts.google.com/o/oauth2/auth")) {
                    log.debug("==========LINK TO LOG IN TO GOOGLE==========");
                    log.debug(x);
                    log.debug("==========LINK TO LOG IN TO GOOGLE==========");
                } else {
                    log.debug(x);
                }
            }
        };
        System.setOut(printStream);*/
        new Gui();
        Thread.setDefaultUncaughtExceptionHandler(new GlobalExceptionHandler()); // https://stackoverflow.com/questions/73207244/java-swing-swingworker-uncaught-exceptions-are-not-getting-picked-up-by-thread
    }
}
