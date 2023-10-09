package com.example.tiktaktoe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.icu.text.Edits;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.security.cert.Extension;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class OnlineLoginActivity extends AppCompatActivity {

    ListView lv_loginUsers;
    ArrayList<String> list_loginUsers = new ArrayList<String>();
    ArrayAdapter adpt;

    ListView lv_requestedUsers;
    ArrayList<String> list_requestedUsers = new ArrayList<String>();
    ArrayAdapter reqUsersAdpt;

    TextView tvUserID, tvSendRequest;
    String LoginUserID, UserName, LoginUID;

    //Button btnLogin;
    //EditText etEmail;

    FirebaseDatabase database = FirebaseDatabase.getInstance("https://tik-tak-toe-7f9c3-default-rtdb.firebaseio.com/");
    DatabaseReference myRef = database.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_login);

        JoinOnlineGame();

        tvSendRequest = (TextView) findViewById(R.id.tvSendRequest);
        tvSendRequest.setText("Please wait...");

        MyInterface UpdateLoginUsers = new MyInterface() {
            @Override
            public void apply(DataSnapshot dataSnapshot) {
                String key = "";
                Object val="";
                Class<?> type;
                Set<String> set = new HashSet<String>();
                Iterator i = dataSnapshot.getChildren().iterator();

                while(i.hasNext()){
                    key = ((DataSnapshot) i.next()).getKey();
                    val = ((DataSnapshot) i.next()).getValue();
                    type = val.getClass();
                    if((val=="open" || type.isArray()) && !key.equalsIgnoreCase(MainActivity.userMe.getUsername())){
                        set.add(key);
                    }
                }

                adpt.clear();
                adpt.addAll(set);
                adpt.notifyDataSetChanged();
                tvSendRequest.setText("Send request to");
            }
        };
        DataBaseOp.onlineUserChanges(UpdateLoginUsers);

        adpt = new ArrayAdapter(this, android.R.layout.simple_list_item_1, list_loginUsers){

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                View view = super.getView(position, convertView, parent);
                TextView text = (TextView) view.findViewById(android.R.id.text1);
                text.setTextColor(Color.WHITE);

                return view;
            }
        };
        lv_loginUsers = (ListView) findViewById(R.id.lv_loginUsers);
        lv_loginUsers.setAdapter(adpt);

        lv_loginUsers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final String requestToUser = ((TextView)view).getText().toString();
                sendInvite(requestToUser);
            }
        });

    }

    void sendInvite(final String OtherPlayer){
        AlertDialog.Builder b = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.connect_player_dialog, null);
        b.setView(dialogView);

        b.setTitle("Outgoing Invitation");
        b.setMessage("Do you wish to play with " + OtherPlayer + "?");
        b.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DataBaseOp.sendInvitation(MainActivity.userMe.getUsername(), OtherPlayer);
//                myRef.child("users").child(OtherPlayer).child("request").push().setValue(LoginUserID);
//                StartGame(OtherPlayer + ":" + UserName, OtherPlayer, "From");
            }
        });
        b.setNegativeButton("Back", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        b.show();
    }

    void confirmInvite(final String OtherPlayer){
        AlertDialog.Builder b = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.connect_player_dialog, null);
        b.setView(dialogView);

        b.setTitle("Incoming Invitation");
        b.setMessage("Do you wish to play with " + OtherPlayer + "?");
        b.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DataBaseOp.createGame(MainActivity.userMe.getUsername(), OtherPlayer);
//                myRef.child("users").child(OtherPlayer).child("request").push().setValue(LoginUserID);
//                StartGame(OtherPlayer + ":" + UserName, OtherPlayer, "From");
            }
        });
        b.setNegativeButton("Back", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        b.show();
    }


    void StartGame(String PlayerGameID, String OtherPlayer, String requestType){
        myRef.child("playing").child(PlayerGameID).removeValue();
        Intent i = new Intent(getApplicationContext(), OnlineGameActivity.class);
        i.putExtra("player_session", PlayerGameID);
        i.putExtra("user_name", UserName);
        i.putExtra("login_uid", LoginUID);
        i.putExtra("other_player", OtherPlayer);
        i.putExtra("request_type", requestType);
        startActivity(i);
        finish();
    }

    void JoinOnlineGame(){
        final AlertDialog.Builder b = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.login_dialog, null);
        b.setView(dialogView);

        final EditText etUsername = (EditText) dialogView.findViewById(R.id.etUsername);

        b.setTitle("Username");
        b.setMessage("Please enter how you wish to be called");
        b.setPositiveButton("Register", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                MainActivity.userMe.setUsername(etUsername.getText().toString());
                DataBaseOp.playerStatus(etUsername.getText().toString(), true);
                InterfaceRequest interfaceRequest = new InterfaceRequest() {
                    @Override
                    public void apply(String name) {
                        confirmInvite(name);
                    }
                };
                DataBaseOp.waitForRequest(MainActivity.userMe.getUsername(), interfaceRequest);
            }
        });
        b.setNegativeButton("Back", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent i = new Intent(getApplicationContext(), MenuActivity.class);
                startActivity(i);
                finish();
            }
        });
        b.show();
    }

    @Override
    public void onStop() {
        super.onStop();
        DataBaseOp.playerStatus(MainActivity.userMe.getUsername(), false);
    }

}

interface MyInterface {
    void apply(DataSnapshot dataSnapshot);
}
interface InterfaceRequest {
    void apply(String name);
}