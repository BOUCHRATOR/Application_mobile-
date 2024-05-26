package com.example.gestion_notes;

import static android.content.ContentValues.TAG;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.File;

public class Editprofile extends AppCompatActivity {
    DatabaseReference reference,reference1;
    ImageView profile;
    EditText editText1, editText2, editText3, editText4;
    private static final int REQUEST_IMAGE_GALLERY = 1;
    private static final int REQUEST_STORAGE_PERMISSION = 101;
    SharedPreferences sharedPreferences1;
    SharedPreferences sharedPreferences;

    String collectionName;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editprofile);
        // Initialisation des SharedPreferences et de DatabaseReference
        sharedPreferences = getSharedPreferences("collection", MODE_PRIVATE);
        collectionName = sharedPreferences.getString("collectionName", "");
        reference = FirebaseDatabase.getInstance().getReference().child(collectionName);
        reference1=FirebaseDatabase.getInstance().getReference().child("users");
        profile = findViewById(R.id.profileImageView);
        editText1 = findViewById(R.id.editTextTextEmailAddress);
        editText2 = findViewById(R.id.editTextTextEmailAddress1);
        editText3 = findViewById(R.id.editTextTextEmailAddress2);
        editText4 = findViewById(R.id.editTextPhone);
        sharedPreferences1 = getSharedPreferences("session", MODE_PRIVATE);
        userId = sharedPreferences1.getString("sessionId", "");

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        reference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String profil = dataSnapshot.child("image").getValue(String.class);
                    String nom = dataSnapshot.child("nom").getValue(String.class);
                    String prenom = dataSnapshot.child("prenom").getValue(String.class);
                    String email = dataSnapshot.child("email").getValue(String.class);
                    String tel = dataSnapshot.child("tel").getValue(String.class);
                    editText1.setText(nom);
                    editText2.setText(prenom);
                    editText3.setText(email);
                    editText4.setText(tel);
                    if (profil != null && !profil.isEmpty()) {
                        Glide.with(Editprofile.this)
                                .load(profil)
                                .apply(RequestOptions.circleCropTransform())
                                .into(profile);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Editprofile.this, "Une erreur s'est produite. Veuillez réessayer plus tard.", Toast.LENGTH_SHORT).show();
            }
        });

        Button saveButton = findViewById(R.id.button8);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newNom = editText1.getText().toString();
                String newPrenom = editText2.getText().toString();
                String newEmail = editText3.getText().toString();
                String newTel = editText4.getText().toString();

                if (!newNom.isEmpty()) {
                    reference.child(userId).child("nom").setValue(newNom);
                    reference1.child(userId).child("nom").setValue(newNom);
                }

                if (!newPrenom.isEmpty()) {
                    reference.child(userId).child("prenom").setValue(newPrenom);
                    reference1.child(userId).child("prenom").setValue(newPrenom);
                }
                if (!newEmail.isEmpty()) {
                    reference.child(userId).child("email").setValue(newEmail);
                }
                if (!newTel.isEmpty()) {
                    reference.child(userId).child("tel").setValue(newTel);
                    reference1.child(userId).child("tel").setValue(newTel);
                }

                Toast.makeText(Editprofile.this, "Les données ont été modifiées avec succès", Toast.LENGTH_SHORT).show();
            }
        });

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Editprofile.this, ActivityProfile.class);
                startActivity(intent);
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkStoragePermission();
            }
        });
    }

    private void checkStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_STORAGE_PERMISSION);
        } else {
            openGallery();
        }
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_IMAGE_GALLERY);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menuset) {
            Intent intent = new Intent(Editprofile.this, ActivityProfile.class);
            startActivity(intent);
            return true;
        } else if (item.getItemId() == R.id.menuout) {
            Intent intent = new Intent(Editprofile.this, Deconnexion.class);
            startActivity(intent);
            return true;
        } else if (item.getItemId() == R.id.menumot) {
            Intent intent = new Intent(Editprofile.this, password.class);
            startActivity(intent);
            return true;
        } else {
            Toast.makeText(Editprofile.this, "Une erreur s'est produite. Veuillez réessayer plus tard.", Toast.LENGTH_SHORT).show();
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_IMAGE_GALLERY && data != null) {
                Uri imageUri = data.getData();
                String filePath = getRealPathFromURI(imageUri);
                if (filePath != null) {
                    Picasso.get().load(new File(filePath)).into(profile);
                    uploadImageToDatabase(imageUri);
                } else {
                    Toast.makeText(this, "Erreur: Impossible de récupérer le chemin du fichier", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private String getRealPathFromURI(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor == null) {
            return null;
        }
        int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String filePath = cursor.getString(columnIndex);
        cursor.close();
        return filePath;
    }

    private void uploadImageToDatabase(Uri imageUri) {
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference();
        databaseRef.child(collectionName).child(userId).child("image").setValue(imageUri.toString())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "Image ajoutée avec succès dans la base de données en temps réel");
                        } else {
                            Log.e(TAG, "Erreur lors de l'ajout de l'image dans la base de données en temps réel", task.getException());
                        }
                    }
                });
        databaseRef.child("users").child(userId).child("image").setValue(imageUri.toString())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "Image ajoutée avec succès dans la table des utilisateurs");
                        } else {
                            Log.e(TAG, "Erreur lors de l'ajout de l'image dans la table des utilisateurs", task.getException());
                        }
                    }
                });
    }
}
