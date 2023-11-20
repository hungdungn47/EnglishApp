package com.example.englishapp;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.Duration;


import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

class GameData {
    private static GameData instance;
    private Map<String, Integer> history_score;
    private String[] array;
    private String select;

    private GameData() {
        history_score = new HashMap<>();
    }

    public Map<String, Integer> getHistoryScore() {
        return history_score;
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
    public Label yourScoreLabel;
    @FXML
    private Label rank1;
    @FXML
    private Label rank2;
    @FXML
    private Label rank3;
    @FXML
    ImageView image_question;
    @FXML
    private Label timerLabel;
    private Timeline timeline;
    private static final int GAME_DURATION = 10;
    private int secondsLeft = GAME_DURATION;
    private String[] a = new String[10];
    private final boolean[] check = new boolean[10]; // mac dinh la false
    private String selected_topic;
    private int x;
    private String correct_answer;
    private int current_score = 0;

    private void firstQuestion() {
        updateTimerLabel();
        timeline = new Timeline();
        timeline.setCycleCount(Animation.INDEFINITE);
        KeyFrame keyFrame = new KeyFrame(Duration.seconds(1), event -> {
            secondsLeft--;
            updateTimerLabel();
            if (secondsLeft == 0) {
                timeline.stop();
                gameOver();
            }
        });
        timeline.getKeyFrames().add(keyFrame);
        randomQuestion();
        timeline.play();
    }

    private void updateTimerLabel() {
        Platform.runLater(() -> {
            if (secondsLeft > 0) {
                timerLabel.setText("Time: " + secondsLeft);
            } else {
                timerLabel.setText("Time out!");
            }
        });
    }

    public void gameOver() {
        updateScore();
        try {
            insertScoreFromTxt();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        GameData gameData = GameData.getInstance();
        Map<String, Integer> historyScore = gameData.getHistoryScore();

        List<Map.Entry<String, Integer>> sortedEntries = new ArrayList<>(historyScore.entrySet());
        Collections.sort(sortedEntries, (entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue()));

        int size = Math.min(sortedEntries.size(), 3);

        String[] rank = new String[3];
        for (int i = 0; i < size; i++) {
            Map.Entry<String, Integer> entry = sortedEntries.get(i);
            rank[i] = entry.getKey();
        }

        if (size < 3) {
            for (int i = size; i < 3; i++) {
                rank[i] = "";
            }
        }

        FXMLLoader loader = new FXMLLoader(getClass().getResource("end_game2.fxml"));
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Game2 controller = loader.getController();

        controller.rank1.setText(rank[0] + ":" + historyScore.get(rank[0]));
        controller.rank2.setText(rank[1] + ":" + historyScore.get(rank[1]));
        controller.rank3.setText(rank[2] + ":" + historyScore.get(rank[2]));

        controller.yourScoreLabel.setText("Your Score: " + current_score);

        // Access the current stage
        Stage stage = (Stage) answer1.getScene().getWindow();

        // Set the new scene on the current stage
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void play_game_2(ActionEvent event) throws IOException {
        try {
            insertScoreFromTxt();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
            String directoryPath = "./src/main/resources/data/learnWord";
            List<String> imageNames = getImageNames(directoryPath, selected_topic);
            int i = 0;
            for (String name : imageNames) {
                if (i == 10) {
                    break;
                } else {
                    a[i++] = name;
                }
            }
            GameData gameData = GameData.getInstance();
            gameData.setArray(a);
            gameData.setSelect(selected_topic);

            FXMLLoader loader = new FXMLLoader(getClass().getResource("game2.fxml"));
            Parent root = null;
            try {
                root = loader.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            Game2 gameController = loader.getController();
            gameController.firstQuestion();

            Scene gameScene = new Scene(root);
            Stage gameStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            gameStage.setScene(gameScene);
            gameStage.show();
        });
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

    private void randomQuestion() {
        GameData gameData = GameData.getInstance();
        a = gameData.getArray();
        selected_topic = gameData.getSelect();
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

    public void next_question(ActionEvent event) throws IOException {
        secondsLeft = GAME_DURATION;
        updateTimerLabel();
        boolean allQuestionsUsed = true;
        for (boolean used : check) {
            if (!used) {
                allQuestionsUsed = false;
                break;
            }
        }

        if (allQuestionsUsed) {
            gameOver();
            return;
        }
        randomQuestion();
        timeline.play();
    }

    public void check_answer(ActionEvent event) throws IOException {
        timeline.stop();
        Button clickedButton = (Button) event.getSource();
        String selectedAnswer = clickedButton.getText();
        boolean isCorrect = selectedAnswer.equals(correct_answer);

        if (isCorrect) {
            secondsLeft = GAME_DURATION;
            updateTimerLabel();
            current_score++;
            next_question(event);
            scoreLabel.setText("Score: " + current_score);
        } else {
            gameOver();
        }
    }

    private void insertScoreFromTxt() throws IOException {
        String filePath = "src/main/resources/data/learnWord/score.txt";
        Path path = Paths.get(filePath);
        List<String> lines = Files.readAllLines(path);
        GameData gameData = GameData.getInstance();
        Map<String, Integer> historyScore = gameData.getHistoryScore();
        for (String line : lines) {
            int index = line.indexOf(':');
            if (index != -1) {
                String username = line.substring(0, index).trim();
                int scorefromtxt = Integer.parseInt(line.substring(index + 1).trim());
                historyScore.put(username, scorefromtxt);
            }
        }
    }

    private void exportScoreToTxt() {
        GameData gameData = GameData.getInstance();
        Map<String, Integer> historyScore = gameData.getHistoryScore();
        FileWriter fw = null;
        try {
            fw = new FileWriter("src/main/resources/data/learnWord/score.txt");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        for (Map.Entry<String, Integer> entry : historyScore.entrySet()) {
            String key = entry.getKey();
            Integer value = entry.getValue();
            try {
                fw.write(key + ":" + value + "\n");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        try {
            fw.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void updateScore() {
        GameData gameData = GameData.getInstance();
        Map<String, Integer> historyScore = gameData.getHistoryScore();
        String username = Login.getUsername();
        if (historyScore.containsKey(username)) {
            int temp = historyScore.get(username);
            if (temp > current_score) {
                exportScoreToTxt();
                return;
            } else {
                historyScore.put(username, current_score);
                exportScoreToTxt();
                return;
            }
        } else {
            historyScore.put(username, current_score);
            exportScoreToTxt();
        }
    }
}
