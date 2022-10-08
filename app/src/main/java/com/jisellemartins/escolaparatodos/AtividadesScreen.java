package com.jisellemartins.escolaparatodos;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.jisellemartins.escolaparatodos.adapter.AdapterAtividades;
import com.jisellemartins.escolaparatodos.adapter.AdapterConteudo;
import com.jisellemartins.escolaparatodos.model.Atividade;
import com.jisellemartins.escolaparatodos.model.Conteudo;

import java.util.ArrayList;

public class AtividadesScreen extends AppCompatActivity {

    RecyclerView listaAtividades;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks_screen);

        listaAtividades = findViewById(R.id.listaAtividades);

        ArrayList<Atividade> list = new ArrayList<>();


        Atividade atividade =  new Atividade();
        atividade.setDescricao("Atividade sobre Matriz");
        atividade.setData("29/09/2022");
        atividade.setQtdQuestoes("10");
        atividade.setStatus("Pendente");


        Atividade atividade2 =  new Atividade();
        atividade2.setDescricao("Atividade sobre Matriz");
        atividade2.setData("29/09/2022");
        atividade2.setQtdQuestoes("10");
        atividade2.setStatus("Concluido");

        Atividade atividade3 =  new Atividade();
        atividade3.setDescricao("Atividade sobre Matriz");
        atividade3.setData("29/09/2022");
        atividade3.setQtdQuestoes("10");
        atividade3.setStatus("-");


        list.add(atividade);
        list.add(atividade2);
        list.add(atividade3);



        listaAtividades.setAdapter(new AdapterAtividades(this, list));
        RecyclerView.LayoutManager layout = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        listaAtividades.setLayoutManager(layout);
    }
}