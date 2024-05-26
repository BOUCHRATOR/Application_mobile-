package com.example.gestion_notes;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.GenericTypeIndicator;
import java.util.Map;

public class delibration1 extends AppCompatActivity {

    private TableLayout tableLayout;
    private ImageView plus, img;
    private String matiere;
    private TextView mat1, mat2, matiere1TextView, matiere2TextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.table_excel1);
        tableLayout = findViewById(R.id.tableLayout1);
        mat1 = findViewById(R.id.matiere1TextView);
        mat2 = findViewById(R.id.matiere2TextView);
        plus = findViewById(R.id.imageView5);
        img = findViewById(R.id.imageView10);
        plus.setVisibility(View.INVISIBLE);
        img.setVisibility(View.INVISIBLE);


        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        matiere = sharedPreferences.getString("matiere", "");

        handleDeliberation();
    }

    private void handleDeliberation() {
        DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("delibration").child("matiere2");

        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Clear the table layout before adding new rows
                //tableLayout.removeAllViews();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    GenericTypeIndicator<Map<String, Object>> t = new GenericTypeIndicator<Map<String, Object>>() {};
                    Map<String, Object> studentData = snapshot.getValue(t);

                    if (studentData != null) {
                        addStudentToTable(studentData);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("Deliberation", "loadPost:onCancelled", databaseError.toException());
            }
        });
    }

    private void addStudentToTable(Map<String, Object> studentData) {
        LayoutInflater inflater = LayoutInflater.from(this);
        TableRow row = (TableRow) inflater.inflate(R.layout.table_row1, null);

        // Récupération des vues par ID
        TextView appogeTextView = row.findViewById(R.id.nameTextView);
        TextView nomTextView = row.findViewById(R.id.numberTextView);
        TextView prenomTextView = row.findViewById(R.id.emailTextView);
        TextView sessionTextView = row.findViewById(R.id.emailTextView1);
        TextView note1TextView = row.findViewById(R.id.emailTextView2);
        TextView bareme1TextView = row.findViewById(R.id.emailTextView3);
        TextView note2TextView = row.findViewById(R.id.emailTextView4);
        TextView bareme2TextView = row.findViewById(R.id.emailTextView5);
        TextView noteGTextView = row.findViewById(R.id.emailTextView6);
        TextView validationTextView = row.findViewById(R.id.emailTextView7);

        // Vérifications pour s'assurer que les vues ne sont pas nulles
        if (appogeTextView == null || nomTextView == null || prenomTextView == null || sessionTextView == null ||
                note1TextView == null || bareme1TextView == null || note2TextView == null || bareme2TextView == null ||
                noteGTextView == null || validationTextView == null) {
            Log.e("ERROR", "Une ou plusieurs TextViews sont nulles");
            return;  // Quitter la méthode si une TextView est null
        }

        String appoge = (String) studentData.get("appoge");
        String nom = (String) studentData.get("nom");
        String prenom = (String) studentData.get("prenom");
        String session = (String) studentData.get("session");
        String mat1Str = (String) studentData.get("mat1");
        String note1 = (String) studentData.get("note1");
        String bareme1 = (String) studentData.get("bareme1");
        String mat2Str = (String) studentData.get("mat2");
        String note2 = (String) studentData.get("note2");
        String bareme2 = (String) studentData.get("bareme2");
        String validation = (String) studentData.get("validation");

        // Calcul de noteG
        float noteGValue = (Float.parseFloat(note1) * Float.parseFloat(bareme1)) + (Float.parseFloat(note2) * Float.parseFloat(bareme2));
        String noteG = String.valueOf(noteGValue);

        if (matiere.equals(mat1Str) || matiere.equals(mat2Str)) {
            appogeTextView.setText(appoge);
            nomTextView.setText(nom);
            prenomTextView.setText(prenom);
            sessionTextView.setText(session);
            note1TextView.setText(note1);
            bareme1TextView.setText(bareme1);
            note2TextView.setText(note2);
            bareme2TextView.setText(bareme2);
            noteGTextView.setText(noteG);
            validationTextView.setText(validation);

            // Set matiere values
            mat1.setText(mat1Str);
            mat2.setText(mat2Str);

            float noteValue = Float.parseFloat(noteG);
            if (noteValue < 6.0) {
                noteGTextView.setBackgroundResource(R.drawable.red_background);
                validationTextView.setBackgroundResource(R.drawable.red_background);
            } else if (noteValue < 12.0 && noteValue >= 6.0) {
                noteGTextView.setBackgroundResource(R.drawable.yellow_background);
                validationTextView.setBackgroundResource(R.drawable.yellow_background);
            }

            tableLayout.addView(row);
        }
    }
}
