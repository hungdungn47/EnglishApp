package com.example.englishapp;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.util.Objects;

public class add_or_delete {
    @FXML
    private TextField add_word_target_textField;
    @FXML
    private TextField add_word_explain_textField;
    @FXML
    private TextField word_delete_textField;
    public void add_word(ActionEvent event) {
        if (!Objects.equals(add_word_target_textField.getText(), "") && !Objects.equals(add_word_explain_textField.getText(), "")) {
            DictionaryManagement.addWord(add_word_target_textField.getText(), add_word_explain_textField.getText());
        }
    }
    public void delete_word(ActionEvent event) {
        String word_delete = word_delete_textField.getText();
        DictionaryManagement.delete_word(word_delete);
    }
    public void return_back(ActionEvent event) throws IOException {
        Application app = new Application();
        app.changeScene("main-screen.fxml");
    }
}
