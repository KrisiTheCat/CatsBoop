package com.example.tiktaktoe;

public class User {
    private String username;
    private String gameId;

    public User(String username) {
        this.username = username;
        this.gameId = "";
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }
}
