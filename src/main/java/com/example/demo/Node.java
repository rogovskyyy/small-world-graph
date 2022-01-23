package com.example.demo;

import java.util.List;

public class Node {
    public List<Vector2f> origin;
    public List<Vector2f> destination;
    public int generation;
    public Node(List<Vector2f> o, List<Vector2f> d, int g) {
        this.origin = o;
        this.destination = d;
        this.generation = g;
    }
}
