package kg.musabaev.gschelper.swinggui.view;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.fonts.roboto.FlatRobotoFont;
import kg.musabaev.gschelper.swinggui.component.ExceptionDialog;
import kg.musabaev.gschelper.swinggui.component.ReportGenerateForm;
import kg.musabaev.gschelper.swinggui.component.WarningDialog;
import kg.musabaev.gschelper.swinggui.presenter.ReportGeneratePresenter;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.time.LocalDate;

import static com.google.common.base.Preconditions.checkNotNull;

public class ReportGenerateView extends JFrame implements ReportGenerateForm.Listener {

//    private final ReportViewModel viewModel;
    private ReportGeneratePresenter presenter;

    private final ReportGenerateForm form;

    public ReportGenerateView() {
        setupLaf();

        this.form = new ReportGenerateForm();
        form.setListener(this);

        setupAppIcon();
        setupMenuBar();
        setupUi();
    }

    @Override
    public void generateReportClicked(LocalDate startDate, LocalDate endDate, File savePath) {
        presenter.onClickGenerateReport(startDate, endDate, savePath);
    }

    public void disableSubmitButton() {
        form.submitButton().setEnabled(false);
    }

    public void enableSubmitButton() {
        form.submitButton().setEnabled(true);
    }

    public void showWarningDialog(String message) {
        WarningDialog.show(message);
    }

    public void showExceptionDialog(Exception e) {
        ExceptionDialog.show(e);
    }

    private void setupUi() {
        super.setTitle("GSC Helper");
        super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        super.setSize(650, 200);
        super.setLocationRelativeTo(null);

        super.add(form);
    }

    public void showFrame() {
        SwingUtilities.invokeLater(() -> {
            super.pack();
            SwingUtilities.updateComponentTreeUI(this);
            super.setVisible(true);
            super.getContentPane().requestFocusInWindow();
        });
    }

    private void setupLaf() {
        FlatDarkLaf.setup();
        FlatRobotoFont.install();
        UIManager.put("defaultFont", new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 15));
        UIManager.put("TextComponent.arc", 7);
        UIManager.put("Button.arc", 7);
    }

    private void setupAppIcon() {
        Image icon = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/icon.png"));
        super.setIconImage(icon);
    }

    private void setupMenuBar() {
//        super.setJMenuBar(new MenuBar(viewModel));
    }

    public void setReportGeneratePresenter(ReportGeneratePresenter presenter) {
        checkNotNull(presenter);
        this.presenter = presenter;
    }

    private void setupUncaughtExceptionHandler() {
        Thread.setDefaultUncaughtExceptionHandler((thread, throwable) ->
                ExceptionDialog.show(throwable));
    }
}
