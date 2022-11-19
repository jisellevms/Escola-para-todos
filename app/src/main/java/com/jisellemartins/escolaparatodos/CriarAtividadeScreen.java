package com.jisellemartins.escolaparatodos;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.jisellemartins.escolaparatodos.adapter.AdapterItemQuestao;
import com.jisellemartins.escolaparatodos.adapter.AdapterQuestao;
import com.jisellemartins.escolaparatodos.model.Questao;

import java.util.ArrayList;

public class CriarAtividadeScreen extends AppCompatActivity {

    ImageView imgVoltar, imgConfig;
    RecyclerView listaQuestoes;
    ArrayList<Questao> list = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_criar_atividade_screen);
        imgVoltar = findViewById(R.id.imgVoltar);
        imgConfig = findViewById(R.id.imgConfig);

        listaQuestoes = findViewById(R.id.listaQuestoes);

        imgVoltar.setOnClickListener(view -> {
            finish();
        });
        imgConfig.setOnClickListener(view -> {
            Intent i = new Intent(CriarAtividadeScreen.this, ConfiguracoesScreen.class);
            startActivity(i);
        });

        popularLista();

        listaQuestoes.setAdapter(new AdapterItemQuestao(this, list));
        RecyclerView.LayoutManager layout = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        listaQuestoes.setLayoutManager(layout);


    }

    public void popularLista(){
        Questao questao = new Questao();
        questao.setDesc("teste");
        questao.setItemA("Item A");
        questao.setItemB("Item B");
        questao.setItemC("Item C");
        questao.setItemD("Item D");
        questao.setItemE("Item E");
        questao.setQtdItens(5);


        Questao questao2 = new Questao();
        questao2.setDesc("teste");
        questao2.setItemA("Item A");
        questao2.setItemB("Item B");
        questao2.setItemC("Item C");
        questao2.setItemD("Item D");
        questao2.setQtdItens(4);

        Questao questao3 = new Questao();
        questao3.setDesc("teste");
        questao3.setItemA("Item A");
        questao3.setItemB("Item B");
        questao3.setQtdItens(2);

        Questao questao4 = new Questao();
        questao4.setDesc("teste");
        questao4.setItemA("Item A");
        questao4.setItemB("Item B");
        questao4.setItemC("Item C");
        questao4.setItemD("Item D");
        questao4.setItemE("Item E");
        questao4.setQtdItens(5);


        Questao questao5 = new Questao();
        questao5.setDesc("teste");
        questao5.setItemA("Item A");
        questao5.setItemB("Item B");
        questao5.setItemC("Item C");
        questao5.setItemD("Item D");
        questao5.setQtdItens(4);

        Questao questao6 = new Questao();
        questao6.setDesc("teste");
        questao6.setItemA("Item A");
        questao6.setItemB("Item B");
        questao6.setQtdItens(2);

        Questao questao7 = new Questao();
        questao7.setDesc("teste");
        questao7.setItemA("Item A");
        questao7.setItemB("Item B");
        questao7.setItemC("Item C");
        questao7.setItemD("Item D");
        questao7.setItemE("Item E");
        questao7.setQtdItens(5);


        Questao questao8 = new Questao();
        questao8.setDesc("teste");
        questao8.setItemA("Item A");
        questao8.setItemB("Item B");
        questao8.setItemC("Item C");
        questao8.setItemD("Item D");
        questao8.setQtdItens(4);

        Questao questao9 = new Questao();
        questao9.setDesc("teste");
        questao9.setItemA("Item A");
        questao9.setItemB("Item B");
        questao9.setQtdItens(2);

        Questao questao10 = new Questao();
        questao10.setDesc("teste");
        questao10.setItemA("Item A");
        questao10.setItemB("Item B");
        questao10.setItemC("Item C");
        questao10.setItemD("Item D");
        questao10.setItemE("Item E");
        questao10.setQtdItens(5);


        Questao questao11 = new Questao();
        questao11.setDesc("teste");
        questao11.setItemA("Item A");
        questao11.setItemB("Item B");
        questao11.setItemC("Item C");
        questao11.setItemD("Item D");
        questao11.setQtdItens(4);

        Questao questao12 = new Questao();
        questao12.setDesc("teste");
        questao12.setItemA("Item A");
        questao12.setItemB("Item B");
        questao12.setQtdItens(2);


        list.add(questao);
        list.add(questao2);
        list.add(questao3);
        list.add(questao4);
        list.add(questao5);
        list.add(questao6);
        list.add(questao7);
        list.add(questao8);
        list.add(questao9);
        list.add(questao10);
        list.add(questao11);
        list.add(questao12);
    }
}