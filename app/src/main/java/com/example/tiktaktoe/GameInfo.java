package com.example.tiktaktoe;

import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GameInfo {
    private List<List<Pawn>> pawns;
    private int turn;
    private String player0, player1;

    private GameStates gameState;

    public GameInfo() {
        this.player0 = "";
        this.player1 = "";
        pawns = new ArrayList<>();
        this.turn = 0;
        this.gameState = GameStates.INIT;
    }
    public GameInfo(String player0, String player1) {
        this.player0 = player0;
        this.player1 = player1;
        pawns = new ArrayList<>();
        for(int pl = 0; pl < 2; pl++){
            pawns.add(new ArrayList<>());
            for(int si = 0; si < 3; si++){
                for(int i = 0; i < 3; i++){
                    pawns.get(pl).add(new Pawn(pl, si));
                }
            }
        }
        this.turn = 0;
        this.gameState = GameStates.PLAYING;
    }

    public int getFreePawns(int player, int size){
        int count = 0;
        for(int i = 0; i < 3; i++)
            if(pawns.get(player).get(size*3 + i).isFree())
                count++;
        return count;
    }
    public int getFreePawns(){
        int count = 0;
        for(int pl = 0; pl < 2; pl++)
            for(int i = 0; i < 9; i++)
                if(pawns.get(pl).get(i).isFree())
                    count++;
        return count;
    }

    public boolean checkPossibleMove(int player, int size, int position){
        if(getFreePawns(player,size) == 0) return false;
        for(int pl = 0; pl < 2; pl++) {
            for (int s = 0; s < 3; s++) {
                for (int i = 0; i < 3; i++) {
                    if (pawns.get(pl).get(s * 3 + i).getPos() == position && s >= size) {
                        return false;
                    }
                }
            }
        }
        return true;
    }
    public void movePawn(int player, int pawnId, int position){
        pawns.get(player).get(pawnId).setPos(position);
    }

    public GameStates checkWin(){
        int[][] grid = new int[3][3];
        int pos = 0;
        for (int i = 0; i < 9; i++){
            for (int pl = 0; pl < 2; pl++){
                pos = pawns.get(pl).get(i).getPos();
                if(pos!=-1) grid[pos/3][pos%3] = pl+1;
            }
        }
        if(grid[0][0] != 0 && grid[0][0] == grid[1][1] && grid[1][1] == grid[2][2])
            return GameStates.valueOf("WIN_PLAYER" + (grid[1][1]-1));
        if(grid[2][0] != 0 && grid[2][0] == grid[1][1] && grid[1][1] == grid[0][2])
            return GameStates.valueOf("WIN_PLAYER" + (grid[1][1]-1));
        for(int i = 0; i < 3; i++){
            if(grid[i][0] != 0 && grid[i][0] == grid[i][1] && grid[i][1] == grid[i][2])
                return GameStates.valueOf("WIN_PLAYER" + (grid[i][1]-1));
            if(grid[0][i] != 0 && grid[0][i] == grid[1][i] && grid[1][i] == grid[2][i])
                return GameStates.valueOf("WIN_PLAYER" + (grid[1][i]-1));
        }
        if(getFreePawns() == 0) return GameStates.TIE;
        return GameStates.PLAYING;
    }

    public String getTurnName() {
        return (turn == 0) ? player0 : player1;
    }

    public List<List<Pawn>> getPawns() {
        return pawns;
    }

    public void setPawns(List<List<Pawn>> pawns) {
        this.pawns = pawns;
    }

    public int getTurn() {
        return turn;
    }

    public void setTurn(int turn) {
        this.turn = turn;
    }

    public String getPlayer0() {
        return player0;
    }

    public void setPlayer0(String player0) {
        this.player0 = player0;
    }

    public String getPlayer1() {
        return player1;
    }

    public void setPlayer1(String player1) {
        this.player1 = player1;
    }

    public GameStates getGameState() {
        return gameState;
    }

    public void setGameState(GameStates gameState) {
        this.gameState = gameState;
    }
}
