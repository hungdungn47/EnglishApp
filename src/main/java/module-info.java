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

    opens com.example.englishapp to javafx.fxml;
    exports com.example.englishapp;
    exports com.example.englishapp.Util;
    opens com.example.englishapp.Util to javafx.fxml;
    exports com.example.englishapp.Game;
    opens com.example.englishapp.Game to javafx.fxml;
    exports com.example.englishapp.Page;
    opens com.example.englishapp.Page to javafx.fxml;
}