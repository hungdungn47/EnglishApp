package com.example.englishapp;

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
    static final String DB_URL = "jdbc:mysql://192.168.76.82:3306/account"; // Thay thế bằng URL của cơ sở dữ liệu MySQL
    static final String USER = "dung"; // Thay thế bằng tên người dùng MySQL
    static final String PASS = "Hungdung030105?"; // Thay thế bằng mật khẩu người dùng MySQL
    public void signup(ActionEvent event) throws IOException {
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
        String username = usernameField.getText();
        String password = passwordField.getText();
        String passwordConfirm = confirmPasswordField.getText();
        if(passwordsMap.containsKey(username)) {
            wrongUsername.setText("Username already exists!");
        } else if (!passwordConfirm.equals(password)) {
            wrongConfirm.setText("Password and confirm password doesn't match!");
        } else {
            FileWriter fw = null;
            String filePath = "src/main/resources/data/passwords.txt";
            try {
                fw = new FileWriter(filePath, true);
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
            String fileName = username + "FavoriteWord.txt";
            File favoriteWord = new File("src/main/resources/data/favoriteWords/" + fileName);
            favoriteWord.createNewFile();
            Application app = new Application();
            app.changeScene("login.fxml");
        }
//        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
//
//            // Kiểm tra xem tên người dùng mới đã tồn tại hay chưa
//            String checkUserQuery = "SELECT * FROM tai_khoan WHERE username = ?";
//            PreparedStatement checkStatement = conn.prepareStatement(checkUserQuery);
//            checkStatement.setString(1, username);
//            ResultSet resultSet = checkStatement.executeQuery();
//
//            if (resultSet.next()) {
//                wrongUsername.setText("Username already exists!");
//                wrongConfirm.setText("");
//            } else if(!passwordConfirm.equals(password)) {
//                wrongUsername.setText("");
//                wrongConfirm.setText("Password and confirm password doesn't match!");
//            } else {
//                // Nếu tên người dùng mới không tồn tại, thêm tài khoản mới vào cơ sở dữ liệu
//                String insertQuery = "INSERT INTO tai_khoan (username, password) VALUES (?, ?)";
//                PreparedStatement insertStatement = conn.prepareStatement(insertQuery);
//                insertStatement.setString(1, username);
//                insertStatement.setString(2, password);
//
//                int rowsInserted = insertStatement.executeUpdate();
//                if (rowsInserted > 0) {
//                    // create a new file to save this user's favorite words
//                    String fileName = username + "FavoriteWord.txt";
//                    File file = new File("src/main/resources/data/favoriteWords/" + fileName);
//                    file.createNewFile();
//                    System.out.println("Tài khoản đã được đăng ký thành công!");
//                }
//                Application app = new Application();
//                app.changeScene("login.fxml");
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
    }
    public void back(ActionEvent event) throws IOException {
        Application app = new Application();
        app.changeScene("login.fxml");
    }
}
