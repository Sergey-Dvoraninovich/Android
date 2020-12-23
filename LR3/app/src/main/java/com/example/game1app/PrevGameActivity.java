package com.example.game1app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.widget.GridView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class PrevGameActivity extends AppCompatActivity {

    GridView top_field;
    GridView down_field;
    FieldHidenAdapter hiden_adapter;
    FieldAdapter top_adapter;
    FieldAdapter down_adapter;
    ArrayList<String> host_list;
    ArrayList<String> client_list;

    TextView text;

    FirebaseDatabase database;
    DatabaseReference myRef;
    String gameId;
    Resources res;

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    String currentUserRole;
    boolean isHiden;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prev_game);

        Bundle arguments = getIntent().getExtras();
        gameId = arguments.get("gameId").toString();

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        currentUserRole = "host";
        isHiden = true;

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference().child(gameId);
        res = getResources();

        top_field = (GridView) findViewById(R.id.prev_game_gridview_top);
        down_field = (GridView) findViewById(R.id.prev_game_gridview_down);

        text = (TextView) findViewById(R.id.prev_game_text);

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String clientName = "";
                String hostName = "";
                String winner = "";
                ArrayList<String> host_temp = new ArrayList<String>();
                ArrayList<String> client_temp = new ArrayList<String>();

                DataSnapshot client_name = dataSnapshot.child("clientName");
                if (client_name.getValue(String.class) != null)
                    clientName = client_name.getValue(String.class);

                DataSnapshot host_name = dataSnapshot.child("hostName");
                hostName = host_name.getValue(String.class);

                DataSnapshot Winner = dataSnapshot.child("winner");
                winner = Winner.getValue(String.class);

                DataSnapshot host_items = dataSnapshot.child("host");
                for (DataSnapshot item : host_items.getChildren())
                {
                    host_temp.add(item.getValue(String.class));
                }

                DataSnapshot client_items = dataSnapshot.child("client");
                for (DataSnapshot item : client_items.getChildren())
                {
                    client_temp.add(item.getValue(String.class));
                }

                if (clientName.equals(""))
                {
                    text.setText(R.string.no_second_user);
                    currentUserRole = "host";
                    isHiden = true;
                }

                if (clientName.equals(currentUser.getUid()))
                {
                    currentUserRole = "client";
                    if (winner.equals("")) {
                        text.setText(R.string.game_not_finished);
                        isHiden = true;
                    }
                    else {
                        isHiden = false;
                        if (winner.equals("client"))
                            text.setText(R.string.you_win);
                        else
                            text.setText(R.string.you_lose);
                    }
                }

                if (hostName.equals(currentUser.getUid()) && !clientName.equals(""))
                {
                    currentUserRole = "host";
                    if (winner.equals("")) {
                        text.setText(R.string.game_not_finished);
                        isHiden = true;
                    }
                    else {
                        isHiden = false;
                        if (winner.equals("host"))
                            text.setText(R.string.you_win);
                        else
                            text.setText(R.string.you_lose);
                    }
                }

                host_list = host_temp;
                client_list = client_temp;

                InitAdapters(currentUserRole, isHiden);

                Log.d("TAG", "Value is: " + currentUser);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("TAG", "Failed to read value.", error.toException());
            }
        });
    }

    private void InitAdapters(String currentUserRole, boolean isHiden)
    {
        if (currentUserRole.equals("host"))
        {
            top_adapter = new FieldAdapter(this, R.layout.field_item, host_list);
            top_field.setAdapter(top_adapter);
            if (isHiden) {
                hiden_adapter = new FieldHidenAdapter(this, R.layout.field_item, client_list);
                down_field.setAdapter(hiden_adapter);
            }
            else {
                down_adapter = new FieldAdapter(this, R.layout.field_item, client_list);
                down_field.setAdapter(down_adapter);
            }
        }
        else{
            top_adapter = new FieldAdapter(this, R.layout.field_item, client_list);
            top_field.setAdapter(top_adapter);
            if (isHiden) {
                hiden_adapter = new FieldHidenAdapter(this, R.layout.field_item, host_list);
                down_field.setAdapter(hiden_adapter);
            }
            else {
                down_adapter = new FieldAdapter(this, R.layout.field_item, host_list);
                down_field.setAdapter(down_adapter);
            }
        }
    }
}