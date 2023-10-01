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

import java.io.FileNotFoundException;
import java.net.URL;
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
    private String selectedWord;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            DictionaryManagement.insertFromFile();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        word_list_listView.getItems().addAll(Dictionary.get_target_list());
        word_list_listView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                selectedWord = word_list_listView.getSelectionModel().getSelectedItem();
                word_definition_label.setText(Dictionary.get_definition(selectedWord));
            }
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
        DictionaryManagement.addWord(add_word_target_textField.getText(), add_word_explain_textField.getText());
        word_list_listView.getItems().add(add_word_target_textField.getText());
    }
}