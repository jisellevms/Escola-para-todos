package com.jisellemartins.escolaparatodos;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.jisellemartins.escolaparatodos.adapter.AdapterBoletimProfessor;
import com.jisellemartins.escolaparatodos.adapter.AdapterGerenciarAlunos;
import com.jisellemartins.escolaparatodos.model.Aluno;

import java.util.ArrayList;

public class GerenciarAlunosScreen extends AppCompatActivity {

    RecyclerView listaAlunos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gerenciar_alunos);

        listaAlunos = findViewById(R.id.listaAlunos);

        ArrayList<Aluno> list = new ArrayList<>();
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
        list.add(aluno3);

        listaAlunos.setAdapter(new AdapterGerenciarAlunos(this, list));
        RecyclerView.LayoutManager layout = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        listaAlunos.setLayoutManager(layout);
    }
}