package com.example.englishapp;
import java.sql.*;

import com.example.englishapp.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.*;
import java.net.URL;
import java.util.*;

public class Login {
    @FXML
    private TextField usernameTextField;
    @FXML
    private PasswordField passwordTextField;
    @FXML
    private Label wrongPasswordLabel;
    private static String username;
    private static String password;
    static final String DB_URL = "jdbc:mysql://sql12.freesqldatabase.com/sql12662519";
    static final String USER = "sql12662519";
    static final String PASS = "EmA6Z8XLRD";
    public void login(ActionEvent event) throws IOException {
        Application app = new Application();
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
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
