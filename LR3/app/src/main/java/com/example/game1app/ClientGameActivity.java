package com.example.game1app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ClientGameActivity extends AppCompatActivity {

    GridView host_field;
    GridView client_field;
    FieldHidenAdapter host_adapter;
    FieldAdapter client_adapter;
    ArrayList<String> host_list;
    ArrayList<String> client_list;

    TextView text_top;
    TextView text_down;

    FirebaseDatabase database;
    DatabaseReference myRef;
    DatabaseReference choiceRef;
    String gameId;
    Resources res;

    boolean ableToShoot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_game);

        Bundle arguments = getIntent().getExtras();
        gameId = arguments.get("gameId").toString();

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference().child(gameId);
        choiceRef = database.getReference().child(gameId+"-u");
        initData();
        res = getResources();

        host_field = (GridView) findViewById(R.id.client_game_gridview_down);
        hostAdapterInit();

        host_field.setOnItemClickListener(gridviewHostOnItemClickListener);

        client_field = (GridView) findViewById(R.id.client_game_gridview_top);
        clientAdapterInit();

        text_top = (TextView) findViewById(R.id.client_game_text_top);
        text_down = (TextView) findViewById(R.id.client_game_text_down);

        ableToShoot = true;

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String currentUser = "";
                String winner = "";
                ArrayList<String> host_temp = new ArrayList<String>();
                ArrayList<String> client_temp = new ArrayList<String>();

                DataSnapshot user_item = dataSnapshot.child("currentUser");
                currentUser = user_item.getValue(String.class);

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

                host_list = host_temp;
                client_list = client_temp;

                hostAdapterInit();
                clientAdapterInit();

                if (currentUser.equals("client"))
                {
                    ableToShoot = true;
                    text_down.setText(R.string.your_move);
                }
                else
                {
                    ableToShoot = false;
                    text_down.setText(R.string.wait);
                }

                if (winner.equals("client"))
                {
                    ableToShoot = false;
                    text_down.setText(res.getString(R.string.you_win));
                }
                if (winner.equals("host"))
                {
                    ableToShoot = false;
                    text_down.setText(res.getString(R.string.you_lose));
                }

                Log.d("TAG", "Value is: " + currentUser);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("TAG", "Failed to read value.", error.toException());
            }
        });
    }

    private GridView.OnItemClickListener gridviewHostOnItemClickListener = new GridView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View v, int position,
                                long id) {
            if (ableToShoot) {
                if (!host_list.get(position).equals("shooted") &&
                    !host_list.get(position).equals("used"))
                myRef.child("currentUser").setValue("host");
                choiceRef.child("clientChoice").setValue(Integer.toString(position));
            }
        }
    };

    void hostAdapterInit()
    {
        host_adapter = new FieldHidenAdapter(this, R.layout.field_item, host_list);
        host_field.setAdapter(host_adapter);
    }

    void clientAdapterInit()
    {
        client_adapter = new FieldAdapter(this, R.layout.field_item, client_list);
        client_field.setAdapter(client_adapter);
    }

    void initData()
    {
        host_list = new ArrayList<String>();
        client_list = new ArrayList<String>();
        for (int i = 0; i < 100; i ++)
        {
            host_list.add("blank");
            client_list.add("blank");
        }
    }
}