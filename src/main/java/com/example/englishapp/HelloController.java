package com.example.englishapp;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.text.TextAlignment;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class HelloController implements Initializable {
    @FXML
    private TextField search_box;
    @FXML
    private Button translate_button;
    @FXML
    private Label word_definition_label;
    @FXML
    private ListView<String> word_list_listView;
    private String selectedWord;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // wrapping the label
        word_definition_label.setWrapText(true);
        // add words to list view
        word_list_listView.getItems().addAll(Dictionary.get_target_list());
        word_list_listView.getSelectionModel().selectedItemProperty().addListener((observableValue, s, t1) -> {
            selectedWord = word_list_listView.getSelectionModel().getSelectedItem();
            word_definition_label.setText(DictionaryManagement.dictionaryLookup(selectedWord));
        });
        search_box.textProperty().addListener((observable, oldValue, newValue) -> {
            word_list_listView.getItems().clear();
            word_list_listView.getItems().addAll(DictionaryManagement.searchHint(newValue));
        });
    }
    public void translate(ActionEvent event) {
        String target = search_box.getText();
        word_definition_label.setText(DictionaryManagement.dictionaryLookup(target));
    }

    public void add_delete (ActionEvent event) throws IOException {
        HelloApplication app = new HelloApplication();
        app.changeScene("addordelete.fxml");
    }
    public void play_game(ActionEvent event) throws IOException {
        HelloApplication app = new HelloApplication();
        app.changeScene("game.fxml");
    }
}