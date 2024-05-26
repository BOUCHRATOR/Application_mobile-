package com.example.gestion_notes;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
public class MainActivity extends AppCompatActivity {
    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.button); // Assurez-vous que l'ID est correct
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Code exécuté lorsque le TextView est cliqué
                Intent intent = new Intent(MainActivity.this,ActivityDestination.class);
                startActivity(intent);
            }
        });
    }
}
