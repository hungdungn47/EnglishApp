package com.example.englishapp;

import com.example.englishapp.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import javax.swing.*;
import java.io.*;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
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
    private Map<String, String> passwordsMap = new HashMap<>();
    static final String DB_URL = "jdbc:mysql://sql12.freesqldatabase.com/sql12662519";
    static final String USER = "sql12662519";
    static final String PASS = "EmA6Z8XLRD";
    public void signup(ActionEvent event) throws IOException {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String passwordConfirm = confirmPasswordField.getText();
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {

            // Kiểm tra xem tên người dùng mới đã tồn tại hay chưa
            String checkUserQuery = "SELECT * FROM userAccounts WHERE username = ?";
            PreparedStatement checkStatement = conn.prepareStatement(checkUserQuery);
            checkStatement.setString(1, username);
            ResultSet resultSet = checkStatement.executeQuery();

            if (resultSet.next()) {
                wrongUsername.setText("Username already exists!");
                wrongConfirm.setText("");
            } else if(!passwordConfirm.equals(password)) {
                wrongUsername.setText("");
                wrongConfirm.setText("Password and confirm password doesn't match!");
            } else {
                String insertQuery = "INSERT INTO userAccounts (username, password) VALUES (?, ?)";
                PreparedStatement insertStatement = conn.prepareStatement(insertQuery);
                insertStatement.setString(1, username);
                insertStatement.setString(2, password);

                int rowsInserted = insertStatement.executeUpdate();
                if (rowsInserted > 0) {
                    System.out.println("Tài khoản đã được đăng ký thành công!");
                }
                Application app = new Application();
                app.changeScene("login.fxml");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void back(ActionEvent event) throws IOException {
        Application app = new Application();
        app.changeScene("login.fxml");
    }
    private void readData() {
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
