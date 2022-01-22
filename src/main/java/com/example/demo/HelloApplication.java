package com.example.demo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.io.IOException;

// https://www.geeksforgeeks.org/implementing-generic-graph-in-java/

// TOOD
// Najbliższy wierzchołek musi być zawsze połączony z resztą.

public class HelloApplication extends Application {

    final int WINDOW_SIZE = 500;                    // Wielkość okna
    final int VERTEXES = 10;                       // Liczba wierzchołków grafu
    int[][] points = new int[50][50];               // Pojedyńcza komórka na sieci kwadratowej
    final int SIZE = WINDOW_SIZE / 10;              // Ilość elementów na długość / szerokość
    List<Vector2f> v = new Vertexes().vertexes;     // Lista wierzchołków
    List<Edge> e = new Edges().edges;               // Lista krawędzi

    @Override
    public void start(Stage stage) throws IOException, InterruptedException {

        /* Główna scena */
        Group root = new Group();

        /* Wygeneruj komórki dla sieci kwadratowej */
        for (int j = 0; j < SIZE; j++) {
            for (int i = 0; i < SIZE; i++) {
                points[i][j] = 0;
            }
        }

        /* Wygeneruj n losowych punktów */
        for (int i = 0; i < VERTEXES; i++) {
            while(true) {
                int x = getRandomNumber(1, 50);
                int y = getRandomNumber(1, 50);
                if(points[x][y] == 0) {
                    points[x][y] = 1;
                    v.add(new Vector2f(x, y));
                    break;
                }
            }
        }

        Vector2f nearest_point = new Vector2f(50, 50);

        for(Vector2f item : v) {
            if ((item.x + item.y) < (nearest_point.x + nearest_point.y)) {
                nearest_point = item;
            }
        }

        System.out.println("Najblizszy punkt to x: " + nearest_point.x + " y: " + nearest_point.y);

        /* Wygeneruj m losowych krawędzi */
        for (int i = 0; i < VERTEXES; i++) {
            while(true) {
                Random rand = new Random();
                Vector2f vertex1 = v.get(rand.nextInt(v.size()));
                Vector2f vertex2 = v.get(rand.nextInt(v.size()));

                // Wygeneruj krawędź dla pary wierzchołków bez powtórzeń
                // 1-2, 1-3, 1-4, 2-4 itd...
                if(vertex1 != vertex2) {
                    if(!e.contains(new Edge(vertex1, vertex2)) && !e.contains(new Edge(vertex2, vertex1))) {
                        e.add(new Edge(vertex1, vertex2));
                        break;
                    }
                }
            }
        }

        // Czy najbliższy element w lewym górnym rogu ma krawędź
        for(Vector2f item : v) {
            boolean state = true;
            for(Edge subitem : e) {
                if(subitem.v1 == item || subitem.v2 == item) {
                    state = false;
                }
            }
            // Jeżeli nie - dodaj ją
            if(state) {
                boolean loop = true;
                while(loop) {
                    Random rand = new Random();
                    Vector2f el = v.get(rand.nextInt(v.size()));
                    if(item != el) {
                        e.add(new Edge(item, el));
                        loop = !loop;
                    }
                }
            }
        }

        this.draw_background(root);

        /* Rysuj krawędzie */

        for(Edge item : e) {
            Vector2f item1 = item.v1;
            Vector2f item2 = item.v2;
            Line line = new Line((double) item1.x * 10, (double) item1.y * 10, (double) item2.x * 10, (double) item2.y * 10);
            line.setStroke(Color.DARKGRAY);
            root.getChildren().add(line);
        }

        Line line = new Line((double) 0, (double) 0, (double) nearest_point.x * 10, (double) nearest_point.y * 10);
        line.setStroke(Color.DARKGRAY);
        root.getChildren().add(line);

        /* Rysuj wierzchołki */
        for(Vector2f item : v) {
            Circle point = new Circle((item.x * 10), (item.y * 10), 3);
            point.setFill(Color.BLACK);
            root.getChildren().add(point);
        }

        for(Edge item : e) {
            System.out.println("[" + item.v1.x + ";" + item.v1.y + "] <=> [" + item.v2.x + ";" + item.v2.y + "]");
        }

        Scene scene = new Scene(root, WINDOW_SIZE, WINDOW_SIZE);
        stage.setTitle("Projekt 22 - Bartosz Rogowski");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();

    }

    private void draw_background(Group root) {

        /* Ustaw tło na białe */
        Rectangle background = new Rectangle(0,0, WINDOW_SIZE, WINDOW_SIZE);
        background.setFill(Color.WHITE);
        root.getChildren().add(background);

        /* Siatka sieci kwadratowej */
        for (int i = 0; i < 50; i++) {
            Line line = new Line(0.0, (double) (i * 10), 500, (double) (i * 10));
            line.setStroke(Color.LIGHTGRAY);
            root.getChildren().add(line);

            line = new Line((double) (i * 10), 0.0, (double) (i * 10), 500);
            line.setStroke(Color.LIGHTGRAY);
            root.getChildren().add(line);
        }

    }

    public int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    public Thread clock(Stage window) {
        return new Thread(() -> {
            boolean loop = true;
            double change = 0.000;
            try {
                if(loop)
                    change = 0.001;
                else
                    change = -0.001;
                window.getScene().getWindow().setWidth(window.getScene().getWidth() + change);
                loop = !loop;
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }

    public static void main(String[] args) {
        launch();
    }
}