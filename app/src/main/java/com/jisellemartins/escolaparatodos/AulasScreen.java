package com.jisellemartins.escolaparatodos;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.jisellemartins.escolaparatodos.adapter.AdapterAulas;
import com.jisellemartins.escolaparatodos.model.Aula;

import java.util.ArrayList;

public class AulasScreen extends AppCompatActivity {
    RecyclerView listaAulas;
    Button iniciarAula;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aulas_screen);

        listaAulas = findViewById(R.id.listaAulas);
        iniciarAula = findViewById(R.id.iniciarAula);

        iniciarAula.setOnClickListener(view -> {
            DialogAula alert = new DialogAula();
            alert.showDialog(this);
        });

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