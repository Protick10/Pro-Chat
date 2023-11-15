package com.example.prochat;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
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

public class LogIn extends AppCompatActivity {

    Button login;
    EditText email,password;
    FirebaseAuth auth;

    TextView signupbtn;
    FirebaseUser firebaseUser;

    android.app.ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        auth = FirebaseAuth.getInstance();
        login = findViewById(R.id.loginButton);
        email = findViewById(R.id.etLoginEmail);
        password = findViewById(R.id.etLoginPassword);
        signupbtn = findViewById(R.id.loginSignup);
//creating a loading before main screen comes..

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);




        signupbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LogIn.this, Signup.class);
                startActivity(intent);
                finish();
            }
        });

// this is to make the user singed in after loging to the app...
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // User is signed in
            Intent i = new Intent(LogIn.this, MainActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
        }
        else {
            // User is signed out
            Log.d(TAG, "onAuthStateChanged:signed_out");
        }


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//if any work is done it will save on task and if not then it ill show error..
                
                if ((TextUtils.isEmpty(email.getText().toString()))){
                    progressDialog.dismiss();//we dont want progress dialoge here
                    Toast.makeText(LogIn.this, "Enter the email", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(password.getText().toString())) {
                    progressDialog.dismiss();
                    Toast.makeText(LogIn.this, "Enter the password", Toast.LENGTH_SHORT).show();
                    
                } else if (!Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()) {
                    progressDialog.dismiss();
                    email.setError("Give Proper Email");
                    
                } else if (password.getText().toString().length()<6) {
                    progressDialog.dismiss();
                    password.setError("password should be more than 6 character");

                }else {

                    String hashedPassword = PasswordHasher.hashString(password.getText().toString());

                    auth.signInWithEmailAndPassword(email.getText().toString(),hashedPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()){
                                progressDialog.show();//here when task is succcessful here we ill show our progress dialogue.
                                try {
                                    Intent intent= new Intent(LogIn.this,MainActivity.class);
                                    startActivity(intent);
                                    finish(); //if we don't use finish then if user press back button it ill come to login screen rather than exitting
                                }catch (Exception e){

                                    Toast.makeText(LogIn.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                                }
                            }else {
                                Toast.makeText(LogIn.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }

                        }
                    });

                }

            }
        });
    }
}