package kg.musabaev.gschelper.swinggui.component;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import kg.musabaev.gschelper.swinggui.listener.SavePathChangeListener;
import kg.musabaev.gschelper.swinggui.util.XlsxFiles;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.lang.String.format;

public class SavePathPickerInput extends JTextField {

    private final JToolBar toolBar;
    private final JButton button;
    private final List<SavePathChangeListener> savePathChangeListeners;

    // Данные для отображения (model) будут в этом же классе хранится
    private String suggestedFilename;

    public SavePathPickerInput() {
        this.toolBar = new JToolBar();
        this.button = new JButton();
        this.savePathChangeListeners = new ArrayList<>();

        setupUi();
        setupListeners();
    }

    protected void setupUi() {
        super.setEditable(false);
        button.setIcon(new FlatSVGIcon("file-chooser-icon.svg", 0.7F));
        toolBar.add(button);
        super.putClientProperty("JTextField.trailingComponent", toolBar);
    }

    private void setupListeners() {
        button.addActionListener(this::onClickButton);
    }

    private void onClickButton(ActionEvent event) {
        JFileChooser chooser = new JFileChooser();

        if (super.getText().isEmpty())
            if (suggestedFilename.isEmpty())
                chooser.setSelectedFile(new File(XlsxFiles.NULL_FILENAME_TEMPLATE));
            else
                chooser.setSelectedFile(new File(format(XlsxFiles.FILENAME_TEMPLATE, suggestedFilename)));
        chooser.setFileFilter(new FileNameExtensionFilter("Файл Excel", "xlsx"));

        int result = chooser.showSaveDialog(button);
        if (result == JFileChooser.APPROVE_OPTION) {
            File newSelectedFile = chooser.getSelectedFile();
            super.setText(newSelectedFile.getAbsolutePath());
            fireSavePathChange(newSelectedFile.toPath());
        }
    }

    public Path savePath() {
        return Paths.get(super.getText());
    }

    public void setSavePath(Path savePath) {
        if (super.getText().isEmpty()) return;
        super.setText(savePath.toAbsolutePath().toString());
    }

    public void setSuggestedFilename(String suggestedFilename) {
        this.suggestedFilename = suggestedFilename;
    }

    public void addSavePathChangeListener(SavePathChangeListener l) {
        synchronized (savePathChangeListeners) {
            savePathChangeListeners.add(checkNotNull(l));
        }
    }

    public void removeSavePathChangeListener(SavePathChangeListener l) {
        synchronized (savePathChangeListeners) {
            savePathChangeListeners.remove(checkNotNull(l));
        }
    }

    public void fireSavePathChange(Path newSavePath) {
        synchronized (savePathChangeListeners) {
            for (SavePathChangeListener l : savePathChangeListeners)
                l.savePathChanged(newSavePath);
        }
    }
}
