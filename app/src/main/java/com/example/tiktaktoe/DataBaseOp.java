package com.example.tiktaktoe;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DataBaseOp {


    private static FirebaseDatabase database = FirebaseDatabase.getInstance("https://tik-tak-toe-7f9c3-default-rtdb.firebaseio.com/");
    private static DatabaseReference myRef = database.getReference();

    private static OnlineLoginActivity loginActivity;

    public static void playerStatus(String playerName, boolean status){
        if(status){
            myRef.child("online").child(playerName).setValue("open");
        }
        else {
            myRef.child("online").child(playerName).removeValue();
        }
    }

    public static void onlineUserChanges(MyInterfaceDataSnapshot func) {
        myRef.child("online").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                func.apply(snapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public static void movePawn(String gameId, int playerId, int pawnId, int position) {
        myRef.child("games").child(gameId)
                .child("pawns").child(playerId + "")
                .child(pawnId+"").child("pos").setValue(position);

    }
    public static void onlinePlayerStatusUpd(String playerName, MyInterfaceString recieveReq, MyInterfaceString startGame){

        myRef.child("online").child(playerName+"").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChildren()) {
                    for (DataSnapshot child : snapshot.getChildren()) {
                        recieveReq.apply(child.getValue().toString());
                    }
                }
                else if(!snapshot.getValue().toString().equals("open")){
                    startGame.apply(snapshot.getValue().toString());
                    myRef.child("online").child(playerName+"").removeEventListener(this);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public static void sendInvitation(String playerFrom, String playerTo){
        myRef.child("online").child(playerTo).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                myRef.child("online").child(playerTo).child(snapshot.getChildrenCount()+"").setValue(playerFrom);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public static void createGame(String player1, String player2){
        DatabaseReference newGame = myRef.child("games").push();
        myRef.child("online").child(player1).setValue(newGame.getKey());
        myRef.child("online").child(player2).setValue(newGame.getKey());
        GameInfo gameInfo = new GameInfo(player1, player2);
        newGame.setValue(gameInfo);
    }
    public static void gameUpdates(String gameId, MyInterfaceDataSnapshot func){
        myRef.child("games").child(gameId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                func.apply(snapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public static void initGame(String gameId, MyInterfaceDataSnapshot initGame) {
        myRef.child("games").child(gameId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                initGame.apply(snapshot);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
