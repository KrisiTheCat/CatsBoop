package com.example.tiktaktoe;

public class Pawn {
    int affiliation; //1 or 2
    int size; // 1,2 or 3
    Coords pos; // position -1;-1 means unused

    public Pawn() {
        this.affiliation = 0;
        this.size = 0;
        this.pos = new Coords();
    }


    public Pawn(int affiliation, int size) {
        this.affiliation = affiliation;
        this.size = size;
        this.pos = new Coords();
    }

    public Coords getPos() {
        return pos;
    }

    public int getAffiliation() {
        return affiliation;
    }

    public void setAffiliation(int affiliation) {
        this.affiliation = affiliation;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void setPos(int x, int y) {
        this.pos = new Coords(x,y);
    }
    public void setPos(Coords pos) {
        this.pos = pos;
    }
}
