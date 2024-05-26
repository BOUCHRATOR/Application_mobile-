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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
public class forgetpass extends AppCompatActivity {
    Button contenuer;
    EditText emailnum;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oblie);
        contenuer = findViewById(R.id.button2);
        emailnum = findViewById(R.id.editTextTextpassword);
        toolbar = findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(forgetpass.this, ActivityDestination.class);
                startActivity(intent);
            }
        });
        contenuer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailnumo = emailnum.getText().toString().trim();
                DatabaseReference referenceProf = FirebaseDatabase.getInstance().getReference("prof");
                DatabaseReference referenceEtudiant = FirebaseDatabase.getInstance().getReference("etudiant");
                DatabaseReference referenceResponsable = FirebaseDatabase.getInstance().getReference("responsable");
                DatabaseReference referenceChefFiliere = FirebaseDatabase.getInstance().getReference("chef_filiere");
                checkUserInCollection(referenceProf, emailnumo);
                checkUserInCollection(referenceEtudiant, emailnumo);
                checkUserInCollection(referenceResponsable, emailnumo);
                checkUserInCollection(referenceChefFiliere, emailnumo);
            }
        });
    }


    private void checkUserInCollection(DatabaseReference reference, final String emailnumo) {
        Query checkUserDatabase = reference.orderByChild("email").equalTo(emailnumo);
        Query checkuserdatabase = reference.orderByChild("tel").equalTo(emailnumo);
        checkUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                boolean userExists = false;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String email = snapshot.child("email").getValue(String.class);
                    if (email != null && email.equals(emailnumo)) {
                        userExists = true;
                        break;
                    }
                }
                    // L'utilisateur n'existe pas, effectuez les vérifications supplémentaires ici
                    if (emailnumo.isEmpty()) {

                        emailnum.setError("Veuillez saisir votre email ou numéro de téléphone");
                    }
                if(userExists) {
                    // L'utilisateur existe, enregistrer la session et rediriger
                    String tableId = reference.getKey();

                    saveSession(tableId);
                    Intent intent = new Intent(forgetpass.this, oblier1.class);
                    startActivity(intent);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Erreur de recherche d'utilisateur dans " + reference.getKey(), Toast.LENGTH_SHORT).show();
            }
        });
        checkuserdatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                boolean userExists1 = false;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String phoneNumberStr = snapshot.child("tel").getValue(String.class);
                    if (phoneNumberStr != null && phoneNumberStr.equals(emailnumo)) {
                        userExists1 = true;
                        break;
                    }
                }
                    // L'utilisateur n'existe pas, effectuez les vérifications supplémentaires ici
                    if (emailnumo.isEmpty()) {

                        emailnum.setError("Veuillez saisir votre email ou numéro de téléphone");
                    }
                if (userExists1) {
                    // L'utilisateur existe, enregistrer la session et rediriger
                    String tableId = reference.getKey();
                    saveSession(tableId);
                    Intent intent = new Intent(forgetpass.this, oblier1.class);
                    startActivity(intent);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Erreur de recherche d'utilisateur dans " + reference.getKey(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void saveSession(String tableId) {
        SharedPreferences sharedPreferences = getSharedPreferences("session", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("tableId", tableId);
        editor.apply();
    }
}
