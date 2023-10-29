package com.example.englishapp;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.w3c.dom.Text;

import java.io.*;
import java.util.concurrent.ThreadLocalRandom;

public class Game2 {
    @FXML
    private Button next_button;
    @FXML
    private Button answer1;
    @FXML
    private Button answer2;
    @FXML
    private Button answer3;
    @FXML
    private Button answer4;
    @FXML
    private Label scoreLabel;
    @FXML
    ImageView image_question;
    private boolean[] check = new boolean[30]; // mac dinh la false
    private int x;
    private String correct_answer;
    private int current_score = 0;


    public void back_to_main(ActionEvent event) throws IOException {
        Application app = new Application();
        app.changeScene("main-screen.fxml");
    }

    public void play_game_2(ActionEvent event) throws IOException {
        Application app = new Application();
        app.changeScene("game2.fxml");
    }

    public void next_question(ActionEvent event) throws IOException {
        next_button.setText("Next");
        String[] a = new String[30];
        a[0] = "aircraft";
        a[1] = "bird";
        a[2] = "book";
        a[3] = "cat";
        a[4] = "computer";
        a[5] = "cool";
        a[6] = "dandelion";
        a[7] = "depression";
        a[8] = "dog";
        a[9] = "ear buds";
        a[10] = "emotional";
        a[11] = "forget";
        a[12] = "four-leaf clover";
        a[13] = "headphone";
        a[14] = "hot-air balloon";
        a[15] = "key";
        a[16] = "money";
        a[17] = "mountain";
        a[18] = "mouse";
        a[19] = "newspaper";
        a[20] = "rabbit";
        a[21] = "scared";
        a[22] = "shark";
        a[23] = "sky";
        a[24] = "smart watch";
        a[25] = "suitcase";
        a[26] = "television";
        a[27] = "tired";
        a[28] = "volcano";
        a[29] = "wallet";

        boolean allQuestionsUsed = true;
        for (boolean used : check) {
            if (!used) {
                allQuestionsUsed = false;
                break;
            }
        }

        if (allQuestionsUsed) {
            Application app = new Application();
            app.changeScene("end_game2.fxml");
//            FXMLLoader loader = new FXMLLoader(getClass().getResource("end_game2.fxml"));
//            Parent root = loader.load();
//            Scene scene = new Scene(root);
//            Stage stage = (Stage) next_button.getScene().getWindow();
//            stage.setScene(scene);
//            scoreLabel.setText("Your score is: " + current_score);
            return;
        }
        // random question
        do {
            x = ThreadLocalRandom.current().nextInt(0, 30);
        } while (check[x]);

        check[x] = true;
        correct_answer = a[x];
        // xu ly hinh anh
        try {
            // Thay doi hinh anh
            String tmp = "/data/hinh_anh_game/"+ a[x] + ".png";
            String imagePath = tmp; // Duong dan toi hinh anh moi
            InputStream inputStream = getClass().getResourceAsStream(imagePath);

            if (inputStream != null) {
                Image newImage = new Image(inputStream);
                image_question.setImage(newImage);
            } else {
                System.out.println("Không tìm thấy hình ảnh: " + imagePath);
            }
        } catch (Exception e) {
            System.out.println("Lỗi khi thay đổi hình ảnh: " + e.getMessage());
        }
        // random vi tri cau tra loi dung
        int randomAnswer = ThreadLocalRandom.current().nextInt(1, 5);
        if(randomAnswer == 1){
            answer1.setText(a[x]);
            answer2.setText(a[(x + 1) % 30]);
            answer3.setText(a[(x + 2) % 30]);
            answer4.setText(a[(x + 3) % 30]);
        } else if(randomAnswer == 2){
            answer2.setText(a[x]);
            answer1.setText(a[(x + 1) % 30]);
            answer3.setText(a[(x + 2) % 30]);
            answer4.setText(a[(x + 3) % 30]);
        } else if(randomAnswer == 3){
            answer3.setText(a[x]);
            answer1.setText(a[(x + 1) % 30]);
            answer2.setText(a[(x + 2) % 30]);
            answer4.setText(a[(x + 3) % 30]);
        } else{
            answer4.setText(a[x]);
            answer1.setText(a[(x + 1) % 30]);
            answer2.setText(a[(x + 2) % 30]);
            answer3.setText(a[(x + 3) % 30]);
        }
    }
    public void check_answer(ActionEvent event) throws IOException{
        Button clickedButton = (Button) event.getSource();
        String selectedAnswer = clickedButton.getText();
        boolean isCorrect = selectedAnswer.equals(correct_answer);

        if (isCorrect) {
            next_question(event);
            current_score++;
            scoreLabel.setText(String.valueOf(current_score));
        } else {
            Application app = new Application();
            app.changeScene("end_game2.fxml");
        }
    }
}
