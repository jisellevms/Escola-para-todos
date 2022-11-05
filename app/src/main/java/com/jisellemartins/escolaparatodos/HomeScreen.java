package com.jisellemartins.escolaparatodos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class HomeScreen extends AppCompatActivity {
    Button btnAreaProf,btnAreaAluno;
    TextView txtNaoCadastrado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        btnAreaProf = findViewById(R.id.btnAreaProf);
        btnAreaAluno = findViewById(R.id.btnAreaAluno);
        txtNaoCadastrado = findViewById(R.id.txtNaoCadastrado);

        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        btnAreaProf.setOnClickListener(view -> {
            editor.putString("usuario","professor");
            editor.apply();
            Intent i = new Intent(HomeScreen.this,
                    LoginScreen.class);
            startActivity(i);

        });

        btnAreaAluno.setOnClickListener(view -> {
            editor.putString("usuario","aluno");
            editor.apply();
            Intent i = new Intent(HomeScreen.this,
                    LoginScreen.class);
            startActivity(i);
        });

        txtNaoCadastrado.setOnClickListener(view -> {
            Intent i = new Intent(HomeScreen.this,
                    CadastroScreen.class);
            startActivity(i);
        });


    }
}