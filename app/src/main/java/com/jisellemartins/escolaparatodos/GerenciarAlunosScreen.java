package com.jisellemartins.escolaparatodos;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
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

    RecyclerView listaAlunos;
    Button btnAdcAluno;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String jsonAlunos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gerenciar_alunos);

        listaAlunos = findViewById(R.id.listaAlunos);
        btnAdcAluno = findViewById(R.id.btnAdcArquivo);

        btnAdcAluno.setOnClickListener(view -> {
            DialogAdicionarAluno alert = new DialogAdicionarAluno();
            alert.showDialog(this);
        });

        /*ArrayList<Aluno> list = new ArrayList<>();
        Aluno aluno = new Aluno();
        Aluno aluno2 = new Aluno();
        Aluno aluno3 = new Aluno();

        aluno.setNome("ANA CLARA MEDEIROS");
        aluno2.setNome("ANA CLARA MEDEIROS");
        aluno3.setNome("ANA CLARA MEDEIROS");

        aluno.setTelefone("(85) 986373679");
        aluno2.setTelefone("(85) 986373679");
        aluno3.setTelefone("(85) 986373679");

        list.add(aluno);
        list.add(aluno2);
        list.add(aluno3);*/


        SharedPreferences sharedPref = getSharedPreferences("chaves", MODE_PRIVATE);
        String disciplinaTime = sharedPref.getString("disciplina", "");
        Gson gson = new Gson();

        CollectionReference complaintsRef = db.collection("Disciplina");
        complaintsRef.whereEqualTo("dataCriacao", disciplinaTime).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    jsonAlunos = document.get("alunos").toString();

                    if (!jsonAlunos.equals("{}")){
                        ArrayList<Aluno> list = gson.fromJson(jsonAlunos, new TypeToken<List<Aluno>>(){}.getType());

                        listaAlunos.setAdapter(new AdapterGerenciarAlunos(this, list));
                        RecyclerView.LayoutManager layout = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

                        listaAlunos.setLayoutManager(layout);
                    }else{
                        Toast.makeText(this, "Não existe alunos cadastrados", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });



    }
}