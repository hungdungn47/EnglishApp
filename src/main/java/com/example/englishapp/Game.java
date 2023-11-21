package com.example.englishapp;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.event.ActionEvent;
import javafx.scene.ImageCursor;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.awt.*;
import java.util.*;
import java.util.List;

public class Game {

    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    private static final int ROWS = 20;
    private static final int COLUMNS = 15;
    private static final int SQUARE_SIZE = WIDTH / ROWS;
    private static final int RIGHT = 0;
    private static final int LEFT = 1;
    private static final int UP = 2;
    private static final int DOWN = 3;
    private GraphicsContext gc;
    private List<Point> snakebody = new ArrayList<>();
    private Point snakehead;
    private Image image_of_character;
    private Image image_of_fakecharacter1;
    private Image image_of_fakecharacter2;
    private Image image_of_fakecharacter3;
    private int x_foodcoor;
    private int y_foodcoor;
    private int x_fakecharacter;
    private int y_fakecharacter;
    private int x_fakecharacter1;
    private int y_fakecharacter1;
    private int x_fakecharacter2;
    private int y_fakecharacter2;
    private boolean gameOver;
    private boolean wingame;
    private int direction;
    private int health = 3;
    private int score = 0;
    private char char_to_add;
    Group root = new Group();
    String word_hidden = "";
    String word_origin = "";
    int temp = 0;
    private final List<String> listfavoriteWords = new ArrayList<>();
    private final Map<String, Integer> History_score = new HashMap<>();
    private final MediaPlayer sound_game = new MediaPlayer(new Media(new File("src/main/resources/data/snake_game/music.mp3").toURI().toString()));
    private final AudioClip eat_sound = new AudioClip(new File("src/main/resources/data/snake_game/eat_sound.mp3").toURI().toString());
    private final AudioClip game_over = new AudioClip(new File("src/main/resources/data/snake_game/game_over.mp3").toURI().toString());

    public void back_to_main(ActionEvent event) throws IOException {
        Application app = new Application();
        app.changeScene("main-screen.fxml");
    }

