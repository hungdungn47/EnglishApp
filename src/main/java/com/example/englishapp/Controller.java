package com.example.englishapp;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    @FXML
    private TextField search_box;
    @FXML
    private Label phonetic;
    @FXML
    private Label part_of_speech;
    @FXML
    private Label definition;
    @FXML
    private Label example;
    @FXML
    private ListView<String> word_list_listView;
    @FXML
    private ToggleButton change_language_button;
    private String selectedWord;
    private int language_options;
    // 0: anh - viet
    // 1: viet - anh
    private void updateLabels(Word result) {
        phonetic.setText(result.phonetic);
        part_of_speech.setText("(" + result.partOfSpeech + ")");
        definition.setText("Definition: " + result.definitions.get(0).get("definition"));
        if(result.definitions.get(0).get("example") != null) {
            example.setText("Example: " + result.definitions.get(0).get("example"));
        } else {
            example.setText("");
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        language_options = 0;

        // add words to list view
        word_list_listView.getItems().addAll(Dictionary.get_target_list());
        word_list_listView.getSelectionModel().selectedItemProperty().addListener((observableValue, s, t1) -> {
            selectedWord = word_list_listView.getSelectionModel().getSelectedItem();
            String result = DictionaryManagement.dictionaryLookup(selectedWord, language_options);
            definition.setText(result);
            //updateLabels(result);
        });
        search_box.textProperty().addListener((observable, oldValue, newValue) -> {
            word_list_listView.getItems().clear();
            word_list_listView.getItems().addAll(DictionaryManagement.searchHint(newValue));
        });
    }
    public void translate(ActionEvent event) {
        String target = search_box.getText();
        String result = DictionaryManagement.dictionaryLookup(target, language_options);
        definition.setText(result);

        //updateLabels(result);
    }

    public void add_delete (ActionEvent event) throws IOException {
        Application app = new Application();
        app.changeScene("addordelete.fxml");
    }
    public void play_game(ActionEvent event) throws IOException {
        Application app = new Application();
        app.changeScene("game.fxml");
    }
    public void start_game_2(ActionEvent event) throws IOException {
        Application app = new Application();
        app.changeScene("start_game2.fxml");
    }

    public void changeLanguage(ActionEvent event) {

        language_options = 1 - language_options;
        if(language_options == 1) {
            change_language_button.setText("Vietnamese - English");
        } else {
            change_language_button.setText("English - Vietnamese");
        }
    }
}