package com.example.demo;

import java.util.ArrayList;
import java.util.List;

public class Edges {
    public List<Edge> edges;
    public Edges() {
        this.edges = new ArrayList<>();
    }
    public void add(Vector2f v1, Vector2f v2) {
        this.edges.add(new Edge(v1, v2));
    }
}