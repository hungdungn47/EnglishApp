package com.example.englishapp;

import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.web.HTMLEditor;
import javafx.stage.Stage;

import java.awt.*;
import java.io.*;
import java.net.URL;
import java.util.*;
import java.util.List;
import java.util.logging.Level;

import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.scene.effect.ColorAdjust;

import java.util.logging.Logger;

import javafx.util.Duration;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Whitelist;

public class Controller implements Initializable {
    public static final int EN_TO_VI = 0;
    public static final int VI_TO_EN = 1;
    @FXML
    private Button updateButton;
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
    private Rectangle dictionaryButtonSign;
    @FXML
    private Rectangle transParaButtonSign;
    @FXML
    private Rectangle studyButtonSign;
    @FXML
    private Rectangle gameButtonSign;
    @FXML
    private Rectangle logOutButtonSign;
    @FXML
    private Button transParaButton;
    @FXML
    private Button studyButton;
    @FXML
    private Button gameButton;
    @FXML
    private Button dictionaryButton;
    @FXML
    private HTMLEditor definitionEditor;
    @FXML
    private Button logOutButton;
    @FXML
    private ImageView pronounceButton;
    private final Image vietnamese = new Image(new File("src/main/resources/images/vietnam.png").toURI().toString());
    private final Image english = new Image(new File("src/main/resources/images/england.png").toURI().toString());
    private final Image blankHeart = new Image(new File("src/main/resources/images/love.png").toURI().toString());
    private final Image redHeart = new Image(new File("src/main/resources/images/heart.png").toURI().toString());
    private final Image doneIcon = new Image(new File("src/main/resources/images/check-mark.png").toURI().toString());
    private final Image editIcon = new Image(new File("src/main/resources/images/editing.png").toURI().toString());
    private String selectedWord;
    private int languageOptions;
    private final List<String> favoriteWords = new ArrayList<>();

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
        dictionaryButtonSign.setVisible(false);
        transParaButtonSign.setVisible(false);
        studyButtonSign.setVisible(true);
        gameButtonSign.setVisible(false);
        logOutButtonSign.setVisible(false);
        updateButton.setVisible(false);
        definitionEditor.setVisible(false);
        loadPage("studyPage");
        try {
            DictionaryManagement.readAddedAndDeletedWord();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        pronounceButton.setVisible(false);
        favoriteButton.setVisible(false);
        languageOptions = EN_TO_VI;

        DictionaryManagement.readUpdatedWord(Login.getUsername());
        word_list_listView.getItems().addAll(DictionaryManagement.searchHint("", languageOptions));

        getFavoriteWords();
        word_list_listView.getSelectionModel().selectedItemProperty().addListener((observableValue, s, t1) -> {
            updateButton.setVisible(true);
            pronounceButton.setVisible(true);
            favoriteButton.setVisible(true);
            selectedWord = word_list_listView.getSelectionModel().getSelectedItem();
            if (selectedWord != null) {
                if (!favoriteWords.contains(selectedWord)) {
                    favoriteButton.setImage(blankHeart);
                } else {
                    favoriteButton.setImage(redHeart);
                }
                DictionaryCommandLine.addToRecentList(Login.getUsername(), selectedWord);
                String result = DictionaryManagement.dictionaryLookup(selectedWord, languageOptions);
                WebEngine webEngine = definitionWebView.getEngine();
                webEngine.loadContent(result, "text/html");
                definitionEditor.setHtmlText(result);
            }
        });

        search_box.textProperty().addListener((observable, oldValue, newValue) -> {
            word_list_listView.getItems().clear();
            word_list_listView.getItems().addAll(DictionaryManagement.searchHint(newValue, languageOptions));
        });
        Tooltip dictionary = new Tooltip("Dictionary");
        Tooltip swap = new Tooltip("Swap language");
        Tooltip study = new Tooltip("Study");
        Tooltip game = new Tooltip("Game");
        Tooltip translatePara = new Tooltip("Translate paragraph");

        dictionary.setShowDelay(javafx.util.Duration.millis(10));
        swap.setShowDelay(javafx.util.Duration.millis(10));
        study.setShowDelay(javafx.util.Duration.millis(10));
        game.setShowDelay(javafx.util.Duration.millis(10));
        translatePara.setShowDelay(javafx.util.Duration.millis(10));

        Tooltip.install(dictionaryButton, dictionary);
        Tooltip.install(changeLanguageButton, swap);
        Tooltip.install(transParaButton, translatePara);
        Tooltip.install(studyButton, study);
        Tooltip.install(gameButton, game);

        addScaleTransition(dictionaryButton);
        addScaleTransition(transParaButton);
        addScaleTransition(studyButton);
        addScaleTransition(gameButton);
        addScaleTransition(logOutButton);

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

    public void translate() {
        selectedWord = search_box.getText();
        if(!selectedWord.isEmpty()) {
            DictionaryCommandLine.addToRecentList(Login.getUsername(), selectedWord);
            String result = DictionaryManagement.dictionaryLookup(selectedWord, languageOptions);
            WebEngine webEngine = definitionWebView.getEngine();
            webEngine.loadContent(result, "text/html");
            pronounceButton.setVisible(true);
            favoriteButton.setVisible(true);
        }
    }

    public void translateParagraph() {
        loadPage("translateParagraph");
    }

    private void loadPage(String page) {
        Parent root = null;
        try {
            root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(page + ".fxml")));
        } catch (IOException e) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, e);
        }
        bp.setCenter(root);
    }
    public void dictionaryPage() {
        bp.setCenter(anchorPane);
    }

    public void studyPage() {
        loadPage("studyPage");
    }

    public void add() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("add.fxml"));
        Parent root1 = fxmlLoader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(root1));
        stage.show();
    }

    public void delete() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("delete.fxml"));
        Parent root1 = fxmlLoader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(root1));
        stage.show();
    }

    public void snakeGame() throws IOException {
        String fileName = Login.getUsername() + "FavoriteWord.txt";
        String filePath = "src/main/resources/data/favoriteWords/" + fileName;
        Scanner sc = new Scanner(new File(filePath));
        boolean check = !sc.hasNext();
        Application app = new Application();
        if (!check) {
            app.changeScene("game.fxml");
        } else {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("warning_game_snake.fxml"));
            Parent root1 = fxmlLoader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root1));
            stage.show();
        }
    }

    public void changeLanguage() {

        languageOptions = 1 - languageOptions;
        word_list_listView.getItems().clear();
        word_list_listView.getItems().addAll(DictionaryManagement.searchHint("", languageOptions));
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

    public void addToFavorite() {
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

    public void updateDefinition() {
        ImageView iv = (ImageView) updateButton.getGraphic();
        if (iv.getImage().equals(doneIcon)) {
            String editedContent = definitionEditor.getHtmlText();
            editedContent = editedContent.replaceAll(" contenteditable=\"true\"", "")
                                            .replaceAll(" dir=\"ltr\"", "");

            String filePath = "src/main/resources/data/UpdatedWord/" + Login.getUsername() + "UpdatedWords.txt";
            Utils.exportToFile(filePath, true, selectedWord, editedContent, "");
            definitionWebView.getEngine().loadContent(editedContent);
            definitionEditor.setVisible(false);
            definitionWebView.setVisible(true);
            iv.setImage(editIcon);
        } else {
            definitionWebView.setVisible(false);
            definitionEditor.setVisible(true);
            iv.setImage(doneIcon);
        }
    }

    public void logOut() throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Logout");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to logout?");

        String textColor = "#8d2aa4";
        alert.getDialogPane().lookup(".content.label").setStyle("-fx-text-fill: " + textColor + ";");

        InputStream inputStream = getClass().getResourceAsStream("/images/close.png");
        assert inputStream != null;
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

    private void addScaleTransition(Button btn) {
        List<Rectangle> signs = new ArrayList<>(Arrays.asList(dictionaryButtonSign, transParaButtonSign,
                studyButtonSign, gameButtonSign, logOutButtonSign));
        ScaleTransition hover = new ScaleTransition(Duration.millis(40), btn);
        hover.setToX(1.3);
        hover.setToY(1.3);

        ScaleTransition exit = new ScaleTransition(Duration.millis(40), btn);
        exit.setToX(1);
        exit.setToY(1);

        btn.setOnMouseEntered(event -> {
            hover.play();
        });
        btn.setOnMouseExited(event -> {
            exit.play();
        });
        btn.setOnMousePressed(event -> {
            for(Rectangle sign : signs) {
                sign.setVisible(sign.getId().contains(btn.getId()));
            }
        });
    }
}