package com.jisellemartins.escolaparatodos;

import static com.jisellemartins.escolaparatodos.Utils.UtilAutenticacao.aluno;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.jisellemartins.escolaparatodos.adapter.AdapterAtividades;
import com.jisellemartins.escolaparatodos.adapter.AdapterQuestao;
import com.jisellemartins.escolaparatodos.model.Atividade;
import com.jisellemartins.escolaparatodos.model.Questao;

import java.util.ArrayList;

public class AtividadeQuestaoScreen extends AppCompatActivity {
    ImageView imgVoltar, imgConfig;
    RecyclerView listaQuestao;
    Button btnEnviarAtv;

    String usuario = aluno;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atividade_questao_screen);
        imgVoltar = findViewById(R.id.imgVoltar);
        imgConfig = findViewById(R.id.imgConfig);
        listaQuestao = findViewById(R.id.listaQuestao);
        btnEnviarAtv = findViewById(R.id.btnEnviarAtv);
        imgVoltar.setOnClickListener(view -> {
            finish();
        });
        imgConfig.setOnClickListener(view -> {
            Intent i = new Intent(AtividadeQuestaoScreen.this, ConfiguracoesScreen.class);
            startActivity(i);
        });

        SharedPreferences sharedPref = getSharedPreferences("chaves", MODE_PRIVATE);
        usuario = sharedPref.getString("usuario", aluno);

        if (usuario.equals(aluno)){
            btnEnviarAtv.setVisibility(View.VISIBLE);
        }else{
            btnEnviarAtv.setVisibility(View.GONE);
        }

        ArrayList<Questao> list = new ArrayList<>();


        Questao questao = new Questao();
        questao.setDesc("typesetting, remaining essentially unchanged. It was popularised in the 1960s" +
                " with the release of Letraset sheets containing Lorem Ipsum passages, and more recently " +
                "with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.");

        questao.setItemA("Item A");
        questao.setItemB("Item B");
        questao.setItemC("Item C");
        questao.setItemD("Item D");
        questao.setItemE("Item E");
        questao.setQtdItens(5);


        Questao questao2 = new Questao();
        questao2.setDesc("typesetting, remaining essentially unchanged. It was popularised in the 1960s" +
                " with the release of Letraset sheets containing Lorem Ipsum passages, and more recently " +
                "with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.");

        questao2.setItemA("Item A");
        questao2.setItemB("Item B");
        questao2.setItemC("Item C");
        questao2.setItemD("Item D");
        questao2.setQtdItens(4);

        Questao questao3 = new Questao();
        questao3.setDesc("typesetting, remaining essentially unchanged. It was popularised in the 1960s" +
                " with the release of Letraset sheets containing Lorem Ipsum passages, and more recently " +
                "with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.");

        questao3.setItemA("Item A");
        questao3.setItemB("Item B");
        questao3.setQtdItens(2);


        list.add(questao);
        list.add(questao2);
        list.add(questao3);


        listaQuestao.setAdapter(new AdapterQuestao(this, list));
        RecyclerView.LayoutManager layout = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        listaQuestao.setLayoutManager(layout);
    }
}