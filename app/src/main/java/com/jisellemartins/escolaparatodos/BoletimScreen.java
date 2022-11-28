package com.jisellemartins.escolaparatodos;

import static com.jisellemartins.escolaparatodos.Utils.Utils.aluno;
import static com.jisellemartins.escolaparatodos.Utils.Utils.nomeDisciplina;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jisellemartins.escolaparatodos.adapter.AdapterBoletim;
import com.jisellemartins.escolaparatodos.adapter.AdapterBoletimProfessor;
import com.jisellemartins.escolaparatodos.model.Aluno;
import com.jisellemartins.escolaparatodos.model.Boletim;

import java.util.ArrayList;
import java.util.List;

public class BoletimScreen extends AppCompatActivity {
    ImageView imgVoltar, imgConfig;
    RecyclerView listaReportCard;


    String usuario = aluno;
    String jsonAlunos;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String numeroUsuario;
    String disciplinaTime;
    ArrayList<Boletim> list = new ArrayList<>();
    Gson gson = new Gson();

    Button tituloDisciplina;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boletim_screen);
        imgVoltar = findViewById(R.id.imgVoltar);
        imgConfig = findViewById(R.id.imgConfig);
        listaReportCard = findViewById(R.id.listaReportCard);
        tituloDisciplina = findViewById(R.id.tituloDisciplina);

        tituloDisciplina.setText("Boletim - " + nomeDisciplina);

        SharedPreferences sharedPref = getSharedPreferences("chaves", MODE_PRIVATE);
        usuario = sharedPref.getString("usuario", aluno);
        disciplinaTime = sharedPref.getString("disciplina", "");
        numeroUsuario = sharedPref.getString("numero", "");

        if (usuario.equals(aluno)) {
            exibirParaAluno();
        } else {
            exibirParaProfessor();
        }

        imgVoltar.setOnClickListener(view -> {
            finish();
        });
        imgConfig.setOnClickListener(view -> {
            Intent i = new Intent(BoletimScreen.this, ConfiguracoesScreen.class);
            startActivity(i);
        });
    }

    public void exibirParaProfessor() {
        CollectionReference complaintsRef = db.collection("Disciplina");
        complaintsRef.whereEqualTo("dataCriacao", disciplinaTime).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    jsonAlunos = document.get("alunos").toString();

                    if (!jsonAlunos.equals("{}")) {
                        ArrayList<Aluno> list = gson.fromJson(jsonAlunos, new TypeToken<List<Aluno>>() {
                        }.getType());

                        listaReportCard.setAdapter(new AdapterBoletimProfessor(this, list));
                        RecyclerView.LayoutManager layout = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

                        listaReportCard.setLayoutManager(layout);
                    } else {
                        Toast.makeText(this, "Não existe alunos cadastrados", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }

    public void exibirParaAluno() {
        CollectionReference complaintsRef = db.collection("Boletim");
        complaintsRef.whereEqualTo("disciplina", disciplinaTime).whereEqualTo("aluno", numeroUsuario).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                if (task.getResult().size() > 0) {
                    for (QueryDocumentSnapshot document : task.getResult()) {

                        Boletim boletim = new Boletim();
                        boletim.setDescricao(document.get("descricao").toString());
                        boletim.setAcertos(Integer.parseInt(document.get("acertos").toString()));
                        boletim.setTotal(Integer.parseInt(document.get("totalQuestao").toString()));
                        list.add(boletim);
                    }

                    listaReportCard.setAdapter(new AdapterBoletim(this, list));
                    RecyclerView.LayoutManager layout = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

                    listaReportCard.setLayoutManager(layout);
                } else {
                    Toast.makeText(this, "Ainda não existe boletim para essa disciplina", Toast.LENGTH_LONG).show();
                }

            } else {
                Toast.makeText(this, "Erro: " + task.getException(), Toast.LENGTH_LONG).show();
            }
        });
    }
}