package com.example.englishapp;

import com.example.englishapp.Util.Dictionary;
import com.example.englishapp.Util.DictionaryCommandLine;
import com.example.englishapp.Util.DictionaryManagement;
import com.example.englishapp.Util.Login;
import com.example.englishapp.Util.TextToSpeech;
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
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.util.*;
import java.util.logging.Level;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import java.util.logging.Logger;

public class Controller implements Initializable {
    public static final int EN_TO_VI = 0;
    public static final int VI_TO_EN = 1;
    @FXML
    private WebView definitionWebView;
    @FXML
    private BorderPane bp;
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private TextField search_box;
    @FXML
    private ListView<String> word_list_listView;
    @FXML
    private Button changeLanguageButton;
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
    private Button logOutButton;
    private ImageView addbutton;
    @FXML
    private ImageView deletebutton;
    @FXML
    private ImageView change_lang;
    @FXML
    private ImageView pronounceButton;
    private final Image vietnamese = new Image(new File("src/main/resources/images/vietnam.png").toURI().toString());
    private final Image english = new Image(new File("src/main/resources/images/england.png").toURI().toString());
    private final Image blankHeart = new Image(new File("src/main/resources/images/love.png").toURI().toString());
    private final Image redHeart = new Image(new File("src/main/resources/images/heart.png").toURI().toString());
    private String selectedWord;
    private int languageOptions;
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
        languageOptions = EN_TO_VI;

        word_list_listView.getItems().addAll(Dictionary.get_target_list());

        getFavoriteWords();
        word_list_listView.getSelectionModel().selectedItemProperty().addListener((observableValue, s, t1) -> {
            pronounceButton.setVisible(true);
            favoriteButton.setVisible(true);
            selectedWord = word_list_listView.getSelectionModel().getSelectedItem();
            if(selectedWord != null) {
                if (!favoriteWords.contains(selectedWord)) {
                    favoriteButton.setImage(blankHeart);
                } else {
                    favoriteButton.setImage(redHeart);
                }
                String result = DictionaryManagement.dictionaryLookup(selectedWord, languageOptions);
                WebEngine webEngine = definitionWebView.getEngine();
                webEngine.loadContent(result, "text/html");
            }
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

        Tooltip.install(changeLanguageButton, swap);
        Tooltip.install(transParaButton, translatePara);
        Tooltip.install(studyButton, study);
        Tooltip.install(gameButton, game);
        word_list_listView.setCellFactory(param -> new ListCell<>() {
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
        word_list_listView.setFixedCellSize(30);
    }

    public void translate(ActionEvent event) {
        pronounceButton.setVisible(true);
        favoriteButton.setVisible(true);
        selectedWord = search_box.getText();
        String result = DictionaryManagement.dictionaryLookup(selectedWord, languageOptions);
        WebEngine webEngine = definitionWebView.getEngine();
        webEngine.loadContent(result, "text/html");
    }

    public void translateParagraph(ActionEvent event) {
        loadPage("translateParagraph");
    }
    private void loadPage(String page) {
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource(page + ".fxml"));
        } catch (IOException e) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, e);
        }
        bp.setCenter(root);
    }
    public void dictionaryPage(ActionEvent event) {
        bp.setCenter(anchorPane);
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

    public void snakeGame() throws IOException {
        Application app = new Application();
        app.changeScene("game.fxml");
    }

    public void vocabGame() throws IOException {
        Application app = new Application();
        app.changeScene("start_game2.fxml");
    }

    public void changeLanguage(ActionEvent event) {

        languageOptions = 1 - languageOptions;
        if (languageOptions == VI_TO_EN) {
            translateFromIcon.setImage(vietnamese);
            translateToIcon.setImage(english);
        } else {
            translateFromIcon.setImage(english);
            translateToIcon.setImage(vietnamese);
        }
    }

    public void pronounce() {
        if (selectedWord != null) {
            if (DictionaryManagement.isEnglish(selectedWord)) {
                TextToSpeech.pronounce(selectedWord, "en");
            } else {
                System.out.println(selectedWord);
                TextToSpeech.pronounce(selectedWord, "vi");
            }
        }
    }

    public void addToFavorite(MouseEvent mouseEvent) {
        System.out.println("favor");
        if (favoriteButton.getImage() == redHeart) {
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

    public void logOut(ActionEvent actionEvent) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Logout");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to logout?");

        String textColor = "#8d2aa4";
        alert.getDialogPane().lookup(".content.label").setStyle("-fx-text-fill: " + textColor + ";");

        InputStream inputStream = getClass().getResourceAsStream("/images/close.png");
        Image image = new Image(inputStream);
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(30);
        imageView.setFitHeight(30);
        alert.setGraphic(imageView);

        if (alert.showAndWait().get() == ButtonType.OK) {
            Application application = new Application();
            application.changeScene("login.fxml");
        }
    }
}