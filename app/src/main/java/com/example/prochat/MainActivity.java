package com.example.prochat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {


    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

// If the user is already logd in he ill directed to the main screen..If he is not loged in that means current user == null then he ill redirected to login page
        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() == null){
            Intent intent = new Intent(MainActivity.this, LogIn.class);
            startActivity(intent);

        }

    }
}


