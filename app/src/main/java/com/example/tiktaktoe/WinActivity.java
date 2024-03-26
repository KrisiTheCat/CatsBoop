package com.example.tiktaktoe;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;

public class WinActivity extends AppCompatActivity {
//TODO toasts when rejected
//TODO play again - number
    String gameId = "";
    GameInfo gameInfo;
    String otherPlayer = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_win);

        gameId = getIntent().getExtras().get("game_id").toString();
        gameInfo = new GameInfo();
        DataBaseOp.setWinActivity(this);
        DataBaseOp.getGameInfo(gameId);
    }

    public void becomeGameInfo(DataSnapshot dataSnapshot){
        gameInfo = dataSnapshot.getValue(GameInfo.class);
        Log.d("krisi", gameInfo.getTurnName());
        assert gameInfo != null;
        initWinScreen();
    }

    @SuppressLint("SetTextI18n")
    private void initWinScreen(){
        TextView tvWin = (TextView) findViewById(R.id.tvWin);
        otherPlayer = (MainActivity.userMe.getUsername().equals(gameInfo.getPlayer0())) ? gameInfo.getPlayer1() : gameInfo.getPlayer0();
        switch(gameInfo.getGameState()) {
            case WIN_PLAYER0:
                tvWin.setText(gameInfo.getPlayer0() + " " + getString(R.string.win));
                break;
            case WIN_PLAYER1:
                tvWin.setText(gameInfo.getPlayer1() + " " + getString(R.string.win));
                break;
            default:
                tvWin.setText(getString(R.string.tie));
                break;
        }
    }

    public void backToMainMenu(View view) {
        DataBaseOp.deleteGame(gameId);
        DataBaseOp.playerStatus(MainActivity.userMe.getUsername(), true, "");
        Intent i = new Intent(this, MenuActivity.class);
        startActivity(i);
        finish();
    }
    public void playAgain(View view) {
        DataBaseOp.playerStatus(MainActivity.userMe.getUsername(), true, otherPlayer);
        DataBaseOp.deleteGame(gameId);
    }
}
