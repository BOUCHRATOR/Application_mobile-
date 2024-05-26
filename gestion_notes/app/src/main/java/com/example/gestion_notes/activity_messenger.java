package com.example.gestion_notes;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.webkit.ConsoleMessage;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.gestion_notes.messages.MessagesAdapter;
import com.example.gestion_notes.messages.MessagesList;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class activity_messenger extends AppCompatActivity {

    private final List<MessagesList> messagesLists = new ArrayList<>();
    private String mobile;
    private boolean dataset = false;
    String lastMessage;
    int unseenMessages;
    private String chatKEY = "";
    private String email;
    private String name;
    private RecyclerView messagesRecyclerView;
    private DatabaseReference databaseReference;
    private MessagesAdapter messagesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences sharedPreferences = getSharedPreferences("session", MODE_PRIVATE);
        String userId = sharedPreferences.getString("sessionId", "");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messenger);
        final String[] tel = new String[1];
        ImageView userProfilePic = findViewById(R.id.userProfilePic);
       // mobile = getIntent().getStringExtra("tel");
       // Log.e("ActivityMessenger", "mobile: " + mobile);

        email = getIntent().getStringExtra("email");
        name = getIntent().getStringExtra("name");

        messagesRecyclerView = findViewById(R.id.messagesRecyclerView);
        messagesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        messagesAdapter = new MessagesAdapter(messagesLists, activity_messenger.this);
        messagesRecyclerView.setAdapter(messagesAdapter);

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        databaseReference = FirebaseDatabase.getInstance().getReference();


        databaseReference.child("users").child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                final String profilePicUrl = snapshot.child("image").getValue(String.class);
                final String name = snapshot.child("name").getValue(String.class);
                final String prenom = snapshot.child("prenom").getValue(String.class);
                tel[0] = snapshot.child("tel").getValue(String.class);

                if (profilePicUrl != null && !profilePicUrl.isEmpty()) {
                    Log.d("ActivityMessenger", "URL de l'image: " + profilePicUrl);
                    Glide.with(activity_messenger.this)
                            .load(profilePicUrl)
                            .apply(RequestOptions.circleCropTransform())
                            .into(userProfilePic);
                } else {
                    Toast.makeText(activity_messenger.this, "Erreur lors de la récupération des données", Toast.LENGTH_SHORT).show();
                }

                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressDialog.dismiss();
                Log.e("ActivityMessenger", "Erreur de base de données: " + error.getMessage());
            }
        });

        databaseReference.child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messagesLists.clear();
                unseenMessages = 0;
                lastMessage = "";
                chatKEY = "";
                final String gettel = snapshot.child(userId).child("tel").getValue(String.class);
                  MemoryData.saveUserMobile(activity_messenger.this,gettel);
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String getid = dataSnapshot.getKey();
                     dataset = false;  // Déplacer cette variable à l'intérieur de la boucle

                    if (!getid.equals(userId)) {
                        final String getPrnom = dataSnapshot.child("prenom").getValue(String.class);
                        final String getName = dataSnapshot.child("nom").getValue(String.class);
                        final String getFull = getPrnom + " " + getName;
                        final String getProfilePic = dataSnapshot.child("image").getValue(String.class);
                        final String mobile = dataSnapshot.child("tel").getValue(String.class);  // Assurez-vous que mobile est défini

                        // Parcourir les conversations pour trouver les messages non lus
                        databaseReference.child("chat").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot chatSnapshot) {
                                int getChatCounts = (int) chatSnapshot.getChildrenCount();

                                if (getChatCounts > 0) {
                                    for (DataSnapshot dataSnapshot1 : chatSnapshot.getChildren()) {
                                        final String getKEY = dataSnapshot1.getKey();
                                        chatKEY = getKEY;

                                        if (dataSnapshot1.hasChild("Sender") && dataSnapshot1.hasChild("Recever") && dataSnapshot1.hasChild("message")) {
                                            final String getUserOne = dataSnapshot1.child("Sender").getValue(String.class);
                                            final String getUserTwo = dataSnapshot1.child("Recever").getValue(String.class);

                                            if ((getUserOne.equals(gettel) && getUserTwo.equals(tel[0])) || (getUserOne.equals(tel[0]) && getUserTwo.equals(gettel))) {
                                                for (DataSnapshot chatDataSnapshot : dataSnapshot1.child("message").getChildren()) {
                                                    final long getMessageKey = Long.parseLong(chatDataSnapshot.getKey());
                                                    final long getLastSeenMessage = Long.parseLong(MemoryData.getLastMsgTS(activity_messenger.this, chatKEY));
                                                    lastMessage = chatDataSnapshot.child("msg").getValue(String.class);
                                                    if (getMessageKey > getLastSeenMessage) {
                                                        unseenMessages++;
                                                    }
                                                }
                                                if (!dataset) {
                                                    dataset = true;
                                                    MessagesList messagesList = new MessagesList(getFull, mobile, lastMessage, getProfilePic, unseenMessages, chatKEY);
                                                    messagesLists.add(messagesList);
                                                    messagesAdapter.updateData(messagesLists);
                                                }
                                            }
                                        }
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Log.e("FirebaseData", "Database Error: " + error.getMessage());
                            }
                        });
                        MessagesList messagesList = new MessagesList(getFull, mobile, lastMessage, getProfilePic, unseenMessages, chatKEY);
                        messagesLists.add(messagesList);
                        messagesAdapter.updateData(messagesLists);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FirebaseData", "Database Error: " + error.getMessage());
            }
        });
    }}