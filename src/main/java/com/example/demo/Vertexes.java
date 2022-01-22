package com.example.demo;

import java.util.ArrayList;
import java.util.List;

public class Vertexes {
    public List<Vector2f> vertexes;
    public Vertexes() {
        this.vertexes = new ArrayList<>();
    }
    public void add(int v1, int v2) {
        vertexes.add(new Vector2f(v1, v2));
    }
}
