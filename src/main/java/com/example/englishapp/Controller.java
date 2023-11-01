package com.example.englishapp;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Scanner;

public class Controller implements Initializable {
    @FXML
    private TextField search_box;
    @FXML
    private Label definition;
    @FXML
    private ListView<String> word_list_listView;
    @FXML
    private ToggleButton change_language_button;
    @FXML
    private ImageView favoriteButton;
    private final Image blankHeart = new Image(new File("src/main/resources/images/heart1.png").toURI().toString());
    private final Image redHeart = new Image(new File("src/main/resources/images/heart2.png").toURI().toString());
    private String selectedWord;
    private int language_options;
    private final List<String> favoriteWords = new ArrayList<>();

    // 0: anh - viet
    // 1: viet - anh
    private void getFavoriteWords() {
        String fileName = Login.getUsername() + "FavoriteWord.txt";
        String filePath = "src/main/resources/data/favoriteWords/" + fileName;
        try {
            Scanner sc = new Scanner(new File(filePath));
            while (sc.hasNextLine()) {
                String tmp = sc.nextLine();
                favoriteWords.add(tmp);
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        language_options = 0;
        // add words to list view
        word_list_listView.getItems().addAll(Dictionary.get_target_list());
        // add favorite words to list
        getFavoriteWords();
        word_list_listView.getSelectionModel().selectedItemProperty().addListener((observableValue, s, t1) -> {
            selectedWord = word_list_listView.getSelectionModel().getSelectedItem();
            if (!favoriteWords.contains(selectedWord)) {
                favoriteButton.setImage(blankHeart);
            } else {
                favoriteButton.setImage(redHeart);
            }
            String result = DictionaryManagement.dictionaryLookup(selectedWord, language_options);
            definition.setText(result);
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
    }

    public void add_delete(ActionEvent event) throws IOException {
        Application app = new Application();
        app.changeScene("addordelete.fxml");
    }

    public void play_game() throws IOException {
        Application app = new Application();
        app.changeScene("game.fxml");
    }

    public void start_game_2(ActionEvent event) throws IOException {
        Application app = new Application();
        app.changeScene("start_game2.fxml");
    }

    public void changeLanguage(ActionEvent event) {

        language_options = 1 - language_options;
        if (language_options == 1) {
            change_language_button.setText("Vietnamese - English");
        } else {
            change_language_button.setText("English - Vietnamese");
        }
    }

    public void pronounce() {
        if (selectedWord != null) {
            TextToSpeech.pronounce(selectedWord);
        }
    }

    public void addToFavorite(MouseEvent mouseEvent) {
        String fileName = Login.getUsername() + "FavoriteWord.txt";
        if (selectedWord != null && !favoriteWords.contains(selectedWord)) {
            favoriteWords.add(selectedWord);
            DictionaryCommandLine.dictionaryExportToFile(fileName, selectedWord);
            favoriteButton.setImage(redHeart);
        }
    }
}