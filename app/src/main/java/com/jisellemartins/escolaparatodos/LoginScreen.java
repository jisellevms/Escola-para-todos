package com.jisellemartins.escolaparatodos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class LoginScreen extends AppCompatActivity {
    TextView txtNaoCadastrado;
    ImageView imgVoltar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);

        txtNaoCadastrado = findViewById(R.id.txtNaoCadastrado);
        imgVoltar = findViewById(R.id.imgVoltar);

        txtNaoCadastrado.setOnClickListener(view -> {
            Intent i = new Intent(LoginScreen.this,
                    RegisterScreen.class);
            startActivity(i);
        });

        imgVoltar.setOnClickListener(view -> {
            finish();
        });
    }
}