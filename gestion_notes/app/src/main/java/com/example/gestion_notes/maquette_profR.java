package com.example.gestion_notes;

import android.content.ClipData;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class maquette_profR extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    String collectionName;
    DatabaseReference reference;
    ImageView  profile;
    TextView text1;
    Button m1,m2,m3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.maquette_prof);
        m1=findViewById(R.id.button4);
        m2=findViewById(R.id.button43);
        m3=findViewById(R.id.button2);
        sharedPreferences = getSharedPreferences("collection", MODE_PRIVATE);
        collectionName = sharedPreferences.getString("collectionName", "");
        reference = FirebaseDatabase.getInstance().getReference().child(collectionName);
        profile = findViewById(R.id.profileImageView2);
        text1 = findViewById(R.id.username2);
        Toolbar toolbar = findViewById(R.id.toolbar3);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        m1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(maquette_profR.this, note_m1.class);
                startActivity(intent);
            }
        });
        m2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(maquette_profR.this, note_m2.class);
                startActivity(intent);
            }
        });
        m3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(maquette_profR.this, note_m3.class);
                startActivity(intent);
            }
        });
        SharedPreferences sharedPreferences = getSharedPreferences("session", MODE_PRIVATE);
        String userId = sharedPreferences.getString("sessionId", "");
        reference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String profil = dataSnapshot.child("image").getValue(String.class);
                    String nom = dataSnapshot.child("nom").getValue(String.class);
                    String prenom = dataSnapshot.child("prenom").getValue(String.class);
                    String nomComplet = nom + " " + prenom;
                    text1.setText(nomComplet);
                    if (profil != null && !profil.isEmpty()) {
                        Glide.with(maquette_profR.this)
                                .load(profil)
                                .apply(RequestOptions.circleCropTransform())
                                .into(profile);
                    } else {
                        Toast.makeText(maquette_profR.this, "url est vide.", Toast.LENGTH_SHORT).show();
                    }
                }
            }



            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(maquette_profR.this, "Une erreur s'est produite. Veuillez réessayer plus tard.", Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu3, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menuset) {
            Intent intent = new Intent(maquette_profR.this, Editprofile.class);
            startActivity(intent);
            return true;
        } else if (item.getItemId() == R.id.menuout) {
            Intent intent = new Intent(maquette_profR.this, Deconnexion.class);
            startActivity(intent);
            return true;
        } else if (item.getItemId() == R.id.menupara) {
            Intent intent = new Intent(maquette_profR.this, ActivityProfile.class);
            startActivity(intent);
            return true;
        } else if (item.getItemId() == R.id.menumot) {
            Intent intent = new Intent(maquette_profR.this, password.class);
            startActivity(intent);
            return true;
        } else if (item.getItemId() == R.id.menunot) {
            Intent intent = new Intent(maquette_profR.this, activity_messenger.class);
            startActivity(intent);
            return true;
        } else {
            Toast.makeText(maquette_profR.this, "Une erreur s'est produite. Veuillez réessayer plus tard.", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }
}