package com.example.englishapp;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.text.Font;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class TranslateParagraph implements Initializable {
    @FXML
    private ImageView translateFromIcon;
    @FXML
    private ImageView translateToIcon;
    private String lang_from = "en";
    private String lang_to = "vi";
    @FXML
    private TextArea toBeTranslatedTextArea;
    @FXML
    private MenuButton Menu_detectLanguage = new MenuButton("English");
    @FXML
    private MenuItem english1 = new MenuItem("English");
    @FXML
    private MenuItem vietnamese1 = new MenuItem("VietNamese");
    @FXML
    private MenuItem chinese1 = new MenuItem("Chinese");
    @FXML
    private MenuItem japanese1 = new MenuItem("Japanese");
    @FXML
    private MenuItem french1 = new MenuItem("French");
    @FXML
    private MenuButton Menu_OutputLanguage = new MenuButton("Vietnamese");
    @FXML
    private MenuItem english2 = new MenuItem("English");
    @FXML
    private MenuItem vietnamese2 = new MenuItem("VietNamese");
    @FXML
    private MenuItem chinese2 = new MenuItem("Chinese");
    @FXML
    private MenuItem japanese2 = new MenuItem("Japanese");
    @FXML
    private MenuItem french2 = new MenuItem("French");
    @FXML
    private TextArea resultTextArea;

    public void initializeMenu_Button() {
        Menu_detectLanguage.getItems().addAll(english1,vietnamese1,chinese1,japanese1,french1);
        english1.setOnAction(event -> {
            Menu_detectLanguage.setText("English");
            lang_from = "en";
        });
        vietnamese1.setOnAction(event -> {
            Menu_detectLanguage.setText("Vietnamese");
            lang_from = "vi";
        });
        chinese1.setOnAction(event -> {
            Menu_detectLanguage.setText("Chinese");
            lang_from = "zh";
        });
        japanese1.setOnAction(event -> {
            Menu_detectLanguage.setText("Japanese");
            lang_from = "ja";
        });
        french1.setOnAction(event -> {
            Menu_detectLanguage.setText("French");
            lang_from = "fr";
        });
        Menu_OutputLanguage.getItems().addAll(english2,vietnamese2,chinese2,japanese2,french2);
        english2.setOnAction(event -> {
            Menu_OutputLanguage.setText("English");
            lang_to = "en";
        });
        vietnamese2.setOnAction(event -> {
            Menu_OutputLanguage.setText("Vietnamese");
            lang_to = "vi";
        });
        chinese2.setOnAction(event -> {
            Menu_OutputLanguage.setText("Chinese");
            lang_to = "zh";
        });
        japanese2.setOnAction(event -> {
            Menu_OutputLanguage.setText("Japanese");
            lang_to = "ja";
        });
        french2.setOnAction(event -> {
            Menu_OutputLanguage.setText("French");
            lang_to = "fr";
        });

    }
    public void paragraph_speech1(){
        String para = toBeTranslatedTextArea.getText();
        TextToSpeech.pronounce(para,lang_from);
    }
    public void paragraph_speech2(){
        String para = resultTextArea.getText();
        TextToSpeech.pronounce(para,lang_to);
    }
    public void translate() {
        String para = toBeTranslatedTextArea.getText();
        String result = DictionaryManagement.TranslateParagraph(para, lang_from, lang_to);
        result = result.replace("&#39;", "'");
        resultTextArea.setFont(new Font(14));
        resultTextArea.setText(result);
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeMenu_Button();
        toBeTranslatedTextArea.setPrefRowCount(5);
        toBeTranslatedTextArea.setWrapText(true);
        resultTextArea.setPrefRowCount(5);
        resultTextArea.setWrapText(true);
        toBeTranslatedTextArea.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER && event.isShiftDown()) {
                toBeTranslatedTextArea.appendText("\n");
            } else if (event.getCode() == KeyCode.ENTER) {
                translate();
            }
        });
    }
}
