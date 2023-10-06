package com.example.englishapp;

import com.almasb.fxgl.entity.action.Action;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.AccessibleAction;
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
        HelloApplication app = new HelloApplication();
        String username = usernameTextField.getText();
        String password = passwordTextField.getText();
        if(!passwordsMap.containsKey(username)) {
            wrongPasswordLabel.setText("This username has not been signed up");
        } else {
            if(passwordsMap.get(username).equals(password)) {
                app.changeScene("hello-view.fxml");
            } else {
                wrongPasswordLabel.setText("Wrong password. Please try again");
            }
        }
    }

    public void signup(ActionEvent event) throws IOException {
        HelloApplication app = new HelloApplication();
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
