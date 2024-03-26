package com.example.tiktaktoe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.List;

public class OnlineGameActivity extends AppCompatActivity {

    //TODO once selected the pawn size glows
    //TODO after setting place, the pawn size is unselected
    //TODO when player leaves

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
        DataBaseOp.setGameActivity(this);
        DataBaseOp.gameUpdates(gameId);

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

    public void gameUpdate(DataSnapshot dataSnapshot){
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
                        final View view1 = findViewById(R.id.iv_pulls10);
                        final View view2 = findViewById(R.id.ivLight);
                        int test1[] = new int[2];
                        view1.getLocationOnScreen(test1);
                        float x = test1[0];
                        float y = test1[1];

                        view2.setX(x);
                        view2.setY(y);
                        Log.d("krisi", "x: " + x);
                        Log.d("krisi", "y: " + y);
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

        if(gameInfo.getGameState() != GameStates.PLAYING && gameInfo.getGameState() != GameStates.INIT){
            Thread timer = new Thread(){
                public void run(){
                    try{
                        sleep(100);
                    } catch (InterruptedException e){
                        e.printStackTrace();
                    } finally {
                        Intent i = new Intent(getApplicationContext(), WinActivity.class);
                        i.putExtra("game_id", gameId);
                        startActivity(i);
                        finish();
                    }
                }
            };
            timer.start();
        }

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

    public void GameBoardClick(View view) {
        if (gameInfo.getTurn() != playerId) {
            Toast toast = Toast.makeText(OnlineGameActivity.this, "Not your turn", Toast.LENGTH_SHORT);
            toast.show();
        } else if (pickedSize == -1) {
            Toast toast = Toast.makeText(this, "Please select pull", Toast.LENGTH_SHORT);
            toast.show();
        } else if (gameInfo.getFreePawns(playerId, pickedSize) == 0) {
            Toast toast = Toast.makeText(this, "Not enough pulls", Toast.LENGTH_SHORT);
            toast.show();
        } else {
            pickedPosition = Integer.parseInt(view.getTag().toString());
            if (gameInfo.checkPossibleMove(playerId, pickedSize, pickedPosition)) {
                int pawnId = pickedSize * 3 + 3 - gameInfo.getFreePawns(playerId, pickedSize);
                gameInfo.movePawn(playerId, pawnId, pickedPosition);
                GameStates newState = gameInfo.checkWin();
                if (newState != GameStates.PLAYING) {
                    DataBaseOp.updateState(gameId, newState);
                }
                DataBaseOp.movePawn(gameId, playerId, pawnId, pickedPosition);
            } else {
                Toast toast = Toast.makeText(this, "Invalid play", Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }
}

