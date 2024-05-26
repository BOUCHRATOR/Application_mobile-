package com.example.gestion_notes;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
public class password extends AppCompatActivity {
    DatabaseReference reference;

    EditText editTextTextEmailAddress1, editTextTextEmailAddress5, editTextTextEmailAddress3;
    TextView text1;
    Button changer;
    String collectionName;
    ImageView profile;
    SharedPreferences sharedPreferences;

    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);
        sharedPreferences = getSharedPreferences("collection", MODE_PRIVATE);
        collectionName = sharedPreferences.getString("collectionName", "");
        reference = FirebaseDatabase.getInstance().getReference().child(collectionName);
        editTextTextEmailAddress1 = findViewById(R.id.editTextTextEmailAddress1);
        editTextTextEmailAddress5 = findViewById(R.id.editTextTextEmailAddress5);
        editTextTextEmailAddress3 = findViewById(R.id.editTextTextEmailAddress3);
        text1 = findViewById(R.id.textView10);
        changer = findViewById(R.id.button10);
        toolbar = findViewById(R.id.toolbar10);
        profile=findViewById(R.id.profileImageView);
        setSupportActionBar(toolbar);
        SharedPreferences sharedPreferences = getSharedPreferences("session", MODE_PRIVATE);
        String userId = sharedPreferences.getString("sessionId", "");
        reference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String passwordFromDatabase = dataSnapshot.child("pass").getValue(String.class);
                    String profil = dataSnapshot.child("image").getValue(String.class);
                    String nom = dataSnapshot.child("nom").getValue(String.class);
                    String prenom = dataSnapshot.child("prenom").getValue(String.class);
                    String nomComplet = nom + " " + prenom;
                    text1.setText(nomComplet);
                    if (profil != null && !profil.isEmpty()) {
                        Glide.with(password.this)
                                .load(profil)
                                .apply(RequestOptions.circleCropTransform())
                                .into(profile);
                    }
                    changer.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String enteredPassword = editTextTextEmailAddress1.getText().toString();
                            String enteredPassword1 = editTextTextEmailAddress5.getText().toString();
                            String enteredPassword2 = editTextTextEmailAddress3.getText().toString();
                            if (passwordFromDatabase.equals(enteredPassword)) {
                                if (enteredPassword1.equals(enteredPassword2)) {
                                    if (isPasswordValid(enteredPassword1)) {
                                        reference.child(userId).child("pass").setValue(enteredPassword1);
                                        Toast.makeText(password.this, "Mot de passe mis à jour avec succès", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(password.this, "Le mot de passe doit contenir au moins 6 caractères, comprenant une combinaison de lettres, de chiffres et de symboles spécifiques (%@$!)", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    // Afficher un message d'erreur si les deux nouveaux mots de passe ne sont pas identiques
                                    editTextTextEmailAddress3.setError("les deux nouveaux mots de passe ne sont pas identiques");
                                    editTextTextEmailAddress3.requestFocus();                                }
                            } else {
                                editTextTextEmailAddress1.setError("mot de passe precedent est incorrecte");
                                editTextTextEmailAddress1.requestFocus();                            }
                        }
                    });
                } else {
                    Toast.makeText(password.this, "Utilisateur non trouvé", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(password.this, "Erreur de récupération des données utilisateur", Toast.LENGTH_SHORT).show();
            }
        });
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(password.this, ActivityProfile.class);
                startActivity(intent);
            }
        });
    }
    // Méthode pour vérifier si un mot de passe est valide
    private boolean isPasswordValid(String password) {
        // Vérifier si le mot de passe a une longueur d'au moins 6 caractères
        if (password.length() < 6) {
            return false;
        }
        // Vérifier si le mot de passe contient au moins un chiffre
        if (!password.matches(".*\\d.*")) {
            return false;
        }
        // Vérifier si le mot de passe contient au moins un caractère spécial
        if (!password.matches(".*[!@#$%&*()_+=|<>?{}\\[\\]~-].*")) {
            return false;
        }
        // Vérifier si le mot de passe contient au moins une lettre
        if (!password.matches(".*[a-zA-Z].*")) {
            return false;
        }
        return true;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu1, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Vérifiez l'ID de l'élément de menu sélectionné
        if (item.getItemId() == R.id.menuset) {
            // Si oui, ouvrez l'activité des paramètres
            Intent intent = new Intent(password.this, Editprofile.class);
            startActivity(intent);
            return true; // Indique que l'élément de menu a été géré
        } else if (item.getItemId() == R.id.menupara) {
            Intent intent = new Intent(password.this, ActivityProfile.class);
            startActivity(intent);
            return true;
        } else if (item.getItemId() == R.id.menuout) {
            Intent intent = new Intent(password.this, Deconnexion.class);
            startActivity(intent);
            return true;
        } else if (item.getItemId() == R.id.menunote) {
            Intent intent = new Intent(password.this, password.class);
            startActivity(intent);
            return true;
        } else {
            Toast.makeText(password.this, "Une erreur s'est produite. Veuillez réessayer plus tard.", Toast.LENGTH_SHORT).show();
            // Si l'ID de l'élément de menu n'est pas celui de "Settings", laissez-le être traité par la superclasse
            return super.onOptionsItemSelected(item);
        }
    }
}
