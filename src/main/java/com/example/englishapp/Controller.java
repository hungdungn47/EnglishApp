package com.example.englishapp;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

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
    private Button change_language_button;
    @FXML
    private ImageView favoriteButton;
    @FXML
    private ImageView translateFromIcon;
    @FXML
    private ImageView translateToIcon;
    @FXML
    private Button transParaButton;
    @FXML
    private Button studyButton;
    @FXML
    private Button gameButton;
    @FXML
    private ImageView pronounceButton;
    private final Image vietnamese = new Image(new File("src/main/resources/images/vietnam.png").toURI().toString());
    private final Image english = new Image(new File("src/main/resources/images/england.png").toURI().toString());
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
        pronounceButton.setVisible(false);
        favoriteButton.setVisible(false);
        language_options = 0;
        // add words to list view
        word_list_listView.getItems().addAll(Dictionary.get_target_list());
        // add favorite words to list
        getFavoriteWords();
        word_list_listView.getSelectionModel().selectedItemProperty().addListener((observableValue, s, t1) -> {
            pronounceButton.setVisible(true);
            favoriteButton.setVisible(true);
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
        Tooltip swap = new Tooltip("Swap language");
        Tooltip study = new Tooltip("Study");
        Tooltip game = new Tooltip("Game");
        Tooltip translatePara = new Tooltip("Translate paragraph");

        swap.setShowDelay(javafx.util.Duration.millis(10));
        study.setShowDelay(javafx.util.Duration.millis(10));
        game.setShowDelay(javafx.util.Duration.millis(10));
        translatePara.setShowDelay(javafx.util.Duration.millis(10));

        Tooltip.install(change_language_button, swap);
        Tooltip.install(transParaButton, translatePara);
        Tooltip.install(studyButton, study);
        Tooltip.install(gameButton, game);
    }

    public void translate(ActionEvent event) {
        pronounceButton.setVisible(true);
        favoriteButton.setVisible(true);
        selectedWord = search_box.getText();
        String result = DictionaryManagement.dictionaryLookup(selectedWord, language_options);
        definition.setText(result);
    }

    public void add() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("add.fxml"));
        Parent root1 = (Parent) fxmlLoader.load();
        Stage stage = new Stage();
//        stage.setX(500);
//        stage.setY(200);
        stage.setScene(new Scene(root1));
        stage.show();
    }
    public void delete() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("delete.fxml"));
        Parent root1 = (Parent) fxmlLoader.load();
        Stage stage = new Stage();
//        stage.setX(500);
//        stage.setY(200);
        stage.setScene(new Scene(root1));
        stage.show();
    }
    public void play_game() throws IOException {
        Application app = new Application();
        app.changeScene("game.fxml");
    }

    public void start_game_2() throws IOException {
        Application app = new Application();
        app.changeScene("start_game2.fxml");
    }

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

    public void pronounce() {
        if (selectedWord != null) {
            if(language_options == 0) {
                TextToSpeech.pronounce(selectedWord);
            } else {
                TextToSpeech.pronounce(DictionaryManagement.dictionaryLookup(selectedWord, language_options));
            }
        }
    }

    public void addToFavorite(MouseEvent mouseEvent) {
        System.out.println("favor");
        if(favoriteButton.getImage() == redHeart) {
            String fileName = Login.getUsername() + "FavoriteWord.txt";
            if (selectedWord != null && favoriteWords.contains(selectedWord)) {
                favoriteWords.remove(selectedWord);
                DictionaryCommandLine.changeFavoriteWords(fileName, favoriteWords);
                favoriteButton.setImage(blankHeart);
            }
        } else {
            String fileName = Login.getUsername() + "FavoriteWord.txt";
            if (selectedWord != null && !favoriteWords.contains(selectedWord)) {
                favoriteWords.add(selectedWord);
                DictionaryCommandLine.changeFavoriteWords(fileName, favoriteWords);
                favoriteButton.setImage(redHeart);
            }
        }
    }
}