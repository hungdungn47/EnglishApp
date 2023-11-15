package com.example.englishapp;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class StudyPage implements Initializable {
    private List<String> todayWords;
    @FXML
    private ListView<String> dailyWordListView;
    @FXML
    private WebView definitionWebView;
    private String selectedWord;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        DailyRandomWordGenerator gen = new DailyRandomWordGenerator(DictionaryManagement.englishWords);
        todayWords = gen.getDailyWords();
        dailyWordListView.getItems().addAll(todayWords);
        selectedWord = todayWords.get(0);
        String res = DictionaryManagement.dictionaryLookup(selectedWord, 0);
        WebEngine webEngine1 = definitionWebView.getEngine();
        webEngine1.loadContent(res, "text/html");

        dailyWordListView.getSelectionModel().selectedItemProperty().addListener((observableValue, s, t1) -> {
            selectedWord = dailyWordListView.getSelectionModel().getSelectedItem();
            if(selectedWord != null) {
                String result = DictionaryManagement.dictionaryLookup(selectedWord, 0);
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
                    setStyle("-fx-border-radius: 10; -fx-background-radius: 10;");
                }
            }
        });
        dailyWordListView.setFixedCellSize(50);
    }
    public void vocabGame() throws IOException {
        Application app = new Application();
        app.changeScene("start_game2.fxml");
    }
}
