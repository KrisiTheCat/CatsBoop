package com.example.tiktaktoe;

public class Pawn {
    private int affiliation; //0 or 1
    private int size; // 0,1 or 2
    private int pos; // -1 to 8

    public Pawn() {
        this.affiliation = 0;
        this.size = 0;
        this.pos = -1;
    }

    public Pawn(int affiliation, int size) {
        this.affiliation = affiliation;
        this.size = size;
        this.pos = -1;
    }

    public boolean isFree(){
        return pos == -1;
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

    public int getPos() {
        return pos;
    }
    public void setPos(int pos) {
        this.pos = pos;
    }
}
