package com.example.game1app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Date;

public class FieldInitActivity extends AppCompatActivity {

    GridView host_field;
    FieldAdapter adapter;
    TextView info;
    TextView ship_type;
    Button flip_button;
    Button to_game_button;
    ImageView image_orientation;
    FieldSet processing;
    ArrayList<String> test;
    boolean ship_orientation;
    int ship_len;
    Resources res;

    String gameId;
    String role;

    FirebaseDatabase database;
    DatabaseReference myRef;
    DatabaseReference choiceRef;
    DatabaseReference userRef;

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_host);

        processing = new FieldSet();
        res = getResources();

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        Bundle arguments = getIntent().getExtras();
        role = arguments.get("role").toString();
        if (role.equals("host")){
            gameId = MakeCode();
        }
        else {
            gameId = arguments.get("gameId").toString();
        }

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference().child(gameId);
        choiceRef = database.getReference().child(gameId+"-u");
        userRef = database.getReference().child(currentUser.getUid());

        info = (TextView) findViewById(R.id.system_text);
        ship_type = (TextView) findViewById(R.id.ship_type);
        flip_button = (Button) findViewById(R.id.flip_button);
        to_game_button = (Button) findViewById(R.id.button_to_game);
        image_orientation = (ImageView) findViewById(R.id.image_orientation);

        test = new ArrayList<String>();
        for (int i = 0; i < 100; i++)
        {
            test.add("blank");
        }
        ship_orientation = true;
        ship_len = processing.getShip();
        ship_type.setText(Integer.toString(ship_len) + " deck");

        host_field = (GridView) findViewById(R.id.host_field);
        adapter = new FieldAdapter(this, R.layout.field_item, test);
        host_field.setAdapter(adapter);
        host_field.setOnItemClickListener(gridviewOnItemClickListener);

        flip_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ship_orientation = !ship_orientation;
                if (ship_orientation)
                    image_orientation.setImageResource(R.mipmap.nrizontal_foreground);
                else
                    image_orientation.setImageResource(R.mipmap.vertical_foreground);
            }
        });

        to_game_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ship_type.getText().equals(res.getString(R.string.no_ship))) {
                    Intent intent;
                    if (role.equals("host"))
                    {
                        intent = new Intent(FieldInitActivity.this, HostGameActivity.class);
                        for (int i = 0; i < 100; i++)
                        {
                            String value = test.get(i);
                            if (value.equals("used"))
                                value = "blank";
                            myRef.child("host").child(Integer.toString(i)).setValue(value);
                        }
                        myRef.child("currentUser").setValue("client");
                        myRef.child("winner").setValue("");
                        myRef.child("hostName").setValue(currentUser.getUid());
                        choiceRef.child("clientChoice").setValue(Integer.toString(100));
                    }
                    else
                    {
                        intent = new Intent(FieldInitActivity.this, ClientGameActivity.class);
                        for (int i = 0; i < 100; i++)
                        {
                            String value = test.get(i);
                            if (value.equals("used"))
                                value = "blank";
                            myRef.child("client").child(Integer.toString(i)).setValue(value);
                        }
                        myRef.child("winner").setValue("");
                        myRef.child("clientName").setValue(currentUser.getUid());
                        choiceRef.child("clientChoice").setValue(Integer.toString(100));
                    }
                    intent.putExtra("gameId", gameId);
                    userRef.child(gameId).setValue("");
                    startActivity(intent);
                }
                else
                {
                    info.setText(res.getString(R.string.locate_all_ships));
                }
            }
        });

    }

    private GridView.OnItemClickListener gridviewOnItemClickListener = new GridView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View v, int position,
                                long id) {
            if (ship_len != -1) {
                boolean ans = processing.Check(test, position, ship_len, ship_orientation);
                if (ans) {
                    test = processing.setShip(test, position, ship_len, ship_orientation);
                    host_field.setAdapter(adapter);
                    ship_len = processing.getShip();
                    if (ship_len != -1)
                        ship_type.setText(Integer.toString(ship_len) + " deck");
                    else
                        ship_type.setText(res.getString(R.string.no_ship));
                    info.setText("");
                } else {
                    info.setText(res.getString(R.string.impossible_locate));
                }
            }
        }
    };

    private String MakeCode()
    {
        String ans = "";
        Date date = new Date();
        long num = (long) date.getTime();
        while (num != 0)
        {
            int val = (int) (num % 26);
            num /= 26;
            ans += (char) (val + 65);
        }
        String newFormAns = "";
        int pos = ans.length() - 1;
        int posDev = 0;
        while(pos >= 0)
        {
            newFormAns += ans.charAt(pos);
            if (posDev == 4)
                newFormAns += "-";
            posDev++;
            pos--;
        }
        return newFormAns;
    }
}