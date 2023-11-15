package com.example.prochat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    FirebaseAuth auth;
    RecyclerView recyclerView;
    UserAdapter userAdapter;
    FirebaseDatabase database;
    ArrayList<Users> usersArrayList;

    ImageView logoutimg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        database = FirebaseDatabase.getInstance();


        auth = FirebaseAuth.getInstance();

        DatabaseReference reference = database.getReference().child("user");
        usersArrayList = new ArrayList<>(); //allocating memory of array list

        if (auth.getCurrentUser() == null){
            Intent intent = new Intent(MainActivity.this, LogIn.class);
            startActivity(intent);

        }

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot: snapshot.getChildren()){

                    Users users = dataSnapshot.getValue(Users.class);
                    usersArrayList.add(users); // all the data from users will be stored in this array list..
                }
                userAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
// when top logout img will be called..

        logoutimg = findViewById(R.id.mainlogoutbtn);

        logoutimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Dialog dialog = new Dialog(MainActivity.this,R.style.dialogue); // we have created this dialog in stylke folder so fechted it here
                dialog.setContentView(R.layout.dialogue_layout); // we fetching the layout that we have made for logout..
                Button no,yes;

                yes = dialog.findViewById(R.id.dialogueYesbtn); // here we have to use dialog.findview by id we cant use findviewby id..
                no = dialog.findViewById(R.id.dialogueNobtn);

                yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FirebaseAuth.getInstance().signOut();
                        Intent intent = new Intent(MainActivity.this,LogIn.class);
                        startActivity(intent);
                        finish();
                    }
                });

                no.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss(); //dialog box will be dissmisssed
                    }
                });

                dialog.show();

            }
        });

        recyclerView = findViewById(R.id.mainRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        userAdapter = new UserAdapter(MainActivity.this,usersArrayList);
        recyclerView.setAdapter(userAdapter);









// If the user is already logd in he ill directed to the main screen..If he is not loged in that means current user == null then he ill redirected to login page





    }
}


