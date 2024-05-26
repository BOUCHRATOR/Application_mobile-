package com.example.gestion_notes;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class note_m1 extends AppCompatActivity {

    private boolean isTableUnfoVisible = false;
    private View tableUnfo;
    private View centerContent;
    private DatabaseReference mDatabase;
    private static final String TAG = "note_m1";
    TableLayout tableLayout;
    ConstraintLayout constraintLayout;
    ImageView imageView10;
    Button delibratrion;

    private List<DataSnapshot> snapshotsList = new ArrayList<>();
    private int currentIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notes);
        delibratrion=findViewById(R.id.button12);
        centerContent = findViewById(R.id.centerContent);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        tableLayout = findViewById(R.id.tableLayout);
        imageView10 = findViewById(R.id.imageView10);
        constraintLayout = findViewById(R.id.laaypt);

        showCustomDialog();
        delibratrion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleDeliberation();
            }
        });
        imageView10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isTableUnfoVisible) {
                    // Si la vue est visible, la supprimer du parent et masquer le contenu central
                    if (tableUnfo != null && tableUnfo.getParent() != null) {
                        ((ViewGroup) tableUnfo.getParent()).removeView(tableUnfo);
                    }
                    centerContent.setVisibility(View.INVISIBLE);
                    isTableUnfoVisible = false;
                } else {
                    // Si la vue n'est pas visible, l'ajouter au parent et afficher le contenu central
                    if (tableUnfo != null && tableUnfo.getParent() != null) {
                        ((ViewGroup) tableUnfo.getParent()).removeView(tableUnfo);
                    }
                    if (tableUnfo == null) {
                        tableUnfo = LayoutInflater.from(note_m1.this).inflate(R.layout.notes_unfo, null);
                        tableUnfo.setId(View.generateViewId());
                    }
                    constraintLayout.addView(tableUnfo);
                    centerContent.setVisibility(View.VISIBLE);
                    isTableUnfoVisible = true;
                }
            }

        });
    }
    private void handleDeliberation() {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference rattrapageRef = db.getReference("delibration").child("matiere1");
        for (int i = 1; i < tableLayout.getChildCount(); i++) {
            View rowView = tableLayout.getChildAt(i);
            if (rowView instanceof TableRow) {
                TableRow row = (TableRow) rowView;
                TextView appogeTextView = (TextView) row.getChildAt(0);
                TextView nomTextView = (TextView) row.getChildAt(1);
                TextView prenomTextView = (TextView) row.getChildAt(2);
                TextView sessionTextView = (TextView) row.getChildAt(3);
                TextView matiereTextView = (TextView) row.getChildAt(4);
                TextView noteTextView = (TextView) row.getChildAt(5);
                TextView validationTextView = (TextView) row.getChildAt(6);
                String note = noteTextView.getText().toString();
                if (!note.isEmpty()) {
                    try {
                        float noteValue = Float.parseFloat(note);
                        // Comparer la note
                        if (noteValue < 6.0 || (noteValue < 12.0 && noteValue >= 6.0)) {
                            // Créer un objet Map pour stocker les informations de l'étudiant en rattrapage
                            Map<String, Object> student = new HashMap<>();
                            student.put("appoge", appogeTextView.getText().toString());
                            student.put("nom", nomTextView.getText().toString());
                            student.put("prenom", prenomTextView.getText().toString());
                            student.put("session", sessionTextView.getText().toString());
                            student.put("matiere", matiereTextView.getText().toString());
                            student.put("note", note);
                            student.put("validation", validationTextView.getText().toString());

                            // Ajouter les informations de l'étudiant en rattrapage à la collection "rattrapage" dans Firebase Realtime Database
                            rattrapageRef.push().setValue(student)
                                    .addOnSuccessListener(aVoid -> Log.d("Firebase", "DocumentSnapshot successfully written!"))
                                    .addOnFailureListener(e -> Log.w("Firebase", "Error writing document", e));
                            Intent intent = new Intent(note_m1.this, delibration.class);

                            startActivity(intent);
                        }
                    } catch (NumberFormatException e) {
                        Log.e("Firebase", "Error parsing note", e);
                    }
                }
            }
        }
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
                    Toast.makeText(note_m1.this, "Please enter a subject", Toast.LENGTH_SHORT).show();
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
        mDatabase.child("maquette").child("matiere1").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean found = false;
                snapshotsList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    HashMap<String, String> noteDetails = (HashMap<String, String>) snapshot.getValue();
                    if (noteDetails != null && matiere.equals(noteDetails.get("matiere"))) {
                        found = true;
                        snapshotsList.add(snapshot);
                    }
                }
                if (found) {
                    currentIndex = 0; // Reset to first entry
                    displayEntry(dialog);
                } else {
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(note_m1.this, "Error fetching data: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displayEntry(Dialog dialog) {
        if (currentIndex < snapshotsList.size()) {
            DataSnapshot snapshot = snapshotsList.get(currentIndex);
            HashMap<String, String> noteDetails = (HashMap<String, String>) snapshot.getValue();
            tableUnfo = LayoutInflater.from(note_m1.this).inflate(R.layout.notes_unfo, null);

            if (noteDetails != null) {

                EditText apg = tableUnfo.findViewById(R.id.nameEditText);
                EditText not = tableUnfo.findViewById(R.id.numberEditText);
                EditText val = tableUnfo.findViewById(R.id.emailEditText);
                Button saveButton = tableUnfo.findViewById(R.id.addButton);

                apg.setText(noteDetails.get("appoge"));
                not.setText(noteDetails.get("note"));
                val.setText(noteDetails.get("validation"));

                saveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String newNote = not.getText().toString().trim();
                        String newValidation = val.getText().toString().trim();

                        if (!newNote.isEmpty() && !newValidation.isEmpty()) {
                            DatabaseReference noteRef = snapshot.getRef();
                            noteRef.child("note").setValue(newNote);
                            noteRef.child("validation").setValue(newValidation);
                            Toast.makeText(note_m1.this, "Data updated successfully", Toast.LENGTH_SHORT).show();
                            currentIndex++;
                            if (currentIndex < snapshotsList.size()) {
                                displayEntry(dialog);
                            } else {
                                constraintLayout.removeView(tableUnfo); // Remove the view after saving
                                isTableUnfoVisible = false;
                            }
                        } else {
                            Toast.makeText(note_m1.this, "Please enter both note and validation", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                constraintLayout.removeView(tableUnfo);
                centerContent.setVisibility(View.INVISIBLE);
                isTableUnfoVisible = false;
                TableRow tableRow = (TableRow) LayoutInflater.from(note_m1.this).inflate(R.layout.notes_row, null);
                TextView nameTextView = tableRow.findViewById(R.id.nameTextView);
                TextView numberTextView = tableRow.findViewById(R.id.numberTextView);
                TextView emailTextView = tableRow.findViewById(R.id.emailTextView);
                TextView emailTextView1 = tableRow.findViewById(R.id.emailTextView1);
                TextView emailTextView2 = tableRow.findViewById(R.id.emailTextView2);
                TextView emailTextView3 = tableRow.findViewById(R.id.emailTextView3);
                TextView emailTextView4 = tableRow.findViewById(R.id.emailTextView4);

                nameTextView.setText(noteDetails.get("appoge"));
                numberTextView.setText(noteDetails.get("nom"));
                emailTextView.setText(noteDetails.get("prenom"));
                emailTextView1.setText(noteDetails.get("session"));
                emailTextView2.setText(noteDetails.get("matiere"));
                emailTextView3.setText(noteDetails.get("note"));
                emailTextView4.setText(noteDetails.get("validation"));

                tableLayout.addView(tableRow);
                constraintLayout.addView(tableUnfo);
                ConstraintSet constraintSet = new ConstraintSet();
                constraintSet.clone(constraintLayout);
                constraintSet.connect(tableUnfo.getId(), ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, 0);
                constraintSet.connect(tableUnfo.getId(), ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, 0);
                constraintSet.connect(tableUnfo.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 0);
                constraintSet.connect(tableUnfo.getId(), ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, 0);
                constraintSet.applyTo(constraintLayout);

                dialog.dismiss();
            }
        }
    }
}
