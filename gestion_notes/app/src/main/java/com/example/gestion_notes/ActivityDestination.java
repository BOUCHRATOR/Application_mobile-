package com.example.gestion_notes;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class ActivityDestination extends AppCompatActivity {
    private TextView text;
    private Button btn_conn;
    private EditText email, pass;
    String userUsername,userPassword;
    // Clé pour enregistrer l'ID de session dans SharedPreferences
    public String SESSION_ID_KEY = "sessionId";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btn_conn = findViewById(R.id.button2);
        email = findViewById(R.id.editTextText);
        pass = findViewById(R.id.editTextTextpassword);
        text = findViewById(R.id.textView5);

        btn_conn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!validateUsername() | !validatePassword()) {
                    return;
                } else {
                     userUsername = email.getText().toString().trim();
                   userPassword = pass.getText().toString().trim();
                    DatabaseReference referenceProf = FirebaseDatabase.getInstance().getReference("prof");
                    DatabaseReference referenceEtudiant = FirebaseDatabase.getInstance().getReference("etudiant");
                    DatabaseReference referenceResponsable = FirebaseDatabase.getInstance().getReference("responsable");
                    DatabaseReference referenceChefFiliere = FirebaseDatabase.getInstance().getReference("chef_filiere");
                    checkUser(referenceProf, userUsername);
                    checkUser(referenceEtudiant, userUsername);
                    checkUser(referenceResponsable, userUsername);
                    checkUser(referenceChefFiliere, userUsername);
                }
            }
        });

        text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityDestination.this, forgetpass.class);
                startActivity(intent);
            }
        });
    }

    public Boolean validateUsername() {
        String val = email.getText().toString();
        if (val.isEmpty()) {
            email.setError("Username cannot be empty");
            return false;
        } else {
            email.setError(null);
            return true;
        }
    }

    public Boolean validatePassword() {
        String val = pass.getText().toString();
        if (val.isEmpty()) {
            pass.setError("Password cannot be empty");
            return false;
        } else {
            pass.setError(null);
            return true;
        }
    }

    // Méthode pour vérifier l'utilisateur
    protected void checkUser(DatabaseReference reference, final String userUsername) {

        Query checkUserDatabase = reference.orderByChild("email").equalTo(userUsername);
        checkUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    email.setError(null);
                    String passwordFromDB = snapshot.getChildren().iterator().next().child("pass").getValue(String.class);
                    String collectionName = reference.getKey();
                    saveCollectionName(collectionName);
                    if (passwordFromDB.equals(userPassword)) {

                        email.setError(null);
                        String userId = snapshot.getChildren().iterator().next().getKey(); // Obtenez l'ID de l'utilisateur correspondant à l'e-mail
                        saveSessionId(userId); // Enregistrez l'ID de session dans SharedPreferences
                        // Authentification réussie, redirigez l'utilisateur vers la page suivante
                        Intent intent = new Intent(ActivityDestination.this, ActivityProfile.class);
                        startActivity(intent);
                    } else {
                        pass.setError("Mot de passe incorrect");
                        pass.requestFocus();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ActivityDestination.this, "Erreur lors de la récupération des données", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Cette méthode enregistre le nom de la collection dans SharedPreferences
    private void saveCollectionName(String collectionName) {
        SharedPreferences sharedPreferences = getSharedPreferences("collection", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("collectionName", collectionName);
        editor.apply();
    }

    // Cette méthode enregistre l'ID de session dans SharedPreferences
    private void saveSessionId(String sessionId) {
        SharedPreferences sharedPreferences = getSharedPreferences("session", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SESSION_ID_KEY, sessionId);
        editor.apply();
    }
}
