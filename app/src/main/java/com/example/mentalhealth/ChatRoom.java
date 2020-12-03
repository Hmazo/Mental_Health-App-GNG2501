package com.example.mentalhealth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ChatRoom extends AppCompatActivity {
    private ImageView chatImage;
    private TextView chatName;
    private User chatUser;
    private EditText messageToSend;
    private ImageButton sendButton;
    private RecyclerView chatRecycler;
    private ArrayList<ChatModel> listChat;

    DatabaseReference myRef;
    FirebaseAuth auth;
    FirebaseUser fUser;
    ChatAdapter chatAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        listChat = new ArrayList<>();

        chatImage = findViewById(R.id.chatImage);
        chatName = findViewById(R.id.chatName);
        messageToSend = findViewById(R.id.sendMessageEdit);
        sendButton = findViewById(R.id.sendMessageButton);
        //recycler
        chatRecycler = findViewById(R.id.chatRecycler);
        chatRecycler.setHasFixedSize(true);



        Intent intent = getIntent();
        String userID = intent.getStringExtra("userID");
        readMessage(FirebaseAuth.getInstance().getCurrentUser().getUid(),userID);
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

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msg = messageToSend.getText().toString();
                if(!msg.equals("")){
                    fUser = FirebaseAuth.getInstance().getCurrentUser();
                    sendMessage(fUser.getUid(), userID, msg);
                    messageToSend.setText("");
                }

            }
        });


        //adapter

        chatAdapter = new ChatAdapter(this);
    }

    private void sendMessage(String fUser, String userID, String msg) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Chats").push();
        ChatModel chatmessage = new ChatModel(fUser,userID,msg);
        databaseReference.setValue(chatmessage);
    }

    private void readMessage(String myId, String userId){
        myRef = FirebaseDatabase.getInstance().getReference("Chats");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listChat.clear();
                for(DataSnapshot snapshot1 : snapshot.getChildren()){
                    ChatModel chat = snapshot1.getValue(ChatModel.class);
                    if(chat.getReceiver().equals(myId) && chat.getSender().equals(userId) || chat.getReceiver().equals(userId) && chat.getSender().equals(myId)){
                        listChat.add(chat);
                    }
                    chatAdapter.setListUser(listChat);
                    chatRecycler.setAdapter(chatAdapter);

                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ChatRoom.this);
                    linearLayoutManager.setStackFromEnd(true);
                    chatRecycler.setLayoutManager(linearLayoutManager);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}