package com.example.mentalhealth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ChatRoom extends AppCompatActivity {
    private ImageView chatImage;
    private TextView chatName;
    private User chatUser;

    DatabaseReference myRef;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);


        chatImage = findViewById(R.id.chatImage);
        chatName = findViewById(R.id.chatName);

        Intent intent = getIntent();
        String userID = intent.getStringExtra("userID");

        chatImage.setImageResource(R.drawable.account_icon);
        myRef = FirebaseDatabase.getInstance().getReference("MyUsers").child(userID);



        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                chatUser = snapshot.getValue(User.class);


                chatName.setText(chatUser.getName());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}