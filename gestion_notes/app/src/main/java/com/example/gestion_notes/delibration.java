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

public class delibration extends AppCompatActivity {

    private TableLayout tableLayout;
    private ImageView plus, img;
    private String matiere;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.table_excel);

        plus = findViewById(R.id.imageView5);
        img = findViewById(R.id.imageView10);
        plus.setVisibility(View.INVISIBLE);
        img.setVisibility(View.INVISIBLE);
        tableLayout = findViewById(R.id.tableLayout);

        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        matiere = sharedPreferences.getString("matiere", "");

        handleDeliberation();
    }

    private void handleDeliberation() {

        DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("delibration").child("matiere1");

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
        TableRow row = (TableRow) inflater.inflate(R.layout.table_row, null);

        TextView appogeTextView = row.findViewById(R.id.nameTextView);
        TextView nomTextView = row.findViewById(R.id.numberTextView);
        TextView prenomTextView = row.findViewById(R.id.emailTextView);
        TextView sessionTextView = row.findViewById(R.id.emailTextView1);
        TextView matiereTextView = row.findViewById(R.id.emailTextView2);
        TextView noteTextView = row.findViewById(R.id.emailTextView3);
        TextView validationTextView = row.findViewById(R.id.emailTextView4);

        String appoge = (String) studentData.get("appoge");
        String nom = (String) studentData.get("nom");
        String prenom = (String) studentData.get("prenom");
        String session = (String) studentData.get("session");
        String matiere1 = (String) studentData.get("matiere");
        String note = (String) studentData.get("note");
        String validation = (String) studentData.get("validation");
        if (matiere.equals(matiere1)) {

        appogeTextView.setText(appoge);
        nomTextView.setText(nom);
        prenomTextView.setText(prenom);
        sessionTextView.setText(session);
        matiereTextView.setText(matiere1);
        noteTextView.setText(note);
        validationTextView.setText(validation);


            float noteValue = Float.parseFloat(note);
            if (noteValue < 6.0) {
                row.findViewById(R.id.emailTextView3).setBackgroundResource(R.drawable.red_background);
                row.findViewById(R.id.emailTextView4).setBackgroundResource(R.drawable.red_background);
            } else if (noteValue < 12.0 && noteValue >= 6.0) {
                row.findViewById(R.id.emailTextView3).setBackgroundResource(R.drawable.yellow_background);
                row.findViewById(R.id.emailTextView4).setBackgroundResource(R.drawable.yellow_background);

        }

        tableLayout.addView(row);
    }
}}
