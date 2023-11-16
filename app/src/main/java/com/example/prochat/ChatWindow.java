package com.example.prochat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatWindow extends AppCompatActivity {

    String reciverimg, reciverUid, reciverName, SenderUID;
    CircleImageView profile;
    TextView recievrNamee;

    CardView sendbtn;
    EditText textmsg;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase database;
    public static String senderImg;
    public static String reciverIImg;

    String senderRoom, reciverRoom;

    RecyclerView recyclerView;
    ArrayList<MessagModelClass> messagModelClassArrayList;
    MessagesAdapter messagesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_window);

        database = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        reciverName = getIntent().getStringExtra("nameeee");
        reciverimg = getIntent().getStringExtra("reciverImg");
        reciverUid = getIntent().getStringExtra("uid");

        messagModelClassArrayList = new ArrayList<>();

        sendbtn = findViewById(R.id.chatwindowSendbtn);
        textmsg = findViewById(R.id.chatwindowetmassege);

        profile = findViewById(R.id.chatwinCameraLogo);
        recievrNamee = findViewById(R.id.chatwinRecname);

        recyclerView = findViewById(R.id.chatwindowrecyclerviw);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        messagesAdapter = new MessagesAdapter(ChatWindow.this,messagModelClassArrayList);
        recyclerView.setAdapter(messagesAdapter);









//setted the name and image
        Picasso.get().load(reciverimg).into(profile);
        recievrNamee.setText(""+reciverName);

        SenderUID = firebaseAuth.getUid(); //senderUID will be saved in SenderUid string from the firebase database
//creating roooms for sender aND revciver
        senderRoom = SenderUID+reciverUid;
        reciverRoom = reciverUid+SenderUID;

        DatabaseReference reference = database.getReference().child("user").child(firebaseAuth.getUid());
        DatabaseReference chatrefrence = database.getReference().child("chats").child(senderRoom).child("messages");


        chatrefrence.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messagModelClassArrayList.clear(); //same message will not be repeated..

                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    MessagModelClass messages = dataSnapshot.getValue(MessagModelClass.class);
                    messagModelClassArrayList.add(messages);
                }

                messagesAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                senderImg = snapshot.child("profilepic").getValue().toString();
                reciverIImg=reciverimg;

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




        sendbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = textmsg.getText().toString();
                if (message.isEmpty()){
                    Toast.makeText(ChatWindow.this, "Enter the message first", Toast.LENGTH_SHORT).show();
                }

                textmsg.setText("");
                Date date = new Date();
                MessagModelClass messagModelClass = new MessagModelClass(message, SenderUID, date.getTime());
                database = FirebaseDatabase.getInstance();
                database.getReference().child("chats").child(senderRoom).child("messages").push().setValue(messagModelClass).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        database.getReference().child("chats").child(reciverRoom).child("messages").push().setValue(messagModelClass).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                            }
                        });
                    }
                });
            }
        });


    }
}