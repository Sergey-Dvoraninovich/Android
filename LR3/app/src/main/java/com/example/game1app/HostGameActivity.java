package com.example.game1app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HostGameActivity extends AppCompatActivity {

    GridView host_field;
    GridView client_field;
    FieldAdapter host_adapter;
    FieldHidenAdapter client_adapter;
    ArrayList<String> host_list;
    ArrayList<String> client_list;

    TextView text_top;
    TextView text_down;
    Button button_copy;

    FirebaseDatabase database;
    DatabaseReference myRef;
    DatabaseReference choiceRef;
    String gameId;
    Resources res;

    FieldProcessing host_fieldProcessing;
    FieldProcessing client_fieldProcessing;

    private ClipboardManager clipboardManager;
    private ClipData clipData;

    boolean ableToShoot;
    int clientChoice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host_game);

        Bundle arguments = getIntent().getExtras();
        gameId = arguments.get("gameId").toString();
        ableToShoot = false;
        res = getResources();

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference().child(gameId);
        choiceRef = database.getReference().child(gameId+"-u");
        initData();
        host_fieldProcessing = new FieldProcessing(host_list);
        client_fieldProcessing = new FieldProcessing(client_list);

        host_field = (GridView) findViewById(R.id.host_game_gridview_top);
        host_adapter = new FieldAdapter(this, R.layout.field_item, host_list);
        host_field.setAdapter(host_adapter);

        client_field = (GridView) findViewById(R.id.host_game_gridview_down);
        client_adapter = new FieldHidenAdapter(this, R.layout.field_item, client_list);
        client_field.setAdapter(client_adapter);
        client_field.setOnItemClickListener(gridviewClientOnItemClickListener);

        text_top = (TextView) findViewById(R.id.host_game_text_top);
        text_top.setText(gameId);
        text_down = (TextView) findViewById(R.id.host_game_text_down);

        button_copy = (Button) findViewById(R.id.copy_code_button);

        button_copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                String local_text = text_top.getText().toString();
                clipData = ClipData.newPlainText("text", local_text);
                clipboardManager.setPrimaryClip(clipData);
                local_text += " was copied";
                Toast toast = Toast.makeText(getApplicationContext(), local_text, Toast.LENGTH_SHORT);
                toast.show();
            }
        });

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String currentUser = "";
                ArrayList<String> host_temp = new ArrayList<String>();
                ArrayList<String> client_temp = new ArrayList<String>();

                DataSnapshot user_item = dataSnapshot.child("currentUser");
                currentUser = user_item.getValue(String.class);

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

                Log.d("TAG", "Value is: " + currentUser);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("TAG", "Failed to read value.", error.toException());
            }
        });

        choiceRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String currentUser = "host";

                DataSnapshot client_choice_data = dataSnapshot.child("clientChoice");
                if (client_choice_data.getValue(String.class) != null)
                {
                    clientChoice = Integer.parseInt(client_choice_data.getValue(String.class));
                }
                else {
                    clientChoice = 100;
                }

                if (clientChoice != 100)
                {
                    {
                        if (host_list.get(clientChoice).equals("ship"))
                        {
                            host_list.set(clientChoice, "shooted");
                            host_list = host_fieldProcessing.ShipRound(host_list, clientChoice);
                            for (int i = 0; i < 100; i++)
                                myRef.child("host").child(Integer.toString(i)).setValue(host_list.get(i));
                            myRef.child("currentUser").setValue("client");
                            currentUser = "client";
                            ableToShoot = false;
                            text_down.setText(res.getString(R.string.wait));
                            if (host_fieldProcessing.isWinner(host_list)) {
                                myRef.child("winner").setValue("client");
                                text_down.setText(res.getString(R.string.you_lose));
                                ableToShoot = false;
                            }
                        }
                        if (host_list.get(clientChoice).equals("blank"))
                        {
                            host_list.set(clientChoice, "used");
                            myRef.child("host").child(Integer.toString(clientChoice)).setValue(host_list.get(clientChoice));
                            ableToShoot = true;
                            text_down.setText(res.getString(R.string.your_move));
                        }
                    }
                }

                hostAdapterInit();
                clientAdapterInit();

                Log.d("TAG", "Value is: " + currentUser);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("TAG", "Failed to read value.", error.toException());
            }
        });
    }

    private GridView.OnItemClickListener gridviewClientOnItemClickListener = new GridView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View v, int position,
                                long id) {
            if (ableToShoot) {
                if (!client_list.get(position).equals("used") &&
                    !client_list.get(position).equals("shooted"))
                {
                    if (client_list.get(position).equals("ship")) {
                        client_list.set(position, "shooted");
                        client_list = client_fieldProcessing.ShipRound(client_list, position);
                        for (int i = 0; i < 100; i++)
                            myRef.child("client").child(Integer.toString(i)).setValue(client_list.get(i));
                        if (client_fieldProcessing.isWinner(client_list)) {
                            myRef.child("winner").setValue("host");
                            text_down.setText(res.getString(R.string.you_win));
                            ableToShoot = false;
                        }
                    }
                    else
                    {
                        client_list.set(position, "used");
                        myRef.child("currentUser").setValue("client");
                        myRef.child("client").child(Integer.toString(position)).setValue(client_list.get(position));
                        ableToShoot = false;
                        text_down.setText(res.getString(R.string.wait));
                    }
                }
            }
        }
    };

    void hostAdapterInit()
    {
        host_adapter = new FieldAdapter(this, R.layout.field_item, host_list);
        host_field.setAdapter(host_adapter);
    }

    void clientAdapterInit()
    {
        client_adapter = new FieldHidenAdapter(this, R.layout.field_item, client_list);
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
        if (ableToShoot)
            text_down.setText(res.getString(R.string.wait));
    }

}