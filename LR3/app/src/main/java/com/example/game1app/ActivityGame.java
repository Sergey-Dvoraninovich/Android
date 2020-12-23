package com.example.game1app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ActivityGame extends AppCompatActivity {

    Button new_game_button;
    Button join_game_button;
    TextView testText;
    EditText gameId_text;
    FirebaseDatabase database;
    DatabaseReference myRef;
    //GameData gameData;
    String child = "1";
    Resources res;

    String gameId = "test_game";

    Map<String, String> gameDict = new HashMap<String, String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        new_game_button = (Button) findViewById(R.id.new_game_button) ;
        join_game_button = (Button) findViewById(R.id.join_game_button);
        testText = (TextView) findViewById(R.id.join_text);
        gameId_text = (EditText) findViewById(R.id.enter_code_edittext);
        res = getResources();

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

        new_game_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ActivityGame.this, FieldInitActivity.class);
                i.putExtra("role", "host");
                startActivity(i);
            }
        });

        join_game_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = gameId_text.getText().toString();
                if (gameDict.get(code) != null) {
                    if (gameDict.get(code).equals("")) {
                        Intent i = new Intent(ActivityGame.this, FieldInitActivity.class);
                        i.putExtra("gameId", code);
                        i.putExtra("role", "client");
                        startActivity(i);
                    }
                    else {
                        Toast toast = Toast.makeText(getApplicationContext(), res.getString(R.string.game_already_started), Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }
                else {
                    Toast toast = Toast.makeText(getApplicationContext(), res.getString(R.string.no_such_game), Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot item : dataSnapshot.getChildren())
                {
                    String client = "";
                    if (item.child("clientName").getValue() != null)
                        client = item.child("clientName").getValue(String.class);
                    gameDict.put(item.getKey(), client);
                }

                Log.d("TAG", "Value");
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("TAG", "Failed to read value.", error.toException());
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.app_settings) {
            Intent i = new Intent(ActivityGame.this, AccountActivity.class);
            startActivity(i);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}