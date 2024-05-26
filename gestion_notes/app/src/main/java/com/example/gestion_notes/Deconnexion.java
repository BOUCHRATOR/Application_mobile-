package com.example.gestion_notes;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class Deconnexion extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Supprimer les données de session
        SharedPreferences.Editor editor = getSharedPreferences("session", MODE_PRIVATE).edit();
        editor.clear(); // Supprimer toutes les données de session
        editor.apply();

// Rediriger vers l'écran de connexion
        Intent intent = new Intent(Deconnexion.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Effacer la pile d'activités
        startActivity(intent);
        finish(); // Terminer l'activité actuelle


    }

}
