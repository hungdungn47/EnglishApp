package com.example.englishapp;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.FileWriter;
import java.io.IOException;

public class Signup {
    @FXML
    private TextField usernameField;
    @FXML
    private TextField passwordField;
    @FXML
    private TextField confirmPasswordField;
    @FXML
    private Label wrongConfirm;

    public void signup(ActionEvent event) throws IOException {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String passwordConfirm = confirmPasswordField.getText();

        if(!passwordConfirm.equals(password)) {
            wrongConfirm.setText("Confirm again please");
        } else {
            wrongConfirm.setText("Signed up successfully");
            FileWriter fw = null;
            try {
                fw = new FileWriter("src/main/resources/data/passwords.txt", true);
                // đường dẫn tương đối để lưu file
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            try {
                fw.write( username+ "," + password + "\n");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            try {
                fw.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            HelloApplication app = new HelloApplication();
            app.changeScene("login.fxml");
        }
    }
}
