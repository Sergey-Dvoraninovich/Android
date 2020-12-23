package com.example.game1app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    private EditText textMail;
    private EditText textPassword;
    private Button buttonRegistration;
    private Button buttonSignIn;

    private TextView textSignAs;
    private TextView textSignedMail;
    private Button buttonSignOut;
    private Button buttonStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        initState();
        buttonRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (InputValidate());
                {
                    Registration(textMail.getText().toString(), textPassword.getText().toString());
                }
            }
        });
        buttonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (InputValidate());
                {
                    SignIn(textMail.getText().toString(), textPassword.getText().toString());
                }
            }
        });
        buttonSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                initUnsignedState();
            }
        });
        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Let it go", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(MainActivity.this, ActivityGame.class);
                startActivity(i);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        currentUser = mAuth.getCurrentUser();
        if (currentUser != null){
            initSignedState();
        }
        else{
            initUnsignedState();
        }
    }

    private void initState()
    {
        textMail = findViewById(R.id.editMail);
        textPassword = findViewById(R.id.editPassword);
        buttonRegistration = findViewById(R.id.buttonRegistration);
        buttonSignIn = findViewById(R.id.buttonSignIn);
        textSignAs = findViewById(R.id.signed_as);
        textSignedMail = findViewById(R.id.signed_mail);
        buttonSignOut = findViewById(R.id.buttonSignOut);
        buttonStart = findViewById(R.id.buttonStart);
    }

    private void initSignedState()
    {
        textMail.setVisibility(View.GONE);
        textPassword.setVisibility(View.GONE);
        buttonRegistration.setVisibility(View.GONE);
        buttonSignIn.setVisibility(View.GONE);

        textSignAs.setVisibility(View.VISIBLE);
        textSignedMail.setVisibility(View.VISIBLE);
        buttonSignOut.setVisibility(View.VISIBLE);
        buttonStart.setVisibility(View.VISIBLE);

        textSignAs.setText(R.string.signed_as);
        textSignedMail.setText(currentUser.getEmail());
    }

    private void initUnsignedState()
    {
        textMail.setVisibility(View.VISIBLE);
        textPassword.setVisibility(View.VISIBLE);
        buttonRegistration.setVisibility(View.VISIBLE);
        buttonSignIn.setVisibility(View.VISIBLE);

        textSignAs.setVisibility(View.GONE);
        textSignedMail.setVisibility(View.GONE);
        buttonSignOut.setVisibility(View.GONE);
        buttonStart.setVisibility(View.GONE);
    }

    private boolean InputValidate()
    {
        if (!textMail.getText().toString().equals("") && !textPassword.getText().toString().equals(""))
        {
            if (textPassword.getText().toString().length() >= 8)
            {
                return true;
            }
            else{
                Toast.makeText(MainActivity.this, getString(R.string.password_short), Toast.LENGTH_SHORT).show();
            }
        }
        else {
            Toast.makeText(MainActivity.this, getString(R.string.fill_in_all), Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    private void Registration(String email, String password)
    {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            currentUser = mAuth.getCurrentUser();
                            initSignedState();
                        } else {
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void SignIn(String email, String password)
    {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d("sign_in", "signInWithEmail:success");
                            currentUser = mAuth.getCurrentUser();
                            initSignedState();
                        } else {
                            Log.w("sign_in", "signInWithEmail:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}