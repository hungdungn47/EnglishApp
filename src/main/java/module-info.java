module com.example.englishapp {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires com.almasb.fxgl.all;
    requires json.simple;
    requires java.desktop;
    requires javafx.media;
    requires java.sql;
    requires javafx.web;
    requires jsoup;

    opens com.example.englishapp to javafx.fxml;
    exports com.example.englishapp;
    exports com.example.englishapp.util;
    opens com.example.englishapp.util to javafx.fxml;
    exports com.example.englishapp.dictionary;
    opens com.example.englishapp.dictionary to javafx.fxml;
    exports com.example.englishapp.game;
    opens com.example.englishapp.game to javafx.fxml;
}