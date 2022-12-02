package com.jisellemartins.escolaparatodos;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jisellemartins.escolaparatodos.adapter.AdapterGerenciarAlunos;
import com.jisellemartins.escolaparatodos.dialogs.DialogAdicionarAluno;
import com.jisellemartins.escolaparatodos.model.Aluno;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GerenciarAlunosScreen extends AppCompatActivity {
    ImageView imgVoltar, imgConfig;
    RecyclerView listaAlunos;
    Button btnAdcAluno;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String jsonAlunos;
    String disciplinaTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gerenciar_alunos);
        imgVoltar = findViewById(R.id.imgVoltar);
        imgConfig = findViewById(R.id.imgConfig);
        listaAlunos = findViewById(R.id.listaAlunos);
        btnAdcAluno = findViewById(R.id.btnAdcArquivo);

        btnAdcAluno.setOnClickListener(view -> {
            DialogAdicionarAluno alert = new DialogAdicionarAluno();
            alert.showDialog(this, this);
        });

        imgVoltar.setOnClickListener(view -> {
            finish();
        });
        imgConfig.setOnClickListener(view -> {
            Intent i = new Intent(GerenciarAlunosScreen.this, ConfiguracoesScreen.class);
            startActivity(i);
        });
        SharedPreferences sharedPref = getSharedPreferences("chaves", MODE_PRIVATE);
        disciplinaTime = sharedPref.getString("disciplina", "");

        listarAlunos();

    }

    public void listarAlunos() {
        Gson gson = new Gson();
        CollectionReference complaintsRef = db.collection("Disciplina");
        complaintsRef.whereEqualTo("dataCriacao", disciplinaTime).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    jsonAlunos = document.get("alunos").toString();

                    if (!jsonAlunos.equals("{}")) {
                        ArrayList<Aluno> list = gson.fromJson(jsonAlunos, new TypeToken<List<Aluno>>() {
                        }.getType());

                        listaAlunos.setAdapter(new AdapterGerenciarAlunos(this, list, this));
                        RecyclerView.LayoutManager layout = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

                        listaAlunos.setLayoutManager(layout);
                    } else {
                        Toast.makeText(this, "Não existe alunos cadastrados", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }
}