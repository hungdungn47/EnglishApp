package com.example.englishapp;

import javafx.fxml.FXMLLoader;
import javafx.scene.ImageCursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.image.Image;

import java.io.File;
import java.io.IOException;
import java.util.Objects;


public class Application extends javafx.application.Application {
    public static Stage myStage;
    private static Image logo = new Image(new File("src/main/resources/images/logo.png").toURI().toString());
    public void changeScene(String fxml) throws IOException {
        Parent pane = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(fxml)));
        myStage.getScene().setCursor(new ImageCursor(new Image(new File("src/main/resources/data/cursor.png").toURI().toString())));
        myStage.getScene().setRoot(pane);
    }
    public Scene getScene() throws IOException {
        return myStage.getScene();
    }
    @Override
    public void start(Stage stage) throws IOException {
        try {
            DictionaryManagement.insertFromFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Parent fxmlLoader = FXMLLoader.load(getClass().getResource("login.fxml"));
        Scene scene = new Scene(fxmlLoader);
        stage.setTitle("Dictionary application");
        stage.setResizable(false);
        stage.setScene(scene);
        scene.setCursor(new ImageCursor(new Image(new File("src/main/resources/images/cursor.png").toURI().toString())));
        stage.getIcons().add(logo);
        stage.show();
        myStage = stage;
    }

    public static void main(String[] args) {
        launch();
    }
}