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
public class oblier1 extends AppCompatActivity {
    Button valider;
    EditText cni;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oblie1);
        valider = findViewById(R.id.button2);
        cni = findViewById(R.id.editTextTextpassword);
        toolbar = findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(oblier1.this, forgetpass.class);
                startActivity(intent);
            }
        });
        valider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String valeurcni = cni.getText().toString().trim();
                SharedPreferences sharedPreferences = getSharedPreferences("session", MODE_PRIVATE);
                String tableId = sharedPreferences.getString("tableId", "");
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference(tableId);
                checkUserInCollection(reference, valeurcni);
            }
            private void checkUserInCollection(DatabaseReference reference, final String valeurcni) {
                Query checkUserDatabase = reference.orderByChild("cni").equalTo(valeurcni);
                checkUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        boolean userExists = false;
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            // Utilisateur trouvé avec le CNI
                            userExists = true;
                            break;
                        }

                        if (userExists) {
                            Intent intent = new Intent(oblier1.this, pass12.class);
                            startActivity(intent);
                        } else {

                            Toast.makeText(getApplicationContext(), "CNI non trouvé", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getApplicationContext(), "Erreur de recherche d'utilisateur dans " + reference.getKey(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

}
