package com.example.englishapp;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import javax.swing.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Signup {
    @FXML
    private TextField usernameField;
    @FXML
    private TextField passwordField;
    @FXML
    private TextField confirmPasswordField;
    @FXML
    private Label wrongConfirm;
    @FXML
    private Label wrongUsername;

    public void signup(ActionEvent event) throws IOException {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String passwordConfirm = confirmPasswordField.getText();

        boolean checkUsername = true;

        Scanner sc = new Scanner(new File("src/main/resources/data/passwords.txt"));
        while (sc.hasNext()) {
            String line = sc.nextLine();
            String[] arrOfStr = line.split(",");
            if (username.equals(arrOfStr[0])) {
                checkUsername = false;
                break;
            }
        }
        if (!checkUsername) {
            wrongUsername.setText("Username already exists");
            wrongConfirm.setText("");
        } else if (!passwordConfirm.equals(password)) {
            wrongUsername.setText("");
            wrongConfirm.setText("Confirm again please");
        } else {
            wrongUsername.setText("Signed up successfully");
            wrongConfirm.setText("Signed up successfully");
            FileWriter fw = null;
            try {
                fw = new FileWriter("src/main/resources/data/passwords.txt", true);
                // đường dẫn tương đối để lưu file
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            try {
                fw.write(username + "," + password + "\n");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            try {
                fw.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            Application app = new Application();
            app.changeScene("login.fxml");
        }
    }
    public void back(ActionEvent event) throws IOException {
        Application app = new Application();
        app.changeScene("login.fxml");
    }
}
