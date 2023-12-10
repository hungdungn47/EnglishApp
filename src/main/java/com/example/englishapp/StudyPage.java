package com.example.englishapp;

import com.example.englishapp.dictionary.DictionaryManagement;
import com.example.englishapp.util.DailyRandomWordGenerator;
import com.example.englishapp.util.Login;
import com.example.englishapp.util.TextToSpeech;
import com.example.englishapp.util.Utils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

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
    private Button deleteButton;
    @FXML
    private Button pronounceButton;
    private String selectedWord;
    @FXML
    private ContextMenu delete = new ContextMenu();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        deleteButton.setVisible(false);
        changeToDailyWords();
        selectedWord = todayWords.get(0);
        String res = DictionaryManagement.dictionaryLookup(selectedWord, 2);
        definitionWebView.getEngine().loadContent(res, "text/html");
        dailyWordListView.getSelectionModel().selectedItemProperty().addListener((observableValue, s, t1) -> {
            selectedWord = dailyWordListView.getSelectionModel().getSelectedItem();
            if(selectedWord != null) {
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

    public void changeToDailyWords() {
        pronounceButton.setVisible(true);
        deleteButton.setVisible(false);
        delete.getItems().clear();
        DailyRandomWordGenerator gen = new DailyRandomWordGenerator(DictionaryManagement.enViDic.getWordList());
        todayWords = gen.getDailyWords();
        changeWordList();
        label.setText("Today's new words");
    }

    public void changeToRecentWords() {
        deleteButton.setVisible(true);
        delete.getItems().clear();
        todayWords = Utils.getRecentWords(Login.getUsername());
        changeWordList();
        if(todayWords.isEmpty()) {
            label.setText("You haven't looked up any word recently!");
            pronounceButton.setVisible(false);
            deleteButton.setVisible(false);
        } else {
            label.setText("Recent words");
            pronounceButton.setVisible(true);
            deleteButton.setVisible(true);
        }

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
                todayWords.remove(selectedWord);
                Utils.removeRecentWord(Login.getUsername(), selectedWord);
                if(todayWords.isEmpty()) {
                    label.setText("You haven't looked up any word recently!");
                    pronounceButton.setVisible(false);
                    deleteButton.setVisible(false);
                }
                changeWordList();
            });
            cell.textProperty().bind(cell.itemProperty());
            return cell;
        });
        deleteButton.setOnAction(event -> {
            todayWords.remove(selectedWord);
            Utils.removeRecentWord(Login.getUsername(), selectedWord);
            changeWordList();
        });
    }

    public void changeToFavoriteWords() {
        deleteButton.setVisible(true);
        delete.getItems().clear();
        todayWords = Utils.getFavoriteWords(Login.getUsername());
        Collections.reverse(todayWords);
        changeWordList();
        if(todayWords.isEmpty()) {
            label.setText("There's no favorite word!");
            pronounceButton.setVisible(false);
            deleteButton.setVisible(false);
        } else {
            label.setText("Favorite words");
            pronounceButton.setVisible(true);
            deleteButton.setVisible(true);
        }

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
                todayWords.remove(selectedWord);
                Controller.removeFavoriteWord(selectedWord);
                changeWordList();
            });
            cell.textProperty().bind(cell.itemProperty());
            return cell;
        });
        deleteButton.setOnAction(event -> {
            todayWords.remove(selectedWord);
            Controller.removeFavoriteWord(selectedWord);
            changeWordList();
        });
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
    public void pronounce() {
        if (DictionaryManagement.isEnglish(selectedWord)) {
            TextToSpeech.pronounce(selectedWord, "en");
        } else {
            System.out.println(selectedWord);
            TextToSpeech.pronounce(selectedWord, "vi");
        }
    }
}
