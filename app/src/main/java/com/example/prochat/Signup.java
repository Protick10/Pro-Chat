package com.example.prochat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import at.favre.lib.crypto.bcrypt.BCrypt;

import de.hdodenhof.circleimageview.CircleImageView;




public class Signup extends AppCompatActivity {

    TextView loginbut;
    EditText signupusername, signupemail, signuppassword, signuprepass;
    Button signup;
    CircleImageView signupprofileimg;
    FirebaseAuth auth;
    Uri imageURI;
    String imageuri;
    FirebaseDatabase database;
    FirebaseStorage storage;
    ProgressDialog progressDialog;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("getting through your data");
        progressDialog.setCancelable(false);

        loginbut = findViewById(R.id.signuploginbut);
        signupusername = findViewById(R.id.etSignupUsername);
        signupemail = findViewById(R.id.etSignupEmail);
        signuppassword = findViewById(R.id.etsignupPassword);
        signuprepass = findViewById(R.id.etsignupRePassword);
        signup = findViewById(R.id.signupButton);
        signupprofileimg = findViewById(R.id.signupLogo);
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();





        loginbut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Signup.this, LogIn.class);
                startActivity(intent);
                finish();
            }
        });


        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String status = "Hey I'm Using This Application";


                if (TextUtils.isEmpty(signupusername.getText().toString()) ||TextUtils.isEmpty(signupemail.getText().toString()) ||
                        TextUtils.isEmpty(signuppassword.getText().toString()) || TextUtils.isEmpty(signuprepass.getText().toString())){

                        progressDialog.dismiss();

                    Toast.makeText(Signup.this, "Please Fill Up all the information", Toast.LENGTH_SHORT).show();

                } else if (!Patterns.EMAIL_ADDRESS.matcher(signupemail.getText().toString()).matches()) {

                    progressDialog.dismiss();
                    signupemail.setError("type valid email");
                    
                } else if (signuppassword.getText().toString().length()<=6) {

                    progressDialog.dismiss();
                    signuppassword.setError("Password must be 6 or more characters");
                } else if (!signuppassword.getText().toString().equals(signuprepass.getText().toString())) {

                    progressDialog.dismiss();
                    signuprepass.setError("Passords Doesn't Match");
                }

                else {

                    String hashedPassword = PasswordHasher.hashString(signuppassword.getText().toString());
//                    auth.createUserWithEmailAndPassword(signupemail.getText().toString(),signuppassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    auth.createUserWithEmailAndPassword(signupemail.getText().toString(),hashedPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                String id = task.getResult().getUser().getUid();
                                DatabaseReference reference = database.getReference().child("user").child(id);
                                StorageReference storageReference = storage.getReference().child("Upload").child(id);
//Imageuri null na hole data store korbe..
                                if (imageURI != null){

                                    storageReference.putFile(imageURI).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                            if (task.isSuccessful()){
                                                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                    @Override
                                                    public void onSuccess(Uri uri) {
                                                        imageuri = uri.toString();
                                                        Users users = new Users(id,signupusername.getText().toString(),signupemail.getText().toString(),hashedPassword,imageuri,status);
                                                        reference.setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {

                                                                if (task.isSuccessful()){

                                                                    progressDialog.show();
                                                                    Intent intent = new Intent(Signup.this, MainActivity.class);
                                                                    startActivity(intent);
                                                                    finish();
                                                                }else {
                                                                    Toast.makeText(Signup.this, "Error in creating user", Toast.LENGTH_SHORT).show();
                                                                }

                                                            }
                                                        });
                                                    }
                                                });
                                            }
                                        }
                                    });
                                }else {//hen user ill not select any image

                                    String status = "Hey I'm Using This Application";
                                    imageuri = "https://firebasestorage.googleapis.com/v0/b/pro-chat-2235a.appspot.com/o/circle.png?alt=media&token=d40c7588-5ab2-425c-a484-9a17b69b1759";
                                    Users users = new Users(id,signupusername.getText().toString(),signupemail.getText().toString(),hashedPassword,imageuri,status);
                                    reference.setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                            if (task.isSuccessful()){

                                                progressDialog.show();
                                                Intent intent = new Intent(Signup.this, MainActivity.class);
                                                startActivity(intent);
                                                finish();
                                            }else {
                                                Toast.makeText(Signup.this, "Error in creating user", Toast.LENGTH_SHORT).show();
                                            }

                                        }
                                    });

                                }
                            }else {
                                Toast.makeText(Signup.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });


        signupprofileimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"),10);
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==10){
            if (data != null){

                imageURI = data.getData();
                signupprofileimg.setImageURI(imageURI);

            }
        }
    }
}