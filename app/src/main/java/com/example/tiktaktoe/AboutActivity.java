package com.example.tiktaktoe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
    }

    public void backToMainMenu(View view) {
        Intent i = new Intent(this, MenuActivity.class);
        startActivity(i);
        finish();
    }
}
