package com.example.demo;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.util.*;
import java.io.IOException;

// https://www.geeksforgeeks.org/implementing-generic-graph-in-java/
// https://www.baeldung.com/java-graphs
// https://en.wikipedia.org/wiki/Erd%C5%91s%E2%80%93R%C3%A9nyi_model
// https://en.wikipedia.org/wiki/Barab%C3%A1si%E2%80%93Albert_model
// https://en.wikipedia.org/wiki/Watts%E2%80%93Strogatz_model

// UWAGI
// Najbliższy wierzchołek musi być zawsze połączony z resztą.
// Dziwny render punktów

public class Main extends Application {

    private final int WINDOW_SIZE = 500;                        // Wielkość okna
    private final int VERTEXES = 250;                           // Liczba wierzchołków grafu
    private final int SIZE = WINDOW_SIZE / 10;                  // Ilość elementów na długość / szerokość
    private int[][] points = new int[50][50];                   // Pojedyńcza komórka na sieci kwadratowej
    private List<Vector2f> v = new Vertexes().vertexes;         // Lista wierzchołków
    private List<Edge> e = new Edges().edges;                   // Lista krawędzi
    private Vector2f FIRST_POINT = new Vector2f(50, 50); // First point

    @Override
    public void start(Stage stage) throws IOException, InterruptedException {

        // Główna scena
        Group root = new Group();

        // Wygeneruj komórki dla sieci kwadratowe
        for (int j = 0; j < SIZE; j++) {
            for (int i = 0; i < SIZE; i++) {
                points[i][j] = 0;
            }
        }

        // Wygeneruj n losowych wierzchołków
        for (int i = 0; i < VERTEXES; i++) {
            while (true) {
                int x = getRandomNumber(1, 50);
                int y = getRandomNumber(1, 50);
                if (!v.contains(new Vector2f(x, y))) {
                    v.add(new Vector2f(x, y));
                    break;
                }
            }
        }

        for(Vector2f item : v) {
            Circle point = new Circle((item.x * 10), (item.y * 10), 4);
            point.setFill(Color.RED);
            root.getChildren().add(point);
        }

        for (Vector2f item : v) {
            if ((item.x + item.y) < (FIRST_POINT.x + FIRST_POINT.y)) {
                FIRST_POINT = item;
            }
        }

        System.out.println("Najblizszy punkt to x: " + FIRST_POINT.x + " y: " + FIRST_POINT.y);

        // Wygeneruj m losowych krawędzi
        for (Vector2f item : v) {
            int numb = getRandomNumber(2, 6);
            for (int i = 0; i < numb - 1; i++) {
                while (true) {
                    Random rand = new Random();
                    Vector2f vertex2 = v.get(rand.nextInt(v.size()));
                    // Wygeneruj krawędź dla pary wierzchołków bez powtórzeń
                    // 1-2, 1-3, 1-4, 2-4 itd...
                    if (item.x != vertex2.x && item.y != vertex2.y) {
                        //System.out.printf("Tak x=%d i y=%d jest rozne od x2=%d i y2=%d \n", item.x, item.y, vertex2.x, vertex2.y);
                        if (!e.contains(new Edge(item, vertex2)) && !e.contains(new Edge(vertex2, item))) {
                            e.add(new Edge(item, vertex2));
                            break;
                        }
                    }
                }
            }
        }



        // Czy najbliższy element w lewym górnym rogu ma krawędź
        for (Vector2f item : v) {
            boolean state = true;
            for (Edge subitem : e) {
                if (subitem.v1 == item || subitem.v2 == item) {
                    state = false;
                }
            }
            // Jeżeli nie - dodaj ją
            if (state) {
                boolean loop = true;
                while (loop) {
                    Random rand = new Random();
                    Vector2f el = v.get(rand.nextInt(v.size()));
                    if (item != el) {
                        e.add(new Edge(item, el));
                        loop = !loop;
                    }
                }
            }
        }

        this.draw_background(root);

        // Rysuj krawędzie

        Line line = new Line((double) 0, (double) 0, (double) FIRST_POINT.x * 10, (double) FIRST_POINT.y * 10);
        line.setStroke(Color.BLUEVIOLET);
        root.getChildren().add(line);

        for (Edge item : e) {
            Vector2f item1 = item.v1;
            Vector2f item2 = item.v2;
            line = new Line(item1.x * 10,  item1.y * 10,  item2.x * 10,  item2.y * 10);
            line.setStroke(Color.BLUEVIOLET);
            root.getChildren().add(line);
        }

        // TODO

        List<Vector2f> visited = new ArrayList<>();
        List<Node> nodes = new ArrayList<>();
        int level = 0;

        List<Vector2f> origin = new ArrayList<>();
        origin.add(new Vector2f(FIRST_POINT.x, FIRST_POINT.y));

        List<Vector2f> destination = new ArrayList<>();

        for(Edge i1 : e) {
            for(Vector2f i2 : origin) {
                if(i1.v1.x == i2.x && i1.v1.y == i2.y && !(visited.contains(new Vector2f(i1.v1.x, i1.v1.y)))) {
                    destination.add(new Vector2f(i1.v2.x, i1.v2.y));
                    visited.add(new Vector2f(i2.x, i2.y));
                }
            }
        }

        nodes.add(new Node(origin, destination, level));
        level++;

        while(visited.size() < v.size()) {

            List<Vector2f> node_origin = nodes.get(level - 1).destination;
            List<Vector2f> node_destination = new ArrayList<>();

            for(Edge i1 : e) {
                for(Vector2f i2 : node_origin) {
                    if(i1.v1.x == i2.x && i1.v1.y == i2.y) {
                        //System.out.println("Wartosci sa sobie rowne => " + i1.v1.x + ";"+i1.v1.y+" & " +i2.x + ";"+ i2.y);
                        if(!visited.contains(new Vector2f(i2.x, i2.y))) {
                            node_destination.add(new Vector2f(i1.v2.x, i1.v2.y));
                            visited.add(new Vector2f(i2.x, i2.y));
                        }
                    }
                }
            }

            nodes.add(new Node(node_origin, node_destination, level));
            level++;

        }

        for(Node item : nodes) {
            System.out.println("\n -----------------------------");
            System.out.printf("[Generation %d] \n", item.generation);
            System.out.printf("[Origin] \n");
            for(Vector2f i2 : item.origin) {
                System.out.println("x: " + i2.x + " y: " + i2.y);
            }
            System.out.printf("[Destination] \n");
            for(Vector2f i2 : item.destination) {
                System.out.println("x: " + i2.x + " y: " + i2.y);
            }
        }

        int color = 256 / nodes.size();
        int RED = 0;
        int GREEN = 255;
        int BLUE = 0;

        for (Edge item : e) {
            System.out.println("[" + item.v1.x + ";" + item.v1.y + "] <=> [" + item.v2.x + ";" + item.v2.y + "]");

            Circle point = new Circle(item.v1.x * 10, item.v1.y * 10, 4);
            point.setFill(Color.RED);
            root.getChildren().add(point);

            Circle point2 = new Circle((item.v2.x * 10), (item.v2.y * 10), 4);
            point2.setFill(Color.RED);
            root.getChildren().add(point2);
        }

        // Rysuj wierzchołki
        for (Node item : nodes) {

            for(Vector2f i2 : item.destination) {
                Circle point = new Circle((i2.x * 10), (i2.y * 10), 4);
                point.setFill(Color.rgb(RED, GREEN, BLUE));
                root.getChildren().add(point);
            }

            for(Vector2f i1 : item.origin) {
                Circle point = new Circle((i1.x * 10), (i1.y * 10), 4);
                point.setFill(Color.rgb(RED, GREEN, BLUE));
                root.getChildren().add(point);
            }

            GREEN = GREEN - color;
            RED = RED + color;

        }

        Circle point = new Circle((FIRST_POINT.x * 10), (FIRST_POINT.y * 10), 4);
        point.setFill(Color.rgb(0, 255, 0));
        root.getChildren().add(point);

        Scene scene = new Scene(root, WINDOW_SIZE, WINDOW_SIZE);
        stage.setTitle("Projekt 22 - Bartosz Rogowski");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }


    private void draw_background(Group root) {

        /* Ustaw tło na białe */
        Rectangle background = new Rectangle(0, 0, WINDOW_SIZE, WINDOW_SIZE);
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