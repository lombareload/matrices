package com.fx.matrices.model;

public class Arista {
    final private int id;
    final private char from;
    final private char to;

    public Arista(int id, char from, char to) {
        this.id = id;
        this.from = from;
        this.to = to;
    }

    public boolean hasConnectionWith(char nodoCharacter) {
        if(nodoCharacter == from || nodoCharacter == to) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return "Arista{" +
                "id=" + id +
                ", from=" + from +
                ", to=" + to +
                '}';
    }

    public int getId() {
        return id;
    }

    public char getFrom() {
        return from;
    }

    public char getTo() {
        return to;
    }
}
