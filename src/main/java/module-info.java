module com.example.englishapp {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires com.almasb.fxgl.all;
    requires json.simple;
    requires java.desktop;
    requires jsapi;
    requires freetts;
    requires javafx.media;

    opens com.example.englishapp to javafx.fxml;
    exports com.example.englishapp;
}