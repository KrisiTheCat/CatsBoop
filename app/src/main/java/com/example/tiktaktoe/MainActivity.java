package com.example.tiktaktoe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    public static User userMe = new User("");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Thread timer = new Thread(){
            public void run(){
                try{
                    sleep(1000);
                } catch (InterruptedException e){
                    e.printStackTrace();
                } finally {
                    Intent i = new Intent(getApplicationContext(), MenuActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        };
        timer.start();
    }

}
