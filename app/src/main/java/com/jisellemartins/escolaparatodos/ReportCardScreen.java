package com.jisellemartins.escolaparatodos;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jisellemartins.escolaparatodos.adapter.AdapterBoletim;
import com.jisellemartins.escolaparatodos.model.Boletim;

import java.util.ArrayList;

public class ReportCardScreen extends AppCompatActivity {
    RecyclerView listaReportCard;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_card_screen);
        listaReportCard = findViewById(R.id.listaReportCard);

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

    }
}