package com.jisellemartins.escolaparatodos;

import static com.jisellemartins.escolaparatodos.Utils.Utils.aluno;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jisellemartins.escolaparatodos.adapter.AdapterDisciplinas;
import com.jisellemartins.escolaparatodos.dialogs.DialogCriarDisciplina;
import com.jisellemartins.escolaparatodos.model.Aluno;
import com.jisellemartins.escolaparatodos.model.Disciplina;

import java.util.ArrayList;
import java.util.List;

public class DisciplinesScreen extends AppCompatActivity {
    ImageView imgVoltar, imgConfig;

    RecyclerView listaDisciplinas;
    Button btnCriarDisciplina;

    TextView bemVindo;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    String usuario = aluno;
    String numeroUser = "";
    ArrayList<Disciplina> list = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disciplinas_screen);
        listaDisciplinas = findViewById(R.id.listaDisciplinas);
        btnCriarDisciplina = findViewById(R.id.criarDisciplina);
        imgVoltar = findViewById(R.id.imgVoltar);
        imgConfig = findViewById(R.id.imgConfig);
        bemVindo = findViewById(R.id.bemVindo);

        SharedPreferences sharedPref = getSharedPreferences("chaves", MODE_PRIVATE);
        usuario = sharedPref.getString("usuario", aluno);
        numeroUser = sharedPref.getString("numero", aluno);

        imgVoltar.setOnClickListener(view -> {
            finish();
        });
        imgConfig.setOnClickListener(view -> {
            Intent i = new Intent(DisciplinesScreen.this, ConfiguracoesScreen.class);
            startActivity(i);
        });


        btnCriarDisciplina.setOnClickListener(view -> {

            DialogCriarDisciplina alert = new DialogCriarDisciplina();
            alert.showDialog(this);

        });

        //showDisciplinas();

    }

    public void showDisciplinas(){
        list.clear();
        if (usuario.equals(aluno)){
            btnCriarDisciplina.setVisibility(View.GONE);
            bemVindo.setText("Bem vindo, aluno");

            db.collection("Disciplina")
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful() && task.getResult().size() > 0) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Disciplina disciplina = new Disciplina();
                                disciplina.setNomeDisciplina(document.get("nome").toString());
                                disciplina.setTimestamp(document.get("dataCriacao").toString());
                                if(!document.get("alunos").toString().equals("{}")){
                                    Gson gson = new Gson();
                                    String json = document.get("alunos").toString();
                                    ArrayList<Aluno> lstObject = gson.fromJson(json, new TypeToken<List<Aluno>>(){}.getType());
                                    for (Aluno a : lstObject){
                                        if (numeroUser.equals(a.getTelefone())){
                                            list.add(disciplina);
                                        }
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
            bemVindo.setText("Bem vindo, professor");
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
                            Log.i("TESTEXX", "Erro: " + task.getException());
                        }
                    });
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        showDisciplinas();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}