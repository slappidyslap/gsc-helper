package kg.musabaev.gschelper.swinggui.component;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import kg.musabaev.gschelper.swinggui.model.ReportGenerateFormModel;
import kg.musabaev.gschelper.swinggui.util.XlsxFiles;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.io.File;
import java.util.regex.Matcher;

import static java.lang.String.format;

public class FileChooserInput extends JTextField {

    private final ReportGenerateFormModel model;

    private final JToolBar toolBar;
    private final JButton button;

    public FileChooserInput(ReportGenerateFormModel model) {
        this.model = model;

        this.toolBar = new JToolBar();
        this.button = new JButton();

        setupUi();
        setupListeners();
    }

    public File savePath() {
        return new File(super.getText());
    }

    protected void setupUi() {
        super.setEditable(false);
        button.setIcon(new FlatSVGIcon("file-chooser-icon.svg", 0.7F));
        toolBar.add(button);
        super.putClientProperty("JTextField.trailingComponent", toolBar);
    }

    private void setupListeners() {
        //  model -> TextField.text (file)
        model.addPropertyChangeListener(event -> {
            if (model.SAVE_PATH_FIELD_NAME.equals(event.getPropertyName())) {
                super.setText((String) event.getNewValue());
            }
        });

        button.addActionListener(this::onClickButton);

        model.addPropertyChangeListener(this::onDateRangeModelChanged);
    }


    /**
     * При клике кнопки внутри TextField, открываем окно выбора файла.
     * Подставляем в названии файла выбранную дату из модели {@link ReportGenerateFormModel}.
     */
    private void onClickButton(ActionEvent event) {
        String dateRangeAsString = model.dateRange();

        JFileChooser chooser = new JFileChooser();
        // Если диапазон дат выбран, то подставляем шаблон названия
        if (!dateRangeAsString.isEmpty())
            chooser.setSelectedFile(new File(format(XlsxFiles.FILENAME_TEMPLATE, dateRangeAsString)));
        // Иначе подставляем шаблон, где не выбранный диапазон дат будет заменен
        else
            chooser.setSelectedFile(new File(XlsxFiles.NULL_FILENAME_TEMPLATE));
        chooser.setFileFilter(new FileNameExtensionFilter("Файл Excel", "xlsx"));

        int result = chooser.showOpenDialog(button);
        if (result == JFileChooser.APPROVE_OPTION) {
            model.setSavePath(Matcher.quoteReplacement(chooser.getSelectedFile().getAbsolutePath()));
        }
    }

    /**
     * Подписываемся на изменения даты в модели {@link ReportGenerateFormModel}.
     * Когда дата изменяется, при определенных условиях подставляем новое название файла
     * @param event событие об изменении <b>всех</b> полей в модели {@link ReportGenerateFormModel}
     */
    private void onDateRangeModelChanged(PropertyChangeEvent event) {
        if (model.DATE_RANGE_FIELD_NAME.equals(event.getPropertyName())) {
            // Если текста в JTextField нет/если пользователь не еще
            // выбрал путь к сохранению, то не меняем название файла
            String path = super.getText().trim();
            if (path.isEmpty()) return;

            Matcher savePathMatcher = XlsxFiles.FILENAME_PATTERN.matcher(path);
            // Если текст в JTextField есть/если пользователь выбрал
            // путь к сохранению, но название файла кастомное
            // (оригинальный смотрите XlsxFiles.FILENAME_PATTERN),
            // то тогда тоже не меняем кастомное название файла
            if (!savePathMatcher.matches()) return;

            // Если текст в JTextField есть/если пользователь выбрал
            // путь к сохранению, и название файла матчится
            // к regex XlsxFiles.FILENAME_PATTERN, то меняем название файла
            String updatedSavePath = path.replace(savePathMatcher.group(1), model.dateRange());
            model.setSavePath(updatedSavePath);
        }
    }

    public ReportGenerateFormModel model() {
        return model;
    }
}
