package com.example.tiktaktoe;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DataBaseOp {


    private static FirebaseDatabase database = FirebaseDatabase.getInstance("https://tik-tak-toe-7f9c3-default-rtdb.firebaseio.com/");
    private static DatabaseReference myRef = database.getReference();

    public static void playerStatus(String playerName, boolean status){
        Log.d("Debug", playerName + " " +  status);
        if(status){
            myRef.child("online").child(playerName).setValue("open");
        }
        else {
            myRef.child("online").child(playerName).removeValue();
        }
    }

    public static void onlineUserChanges(MyInterface func) {
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

    public static void waitForRequest(String playerName, InterfaceRequest recieveReq){
        myRef.child("online").child(playerName+"").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d("DEBUG", snapshot.toString());
                if(snapshot.hasChildren()) {
                    for (DataSnapshot child : snapshot.getChildren()) {
                        recieveReq.apply(child.getValue().toString());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public static boolean sendInvitation(String playerFrom, String playerTo){
        final boolean[] flag = {false};
        myRef.child("online").child(playerTo).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d("DEBUG", "here2");
                if(snapshot.getValue() != "open"){
                    myRef.child("online").child(playerTo).child("0").setValue(playerFrom);
                    flag[0] = true;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        Log.d("DEBUG", "here " + flag.toString());
        return false;
    }
    public static void createGame(String player1, String player2){
        DatabaseReference newGame = myRef.child("games").push();
        newGame.child("player1").setValue(player1);
        newGame.child("player2").setValue(player2);
        myRef.child("online").child(player1).setValue(newGame.getKey());
        myRef.child("online").child(player2).setValue(newGame.getKey());
    }
}
