package com.example.game1app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AccountActivity extends AppCompatActivity {

    Button button_use_gravatar;
    Button button_upload_image;
    Button button_change_name;
    EditText text_name;
    ImageView user_photo;
    TextView prev_game_text;
    ListView prev_game_list;

    private StorageReference mStorageRef;
    private Uri uploadUri;
    static final int GALLERY_REQUEST = 1;

    FirebaseDatabase database;
    DatabaseReference myRef;
    DatabaseReference dbRef;

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    ArrayList<String> games;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        mStorageRef = FirebaseStorage.getInstance().getReference("images");

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference().child(currentUser.getUid());
        dbRef = database.getReference();

        button_use_gravatar = (Button) findViewById(R.id.button_use_gravatar);
        button_upload_image = (Button) findViewById(R.id.button_upload_photo);
        button_change_name = (Button) findViewById(R.id.button_save_name);
        text_name = (EditText) findViewById(R.id.editTextTextPersonName);
        user_photo = (ImageView) findViewById(R.id.user_image);
        prev_game_text = (TextView) findViewById(R.id.account_text_history);
        prev_game_list = (ListView) findViewById(R.id.list_prev_games);


        games = new ArrayList<String>();
        ArrayAdapter<String> adapter = new ArrayAdapter(this,
                android.R.layout.simple_list_item_1, games);
        prev_game_list.setAdapter(adapter);

        prev_game_list.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id)
            {
                String selectedItem = games.get(position);
                Intent i = new Intent(AccountActivity.this, PrevGameActivity.class);
                i.putExtra("gameId", selectedItem);
                startActivity(i);
            }
        });

        button_upload_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, GALLERY_REQUEST);
            }
        });

        button_change_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myRef.child("userName").setValue(text_name.getText().toString());
            }
        });

        button_use_gravatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String hash = MD5Util.md5Hex(currentUser.getEmail());
                String gravatarUrl = "https://www.gravatar.com/avatar/" + hash + "?s=204&d=404";
                myRef.child("photoPath").setValue(gravatarUrl);
            }
        });

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String loadPath = "";
                DataSnapshot photoPath = dataSnapshot.child("photoPath");
                if (photoPath.getValue(String.class) != null) {
                    loadPath = photoPath.getValue(String.class);
                    setImage(loadPath);
                }

                String userName = "";
                DataSnapshot userNameData = dataSnapshot.child("userName");
                if (userNameData.getValue(String.class) != null)
                    userName = userNameData.getValue(String.class);
                text_name.setText(userName);

                ArrayList<String> local_games_list = new ArrayList<>();
                for (DataSnapshot game_id: dataSnapshot.getChildren())
                {
                    String val = game_id.getKey();
                    if (!val.equals("userName") &&
                        !val.equals("photoPath") &&
                        !val.equals("customPhotoPath"))
                    {
                        local_games_list.add(val);
                    }
                }

                games = local_games_list;
                initAdapter();

                Log.d("TAG", "Value is: " + currentUser);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("TAG", "Failed to read value.", error.toException());
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        prev_game_text.setText("Catch");
        if (requestCode == GALLERY_REQUEST) {
            prev_game_text.setText("Image");
            if (resultCode == RESULT_OK) {
                Uri selectedImage = data.getData();
                user_photo.setImageURI(selectedImage);
                uploadImage();
            }
        }
    }

    private void uploadImage()
    {
        Bitmap bitmap = ((BitmapDrawable) user_photo.getDrawable()).getBitmap();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        final StorageReference mRef = mStorageRef.child("_" + System.currentTimeMillis());
        UploadTask uploadTask = mRef.putBytes(byteArray);
        Task<Uri> task = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                return mRef.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                uploadUri = task.getResult();
                myRef.child("photoPath").setValue(uploadUri.toString());
                myRef.child("customPhotoPath").setValue(uploadUri.toString());
            }
        });
    }

    private void setImage(String path)
    {
        Picasso.with(this)
                .load(path)
                .placeholder(R.drawable.ic_launcher_foreground)
                .error(R.drawable.ic_launcher_foreground)
                .into(user_photo);
    }

    private void initAdapter()
    {
        ArrayAdapter<String> adapter = new ArrayAdapter(this,
                android.R.layout.simple_list_item_1, games);
        prev_game_list.setAdapter(adapter);
    }
}