    public void button_to_gameplay(ActionEvent event) throws IOException {
        Stage primaryStage = Application.myStage;
        Canvas canvas = new Canvas(WIDTH, HEIGHT);
        root.getChildren().add(canvas);
        Scene scene = new Scene(root);
        scene.setCursor(new ImageCursor(new Image(new File("src/main/resources/data/snake_game/cursor_snakegame.png").toURI().toString())));
        primaryStage.setScene(scene);
        primaryStage.show();
        sound_game.setOnEndOfMedia(() -> sound_game.seek(Duration.ZERO));
        sound_game.play();
        gc = canvas.getGraphicsContext2D();
        build_list_word();
        insertScoreFromTxt();
        for (int i = 0; i < ROWS; i++) {
            setfilltaskbar(gc);
            gc.fillRect(i * SQUARE_SIZE, 0, SQUARE_SIZE, SQUARE_SIZE);
        }
        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                KeyCode code = keyEvent.getCode();
                if (code == KeyCode.RIGHT || code == KeyCode.D) {
                    if (direction != LEFT) {
                        direction = RIGHT;
                    }
                } else if (code == KeyCode.LEFT || code == KeyCode.A) {
                    if (direction != RIGHT) {
                        direction = LEFT;
                    }
                } else if (code == KeyCode.UP || code == KeyCode.W) {
                    if (direction != DOWN) {
                        direction = UP;
                    }
                } else if (code == KeyCode.DOWN || code == KeyCode.S) {
                    if (direction != UP) {
                        direction = DOWN;
                    }
                }
            }
        });
        for (int i = 0; i < 3; i++) {
            Point snake_piece = new Point(5 - i, ROWS / 2);
            snakebody.add(snake_piece);
        }
        snakehead = snakebody.get(0);
        draw_words_to_complete(gc);
        generate_character(char_to_add);
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(130), e -> {
            try {
                run_game(gc);
                if (gameOver || wingame) {
                    sound_game.pause();
                    if (temp == 0) {
                        game_over.play();
                        temp = 1;
                    }
                    scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
                        @Override
                        public void handle(KeyEvent keyEvent) {
                            KeyCode code = keyEvent.getCode();
                            if (code == KeyCode.R) {
                                Application app = new Application();
                                try {
                                    app.changeScene("game.fxml");
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        }
                    });
                }
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    private void run_game(GraphicsContext gc) throws IOException {
        if (gameOver) {
            UpdateScore();
            ExportScoreToTxt();
            Ifgameover(gc);
            return;
        }
        if (wingame) {
            Ifwingame(gc);
            return;
        }
        drawBackground(gc);
        drawScore();
        draw_character(gc);
        draw_snake(gc);
        draw_heart(gc);
        for (int i = snakebody.size() - 1; i >= 1; i--) {
            snakebody.get(i).x = snakebody.get(i - 1).x;
            snakebody.get(i).y = snakebody.get(i - 1).y;
        }

        if (direction == LEFT) {
            if (snakehead.x <= 0) {
                snakehead.x = ROWS - 1;
            } else {
                snakehead.x--;
            }
        } else if (direction == RIGHT) {
            if (snakehead.x == ROWS - 1) {
                snakehead.x = 0;
            } else {
                snakehead.x++;
            }
        } else if (direction == UP) {
            if (snakehead.y == 1) {
                snakehead.y = COLUMNS - 1;
            } else {
                snakehead.y--;
            }
        } else if (direction == DOWN) {
            if (snakehead.y == COLUMNS - 1) {
                snakehead.y = 1;
            } else {
                snakehead.y++;
            }
        }
        gameover();
        eat();
    }

    private void generate_character(char x) {
        start:
        while (true) {
            x_foodcoor = (int) (Math.random() * ROWS);
            y_foodcoor = (int) (Math.random() * COLUMNS);
            x_fakecharacter = (int) (Math.random() * (ROWS - 1));
            y_fakecharacter = (int) (Math.random() * (COLUMNS - 1));
            x_fakecharacter1 = (int) (Math.random() * (ROWS - 2));
            y_fakecharacter1 = (int) (Math.random() * (COLUMNS - 2));
            x_fakecharacter2 = (int) (Math.random() * (ROWS - 3));
            y_fakecharacter2 = (int) (Math.random() * (COLUMNS - 3));
            for (Point snake : snakebody) {
                if (snake.getX() == x_foodcoor && snake.getY() == y_foodcoor
                        || (y_foodcoor <= 1)
                        || (snake.getX() == x_fakecharacter && snake.getY() == y_fakecharacter)
                        || (y_fakecharacter <= 1)
                        || (snake.getX() == x_fakecharacter1 && snake.getY() == y_fakecharacter1)
                        || (y_fakecharacter1 <= 1)
                        || (snake.getX() == x_fakecharacter2 && snake.getY() == y_fakecharacter2)
                        || (y_fakecharacter2 <= 1)
                        || (x_foodcoor == x_fakecharacter && y_foodcoor == y_fakecharacter)
                        || (x_foodcoor == x_fakecharacter1 && y_foodcoor == y_fakecharacter1)
                        || (x_foodcoor == x_fakecharacter2 && y_foodcoor == y_fakecharacter2)
                        || (x_fakecharacter == x_fakecharacter1 && y_fakecharacter == y_fakecharacter1)
                        || (x_fakecharacter == x_fakecharacter2 && y_fakecharacter == y_fakecharacter2)
                        || (x_fakecharacter2 == x_fakecharacter1 && y_fakecharacter2 == y_fakecharacter1)) {
                    continue start;
                }
            }
            if (x >= 97 && x <= 122) {
                x -= 32;
            }
            String character_image = "src/main/resources/data/bang_chu_cai/" + x + ".png";
            x += 1;
            String fakecharacter_image = "src/main/resources/data/bang_chu_cai/" + x + ".png";
            x -= 3;
            if (x < 65) {
                x = 90;
            }
            String fakecharacter_image1 = "src/main/resources/data/bang_chu_cai/" + x + ".png";
            x -= 3;
            if (x < 65) {
                x = 90;
            }
            String fakecharacter_image2 = "src/main/resources/data/bang_chu_cai/" + x + ".png";
            character_image = new File(character_image).toURI().toString();
            fakecharacter_image = new File(fakecharacter_image).toURI().toString();
            fakecharacter_image1 = new File(fakecharacter_image1).toURI().toString();
            fakecharacter_image2 = new File(fakecharacter_image2).toURI().toString();
            image_of_character = new Image(character_image);
            image_of_fakecharacter1 = new Image(fakecharacter_image);
            image_of_fakecharacter2 = new Image(fakecharacter_image1);
            image_of_fakecharacter3 = new Image(fakecharacter_image2);
            break;
        }
    }

    private void draw_heart(GraphicsContext gc) {
        String heat_image = "src/main/resources/data/snake_game/heart.png";
        heat_image = new File(heat_image).toURI().toString();
        Image image_of_heart = new Image(heat_image);
        for (int i = 0; i < health; i++) {
            gc.drawImage(image_of_heart, (17 + i) * SQUARE_SIZE, 0, SQUARE_SIZE, SQUARE_SIZE);
        }
    }

    private void build_list_word() {
        String fileName = Login.getUsername() + "FavoriteWord.txt";
        String filePath = "src/main/resources/data/favoriteWords/" + fileName;
        try {
            Scanner sc = new Scanner(new File(filePath));
            while (sc.hasNextLine()) {
                String tmp = sc.nextLine();
                listfavoriteWords.add(tmp);
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void draw_words_to_complete(GraphicsContext gc) {
        int index = (int) (Math.random() * listfavoriteWords.size());
        String word = listfavoriteWords.get(index);
        int count_hidden;
        if (word.length() > 3) {
            count_hidden = 3;
        } else {
            count_hidden = 1;
        }
        String temp = word;
        int rand = (int) (Math.random() * word.length());
        for (int i = 0; i < count_hidden; i++) {
            while (temp.charAt(rand) == '_' || temp.charAt(rand) == ' ' || temp.charAt(rand) == '-' || temp.charAt(rand) == '.') {
                rand = (int) (Math.random() * word.length());
            }
            temp = temp.substring(0, rand) + '_' + temp.substring(rand + 1);
        }
        word_hidden = temp;
        word_origin = word;
        for (int i = 0; i < word.length(); i++) {
            if (word_hidden.charAt(i) == '_') {
                char_to_add = word.charAt(i);
                gc.setFill(Color.RED);
                gc.setFont(new Font("Comic sans MS", 40));
                gc.fillText(word_hidden, WIDTH / 2.5, SQUARE_SIZE - 8);
                word_hidden = word_hidden.substring(0, i) + word_origin.charAt(i) + word_hidden.substring(i + 1);
                break;
            }
        }
    }

    private void draw_character(GraphicsContext gc) {
        gc.drawImage(image_of_character, x_foodcoor * SQUARE_SIZE, y_foodcoor * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);
        gc.drawImage(image_of_fakecharacter1, x_fakecharacter * SQUARE_SIZE, y_fakecharacter * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);
        gc.drawImage(image_of_fakecharacter2, x_fakecharacter1 * SQUARE_SIZE, y_fakecharacter1 * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);
        gc.drawImage(image_of_fakecharacter3, x_fakecharacter2 * SQUARE_SIZE, y_fakecharacter2 * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);
    }

    private void draw_snake(GraphicsContext gc) {
        gc.setFill(Color.DARKGREEN);
        gc.fillRoundRect(snakehead.getX() * SQUARE_SIZE, snakehead.getY() * SQUARE_SIZE, SQUARE_SIZE - 1, SQUARE_SIZE - 1, 60, 60);
        for (int i = 1; i < snakebody.size(); i++) {
            gc.fillRoundRect(snakebody.get(i).getX() * SQUARE_SIZE, snakebody.get(i).getY() * SQUARE_SIZE, SQUARE_SIZE - 1, SQUARE_SIZE - 1, 20, 20);
        }
    }

    private void drawBackground(GraphicsContext gc) {
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLUMNS; j++) {
                if (j == 0) {
                    continue;
                } else if ((i + j) % 2 == 0) {
                    gc.setFill(Color.valueOf("#61d8de"));
                } else {
                    gc.setFill(Color.valueOf("#42bbe3"));
                }
                gc.fillRect(i * SQUARE_SIZE, j * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);
            }
        }
    }

    private void gameover() {
        for (int i = 1; i < snakebody.size(); i++) {
            if (snakehead.x == snakebody.get(i).getX() && snakehead.getY() == snakebody.get(i).getY()) {
                gameOver = true;
                break;
            }
        }
    }

    private void Ifgameover(GraphicsContext gc) throws IOException {
        if (gameOver) {
            gc.clearRect(0, 0, WIDTH, HEIGHT);
            setfilltaskbar(gc);
            gc.fillRect(0, 0, WIDTH, HEIGHT);
            String gameoverImage = new File("src/main/resources/data/snake_game/gameover.png").toURI().toString();
            Image gameover = new Image(gameoverImage);
            gc.drawImage(gameover, WIDTH / 4, HEIGHT / 4, WIDTH / 2, HEIGHT / 2);
            gc.setFill(Color.RED);
            gc.setFont(new Font("Verdana", 20));
            gc.fillText("Ấn R để trở lại màn hình chính", WIDTH / 3 - 25, 560);
            gc.setFont(new Font("100px Tahoma", 35));
            gc.setFill(Color.PINK);
            gc.fillText("Điểm của bạn: " + score, WIDTH / 3 + 5, 500);
            PrintRanking();
        }
    }

    private void Ifwingame(GraphicsContext gc) {
        if (wingame) {
            gc.setFill(Color.RED);
            gc.setFont(new Font("Digital-7", 70));
            gc.fillText("Win Game", WIDTH / 3.5, HEIGHT / 2);
            gc.setFont(new Font("Digital-3", 35));
            gc.fillText("Ấn R để trở lại màn hình chính", WIDTH / 3.5, 400);
        }
    }

    private void drawScore() {
        gc.setFill(Color.WHITE);
        gc.setFont(new Font("Comic sans MS", 35));
        gc.fillText("Score: " + score, 10, 35);
    }

    private void eat() {
        if (snakehead.getX() == x_foodcoor && snakehead.getY() == y_foodcoor) {
            eat_sound.play();
            gc.clearRect(0, 0, 16 * SQUARE_SIZE, SQUARE_SIZE);
            setfilltaskbar(gc);
            gc.fillRect(0, 0, 16 * SQUARE_SIZE, SQUARE_SIZE);
            snakebody.add(new Point(-1, -1));
            score += 10;
            gc.setFill(Color.WHITE);
            gc.setFont(new Font("Comic sans MS", 35));
            gc.fillText("Score: " + score, 10, 35);
            if (word_hidden.equals(word_origin)) {
                draw_words_to_complete(gc);
            } else {
                for (int i = 0; i < word_hidden.length(); i++) {
                    if (word_hidden.charAt(i) == '_') {
                        char_to_add = word_origin.charAt(i);
                        gc.setFill(Color.RED);
                        gc.setFont(new Font("Comic sans MS", 40));
                        gc.fillText(word_hidden, WIDTH / 2.5, SQUARE_SIZE - 8);
                        word_hidden = word_hidden.substring(0, i) + word_origin.charAt(i) + word_hidden.substring(i + 1);
                        break;
                    }
                }
            }
            generate_character(char_to_add);
        }
        // nếu rắn ăn phải kì tự sai
        else if (snakehead.getX() == x_fakecharacter && snakehead.getY() == y_fakecharacter
                || snakehead.getX() == x_fakecharacter1 && snakehead.getY() == y_fakecharacter1
                || snakehead.getX() == x_fakecharacter2 && snakehead.getY() == y_fakecharacter2) {
            eat_sound.play();
            health--;
            gc.clearRect((17 + health) * SQUARE_SIZE, 0, SQUARE_SIZE, SQUARE_SIZE);
            setfilltaskbar(gc);
            gc.fillRect((17 + health - 1) * SQUARE_SIZE, 0, 2 * SQUARE_SIZE, SQUARE_SIZE);
            generate_character(char_to_add);
            if (health == 0) {
                gameOver = true;
            }
        }
    }

    private void setfilltaskbar(GraphicsContext gc) {
        gc.setFill(Color.valueOf("EA91E2"));
    }

    private void insertScoreFromTxt() throws IOException {
        Scanner sc = new Scanner(new File("src/main/resources/data/snake_game/score.txt"));
        while (sc.hasNext()) {
            String line = sc.next();
            int index = 0;
            for (int i = 0; i < line.length(); i++) {
                if (line.charAt(i) == ':') {
                    index = i;
                    break;
                }
            }
            String username = line.substring(0, index);
            int scorefromtxt = Integer.parseInt(line.substring(index + 1));
            History_score.put(username, scorefromtxt);
        }
    }

    private void ExportScoreToTxt() {
        FileWriter fw = null;
        try {
            fw = new FileWriter("src/main/resources/data/snake_game/score.txt");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        for (String key : History_score.keySet()) {
            try {
                fw.write(key + ":" + History_score.get(key) + "\n");
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

    private void UpdateScore() {
        String username = Login.getUsername();
        if (History_score.get(username) != null) {
            int temp = History_score.get(username);
            if (temp > score) {
                return;
            } else {
                History_score.put(username, score);
            }
        } else {
            History_score.put(username, score);
        }
    }

    private void PrintRanking() {
        Set<String> set = History_score.keySet();
        String[] rank = new String[3];
        int temp = 0;
        for (String key : set) {
            rank[temp] = key;
            temp++;
            if (temp == 3) {
                break;
            }
        }
        for (String key : set) {
            if (History_score.get(rank[0]) < History_score.get(key)) {
                rank[2] = rank[1];
                rank[1] = rank[0];
                rank[0] = key;
            } else if (History_score.get(rank[1]) < History_score.get(key) && !key.equals(rank[0])) {
                rank[2] = rank[1];
                rank[1] = key;
            } else if (History_score.get(rank[2]) < History_score.get(key) && !key.equals(rank[0]) && !key.equals(rank[1])) {
                rank[2] = key;
            }
        }
        String score = "SCORE\n" + History_score.get(rank[0]) + "\n" + History_score.get(rank[1]) + "\n" + History_score.get(rank[2]);
        String user = "USER\n" + rank[0] + "\n" + rank[1] + "\n" + rank[2];
        gc.setFill(Color.PINK);
        gc.setFont(new Font("Comic sans MS", 17));
        gc.fillText(user, 10, 25);
        gc.fillText(score, 160, 25);
    }
}
