package com.example.englishapp;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.*;

public class StudyPage implements Initializable {
    private List<String> todayWords;
    @FXML
    private Label label;
    @FXML
    private ListView<String> dailyWordListView;
    @FXML
    private WebView definitionWebView;
    @FXML
    private VBox container;
    private String selectedWord;
    @FXML
    private ContextMenu delete = new ContextMenu();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        changeToDailyWords();
        selectedWord = todayWords.get(0);
        String res = DictionaryManagement.dictionaryLookup(selectedWord, 2);
        definitionWebView.getEngine().loadContent(res, "text/html");
        dailyWordListView.getSelectionModel().selectedItemProperty().addListener((observableValue, s, t1) -> {
            selectedWord = dailyWordListView.getSelectionModel().getSelectedItem();
            if (selectedWord != null) {
                container.setVisible(true);
                String result = DictionaryManagement.dictionaryLookup(selectedWord, 2);
                WebEngine webEngine = definitionWebView.getEngine();
                webEngine.loadContent(result, "text/html");
            }
        });

        dailyWordListView.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    // Set text and apply rounded border styling
                    setText(item);
                    setStyle("-fx-border-radius: 10; -fx-background-radius: 10; " +
                            "-fx-font-size: 15;");
                }
            }
        });
        dailyWordListView.setFixedCellSize(50);
    }

    public void vocabGame() throws IOException {
        Application app = new Application();
        app.changeScene("start_game2.fxml");
    }

    public void changeToDailyWords() {
        delete.getItems().clear();
        DailyRandomWordGenerator gen = new DailyRandomWordGenerator(DictionaryManagement.enViDic.getWordList());
        todayWords = gen.getDailyWords();
        changeWordList();
        label.setText("Today's new words");
    }

    public void changeToRecentWords() {
        delete.getItems().clear();
        String filePath = "src/main/resources/data/RecentWords/" + Login.getUsername() + "RecentWords.txt";
        todayWords = Utils.readWordListFromFile(filePath);
        Collections.reverse(todayWords);
        changeWordList();
        label.setText("Recent words");
    }

    public void changeToFavoriteWords() {
        delete.getItems().clear();
        String filePath = "src/main/resources/data/favoriteWords/" + Login.getUsername() + "FavoriteWord.txt";
        todayWords = Utils.readWordListFromFile(filePath);
        Collections.reverse(todayWords);
        changeWordList();
        label.setText("Favorite words");

        MenuItem menuItem = new MenuItem("Delete");
        delete.getItems().add(menuItem);
        dailyWordListView.setCellFactory(lv -> {
            ListCell<String> cell = new ListCell<>();

            cell.emptyProperty().addListener((obs, wasEmpty, isNowEmpty) -> {
                if (isNowEmpty) {
                    cell.setContextMenu(null);
                } else {
                    cell.setContextMenu(delete);
                }
            });
            menuItem.setOnAction(event -> {
                String word = dailyWordListView.getSelectionModel().getSelectedItem();
                delete_favoriteWord(word);
                changeWordList();
            });
            cell.textProperty().bind(cell.itemProperty());
            return cell;
        });

    }
    public void delete_favoriteWord(String word){
        todayWords.remove(word);
        String fileName = Login.getUsername() + "FavoriteWord.txt";
        DictionaryCommandLine.changeFavoriteWords(fileName, todayWords);
    }
    private void changeWordList() {
        dailyWordListView.getItems().clear();
        dailyWordListView.getItems().addAll(todayWords);
        if (!todayWords.isEmpty()) {
            selectedWord = todayWords.get(0);
            String res = DictionaryManagement.dictionaryLookup(selectedWord, 2);
            definitionWebView.getEngine().loadContent(res, "text/html");
        } else {
            definitionWebView.getEngine().loadContent("");
        }
    }
}
