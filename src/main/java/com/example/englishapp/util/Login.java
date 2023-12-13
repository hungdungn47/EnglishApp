package com.example.englishapp.util;
import java.sql.*;

import com.example.englishapp.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.*;

public class Login extends DatabaseConnector {
    @FXML
    private TextField usernameTextField;
    @FXML
    private PasswordField passwordTextField;
    @FXML
    private Label wrongPasswordLabel;
    private static String username;
    private static String password;
    public void login(ActionEvent event) throws IOException {
        Application app = new Application();
        try (Connection conn = getConnection()) {
            username = usernameTextField.getText();
            password = passwordTextField.getText();

            String sql = "SELECT * FROM userAccounts WHERE username = ? AND password = ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, username);
            statement.setString(2, password);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                app.changeScene("main-screen.fxml");
            } else {
                wrongPasswordLabel.setText("Wrong username or password!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static String getUsername() {
        return username;
    }
    public void signup(ActionEvent event) throws IOException {
        Application app = new Application();
        app.changeScene("signup.fxml");
    }
}
