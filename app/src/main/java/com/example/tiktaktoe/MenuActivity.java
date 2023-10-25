package com.example.tiktaktoe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
    }

    public void startGame_SinglePlayer(View view) {
        Intent i = new Intent(this, OnlineGameActivity.class);
        startActivity(i);
    }

    public void EndGame(View view) {
        int pid = android.os.Process.myPid();
        android.os.Process.killProcess(pid);
    }

    public void ShowAboutNote(View view) {
        Intent i = new Intent(this, AboutActivity.class);
        startActivity(i);
    }

    public void StartGameOnline(View view){
        Intent i = new Intent(this, OnlineLoginActivity.class);
        startActivity(i);
    }
}