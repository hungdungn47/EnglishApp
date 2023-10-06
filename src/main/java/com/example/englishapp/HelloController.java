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
    @FXML
    private TextField add_word_target_textField;
    @FXML
    private TextField add_word_explain_textField;
    @FXML
    private TextField word_delete_textField;

    private String selectedWord;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // wrapping the label
        word_definition_label.setWrapText(true);

        // import data
        try {
            DictionaryManagement.readDataFromHtml();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        // add words to list view
        word_list_listView.getItems().addAll(Dictionary.get_target_list());
        word_list_listView.getSelectionModel().selectedItemProperty().addListener((observableValue, s, t1) -> {
            selectedWord = word_list_listView.getSelectionModel().getSelectedItem();
            word_definition_label.setText(Dictionary.get_definition(selectedWord));
        });
        search_box.textProperty().addListener((observable, oldValue, newValue) -> {
            word_list_listView.getItems().clear();
            word_list_listView.getItems().addAll(DictionaryManagement.searchHint(newValue));
        });
    }

    public void translate(ActionEvent event) {
        String target = search_box.getText();
        word_definition_label.setText(Dictionary.get_definition(target));
    }
    public void add_word(ActionEvent event) {
        if(!Objects.equals(add_word_target_textField.getText(), "") && !Objects.equals(add_word_explain_textField.getText(), "")) {
            DictionaryManagement.addWord(add_word_target_textField.getText(), add_word_explain_textField.getText());
            word_list_listView.getItems().add(add_word_target_textField.getText());
        }
    }
    public void delete_word(ActionEvent event){
        String word_delete = word_delete_textField.getText();
        DictionaryManagement.delete_word(word_delete);
        word_list_listView.getItems().clear();
        word_list_listView.getItems().addAll(Dictionary.get_target_list());
    }
}