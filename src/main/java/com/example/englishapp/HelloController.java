package com.example.englishapp;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ResourceBundle;

public class HelloController implements Initializable {
    @FXML
    private Label word_definition_label;
    @FXML
    private ListView<String> word_list_listView;
    private String selectedWord;
    private String[] data = {"hello","hi", "bye"};

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
    }
}