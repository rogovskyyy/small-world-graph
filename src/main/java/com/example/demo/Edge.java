package com.example.demo;

import java.util.Objects;

public class Edge {

    public Vector2f v1, v2;

    public Edge(Vector2f v1, Vector2f v2) {
        this.v1 = v1;
        this.v2 = v2;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj.getClass() != this.getClass()) {
            return false;
        }
        final Edge other = (Edge) obj;
        if (!Objects.equals(this.v1, other.v1)) {
            return false;
        }
        if (!Objects.equals(this.v2, other.v2)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        return 1;
    }
}