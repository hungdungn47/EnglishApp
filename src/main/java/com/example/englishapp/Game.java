package com.example.englishapp;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Game {

    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    private static final int ROWS = 30;
    private static final int COLUMNS = 20;
    private static final int SQUARE_SIZE = WIDTH / ROWS;
    private static final String[] foods_image = new String[]{new File("src/main/resources/data/food.png").toURI().toString()};
    private static final int RIGHT = 0;
    private static final int LEFT = 1;
    private static final int UP = 2;
    private static final int DOWN = 3;
    private GraphicsContext gc;
    private List<Point> snakebody = new ArrayList<>();
    private Point snakehead;
    private Image image_of_food;
    private int x_foodcoor;
    private int y_foodcoor;
    private boolean gameOver;
    private int direction;
    @FXML
    private Button game_button;
    private Stage primaryStage;

    public void button_to_gameplay(ActionEvent event) throws IOException {
        Stage primaryStage = Application.myStage;
        Group root = new Group();
        Canvas canvas = new Canvas(WIDTH, HEIGHT);
        root.getChildren().add(canvas);
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
        gc = canvas.getGraphicsContext2D();
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
            Point snake_piece = new Point(5-i, ROWS / 2);
            snakebody.add(snake_piece);
        }
        snakehead = snakebody.get(0);
        generate_food();
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(130), e-> {
            try {
                run_game(gc);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }
    private void run_game(GraphicsContext gc) throws IOException {
        if(gameOver){
            return;
        }
        drawBackground(gc);
        draw_food(gc);
        draw_snake(gc);
        for (int i = snakebody.size() - 1; i >= 1; i--) {
            snakebody.get(i).x = snakebody.get(i-1).x;
            snakebody.get(i).y = snakebody.get(i-1).y;
        }

        if (direction == LEFT) {
            if(snakehead.x == 0){
                snakehead.x = ROWS;
            }
            else{
                snakehead.x--;
            }
        } else if (direction == RIGHT) {
            if(snakehead.x == ROWS){
                snakehead.x = 0;
            }
            else {
                snakehead.x++;
            }
        } else if (direction == UP) {
            if(snakehead.y ==0){
                snakehead.y = COLUMNS;
            }
            else{
                snakehead.y--;
            }
        } else if (direction == DOWN) {
            if(snakehead.y == COLUMNS){
                snakehead.y = 0;
            }
            else{
                snakehead.y++;
            }
        }
        gameover();
        Ifgameover();
        eat();
    }

    private void generate_food() {
        start:
        while (true) {
            x_foodcoor = (int) (Math.random() * ROWS);
            y_foodcoor = (int) (Math.random() * COLUMNS);

            for (Point snake : snakebody) {
                if (snake.getX() == x_foodcoor && snake.getY() == y_foodcoor) {
                    continue start;
                }
            }
            image_of_food = new Image(foods_image[0]);
            break;
        }
    }

    private void draw_food(GraphicsContext gc) {
        gc.drawImage(image_of_food, x_foodcoor * SQUARE_SIZE, y_foodcoor * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);
    }

    private void draw_snake(GraphicsContext gc) {
        gc.setFill(Color.GRAY);
        gc.fillRoundRect(snakehead.getX() * SQUARE_SIZE, snakehead.getY() * SQUARE_SIZE, SQUARE_SIZE - 1, SQUARE_SIZE - 1, 60, 60);
        for (int i = 1; i < snakebody.size(); i++) {
            gc.fillRoundRect(snakebody.get(i).getX() * SQUARE_SIZE, snakebody.get(i).getY() * SQUARE_SIZE, SQUARE_SIZE - 1, SQUARE_SIZE - 1, 20, 20);
        }
    }

    private void drawBackground(GraphicsContext gc) {
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLUMNS; j++) {
                if ((i + j) % 2 == 0) {
                    gc.setFill(Color.BLACK);
                } else {
                    gc.setFill(Color.WHITE);
                }
                gc.fillRect(i * SQUARE_SIZE, j * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);
            }
        }
    }
    private void gameover() {
        for(int i = 1; i <snakebody.size();i++){
            if(snakehead.x == snakebody.get(i).getX() && snakehead.getY() == snakebody.get(i).getY()){
                gameOver = true;
                break;
            }
        }
    }
    private void Ifgameover() throws IOException{
        if(gameOver){
            Application app = new Application();
            app.changeScene("hello-view.fxml");
        }
    }
    private void eat(){
        if(snakehead.getX() == x_foodcoor && snakehead.getY()== y_foodcoor){
            snakebody.add(new Point(-1,-1));
            generate_food();
        }
    }
}
