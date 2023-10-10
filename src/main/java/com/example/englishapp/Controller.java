package com.example.englishapp;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

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
    private String selectedWord;
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

        // add words to list view
        word_list_listView.getItems().addAll(Dictionary.get_target_list());
        word_list_listView.getSelectionModel().selectedItemProperty().addListener((observableValue, s, t1) -> {
            selectedWord = word_list_listView.getSelectionModel().getSelectedItem();
            Word result = DictionaryManagement.lookUpWithDictionaryAPI(selectedWord);
            updateLabels(result);
        });
        search_box.textProperty().addListener((observable, oldValue, newValue) -> {
            word_list_listView.getItems().clear();
            word_list_listView.getItems().addAll(DictionaryManagement.searchHint(newValue));
        });
    }
    public void translate(ActionEvent event) {
        String target = search_box.getText();
        Word result = DictionaryManagement.lookUpWithDictionaryAPI(target);
        updateLabels(result);
    }

    public void add_delete (ActionEvent event) throws IOException {
        Application app = new Application();
        app.changeScene("addordelete.fxml");
    }
    public void play_game(ActionEvent event) throws IOException {
        Application app = new Application();
        app.changeScene("game.fxml");
    }
}