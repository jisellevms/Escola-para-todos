package com.jisellemartins.escolaparatodos;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.jisellemartins.escolaparatodos.adapter.AdapterAulas;
import com.jisellemartins.escolaparatodos.adapter.AdapterDisciplinas;
import com.jisellemartins.escolaparatodos.model.Aula;
import com.jisellemartins.escolaparatodos.model.Disciplina;

import java.util.ArrayList;

public class LessonScreen extends AppCompatActivity {
    RecyclerView listaAulas;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson_screen);

        listaAulas = findViewById(R.id.listaAulas);

        ArrayList<Aula> list = new ArrayList<>();
        Aula aula = new Aula();
        Aula aula2 = new Aula();
        Aula aula3 = new Aula();

        aula.setDescricao("Aula: Matriz");
        aula2.setDescricao("Aula: Geometria");
        aula3.setDescricao("Aula: Áreas");

        list.add(aula);
        list.add(aula2);
        list.add(aula3);


        listaAulas.setAdapter(new AdapterAulas(this, list));
        RecyclerView.LayoutManager layout = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        listaAulas.setLayoutManager(layout);
    }
}