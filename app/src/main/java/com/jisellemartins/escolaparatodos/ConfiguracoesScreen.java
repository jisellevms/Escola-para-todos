package com.jisellemartins.escolaparatodos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ConfiguracoesScreen extends AppCompatActivity {
    ImageView imgVoltar, imgConfig;
    TextView versao;
    Button btnLogout, btnDocUsuario;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracoes_screen);
        imgVoltar = findViewById(R.id.imgVoltar);
        imgConfig = findViewById(R.id.imgConfig);
        versao = findViewById(R.id.versao);
        btnLogout = findViewById(R.id.btnLogout);
        btnDocUsuario = findViewById(R.id.btnDocUsuario);

        String versionName = BuildConfig.VERSION_NAME;

        versao.setText("Versão: " + versionName);

        imgVoltar.setOnClickListener(view -> {
            finish();
        });
        imgConfig.setOnClickListener(view -> {

        });
        btnLogout.setOnClickListener(view -> {
           finishAffinity();
        });
        btnDocUsuario.setOnClickListener(view -> {
            Toast.makeText(this, "Aqui irá exibir a documentação do usuário", Toast.LENGTH_SHORT).show();
        });



    }
}