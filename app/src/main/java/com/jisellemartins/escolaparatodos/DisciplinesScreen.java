package com.jisellemartins.escolaparatodos;

import static com.jisellemartins.escolaparatodos.Utils.UtilAutenticacao.aluno;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jisellemartins.escolaparatodos.adapter.AdapterDisciplinas;
import com.jisellemartins.escolaparatodos.dialogs.DialogAula;
import com.jisellemartins.escolaparatodos.dialogs.DialogCriarDisciplina;
import com.jisellemartins.escolaparatodos.model.Aluno;
import com.jisellemartins.escolaparatodos.model.Disciplina;

import java.util.ArrayList;
import java.util.List;

public class DisciplinesScreen extends AppCompatActivity {
    RecyclerView listaDisciplinas;
    Button btnCriarDisciplina;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    String usuario = aluno;
    String numeroUser = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disciplinas_screen);
        listaDisciplinas = findViewById(R.id.listaDisciplinas);
        btnCriarDisciplina = findViewById(R.id.criarDisciplina);

        SharedPreferences sharedPref = getSharedPreferences("chaves", MODE_PRIVATE);
        usuario = sharedPref.getString("usuario", aluno);
        numeroUser = sharedPref.getString("numero", aluno);

        ArrayList<Disciplina> list = new ArrayList<>();


        if (usuario.equals(aluno)){
            btnCriarDisciplina.setVisibility(View.GONE);

            db.collection("Disciplina")
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful() && task.getResult().size() > 0) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Disciplina disciplina = new Disciplina();
                                disciplina.setNomeDisciplina(document.get("nome").toString());
                                disciplina.setTimestamp(document.get("dataCriacao").toString());
                                Gson gson = new Gson();
                                String json = document.get("alunos").toString();
                                ArrayList<Aluno> lstObject = gson.fromJson(json, new TypeToken<List<Aluno>>(){}.getType());
                                for (Aluno a : lstObject){
                                    if (numeroUser.equals(a.getTelefone())){
                                        list.add(disciplina);
                                    }
                                }
                            }

                            listaDisciplinas.setAdapter(new AdapterDisciplinas(this, list, usuario));
                            RecyclerView.LayoutManager layout = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
                            listaDisciplinas.setLayoutManager(layout);

                        } else {
                            Toast.makeText(this, "Erro: " + task.getException(), Toast.LENGTH_LONG).show();
                        }
                    });

        }else{
            btnCriarDisciplina.setVisibility(View.VISIBLE);

            db.collection("Disciplina")
                    .whereEqualTo("numeroProf", numeroUser)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful() && task.getResult().size() > 0) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Disciplina disciplina = new Disciplina();
                                disciplina.setNomeDisciplina(document.get("nome").toString());
                                disciplina.setTimestamp(document.get("dataCriacao").toString());
                                list.add(disciplina);
                            }

                            listaDisciplinas.setAdapter(new AdapterDisciplinas(this, list, usuario));
                            RecyclerView.LayoutManager layout = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
                            listaDisciplinas.setLayoutManager(layout);

                        } else {
                            Toast.makeText(this, "Erro: " + task.getException(), Toast.LENGTH_LONG).show();
                        }
                    });
        }
        btnCriarDisciplina.setOnClickListener(view -> {

            DialogCriarDisciplina alert = new DialogCriarDisciplina();
            alert.showDialog(this);

        });

    }
}