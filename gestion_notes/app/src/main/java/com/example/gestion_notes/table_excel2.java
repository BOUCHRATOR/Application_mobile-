package com.example.gestion_notes;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;

public class table_excel2 extends AppCompatActivity {

    private boolean isTableUnfoVisible = false;
    private View tableUnfo;
    private View centerContent;

    // Référence à la base de données Firebase
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.table_excel2);
        centerContent = findViewById(R.id.centerContent);

        // Initialisation de la référence Firebase
        mDatabase = FirebaseDatabase.getInstance().getReference();

        ImageView imageView10 = findViewById(R.id.imageView10);
        TableLayout tableLayout = findViewById(R.id.tableLayout2);
        ConstraintLayout constraintLayout = findViewById(R.id.laaypt2); // Obtenez la référence de votre ConstraintLayout

        imageView10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isTableUnfoVisible) {
                    // Si le contenu est visible, le masquer
                    constraintLayout.removeView(tableUnfo);
                    centerContent.setVisibility(View.INVISIBLE);
                    isTableUnfoVisible = false;
                } else {
                    // Si le contenu n'est pas visible, l'afficher
                    tableUnfo = LayoutInflater.from(table_excel2.this).inflate(R.layout.table_unfo2, null);
                    EditText appoge = tableUnfo.findViewById(R.id.nameEditText);
                    EditText nom = tableUnfo.findViewById(R.id.numberEditText);
                    EditText prenom = tableUnfo.findViewById(R.id.emailEditText);
                    EditText session = tableUnfo.findViewById(R.id.emailEditText1);
                    EditText matiere1 = tableUnfo.findViewById(R.id.emailEditText2);
                    EditText bareme1 = tableUnfo.findViewById(R.id.emailEditText3);
                    EditText matiere2 = tableUnfo.findViewById(R.id.emailEditText4);
                    EditText bareme2 = tableUnfo.findViewById(R.id.emailEditText5);
                    EditText matiere3 = tableUnfo.findViewById(R.id.emailEditText6);
                    EditText bareme3 = tableUnfo.findViewById(R.id.emailEditText7);
                    Button addButton = tableUnfo.findViewById(R.id.addButton); // Ajoutez cette ligne

                    addButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            HashMap<String, String> noteDetails = new HashMap<>();
                            noteDetails.put("appoge", appoge.getText().toString());
                            noteDetails.put("nom", nom.getText().toString());
                            noteDetails.put("prenom", prenom.getText().toString());
                            noteDetails.put("session", session.getText().toString());
                            noteDetails.put("matiere1", matiere1.getText().toString());
                            noteDetails.put("bareme1", bareme1.getText().toString());
                            noteDetails.put("matiere2", matiere2.getText().toString());
                            noteDetails.put("bareme2", bareme2.getText().toString());
                            noteDetails.put("matiere3", matiere3.getText().toString());
                            noteDetails.put("bareme3", bareme3.getText().toString());


                            // Créer un nouvel ID pour chaque entrée
                            String id = mDatabase.push().getKey();

                            // Stocker les données dans la base de données sous /maquette/matiere1/id
                            mDatabase.child("maquette").child("matiere3").child(id).setValue(noteDetails);

                            TableRow tableRow = (TableRow) LayoutInflater.from(table_excel2.this).inflate(R.layout.table_row2, null);
                            ((TextView) tableRow.findViewById(R.id.nameTextView)).setText(noteDetails.get("appoge"));
                            ((TextView) tableRow.findViewById(R.id.numberTextView)).setText(noteDetails.get("nom"));
                            ((TextView) tableRow.findViewById(R.id.emailTextView)).setText(noteDetails.get("prenom"));
                            ((TextView) tableRow.findViewById(R.id.emailTextView1)).setText(noteDetails.get("session"));
                            ((TextView) tableRow.findViewById(R.id.emailTextView2)).setText(noteDetails.get("matiere1"));
                            ((TextView) tableRow.findViewById(R.id.emailTextView3)).setText(noteDetails.get("bareme1"));
                            ((TextView) tableRow.findViewById(R.id.emailTextView4)).setText(noteDetails.get("matiere2"));
                            ((TextView) tableRow.findViewById(R.id.emailTextView5)).setText(noteDetails.get("bareme2"));
                            ((TextView) tableRow.findViewById(R.id.emailTextView6)).setText(noteDetails.get("matiere3"));
                            ((TextView) tableRow.findViewById(R.id.emailTextView5)).setText(noteDetails.get("bareme3"));


                            tableRow.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    // Supprimer la ligne de la table
                                    tableLayout.removeView(tableRow);
                                    mDatabase.child("maquette").child("matiere3").child(id).removeValue();
                                }
                            });

                            tableLayout.addView(tableRow);
                        }
                    });

                    constraintLayout.addView(tableUnfo);
                    centerContent.setVisibility(View.VISIBLE);
                    isTableUnfoVisible = true;

                    // Centrer tableUnfo dans le ConstraintLayout
                    ConstraintSet constraintSet = new ConstraintSet();
                    constraintSet.clone(constraintLayout);
                    constraintSet.connect(tableUnfo.getId(), ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, 0);
                    constraintSet.connect(tableUnfo.getId(), ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, 0);
                    constraintSet.connect(tableUnfo.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 0);
                    constraintSet.connect(tableUnfo.getId(), ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, 0);
                    constraintSet.applyTo(constraintLayout);
                }
            }
        });
    }
}
