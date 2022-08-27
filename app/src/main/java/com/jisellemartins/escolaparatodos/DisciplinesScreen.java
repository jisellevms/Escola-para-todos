package com.jisellemartins.escolaparatodos;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.jisellemartins.escolaparatodos.adapter.AdapterDisciplinas;
import com.jisellemartins.escolaparatodos.model.Disciplina;

import java.util.ArrayList;

public class DisciplinesScreen extends AppCompatActivity {
    RecyclerView listaDisciplinas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disciplines_screen);
        listaDisciplinas = findViewById(R.id.listaDisciplinas);

        ArrayList<Disciplina> list = new ArrayList<>();
        Disciplina disciplina = new Disciplina();
        Disciplina disciplina2 = new Disciplina();
        Disciplina disciplina3 = new Disciplina();
        disciplina.setNomeDisciplina("Matemática");
        disciplina2.setNomeDisciplina("Português");
        disciplina3.setNomeDisciplina("Geografia");

        list.add(disciplina);
        list.add(disciplina2);
        list.add(disciplina3);


        listaDisciplinas.setAdapter(new AdapterDisciplinas(this, list));
        RecyclerView.LayoutManager layout = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        listaDisciplinas.setLayoutManager(layout);

    }
}