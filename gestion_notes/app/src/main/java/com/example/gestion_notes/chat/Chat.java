package com.example.gestion_notes.chat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.gestion_notes.MemoryData;
import com.example.gestion_notes.R;
import com.google.firebase.Timestamp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class Chat extends AppCompatActivity {
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
   private final List<ChatList>chatLists=new ArrayList<>();
    private String chatKey;
    private boolean LoadinfFirstTime=true;
    private String getUserMobile = "";
    private  ChatAdapter chatAdapter;
    private RecyclerView chattingRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        final ImageView backBtn = findViewById(R.id.backBtn);
        final TextView nameTV = findViewById(R.id.name);
        final EditText messageEditText = findViewById(R.id.messageEditTxt);
        final CircleImageView profilePic = findViewById(R.id.profilePic);
        final ImageView sendBtn = findViewById(R.id.sendBtn);

        // Récupérer les données de l'intent
         String getName = getIntent().getStringExtra("nom");
         String getProfilePic = getIntent().getStringExtra("image");
        chatKey = getIntent().getStringExtra("chat_Key");
        String getMobile = getIntent().getStringExtra("tel");
        chattingRecyclerView=findViewById(R.id.chattingRecyclerView);
        // recuperer le numero de tel
        getUserMobile = MemoryData.getUserMobile(Chat.this);
        Log.d("Chat", "User mobile: " + getUserMobile);
        nameTV.setText(getName);
        Glide.with(Chat.this)
                .load(getProfilePic)
                .apply(RequestOptions.circleCropTransform())
                .into(profilePic);
        chattingRecyclerView.setHasFixedSize(true);
        chattingRecyclerView.setLayoutManager(new LinearLayoutManager(Chat.this));
        chatAdapter=new ChatAdapter(chatLists,Chat.this);
       chattingRecyclerView.setAdapter(chatAdapter);

            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (chatKey == null || chatKey.isEmpty()) {
                        chatKey = "1";
                        if (snapshot.hasChild("chat")) {
                            chatKey = String.valueOf(snapshot.child("chat").getChildrenCount() + 1);
                        }
                    }
                    if (snapshot.hasChild("chat")){
                        if (snapshot.child("chat").child(chatKey).hasChild("message")){
                         chatLists.clear();
                          for(DataSnapshot messagesSnapshot:snapshot.child("chat").child(chatKey).child("message").getChildren()){
                              if (messagesSnapshot.hasChild("msg")&&messagesSnapshot.hasChild("mobile")){
                                  final String messageTimestamps=messagesSnapshot.getKey();
                                  final String getMobile =messagesSnapshot.child("mobile").getValue(String.class);
                                  final String getMsg =messagesSnapshot.child("msg").getValue(String.class);
                                  // Convertir le timestamp en millisecondes
                                  long timestampMillis = Long.parseLong(messageTimestamps) * 1000L;

                                  Date date = new Date(timestampMillis);

                                  SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                                  SimpleDateFormat simpleTimeFormat = new SimpleDateFormat("hh-mm-aa", Locale.getDefault());

                                  ChatList chatList = new ChatList(getMobile, getName, getMsg, simpleDateFormat.format(date), simpleTimeFormat.format(date));
                                  chatLists.add(chatList);

                                  if (LoadinfFirstTime||Long.parseLong(messageTimestamps)>Long.parseLong(MemoryData.getLastMsgTS(Chat.this,chatKey))){

                                        LoadinfFirstTime=false;
                                      MemoryData.saveLastMsgTS(messageTimestamps, chatKey, Chat.this);

                                          chatAdapter.updateChatList(chatLists);
                                             chattingRecyclerView.scrollToPosition(chatLists.size()-1);

                                  }

                              }
                          }
                        }
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });


        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String getTxtMessage = messageEditText.getText().toString();
                final String currentTimestamp = String.valueOf(System.currentTimeMillis()).substring(0, 10);

                databaseReference.child("chat").child(chatKey).child("Sender").setValue(getUserMobile);
                databaseReference.child("chat").child(chatKey).child("Recever").setValue(getMobile);
                databaseReference.child("chat").child(chatKey).child("message").child(currentTimestamp).child("msg").setValue(getTxtMessage);
                databaseReference.child("chat").child(chatKey).child("message").child(currentTimestamp).child("mobile").setValue(getUserMobile);


                messageEditText.setText("");

            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
