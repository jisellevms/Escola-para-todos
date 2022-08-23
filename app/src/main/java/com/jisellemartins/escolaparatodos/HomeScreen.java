package com.jisellemartins.escolaparatodos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class HomeScreen extends AppCompatActivity {
    Button btnAreaProf,btnAreaAluno;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        btnAreaProf = findViewById(R.id.btnAreaProf);
        btnAreaAluno = findViewById(R.id.btnAreaAluno);

        btnAreaProf.setOnClickListener(view -> {
            Intent i = new Intent(HomeScreen.this,
                    LoginScreen.class);
            startActivity(i);
        });


    }
}