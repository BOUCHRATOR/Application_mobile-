package com.example.gestion_notes;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class etude_note2 extends AppCompatActivity {

    private View centerContent;
    private DatabaseReference mDatabase;
    private String matiere1Value;
    private String matiere2Value;
    private static final String TAG = "note_m1";
    TableLayout tableLayout;
    ConstraintLayout constraintLayout;
    ImageView imageView10, imageView5;
    Button delibratrion;
    TextView mat1, mat2;
    private List<DataSnapshot> snapshotsList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notes2);
        mat1 = findViewById(R.id.matiere1TextView);
        mat2 = findViewById(R.id.matiere2TextView);
        delibratrion = findViewById(R.id.del);
        centerContent = findViewById(R.id.centerContent);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        tableLayout = findViewById(R.id.tableLayout1);
        imageView10 = findViewById(R.id.imageView10);
        imageView5 = findViewById(R.id.imageView5);
        imageView5.setVisibility(View.INVISIBLE);
        constraintLayout = findViewById(R.id.laaypt1);
        imageView10.setVisibility(View.INVISIBLE);
        delibratrion.setVisibility(View.INVISIBLE);
        showCustomDialog();
    }

    public void showCustomDialog() {
        Dialog dialog = new Dialog(this);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_layout, null);
        Button button = view.findViewById(R.id.dismiss);
        EditText matiere = view.findViewById(R.id.editTextText);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String matiereText = matiere.getText().toString().trim();
                saveMatiereInPreferences(matiereText);
                if (!matiereText.isEmpty()) {
                    checkMatiereAndFetchData(matiereText, dialog);
                } else {
                    Toast.makeText(etude_note2.this, "Please enter a subject", Toast.LENGTH_SHORT).show();
                }
            }
        });

        dialog.setContentView(view);
        dialog.show();
    }

    private void saveMatiereInPreferences(String matiere) {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("matiere", matiere);
        editor.apply();
    }

    private void checkMatiereAndFetchData(String matiere, Dialog dialog) {
        mDatabase.child("maquette").child("matiere2").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean found = false;
                snapshotsList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    HashMap<String, String> noteDetails = (HashMap<String, String>) snapshot.getValue();
                    if (noteDetails != null && matiere.equals(noteDetails.get("matiere1")) || matiere.equals(noteDetails.get("matiere2"))) {
                        found = true;
                        snapshotsList.add(snapshot);
                        matiere1Value = noteDetails.get("matiere1");
                        matiere2Value = noteDetails.get("matiere2");
                    }
                }
                if (found) {
                    displayAllEntries(dialog);
                } else {
                    Toast.makeText(etude_note2.this, "No data found for the subject", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(etude_note2.this, "Error fetching data: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void displayAllEntries(Dialog dialog) {
        SharedPreferences sharedPreferences = getSharedPreferences("session", MODE_PRIVATE);
        String userId = sharedPreferences.getString("sessionId", "");

        // Récupérer le nom de l'utilisateur
        mDatabase.child("etudiant").child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String userFirstName = dataSnapshot.child("prenom").getValue(String.class);
                    String userLastName = dataSnapshot.child("nom").getValue(String.class);

                    for (DataSnapshot snapshot : snapshotsList) {
                        HashMap<String, String> noteDetails = (HashMap<String, String>) snapshot.getValue();
                        if (noteDetails != null) {
                            String nom = noteDetails.get("nom");
                            String prenom = noteDetails.get("prenom");
                            if (userFirstName.equals(prenom) && userLastName.equals(nom)) {
                                TableRow tableRow = createTableRow(noteDetails);
                                TextView nameTextView = tableRow.findViewById(R.id.nameTextView);
                                TextView numberTextView = tableRow.findViewById(R.id.numberTextView);
                                TextView emailTextView = tableRow.findViewById(R.id.emailTextView);
                                TextView emailTextView1 = tableRow.findViewById(R.id.emailTextView1);
                                TextView emailTextView2 = tableRow.findViewById(R.id.emailTextView2);
                                TextView emailTextView3 = tableRow.findViewById(R.id.emailTextView3);
                                TextView emailTextView4 = tableRow.findViewById(R.id.emailTextView4);
                                TextView emailTextView5 = tableRow.findViewById(R.id.emailTextView5);
                                nameTextView.setBackgroundResource(R.drawable.green_background);
                                numberTextView.setBackgroundResource(R.drawable.green_background);
                                emailTextView.setBackgroundResource(R.drawable.green_background);
                                emailTextView1.setBackgroundResource(R.drawable.green_background);
                                emailTextView2.setBackgroundResource(R.drawable.green_background);
                                emailTextView3.setBackgroundResource(R.drawable.green_background);
                                emailTextView4.setBackgroundResource(R.drawable.green_background);
                                emailTextView5.setBackgroundResource(R.drawable.green_background);
                                tableLayout.addView(tableRow);
                            } else {
                                TableRow tableRow = createTableRow(noteDetails);
                                tableLayout.addView(tableRow);
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(etude_note2.this, "Error fetching user data: ", Toast.LENGTH_SHORT).show();
            }
        });

        dialog.dismiss();
    }

    // Méthode utilitaire pour créer une nouvelle ligne de tableau
    private TableRow createTableRow(HashMap<String, String> noteDetails) {
        TableRow tableRow = (TableRow) LayoutInflater.from(etude_note2.this).inflate(R.layout.table_row1, null);
        TextView nameTextView = tableRow.findViewById(R.id.nameTextView);
        TextView numberTextView = tableRow.findViewById(R.id.numberTextView);
        TextView emailTextView = tableRow.findViewById(R.id.emailTextView);
        TextView emailTextView1 = tableRow.findViewById(R.id.emailTextView1);
        TextView emailTextView2 = tableRow.findViewById(R.id.emailTextView2);
        TextView emailTextView3 = tableRow.findViewById(R.id.emailTextView3);
        TextView emailTextView4 = tableRow.findViewById(R.id.emailTextView4);
        TextView emailTextView5 = tableRow.findViewById(R.id.emailTextView5);
        TextView emailTextView6 = tableRow.findViewById(R.id.emailTextView6);
        TextView emailTextView7 = tableRow.findViewById(R.id.emailTextView7);

        // Assurez-vous de convertir les valeurs de type Double en String si nécessaire
        String noteString1 = String.valueOf(noteDetails.get("note1"));
        String noteString2 = String.valueOf(noteDetails.get("note2"));
        String noteString = String.valueOf(noteDetails.get("note"));

        if (noteString != null && !noteString.isEmpty()) {
            float note = Float.parseFloat(noteString);
            if (note < 6.0) {
                emailTextView6.setBackgroundResource(R.drawable.red_background);
                emailTextView7.setBackgroundResource(R.drawable.red_background);
            } else if (note < 12.0 && note >= 6.0) {
                emailTextView6.setBackgroundResource(R.drawable.yellow_background);
                emailTextView7.setBackgroundResource(R.drawable.yellow_background);
            }else {
                emailTextView6.setBackgroundResource(R.drawable.green_background);
                emailTextView7.setBackgroundResource(R.drawable.green_background);
            }
        }

        mat1.setText(matiere1Value);
        mat2.setText(matiere2Value);

        nameTextView.setText(noteDetails.get("appoge"));
        numberTextView.setText(noteDetails.get("nom"));
        emailTextView.setText(noteDetails.get("prenom"));
        emailTextView1.setText(noteDetails.get("session"));
        emailTextView2.setText(noteString1);
        emailTextView3.setText(noteDetails.get("bareme1"));
        emailTextView4.setText(noteString2);
        emailTextView5.setText(noteDetails.get("bareme2"));
        emailTextView6.setText(noteString);
        emailTextView7.setText(noteDetails.get("validation"));

        return tableRow;
    }
}