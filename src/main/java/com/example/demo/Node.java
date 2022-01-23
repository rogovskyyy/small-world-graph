package com.example.demo;

import java.util.List;

public class Node {
    List<Vector2f> origin;
    List<Vector2f> destination;
    int generation;
    public Node(List<Vector2f> o, List<Vector2f> d, int g) {
        this.origin = o;
        this.destination = d;
        this.generation = g;
    }
}
