package com.example.tiktaktoe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class OnlineGameActivity extends AppCompatActivity {

    TextView tvPlayer1, tvPlayer2;

    String winnerString = "";

    String playerSession = "";
    String userName = "";
    String otherPlayer = "";
    String loginUID = "";
    String requestType = "";
    String myGameSign = "X";

    int gameState = 0;
    FirebaseDatabase database = FirebaseDatabase.getInstance("https://tik-tak-toe-7f9c3-default-rtdb.firebaseio.com/");
    DatabaseReference myRef = database.getReference();

    int activePlayer = 1;
    ArrayList<Integer> Player1 = new ArrayList<Integer>();
    ArrayList<Integer> Player2 = new ArrayList<Integer>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_game);

        userName = getIntent().getExtras().get("user_name").toString();
        loginUID = getIntent().getExtras().get("login_uid").toString();
        otherPlayer = getIntent().getExtras().get("other_player").toString();
        requestType = getIntent().getExtras().get("request_type").toString();
        playerSession = getIntent().getExtras().get("player_session").toString();

        tvPlayer1 = (TextView) findViewById(R.id.tvPlayer1);
        tvPlayer2 = (TextView) findViewById(R.id.tvPlayer2);

        gameState = 1;

        if(requestType.equals("From")){
            myGameSign = "O";
            tvPlayer1.setText("Your turn");
            tvPlayer2.setText("Your turn");
            myRef.child("playing").child(playerSession).child("turn").setValue(userName);
        } else {
            myGameSign = "X";
            tvPlayer1.setText(otherPlayer + "\'s turn");
            tvPlayer2.setText(otherPlayer + "\'s turn");
            myRef.child("playing").child(playerSession).child("turn").setValue(otherPlayer);
        }
        myRef.child("playing").child(playerSession).child("turn").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    String value = (String) dataSnapshot.getValue();
                    if(value.equals(userName)){
                        tvPlayer1.setText("Your turn");
                        tvPlayer2.setText("Your turn");
                        setEnableClick(true);
                        activePlayer = 1;
                    } else if (value.equals(otherPlayer)){
                        tvPlayer1.setText(otherPlayer + "\'s turn");
                        tvPlayer2.setText(otherPlayer + "\'s turn");
                        setEnableClick(false);
                        activePlayer = 2;
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        myRef.child("playing").child(playerSession).child("game").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    Player1.clear();
                    Player2.clear();
                    activePlayer = 2;
                    HashMap<String, Object> map = (HashMap<String, Object>) dataSnapshot.getValue();
                    if (map != null) {
                        String value = "";
                        String firstPlayer = userName;
                        for (String key : map.keySet()) {
                            value = (String) map.get(key);
                            if (value.equals(userName)) {
                                activePlayer = 2;
                            } else {
                                activePlayer = 1;
                            }
                            firstPlayer = value;
                            String[] splitID = key.split(":");
                            otherPlayer(Integer.parseInt(splitID[1]));
                        }
                    }
                } catch(Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    void otherPlayer(int selectedBlock) {
        ImageView selectedImage = (ImageView) findViewById(R.id.iv_11);
        switch (selectedBlock){
            case 1: selectedImage = (ImageView) findViewById(R.id.iv_11); break;
            case 2: selectedImage = (ImageView) findViewById(R.id.iv_12); break;
            case 3: selectedImage = (ImageView) findViewById(R.id.iv_13); break;
            case 4: selectedImage = (ImageView) findViewById(R.id.iv_21); break;
            case 5: selectedImage = (ImageView) findViewById(R.id.iv_22); break;
            case 6: selectedImage = (ImageView) findViewById(R.id.iv_23); break;
            case 7: selectedImage = (ImageView) findViewById(R.id.iv_31); break;
            case 8: selectedImage = (ImageView) findViewById(R.id.iv_32); break;
            case 9: selectedImage = (ImageView) findViewById(R.id.iv_33); break;
        }
        PlayGame(selectedBlock,selectedImage);
    }

    void ResetGame(){

        myRef.child("playing").child(playerSession).removeValue();
        winnerString = "";

        gameState = 1;
        activePlayer = 1;
        Player1.clear();
        Player2.clear();

        ImageView iv;
        iv = (ImageView) findViewById(R.id.iv_11); iv.setImageResource(0); iv.setEnabled(true);
        iv = (ImageView) findViewById(R.id.iv_12); iv.setImageResource(0); iv.setEnabled(true);
        iv = (ImageView) findViewById(R.id.iv_13); iv.setImageResource(0); iv.setEnabled(true);
        iv = (ImageView) findViewById(R.id.iv_21); iv.setImageResource(0); iv.setEnabled(true);
        iv = (ImageView) findViewById(R.id.iv_22); iv.setImageResource(0); iv.setEnabled(true);
        iv = (ImageView) findViewById(R.id.iv_23); iv.setImageResource(0); iv.setEnabled(true);
        iv = (ImageView) findViewById(R.id.iv_31); iv.setImageResource(0); iv.setEnabled(true);
        iv = (ImageView) findViewById(R.id.iv_32); iv.setImageResource(0); iv.setEnabled(true);
        iv = (ImageView) findViewById(R.id.iv_33); iv.setImageResource(0); iv.setEnabled(true);

        if(userName == winnerString){
            myRef.child("playing").child(playerSession).child("turn").setValue(otherPlayer);
            requestType = "To";
            setEnableClick(false);
            activePlayer = 2;
            myGameSign = "X";
        } else {
            myRef.child("playing").child(playerSession).child("turn").setValue(userName);
            requestType = "From";
            setEnableClick(true);
            activePlayer = 1;
            myGameSign = "O";
        }
    }

    public void GameBoardClick(View view){
        System.out.println("GAMEBOARDCLICK " + userName);
        ImageView selectedImage = (ImageView) view;

        if(playerSession.length() <= 0){
            Intent i = new Intent(getApplicationContext(), OnlineLoginActivity.class);
            startActivity(i);
            finish();
        } else {
            int selectedBlock = 0;
            switch(selectedImage.getId()){
                case R.id.iv_11: selectedBlock = 1; break;
                case R.id.iv_12: selectedBlock = 2; break;
                case R.id.iv_13: selectedBlock = 3; break;
                case R.id.iv_21: selectedBlock = 4; break;
                case R.id.iv_22: selectedBlock = 5; break;
                case R.id.iv_23: selectedBlock = 6; break;
                case R.id.iv_31: selectedBlock = 7; break;
                case R.id.iv_32: selectedBlock = 8; break;
                case R.id.iv_33: selectedBlock = 9; break;
            }
            myRef.child("playing").child(playerSession).child("game").child("block:"+selectedBlock).setValue(userName);
            myRef.child("playing").child(playerSession).child("turn").setValue(otherPlayer);
            ImageView iv = (ImageView) findViewById(R.id.iv_11);
            if(iv.isClickable()){
                setEnableClick(false);
            } else {
                setEnableClick(true);
            }
            if(activePlayer == 1) activePlayer = 2;
            else activePlayer = 1;
            PlayGame(selectedBlock, selectedImage);
        }
    }

    void PlayGame(int selectedBlock, ImageView selectedImage){
        if(gameState == 1){
            if(activePlayer == 1){
                selectedImage.setImageResource(R.drawable.ttt_x);
                Player1.add(selectedBlock);
            } else if(activePlayer == 2){
                selectedImage.setImageResource(R.drawable.ttt_o);
                Player2.add(selectedBlock);
            }
            selectedImage.setEnabled(false);
            CheckWinner();
        }
    }

    void CheckWinner(){
        int winner = 0;

        /*****************************     Player 1    *********************************/
        if(Player1.contains(1) && Player1.contains(2) && Player1.contains(3)) winner = 1;
        if(Player1.contains(4) && Player1.contains(5) && Player1.contains(6)) winner = 1;
        if(Player1.contains(7) && Player1.contains(8) && Player1.contains(9)) winner = 1;
        if(Player1.contains(1) && Player1.contains(4) && Player1.contains(7)) winner = 1;
        if(Player1.contains(2) && Player1.contains(5) && Player1.contains(8)) winner = 1;
        if(Player1.contains(3) && Player1.contains(6) && Player1.contains(9)) winner = 1;
        if(Player1.contains(1) && Player1.contains(5) && Player1.contains(9)) winner = 1;
        if(Player1.contains(3) && Player1.contains(5) && Player1.contains(7)) winner = 1;

        /*****************************     Player 2    *********************************/
        if(Player2.contains(1) && Player2.contains(2) && Player2.contains(3)) winner = 2;
        if(Player2.contains(4) && Player2.contains(5) && Player2.contains(6)) winner = 2;
        if(Player2.contains(7) && Player2.contains(8) && Player2.contains(9)) winner = 2;
        if(Player2.contains(1) && Player2.contains(4) && Player2.contains(7)) winner = 2;
        if(Player2.contains(2) && Player2.contains(5) && Player2.contains(8)) winner = 2;
        if(Player2.contains(3) && Player2.contains(6) && Player2.contains(9)) winner = 2;
        if(Player2.contains(1) && Player2.contains(5) && Player2.contains(9)) winner = 2;
        if(Player2.contains(3) && Player2.contains(5) && Player2.contains(7)) winner = 2;

        if(winner != 0 && gameState == 1){
            if(winner == 1){
                showAlert(otherPlayer + " is winner");
                winnerString = otherPlayer;
            } else {
                showAlert("You won the game");
                winnerString = userName;
            }
            gameState = 2;
        }

        ArrayList<Integer> emptyBlocks = new ArrayList<Integer>();
        for(int i = 1; i <= 9 ; i++){
            if(!(Player1.contains(i) || Player2.contains(i))){
                emptyBlocks.add(i);
            }
        }
        if(emptyBlocks.size() == 0){
            if(gameState == 1){
                AlertDialog.Builder b = new AlertDialog.Builder(this,android.R.style.Theme_Material_Dialog_Alert);
                showAlert("Draw");
            }
            gameState = 2;
        }
    }

    void showAlert(String title){
        AlertDialog.Builder b = new AlertDialog.Builder(this, R.style.TransperentDialog);
        b.setTitle(title)
                .setMessage("Start a new game?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ResetGame();
                    }
                })
                .setNegativeButton("Menu", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent i = new Intent(getApplicationContext(), MenuActivity.class);
                        startActivity(i);
                        finish();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    void setEnableClick(boolean trueOrFalse) {
        ImageView iv;
        iv = (ImageView) findViewById(R.id.iv_11); iv.setClickable(trueOrFalse);
        iv = (ImageView) findViewById(R.id.iv_12); iv.setClickable(trueOrFalse);
        iv = (ImageView) findViewById(R.id.iv_13); iv.setClickable(trueOrFalse);
        iv = (ImageView) findViewById(R.id.iv_21); iv.setClickable(trueOrFalse);
        iv = (ImageView) findViewById(R.id.iv_22); iv.setClickable(trueOrFalse);
        iv = (ImageView) findViewById(R.id.iv_23); iv.setClickable(trueOrFalse);
        iv = (ImageView) findViewById(R.id.iv_31); iv.setClickable(trueOrFalse);
        iv = (ImageView) findViewById(R.id.iv_32); iv.setClickable(trueOrFalse);
        iv = (ImageView) findViewById(R.id.iv_33); iv.setClickable(trueOrFalse);
        if(trueOrFalse){
            tvPlayer1.setText("Your turn");
            tvPlayer2.setText("Your turn");
        } else {
            tvPlayer1.setText(otherPlayer + "\'s turn");
            tvPlayer2.setText(otherPlayer + "\'s turn");
        }
    }
}
