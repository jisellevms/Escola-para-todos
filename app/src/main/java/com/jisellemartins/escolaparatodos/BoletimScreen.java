package com.jisellemartins.escolaparatodos;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jisellemartins.escolaparatodos.adapter.AdapterBoletim;
import com.jisellemartins.escolaparatodos.adapter.AdapterBoletimProfessor;
import com.jisellemartins.escolaparatodos.model.Aluno;
import com.jisellemartins.escolaparatodos.model.Boletim;

import java.util.ArrayList;

public class BoletimScreen extends AppCompatActivity {
    RecyclerView listaReportCard;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boletim_screen);
        listaReportCard = findViewById(R.id.listaReportCard);

        int type = 2;
        if(type == 1){
            ArrayList<Boletim> list = new ArrayList<>();
            Boletim boletim = new Boletim();
            Boletim boletim2 = new Boletim();
            Boletim boletim3 = new Boletim();

            boletim.setDescricao("Prova Matriz");
            boletim.setAcertos(7);
            boletim.setTotal(10);

            boletim2.setDescricao("Atividade Vetores");
            boletim2.setAcertos(5);
            boletim2.setTotal(10);

            boletim3.setDescricao("Atividade Matemática");
            boletim3.setAcertos(10);
            boletim3.setTotal(10);

            list.add(boletim);
            list.add(boletim2);
            list.add(boletim3);


            listaReportCard.setAdapter(new AdapterBoletim(this, list));
            RecyclerView.LayoutManager layout = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

            listaReportCard.setLayoutManager(layout);
        }else{

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

            listaReportCard.setAdapter(new AdapterBoletimProfessor(this, list));
            RecyclerView.LayoutManager layout = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

            listaReportCard.setLayoutManager(layout);

        }



    }
}