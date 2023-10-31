package com.example.tiktaktoe;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.List;

public class OnlineGameActivity extends AppCompatActivity {

    String gameId = "";
    GameInfo gameInfo;
    int playerId = -1;

    int pickedSize = -1;
    int pickedPosition = -1;

    List<List<TextView>> tvPlayersPulls;
    List<ImageView> ivPositions;
    List<List<Integer>> drPulls;
    TextView tvTurn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_game);

        gameId = getIntent().getExtras().get("game_id").toString();
        gameInfo = new GameInfo();
        MyInterfaceDataSnapshot UpdateGame = new MyInterfaceDataSnapshot() {
            @Override
            public void apply(DataSnapshot dataSnapshot) {
                if (gameInfo.getGameState() == GameStates.INIT) {
                    gameInfo = dataSnapshot.getValue(GameInfo.class);
                    Log.d("krisi", gameInfo.toString());
                    initBoard();
                    refreshBoard();

                } else {
                    gameInfo = dataSnapshot.getValue(GameInfo.class);
                    refreshBoard();
                }
            }
        };
        DataBaseOp.gameUpdates(gameId, UpdateGame);

        drPulls = new ArrayList<>();
        drPulls.add(new ArrayList<>());
        drPulls.get(0).add(R.drawable.pull_0_0);
        drPulls.get(0).add(R.drawable.pull_0_1);
        drPulls.get(0).add(R.drawable.pull_0_2);
        drPulls.add(new ArrayList<>());
        drPulls.get(1).add(R.drawable.pull_1_0);
        drPulls.get(1).add(R.drawable.pull_1_1);
        drPulls.get(1).add(R.drawable.pull_1_2);
        //DataBaseOp.gameUpdatesPawns(gameId, this);

    }

    private void initBoard(){
        playerId = (gameInfo.getPlayer0().equals(MainActivity.userMe.getUsername())) ? 0 : 1;
        TextView tvPlayer0 = (TextView) findViewById(R.id.tvPlayer0);
        TextView tvPlayer1 = (TextView) findViewById(R.id.tvPlayer1);
        tvPlayer0.setText(gameInfo.getPlayer0());
        tvPlayer1.setText(gameInfo.getPlayer1());

        tvPlayersPulls = new ArrayList<>();
        tvPlayersPulls.add(new ArrayList<>());
        tvPlayersPulls.get(0).add((TextView) findViewById(R.id.tv_pulls00));
        tvPlayersPulls.get(0).add((TextView) findViewById(R.id.tv_pulls01));
        tvPlayersPulls.get(0).add((TextView) findViewById(R.id.tv_pulls02));
        tvPlayersPulls.add(new ArrayList<>());
        tvPlayersPulls.get(1).add((TextView) findViewById(R.id.tv_pulls10));
        tvPlayersPulls.get(1).add((TextView) findViewById(R.id.tv_pulls11));
        tvPlayersPulls.get(1).add((TextView) findViewById(R.id.tv_pulls12));

        List<ImageView> pulls = new ArrayList<>();
        if(playerId==0){
            pulls.add((ImageView) findViewById(R.id.iv_pulls00));
            pulls.add((ImageView) findViewById(R.id.iv_pulls01));
            pulls.add((ImageView) findViewById(R.id.iv_pulls02));
        }
        else {
            pulls.add((ImageView) findViewById(R.id.iv_pulls10));
            pulls.add((ImageView) findViewById(R.id.iv_pulls11));
            pulls.add((ImageView) findViewById(R.id.iv_pulls12));
        }
        for(int i = 0; i < 3; i++){
            pulls.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view){
                    if(gameInfo.getTurn() != playerId) {
                        Toast toast = Toast.makeText(OnlineGameActivity.this, "Not your turn", Toast.LENGTH_SHORT);
                        toast.show();
                        return;
                    }
                    pickedSize = Integer.parseInt(view.getTag().toString());
                    if(gameInfo.getFreePawns(playerId, pickedSize) == 0) {
                        pickedSize = -1;
                        Toast toast = Toast.makeText(OnlineGameActivity.this, "Not enough pawns", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                    else {
                        Log.d("krisi", "pickedSize: " + pickedSize);
                    }

                }
            });
        }

        ivPositions = new ArrayList<>();
        ivPositions.add((ImageView) findViewById(R.id.iv_11));
        ivPositions.add((ImageView) findViewById(R.id.iv_12));
        ivPositions.add((ImageView) findViewById(R.id.iv_13));
        ivPositions.add((ImageView) findViewById(R.id.iv_21));
        ivPositions.add((ImageView) findViewById(R.id.iv_22));
        ivPositions.add((ImageView) findViewById(R.id.iv_23));
        ivPositions.add((ImageView) findViewById(R.id.iv_31));
        ivPositions.add((ImageView) findViewById(R.id.iv_32));
        ivPositions.add((ImageView) findViewById(R.id.iv_33));

        tvTurn = (TextView) findViewById(R.id.tvTurn);

        /*myRef.child("playing").child(playerSession).child("turn").addValueEventListener(new ValueEventListener() {
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
        });*/
    }

    public void refreshBoard(){

        tvTurn.setText(gameInfo.getTurnName());

        for(int pl = 0; pl < 2; pl++){
            for(int s = 0; s < 3; s++){
                tvPlayersPulls.get(pl).get(s).setText("x" + gameInfo.getFreePawns(pl, s));
            }
        }
        for(int i = 0; i < 9; i++){
            for(int pl = 0; pl < 2; pl++){
                int pos = gameInfo.getPawns().get(pl).get(i).getPos();
                if(pos != -1) {
                    Log.d("krisi", "Pawn " + pl + " " + i + ": " + pos);
                    ivPositions.get(pos).setBackgroundResource(drPulls.get(pl).get(i/3));
                }
            }
        }
        Log.d("krisi", gameInfo.toString());
    }

    /*void otherPlayer(int selectedBlock) {
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
        //PlayGame(selectedBlock,selectedImage);
    }*/

    public void GameBoardClick(View view){
        if(gameInfo.getTurn() != playerId) {
            Toast toast = Toast.makeText(OnlineGameActivity.this, "Not your turn", Toast.LENGTH_SHORT);
            toast.show();
        }
        else if(pickedSize == -1){
            Toast toast = Toast.makeText(this, "Please select pull", Toast.LENGTH_SHORT);
            toast.show();
        }
        else if(gameInfo.getFreePawns(playerId, pickedSize) == 0){
            Toast toast = Toast.makeText(this, "Not enough pulls", Toast.LENGTH_SHORT);
            toast.show();
        }
        else {
            pickedPosition = Integer.parseInt(view.getTag().toString());
            Log.d("krisi", "pickedPosition: " + pickedPosition);
            if(gameInfo.checkPossibleMove(playerId, pickedSize, pickedPosition)){
                int pawnId = pickedSize*3 + 3-gameInfo.getFreePawns(playerId, pickedSize);
                gameInfo.movePawn(playerId, pawnId, pickedPosition);
                GameStates newState = gameInfo.checkWin();
                Log.d("krisi", newState.toString());
                if(newState != GameStates.PLAYING) {
                    Log.d("krisi", "inside "+newState.toString());
                    DataBaseOp.updateState(gameId, newState);
                }
                DataBaseOp.movePawn(gameId, playerId, pawnId, pickedPosition);
            }
            else {
                Toast toast = Toast.makeText(this, "Invalid play", Toast.LENGTH_SHORT);
                toast.show();
            }
        }


        /*
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
        }*/
    }

    /*
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

        if(Player1.contains(1) && Player1.contains(2) && Player1.contains(3)) winner = 1;
        if(Player1.contains(4) && Player1.contains(5) && Player1.contains(6)) winner = 1;
        if(Player1.contains(7) && Player1.contains(8) && Player1.contains(9)) winner = 1;
        if(Player1.contains(1) && Player1.contains(4) && Player1.contains(7)) winner = 1;
        if(Player1.contains(2) && Player1.contains(5) && Player1.contains(8)) winner = 1;
        if(Player1.contains(3) && Player1.contains(6) && Player1.contains(9)) winner = 1;
        if(Player1.contains(1) && Player1.contains(5) && Player1.contains(9)) winner = 1;
        if(Player1.contains(3) && Player1.contains(5) && Player1.contains(7)) winner = 1;

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
    */
}

