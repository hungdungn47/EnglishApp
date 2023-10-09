package com.example.englishapp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;


public class HelloApplication extends Application {
    public static Stage myStage;
    @Override
    public void start(Stage stage) throws IOException {
        try {
            DictionaryManagement.readDataFromHtml();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        myStage = stage;
        Parent fxmlLoader = FXMLLoader.load(getClass().getResource("login.fxml"));
        Scene scene = new Scene(fxmlLoader);
        stage.setTitle("Dictionary application");
        stage.setScene(scene);
        stage.show();
    }

    public void changeScene(String fxml) throws IOException {
        Parent pane = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(fxml)));
        myStage.getScene().setRoot(pane);
    }

    public static void main(String[] args) {
        launch();
    }
}