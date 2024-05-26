package com.example.gestion_notes;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
import com.example.gestion_notes.ActivityProfile;
import com.example.gestion_notes.DataFromExcel;
import com.example.gestion_notes.Deconnexion;
import com.example.gestion_notes.Editprofile;
import com.example.gestion_notes.R;
import com.example.gestion_notes.activity_messenger;
import com.example.gestion_notes.password;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class excel_chef extends AppCompatActivity {

    DatabaseReference reference;
    Button excel1,excel2,excel3,note;

    ImageView profile;
    TextView text2;

    private static final int PICK_EXCEL_FILE_REQUEST = 1;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.excel_chef);
        Toolbar toolbar = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        excel1=findViewById(R.id.button4);
        excel2=findViewById(R.id.button43);
        excel3=findViewById(R.id.button2);
        note=findViewById(R.id.button3);
        profile = findViewById(R.id.profileImageView2);
        text2 = findViewById(R.id.username2);

        SharedPreferences sharedPreferences = getSharedPreferences("session", MODE_PRIVATE);
        String userId = sharedPreferences.getString("sessionId", "");
        reference = FirebaseDatabase.getInstance().getReference().child("chef_filiere");
        reference.child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String nom = dataSnapshot.child("nom").getValue(String.class);
                String prenom = dataSnapshot.child("prenom").getValue(String.class);
                String username = prenom + " " + nom;
                String imageURL = dataSnapshot.child("image").getValue(String.class);

                if (username != null) {
                    text2.setText(username);
                }

                if (imageURL != null && imageURL.equals("default")) {
                    profile.setImageResource(R.mipmap.ic_launcher);
                } else if (imageURL != null) {
                    Glide.with(excel_chef.this)
                            .load(imageURL)
                            .into(profile);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("DatabaseError", "Failed to read value.", error.toException());
            }
        });
        excel1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent intent = new Intent(excel_chef.this, tableExcel.class);
                startActivity(intent);
            }
        });
        excel2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(excel_chef.this, table1_excel.class);
                startActivity(intent);
            }
        });
        excel3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(excel_chef.this, table_excel2.class);
                startActivity(intent);
            }
        });
        note.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(excel_chef.this, maquette_profR.class);
                startActivity(intent1);
            }
        });
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu3, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menuset) {
            Intent intent = new Intent(excel_chef.this, Editprofile.class);
            startActivity(intent);
            return true;
        } else if (item.getItemId() == R.id.menuout) {
            Intent intent = new Intent(excel_chef.this, Deconnexion.class);
            startActivity(intent);
            return true;
        } else if (item.getItemId() == R.id.menupara) {
            Intent intent = new Intent(excel_chef.this, ActivityProfile.class);
            startActivity(intent);
            return true;
        } else if (item.getItemId() == R.id.menumot) {
            Intent intent = new Intent(excel_chef.this, password.class);
            startActivity(intent);
            return true;
        } else if (item.getItemId() == R.id.menunot) {
            Intent intent = new Intent(excel_chef.this, activity_messenger.class);
            startActivity(intent);
            return true;
        } else {
            Toast.makeText(excel_chef.this, "Une erreur s'est produite. Veuillez réessayer plus tard.", Toast.LENGTH_SHORT).show();
            return super.onOptionsItemSelected(item);
        }
    }
}
