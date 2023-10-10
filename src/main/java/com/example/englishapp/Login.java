package com.example.englishapp;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.*;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class Login implements Initializable {
    private Map<String, String> passwordsMap = new HashMap<>();
    @FXML
    private TextField usernameTextField;
    @FXML
    private PasswordField passwordTextField;
    @FXML
    private Button loginButton;
    @FXML
    private Label wrongPasswordLabel;
    public void login(ActionEvent event) throws IOException {
        Application app = new Application();
        String username = usernameTextField.getText();
        String password = passwordTextField.getText();
        if(!passwordsMap.containsKey(username)) {
            wrongPasswordLabel.setText("No such username!");
        } else {
            if(passwordsMap.get(username).equals(password)) {
                app.changeScene("hello-view.fxml");
            } else {
                wrongPasswordLabel.setText("Wrong password!");
            }
        }
    }

    public void signup(ActionEvent event) throws IOException {
        Application app = new Application();
        app.changeScene("signup.fxml");
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        File file = new File("src/main/resources/data/passwords.txt");
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(file));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        String line;
        while(true) {
            try {
                if ((line = br.readLine()) == null) break;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            String[] row = line.split(",");
            passwordsMap.put(row[0], row[1]);
        }
    }
}
