package com.example.tiktaktoe;

public class PlayerInfo {
    private String name;
    private int pulls1, pulls2, pulls3;

    public PlayerInfo() {
        name = "";
        pulls1 = 0;
        pulls2 = 0;
        pulls3 = 0;
    }
    public PlayerInfo(String name) {
        this.name = name;
        pulls1 = 0;
        pulls2 = 0;
        pulls3 = 0;
    }

}
