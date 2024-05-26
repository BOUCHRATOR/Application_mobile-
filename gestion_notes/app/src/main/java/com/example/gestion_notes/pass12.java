package com.example.gestion_notes;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class pass12 extends AppCompatActivity {
    EditText pass1, pass2;
    Button enregistrer;
    DatabaseReference reference;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pass12);
        pass1 = findViewById(R.id.editTextTextpassword);
        pass2 = findViewById(R.id.editTextTextpassword1);
        enregistrer = findViewById(R.id.button2);
        toolbar = findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(pass12.this, oblier1.class);
                startActivity(intent);
            }
        });

        SharedPreferences sharedPreferences = getSharedPreferences("session", MODE_PRIVATE);
        String tableId = sharedPreferences.getString("tableId", "");

        if (tableId != null) {
            reference = FirebaseDatabase.getInstance().getReference(tableId);
        } else {
            // Gérer le cas où collectionName est null
            Toast.makeText(pass12.this, "Nom de collection non trouvé pour cet ID de table", Toast.LENGTH_SHORT).show();
        }

        enregistrer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String enteredPassword = pass1.getText().toString();
                String enteredPassword1 = pass2.getText().toString();
                if (enteredPassword1.equals(enteredPassword)) {
                    if (isPasswordValid(enteredPassword1)) {
                        reference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                // Parcourez les enfants de la référence
                                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                                    // Obtenez l'ID de l'enfant
                                    String collectionId = childSnapshot.getKey();

                                    // Utilisez cet ID pour mettre à jour le mot de passe
                                    reference.child(collectionId).child("pass").setValue(enteredPassword1);
                                    Intent intent = new Intent(pass12.this,ActivityDestination.class);
                                    startActivity(intent);
                                    // Sortez de la boucle après avoir mis à jour le mot de passe pour le premier ID trouvé
                                    break;
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                // Gérer les erreurs de base de données
                                Toast.makeText(pass12.this, "Erreur de récupération des données de la base de données", Toast.LENGTH_SHORT).show();
                            }
                        });
                        Toast.makeText(pass12.this, "Mot de passe mis à jour avec succès", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(pass12.this, "Le mot de passe doit contenir au moins 6 caractères, comprenant une combinaison de lettres, de chiffres et de symboles spécifiques (%@$!)", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Afficher un message d'erreur si les deux nouveaux mots de passe ne sont pas identiques
                    pass1.setError("Les deux nouveaux mots de passe ne sont pas identiques");
                    pass1.requestFocus();
                }
            }
        });
    }

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
}
