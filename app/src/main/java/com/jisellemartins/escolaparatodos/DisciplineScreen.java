package com.jisellemartins.escolaparatodos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.jisellemartins.escolaparatodos.R;

public class DisciplineScreen extends AppCompatActivity {
    Button itemDisciplina;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discipline_screen);

        itemDisciplina = findViewById(R.id.itemDisciplina);

        itemDisciplina.setOnClickListener(view -> {
            Intent i = new Intent(DisciplineScreen.this,
                    LibraryScreen.class);
            startActivity(i);
        });
    }
}