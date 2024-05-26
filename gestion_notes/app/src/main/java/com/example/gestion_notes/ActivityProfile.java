package com.example.gestion_notes;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class ActivityProfile extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    String collectionName;
    DatabaseReference reference;

    Button setting, logout, mot, message,note;
    ImageView flesh, profile;
    TextView text1, text2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Initialisation des SharedPreferences et de DatabaseReference
        sharedPreferences = getSharedPreferences("collection", MODE_PRIVATE);
        collectionName = sharedPreferences.getString("collectionName", "");
        reference = FirebaseDatabase.getInstance().getReference().child(collectionName);

        // Initialisation des vues
        setting = findViewById(R.id.button3);
        logout = findViewById(R.id.button7);
        note=findViewById(R.id.button5);
        mot = findViewById(R.id.button6);
        message = findViewById(R.id.button4);
        flesh = findViewById(R.id.flesh2);
        profile = findViewById(R.id.imageView9);
        text1 = findViewById(R.id.textView6);
        text2 = findViewById(R.id.textView7);
        if(collectionName.equals("etudiant")){
            message.setVisibility(View.GONE);

        }
        // Récupération de l'ID de l'utilisateur à partir des SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("session", MODE_PRIVATE);
        String userId = sharedPreferences.getString("sessionId", "");

        // Récupération des informations de l'utilisateur à partir de la base de données
        reference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    String profil = dataSnapshot.child("image").getValue(String.class);
                    String nom = dataSnapshot.child("nom").getValue(String.class);
                    String prenom = dataSnapshot.child("prenom").getValue(String.class);
                    String email = dataSnapshot.child("email").getValue(String.class);
                    String nomComplet = nom + " " + prenom;
                    text1.setText(nomComplet);
                    text2.setText(email);
                    if (profil != null && !profil.isEmpty()) {
                        Glide.with(ActivityProfile.this)
                                .load(profil)
                                .apply(RequestOptions.circleCropTransform())
                                .into(profile);
                    } else {
                        Toast.makeText(ActivityProfile.this, "url est vide.", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Gestion des erreurs
                Toast.makeText(ActivityProfile.this, "Une erreur s'est produite. Veuillez réessayer plus tard.", Toast.LENGTH_SHORT).show();
            }
        });
          note.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  if(Objects.equals(collectionName, "prof") || Objects.equals(collectionName, "responsable")){
                      Intent intent1 = new Intent(ActivityProfile.this, maquette_profR.class);
                      startActivity(intent1);
                  } else if (Objects.equals(collectionName, "chef_filiere")) {
                      Intent intent1 = new Intent(ActivityProfile.this, excel_chef.class);
                      startActivity(intent1);
                  }else{
                      Intent intent1 = new Intent(ActivityProfile.this, consulter_etud.class);
                      startActivity(intent1);
                  }
              }
          });
        // Configuration des listeners pour les boutons
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(ActivityProfile.this, Editprofile.class);
                startActivity(intent1);
            }
        });

        mot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(ActivityProfile.this, password.class);
                startActivity(intent1);
            }
        });

        message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(ActivityProfile.this, activity_messenger.class);
                startActivity(intent1);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(ActivityProfile.this, Deconnexion.class);
                startActivity(intent1);
            }
        });

        flesh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityProfile.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
