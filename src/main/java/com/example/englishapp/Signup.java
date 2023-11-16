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
    // Các thông tin kết nối cơ sở dữ liệu
    static final String DB_URL = "jdbc:mysql://sql12.freesqldatabase.com/sql12662519"; // Thay thế bằng URL của cơ sở dữ liệu MySQL
    static final String USER = "sql12662519"; // Thay thế bằng tên người dùng MySQL
    static final String PASS = "EmA6Z8XLRD"; // Thay thế bằng mật khẩu người dùng MySQL
    public void signup(ActionEvent event) throws IOException {
//        readData();
//        String username = usernameField.getText();
//        String password = passwordField.getText();
//        String passwordConfirm = confirmPasswordField.getText();
//        if(passwordsMap.containsKey(username)) {
//            wrongConfirm.setText("");
//            wrongUsername.setText("Username already exists!");
//        } else if(password.isEmpty()) {
//            wrongUsername.setText("");
//            wrongConfirm.setText("Please enter your passwords!");
//        }else if (!passwordConfirm.equals(password)) {
//            wrongUsername.setText("");
//            wrongConfirm.setText("Password and confirm password doesn't match!");
//        } else {
//            FileWriter fw = null;
//            FileWriter fw2 = null;
//            String filePath = "src/main/resources/data/passwords.txt";
//            String filePathGame = "src/main/resources/data/snake_game/score.txt";
//            try {
//                fw = new FileWriter(filePath, true);
//                fw2 = new FileWriter(filePathGame, true);
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//            try {
//                fw.write(username + "," + password + "\n");
//                fw2.write(username + ":0");
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//            try {
//                fw.close();
//                fw2.close();
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//            File favoriteWord = new File("src/main/resources/data/favoriteWords/" + username + "FavoriteWord.txt");
//            File wordAdded = new File("src/main/resources/data/WordAdded/" + username + "wordsAdded.txt");
//            File wordDeleted = new File("src/main/resources/data/WordDeleted/" + username + "wordsDeleted.txt");
//            File recentWord = new File("src/main/resources/data/RecentWords/" + username + "RecentWords.txt");
//            favoriteWord.createNewFile();
//            wordAdded.createNewFile();
//            wordDeleted.createNewFile();
//            recentWord.createNewFile();
//            Application app = new Application();
//            app.changeScene("login.fxml");
//        }
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
                    createNewUserFiles(username);
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
    private void createNewUserFiles(String username) throws IOException {
            File favoriteWord = new File("src/main/resources/data/favoriteWords/" + username + "FavoriteWord.txt");
            File wordAdded = new File("src/main/resources/data/WordAdded/" + username + "wordsAdded.txt");
            File wordDeleted = new File("src/main/resources/data/WordDeleted/" + username + "wordsDeleted.txt");
            File recentWord = new File("src/main/resources/data/RecentWords/" + username + "RecentWords.txt");
            favoriteWord.createNewFile();
            wordAdded.createNewFile();
            wordDeleted.createNewFile();
            recentWord.createNewFile();
    }
}
