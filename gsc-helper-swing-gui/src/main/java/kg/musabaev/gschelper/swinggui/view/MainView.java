package kg.musabaev.gschelper.swinggui.view;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.fonts.roboto.FlatRobotoFont;
import kg.musabaev.gschelper.swinggui.component.ReportLocalSaveForm;
import kg.musabaev.gschelper.swinggui.component.dialog.ExceptionDialog;
import kg.musabaev.gschelper.swinggui.component.dialog.WarningDialog;
import kg.musabaev.gschelper.swinggui.exception.AwtEventQueueExceptionHandler;
import kg.musabaev.gschelper.swinggui.exception.GlobalExceptionHandler;
import kg.musabaev.gschelper.swinggui.listener.DateRangeChangeListener;
import kg.musabaev.gschelper.swinggui.listener.ReportLocalSaveFormSubmitListener;
import kg.musabaev.gschelper.swinggui.listener.SavePathChangeListener;
import kg.musabaev.gschelper.swinggui.listener.MenuBarItemsClickListener;
import kg.musabaev.gschelper.swinggui.view.contract.MenuBarPresenterViewContract;
import kg.musabaev.gschelper.swinggui.view.contract.ReportLocalSavePresenterViewContract;

import javax.swing.*;
import java.awt.*;
import java.nio.file.Path;

public class MainView extends JFrame {

    private final JMenuBar menuBarViewWrapper;
    private final ReportLocalSaveForm form;

    public MainView(JMenuBar menuBarViewWrapper) {
        setupLaf();

        this.menuBarViewWrapper = menuBarViewWrapper;
        this.form = new ReportLocalSaveForm();

        setupAppIcon();
        setupMenuBar();
        setupUi();
        setupUncaughtExceptionHandler();
    }

    private void setupUi() {
        super.setTitle("GSC Helper");
        super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        super.setSize(650, 200);
        super.setLocationRelativeTo(null);

        super.add(form);
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
        super.setJMenuBar(menuBarViewWrapper);
    }

    private void setupUncaughtExceptionHandler() {
        Thread.setDefaultUncaughtExceptionHandler(
            new GlobalExceptionHandler());
        Toolkit.getDefaultToolkit().getSystemEventQueue().push(
            new AwtEventQueueExceptionHandler());
    }
}
