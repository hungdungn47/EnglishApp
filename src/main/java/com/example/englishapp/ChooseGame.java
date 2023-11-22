package com.example.englishapp;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ChooseGame implements Initializable {
    @FXML
    private VBox snakeGameButton;
    @FXML
    private VBox multipleChoiceGameButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        snakeGameButton.setOnMouseClicked(mouseEvent -> {
            boolean check = Utils.getFavoriteWords(Login.getUsername()).isEmpty();
            Application app = new Application();
            if (!check) {
                try {
                    app.changeScene("game.fxml");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("warning_game_snake.fxml"));
                Parent root1 = null;
                try {
                    root1 = fxmlLoader.load();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                Stage stage = new Stage();
                stage.setTitle("Warning!");
                stage.setScene(new Scene(root1));
                stage.show();
            }
        });
        multipleChoiceGameButton.setOnMouseClicked(mouseEvent -> {
            Application app = new Application();
            try {
                app.changeScene("start_game2.fxml");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
