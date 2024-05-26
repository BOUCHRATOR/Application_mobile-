package com.example.gestion_notes;

import android.app.Dialog;
import android.content.Context;
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

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class note_m3 extends AppCompatActivity {

    private boolean isTableUnfoVisible = false;
    private View tableUnfo;
    SharedPreferences sharedPreferences;
    String collectionName;
    String module;
    private String matiere1Value,matiere2Value,matiere3Value;
    private View centerContent;
    private DatabaseReference mDatabase;
    private static final String TAG = "note_m3";
    TableLayout tableLayout;
    ConstraintLayout constraintLayout;
    ImageView imageView10;
    Button delibratrion;

    private List<DataSnapshot> snapshotsList = new ArrayList<>();
    private int currentIndex = 0;
    TextView mat1,mat2,mat3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notes3);
        mat1=findViewById(R.id.matiere1TextView);
        mat2=findViewById(R.id.matiere2TextView);
        mat3=findViewById(R.id.matiere3TextView);
        delibratrion = findViewById(R.id.del);
        centerContent = findViewById(R.id.centerContent);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        tableLayout = findViewById(R.id.tableLayout3);
        imageView10 = findViewById(R.id.imageView10);
        constraintLayout = findViewById(R.id.laaypt3);
        delibratrion.setVisibility(View.INVISIBLE);
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
                        tableUnfo = LayoutInflater.from(note_m3.this).inflate(R.layout.notes2unfo, null);
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
        DatabaseReference rattrapageRef = db.getReference("delibration").child("matiere3");

        for (int i = 1; i < tableLayout.getChildCount(); i++) {
            View rowView = tableLayout.getChildAt(i);
            if (rowView instanceof TableRow) {
              TableRow row = (TableRow) rowView;
                //TableRow row = (TableRow) LayoutInflater.from(note_m3.this).inflate(R.layout.table_row2, null);
                TextView appogeTextView = (TextView) row.getChildAt(0);
                TextView nomTextView = (TextView) row.getChildAt(1);
                TextView prenomTextView = (TextView) row.getChildAt(2);
                TextView sessionTextView = (TextView) row.getChildAt(3);
                TextView not1TextView = (TextView) row.getChildAt(4);
                TextView bareme1TextView = (TextView) row.getChildAt(5);
                TextView not2TextView = (TextView) row.getChildAt(6);
                TextView bareme2TextView = (TextView) row.getChildAt(7);
                TextView not3TextView = (TextView) row.getChildAt(8);
                TextView bareme3TextView = (TextView) row.getChildAt(9);
                TextView noteTextView = (TextView) row.getChildAt(10);
                TextView validationTextView = (TextView) row.getChildAt(11);
                if (appogeTextView == null || nomTextView == null || prenomTextView == null ||
                        sessionTextView == null || not1TextView == null || bareme1TextView == null ||
                        not2TextView == null || bareme2TextView == null || not3TextView == null ||
                        bareme3TextView == null || noteTextView == null || validationTextView == null) {

                    Log.e("Firebase", "One of the TextViews is null. Please check your TableLayout.");
                    continue;
                }
                try {
                    String not1String = not1TextView.getText().toString();
                    String bareme1String = bareme1TextView.getText().toString();
                    String not2String = not2TextView.getText().toString();
                    String bareme2String = bareme2TextView.getText().toString();
                    String not3String = not3TextView.getText().toString();
                    String bareme3String = bareme3TextView.getText().toString();
                    Log.d("Firebase", "not1: " + not1String);
                    Log.d("Firebase", "bareme1: " + bareme1String);
                    Log.d("Firebase", "not2: " + not2String);
                    Log.d("Firebase", "bareme2: " + bareme2String);
                    Log.d("Firebase", "not3: " + not3String);
                    Log.d("Firebase", "bareme3: " + bareme3String);
                    if (isNumeric(not1String) && isNumeric(bareme1String) && isNumeric(not2String) && isNumeric(bareme2String) && isNumeric(not3String) && isNumeric(bareme3String)) {
                        float not1 = Float.parseFloat(not1String);
                        float bareme1 = Float.parseFloat(bareme1String);
                        float not2 = Float.parseFloat(not2String);
                        float bareme2 = Float.parseFloat(bareme2String);
                        float not3 = Float.parseFloat(not3String);
                        float bareme3 = Float.parseFloat(bareme3String);
                        // Calculer noteG
                        float noteG = ((not1 * bareme1) + (not2 * bareme2)+(not3 * bareme3));

                        // Déterminer validation
                        String validation;
                        if (noteG >= 12) {
                            validation = "valide";
                        } else if (noteG >= 6) {
                            validation = "ratt";
                        } else {
                            validation = "non valid";
                        }

                        // Mettre à jour la TextView avec noteG et validation
                        noteTextView.setText(String.valueOf(noteG));
                        validationTextView.setText(validation);

                        // Ajouter les informations de l'étudiant en rattrapage à la collection "rattrapage" dans Firebase Realtime Database
                        Map<String, Object> student = new HashMap<>();
                        student.put("appoge", appogeTextView.getText().toString());
                        student.put("nom", nomTextView.getText().toString());
                        student.put("prenom", prenomTextView.getText().toString());
                        student.put("session",  sessionTextView.getText().toString());
                        student.put("mat1", mat1.getText().toString());
                        student.put("note1", not1String);
                        student.put("bareme1", bareme1String);
                        student.put("mat2", mat2.getText().toString());
                        student.put("note2", not2String);
                        student.put("bareme2", bareme2String);
                        student.put("mat3", mat3.getText().toString());
                        student.put("note3", not3String);
                        student.put("bareme3", bareme3String);
                        student.put("note", String.valueOf(noteG));
                        student.put("validation", validation);

                        rattrapageRef.push().setValue(student)
                                .addOnSuccessListener(aVoid -> Log.d("Firebase", "DocumentSnapshot successfully written!"))
                                .addOnFailureListener(e -> Log.w("Firebase", "Error writing document", e));

                        Intent intent = new Intent(note_m3.this, delibration2.class);
                        startActivity(intent);
                    } else {
                        Log.e("Firebase", "Non-numeric value found in notes or barèmes");
                    }
                } catch (NumberFormatException e) {
                    Log.e("Firebase", "Error parsing note", e);
                }
            }
        }
    }

    private boolean isNumeric(String str) {
        if (str == null || str.isEmpty()) {
            return false;
        }
        try {
            Float.parseFloat(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
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
                    Toast.makeText(note_m3.this, "Please enter a subject", Toast.LENGTH_SHORT).show();
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
        sharedPreferences = getSharedPreferences("collection", MODE_PRIVATE);
        collectionName = sharedPreferences.getString("collectionName", "");
        mDatabase.child("maquette").child("matiere3").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean found = false;
                snapshotsList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    HashMap<String, String> noteDetails = (HashMap<String, String>) snapshot.getValue();
                    if (noteDetails != null && matiere.equals(noteDetails.get("matiere1"))||matiere.equals(noteDetails.get("matiere2"))||matiere.equals(noteDetails.get("matiere3"))) {
                        found = true;
                        snapshotsList.add(snapshot);

                        // Store matiere1 and matiere2 values
                        matiere1Value = noteDetails.get("matiere1");
                        matiere2Value = noteDetails.get("matiere2");
                        matiere3Value = noteDetails.get("matiere3");
                    }
                }
                if (found) {
                    currentIndex = 0; // Reset to first entry
                    displayEntry(dialog);
                } else {
                    Toast.makeText(note_m3.this, "No matching data found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(note_m3.this, "Error fetching data: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }



    private float getFloatFromMap(Map<String, String> map, String key) {
        if (map.containsKey(key)) {
            String value = map.get(key);
            try {
                return Float.parseFloat(value);
            } catch (NumberFormatException e) {
                e.printStackTrace();
                return 0; // Ou une autre valeur par défaut, selon votre logique métier
            }
        } else {
            return 0; // Ou une autre valeur par défaut, selon votre logique métier
        }
    }

    private void displayEntry(Dialog dialog) {
        if (constraintLayout != null) {
            if (currentIndex < snapshotsList.size()) {
                DataSnapshot snapshot = snapshotsList.get(currentIndex);
                HashMap<String, String> noteDetails = (HashMap<String, String>) snapshot.getValue();
                tableUnfo = LayoutInflater.from(note_m3.this).inflate(R.layout.notes2unfo, null);
                if (noteDetails != null) {
                    EditText apg = tableUnfo.findViewById(R.id.nameEditText);
                    EditText note1EditText = tableUnfo.findViewById(R.id.numberEditText);
                    Button saveButton = tableUnfo.findViewById(R.id.addButton);
                    apg.setText(noteDetails.get("appoge"));
                    mat1.setText(matiere1Value);
                    mat2.setText(matiere2Value);
                    mat3.setText(matiere3Value);
                    centerContent.setVisibility(View.INVISIBLE);
                    isTableUnfoVisible = false;

                    TableRow tableRow = (TableRow) LayoutInflater.from(note_m3.this).inflate(R.layout.table_row2, null);
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
                    TextView emailTextView8 = tableRow.findViewById(R.id.emailTextView8);
                    TextView emailTextView9 = tableRow.findViewById(R.id.emailTextView9);
                    nameTextView.setText(noteDetails.get("appoge"));
                    numberTextView.setText(noteDetails.get("nom"));
                    emailTextView.setText(noteDetails.get("prenom"));
                    emailTextView1.setText(noteDetails.get("session"));
                    emailTextView2.setText(noteDetails.get("note1"));
                    emailTextView3.setText(noteDetails.get("bareme1"));
                    emailTextView4.setText(noteDetails.get("note2"));
                    emailTextView5.setText(noteDetails.get("bareme2"));
                    emailTextView6.setText(noteDetails.get("note3"));
                    emailTextView7.setText(noteDetails.get("bareme3"));

                    SharedPreferences sharedPreferences = getSharedPreferences("session", MODE_PRIVATE);
                    String userId = sharedPreferences.getString("sessionId", "");
                    int userIdInt = Integer.parseInt(userId);
                    mDatabase.child("Matiere").orderByChild("idprof").equalTo(userIdInt).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot moduleSnapshot : dataSnapshot.getChildren()) {
                               String matiereName = moduleSnapshot.child("nom_matiere").getValue(String.class);
                                 module=matiereName;
                                if (collectionName.equals("prof")||collectionName.equals("chef_filiere")) {
                                    if (module.equals(matiere1Value)) {
                                        note1EditText.setText(noteDetails.get("note1"));
                                        emailTextView4.setText(emailTextView4.getTag() != null ? emailTextView4.getTag().toString() : "");
                                        Log.e("Firebase", String.valueOf(emailTextView4));

                                        //  emailTextView4.setText("");
                                        emailTextView6.setText("");
                                        emailTextView8.setText("");
                                        emailTextView9.setText("");
                                    } else if (module.equals(matiere2Value)) {
                                        note1EditText.setText(noteDetails.get("note2"));

                                        emailTextView2.setText("");
                                        emailTextView6.setText("");
                                        emailTextView8.setText("");
                                        emailTextView9.setText("");
                                    }
                                } else if (collectionName.equals("responsable")) {
                                    note1EditText.setText(noteDetails.get("note4"));
                                    delibratrion.setVisibility(View.VISIBLE);
                                }
                                Log.d("MatiereName", "matiereName: " + matiereName);
                                float note1 = getFloatFromMap(noteDetails, "note1");
                                float bareme1 = getFloatFromMap(noteDetails, "bareme1");
                                float note2 = getFloatFromMap(noteDetails, "note2");
                                float bareme2 = getFloatFromMap(noteDetails, "bareme2");
                                float note3 = getFloatFromMap(noteDetails, "note3");
                                float bareme3 = getFloatFromMap(noteDetails, "bareme3");

                                float noteG = ((note1 * bareme1) + (note2 * bareme2) + (note3 * bareme3));
                                DecimalFormat df = new DecimalFormat("#.00");
                                noteG = Float.parseFloat(df.format(noteG));
                                if (emailTextView2 != null)
                                    emailTextView2.setText(String.valueOf(note1));
                                if (emailTextView3 != null)
                                    emailTextView3.setText(String.valueOf(bareme1));
                                if (emailTextView4 != null)
                                    emailTextView4.setText(String.valueOf(note2));
                                if (emailTextView5 != null)
                                    emailTextView5.setText(String.valueOf(bareme2));
                                if (emailTextView6 != null)
                                    emailTextView6.setText(String.valueOf(note3));
                                if (emailTextView7 != null)
                                    emailTextView7.setText(String.valueOf(bareme3));
                                if (emailTextView8 != null)
                                    emailTextView8.setText(String.valueOf(noteG));
                                if (emailTextView9 != null)
                                    emailTextView9.setText(String.valueOf(noteG));

                                String valid;
                                if (noteG >= 12) {
                                    valid = "valide";
                                } else if (noteG < 12 && noteG >= 6) {
                                    valid = "ratt";
                                } else {
                                    valid = "non valid";
                                }
                                snapshot.getRef().child("note").setValue(noteG);
                                snapshot.getRef().child("validation").setValue(valid);
                                emailTextView8.setText(String.valueOf(noteG)); // Affichage de la note
                                emailTextView9.setText(valid);


                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            // Handle database errors
                        }
                    });
                    saveButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String newNote1 = note1EditText.getText().toString().trim();
                            String newNote2 = note1EditText.getText().toString().trim();
                            String newNote3 = note1EditText.getText().toString().trim();

                            // Assurez-vous que matiereName n'est pas null avant de l'utiliser

                            if (collectionName.equals("prof")||collectionName.equals("chef_filiere")) {
                                if (module.equals(matiere1Value)) {

                                    snapshot.getRef().child("note1").setValue(newNote1);

                                } else if (module.equals(matiere2Value)) {
                                    snapshot.getRef().child("note2").setValue(newNote2);

                                }
                            } else if (collectionName.equals("responsable")) {
                                snapshot.getRef().child("note3").setValue(newNote3);
                            }

                            Toast.makeText(note_m3.this, "Data updated successfully", Toast.LENGTH_SHORT).show();
                            currentIndex++;
                            if (currentIndex < snapshotsList.size()) {
                                displayEntry(dialog);
                            } else {
                                constraintLayout.removeView(tableUnfo);
                                isTableUnfoVisible = false;
                            }
                        }
                    });

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
        } else {
            Toast.makeText(note_m3.this, "ConstraintLayout is null", Toast.LENGTH_SHORT).show();
            Log.e("note_m2", "ConstraintLayout is null");
        }
    }

}