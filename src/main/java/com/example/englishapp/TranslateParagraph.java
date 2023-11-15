package com.example.englishapp;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class TranslateParagraph implements Initializable {
    @FXML
    private ImageView translateFromIcon;
    @FXML
    private ImageView translateToIcon;
    private final Image vietnamese = new Image(new File("src/main/resources/images/vietnam.png").toURI().toString());
    private final Image english = new Image(new File("src/main/resources/images/england.png").toURI().toString());
    private int language_options;
    @FXML
    private TextArea toBeTranslatedTextArea;
    @FXML
    private TextArea resultTextArea;

    public void changeLanguage(ActionEvent event) {

        language_options = 1 - language_options;
        if (language_options == 1) {
            translateFromIcon.setImage(vietnamese);
            translateToIcon.setImage(english);
        } else {
            translateFromIcon.setImage(english);
            translateToIcon.setImage(vietnamese);
        }
    }
    public void translate() {
        String para = toBeTranslatedTextArea.getText();
        String result = DictionaryManagement.dictionaryLookup(para, language_options);
        result = result.replace("&#39;", "'");
        resultTextArea.setText(result);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        toBeTranslatedTextArea.setPrefRowCount(5);
        toBeTranslatedTextArea.setWrapText(true);
        resultTextArea.setPrefRowCount(5);
        resultTextArea.setWrapText(true);
        toBeTranslatedTextArea.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER && event.isShiftDown()) {
                // Handle Shift+Enter (newline without submitting)
                // You can customize this behavior as needed
                toBeTranslatedTextArea.appendText("\n");
            } else if (event.getCode() == KeyCode.ENTER) {
                // Handle Enter key pressed
                translate();
            }
        });
    }
}
