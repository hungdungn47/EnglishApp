package com.example.englishapp;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

class GameData {
    private static GameData instance;
    private String[] array;
    private String select;

    private GameData() {
    }

    public static GameData getInstance() {
        if (instance == null) {
            instance = new GameData();
        }
        return instance;
    }

    public String getSelect() {
        return select;
    }

    public void setSelect(String select) {
        this.select = select;
    }

    public String[] getArray() {
        return array;
    }

    public void setArray(String[] array) {
        this.array = array;
    }
}

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
    public Label scoreLabel;
    @FXML
    ImageView image_question;
    private String[] a = new String[10];
    private boolean[] check = new boolean[10]; // mac dinh la false
    private String selected_topic;
    private int x;
    private String correct_answer;
    private int current_score = 0;

    public void play_game_2(ActionEvent event) throws IOException {
        Alert themeSelectionAlert = new Alert(Alert.AlertType.CONFIRMATION);
        themeSelectionAlert.setTitle("Choose game theme");
        themeSelectionAlert.setHeaderText(null);
        themeSelectionAlert.setContentText("Please select game theme:");
        ButtonType theme1Button = new ButtonType("character");
        ButtonType theme2Button = new ButtonType("emotional");
        ButtonType theme3Button = new ButtonType("environment");
        ButtonType theme4Button = new ButtonType("technology");
        ButtonType theme5Button = new ButtonType("work");
        ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        themeSelectionAlert.getButtonTypes().setAll(theme1Button, theme2Button, theme3Button,
                theme4Button, theme5Button, cancelButton);

        themeSelectionAlert.getDialogPane().setStyle("-fx-background-color: linear-gradient(#b78fcd, #cb9fda, #F0F8FF);");
        themeSelectionAlert.getDialogPane().lookup(".content.label").setStyle("-fx-text-fill: #8d2aa4; -fx-font-size: 18px; -fx-font-weight: bold;");

        InputStream inputStream = getClass().getResourceAsStream("/data/learnWord/choice.png");
        Image image = new Image(inputStream);
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(30);
        imageView.setFitHeight(30);
        themeSelectionAlert.setGraphic(imageView);

        ((Button) themeSelectionAlert.getDialogPane().lookupButton(theme1Button))
                .setStyle("-fx-font-family: 'Bell MT'; -fx-background-color: #c4add1; -fx-text-fill: #8d2aa4; -fx-font-size: 16px; -fx-font-weight: bold;");
        ((Button) themeSelectionAlert.getDialogPane().lookupButton(theme2Button))
                .setStyle("-fx-font-family: 'Bell MT'; -fx-background-color: #c4add1; -fx-text-fill: #8d2aa4; -fx-font-size: 16px; -fx-font-weight: bold;");
        ((Button) themeSelectionAlert.getDialogPane().lookupButton(theme3Button))
                .setStyle("-fx-font-family: 'Bell MT'; -fx-background-color: #c4add1; -fx-text-fill: #8d2aa4; -fx-font-size: 16px; -fx-font-weight: bold;");
        ((Button) themeSelectionAlert.getDialogPane().lookupButton(theme4Button))
                .setStyle("-fx-font-family: 'Bell MT'; -fx-background-color: #c4add1; -fx-text-fill: #8d2aa4; -fx-font-size: 16px; -fx-font-weight: bold;");
        ((Button) themeSelectionAlert.getDialogPane().lookupButton(theme5Button))
                .setStyle("-fx-font-family: 'Bell MT'; -fx-background-color: #c4add1; -fx-text-fill: #8d2aa4; -fx-font-size: 16px; -fx-font-weight: bold;");
        ((Button) themeSelectionAlert.getDialogPane().lookupButton(cancelButton))
                .setStyle("-fx-font-family: 'Bell MT'; -fx-background-color: #c4add1; -fx-text-fill: #8d2aa4; -fx-font-size: 16px; -fx-font-weight: bold;");


        themeSelectionAlert.showAndWait().ifPresent(buttonType -> {
            if (buttonType == cancelButton) {
                return;
            }
            if (buttonType == theme1Button) {
                selected_topic = "character";
            } else if (buttonType == theme2Button) {
                selected_topic = "emotional";
            } else if (buttonType == theme3Button) {
                selected_topic = "environment";
            } else if (buttonType == theme4Button) {
                selected_topic = "technology";
            } else if (buttonType == theme5Button) {
                selected_topic = "work";
            }
            Application application = new Application();
            try {
                application.changeScene("game2.fxml");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        String directoryPath = "./src/main/resources/data/learnWord";
        List<String> imageNames = getImageNames(directoryPath, selected_topic);
        int i = 0;
        for (String name : imageNames) {
            System.out.println(name);
            if (i == 10) {
                break;
            } else {
                a[i++] = name;
            }
        }
        GameData gameData = GameData.getInstance();
        gameData.setArray(a);
        gameData.setSelect(selected_topic);
    }

    public void start_game_2(ActionEvent event) throws IOException {
        Application app = new Application();
        app.changeScene("start_game2.fxml");
    }

    public void how_to_play_game2(ActionEvent event) throws IOException {
        Application app = new Application();
        app.changeScene("how_to_play_game2.fxml");
    }

    public void back_to_main(ActionEvent event) throws IOException {
        Application app = new Application();
        app.changeScene("main-screen.fxml");
    }

    private static List<String> getImageNames(String directoryPath, String subdirectoryName) {
        List<String> imageNames = new ArrayList<>();
        File subdirectory = new File(directoryPath, subdirectoryName);

        if (subdirectory.exists() && subdirectory.isDirectory()) {
            File[] files = subdirectory.listFiles();

            if (files != null) {
                for (File file : files) {
                    if (file.isFile() && file.getName().toLowerCase().endsWith(".png")) {
                        String imageName = removeFileExtension(file.getName());
                        imageNames.add(imageName);
                    }
                }
            }
        }

        return imageNames;
    }

    private static String removeFileExtension(String fileName) {
        int lastDotIndex = fileName.lastIndexOf(".");
        if (lastDotIndex != -1) {
            return fileName.substring(0, lastDotIndex);
        }
        return fileName;
    }

    public void next_question(ActionEvent event) throws IOException {
        next_button.setText("Next");

        GameData gameData = GameData.getInstance();
        a = gameData.getArray();
        selected_topic = gameData.getSelect();
        for (int i = 0; i < 10; i++) {
            System.out.println(a[i]);
        }

        boolean allQuestionsUsed = true;
        for (boolean used : check) {
            if (!used) {
                allQuestionsUsed = false;
                break;
            }
        }

        if (allQuestionsUsed) {
            scoreLabel.setText("");
            answer1.setText("YOUR");
            answer2.setText("SCORE");
            answer3.setText("IS");
            answer4.setText(String.valueOf(current_score + 1));
            next_button.setText("Finish");
            next_button.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    Application app = new Application();
                    try {
                        app.changeScene("end_game2.fxml");
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
            return;
        }
        // random question
        do {
            x = ThreadLocalRandom.current().nextInt(0, 10);
        } while (check[x]);

        check[x] = true;
        correct_answer = a[x];
        // xu ly hinh anh
        try {
            // Thay doi hinh anh
            String tmp = "/data/learnWord/" + selected_topic + "/" + a[x] + ".png";
            String imagePath = tmp; // Duong dan toi hinh anh moi
            InputStream inputStream = getClass().getResourceAsStream(imagePath);

            if (inputStream != null) {
                Image newImage = new Image(inputStream);
                image_question.setImage(newImage);
            } else {
                System.out.println("Không tìm thấy hình ảnh: " + imagePath);
                for (int i = 0; i < 10; i++) {
                    System.out.println(a[i]);
                }
            }
        } catch (Exception e) {
            System.out.println("Lỗi khi thay đổi hình ảnh: " + e.getMessage());
        }
        // random vi tri cau tra loi dung
        int randomAnswer = ThreadLocalRandom.current().nextInt(1, 5);
        if (randomAnswer == 1) {
            answer1.setText(a[x]);
            answer2.setText(a[(x + 1) % 10]);
            answer3.setText(a[(x + 2) % 10]);
            answer4.setText(a[(x + 3) % 10]);
        } else if (randomAnswer == 2) {
            answer2.setText(a[x]);
            answer1.setText(a[(x + 1) % 10]);
            answer3.setText(a[(x + 2) % 10]);
            answer4.setText(a[(x + 3) % 10]);
        } else if (randomAnswer == 3) {
            answer3.setText(a[x]);
            answer1.setText(a[(x + 1) % 10]);
            answer2.setText(a[(x + 2) % 10]);
            answer4.setText(a[(x + 3) % 10]);
        } else {
            answer4.setText(a[x]);
            answer1.setText(a[(x + 1) % 10]);
            answer2.setText(a[(x + 2) % 10]);
            answer3.setText(a[(x + 3) % 10]);
        }
    }

    public void check_answer(ActionEvent event) throws IOException {
        Button clickedButton = (Button) event.getSource();
        String selectedAnswer = clickedButton.getText();
        boolean isCorrect = selectedAnswer.equals(correct_answer);

        if (isCorrect) {
            next_question(event);
            current_score++;
            scoreLabel.setText("Score: " + String.valueOf(current_score));
        } else {
            scoreLabel.setText("");
            answer1.setText("YOUR");
            answer2.setText("SCORE");
            answer3.setText("IS");
            answer4.setText(String.valueOf(current_score));
            next_button.setText("Finish");
            next_button.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    Application app = new Application();
                    try {
                        app.changeScene("end_game2.fxml");
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
        }
    }
}