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

import com.jisellemartins.escolaparatodos.adapter.AdapterAtividades;
import com.jisellemartins.escolaparatodos.model.Atividade;

import java.util.ArrayList;

public class AtividadesScreen extends AppCompatActivity {

    RecyclerView listaAtividades;
    Button btnCriarAtividade;

    String usuario = aluno;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atividades_screen);

        listaAtividades = findViewById(R.id.listaAtividades);
        btnCriarAtividade = findViewById(R.id.btnCriarAtividade);

        SharedPreferences sharedPref = getSharedPreferences("chaves", MODE_PRIVATE);
        usuario = sharedPref.getString("usuario", aluno);

        if (usuario.equals(aluno)){
            btnCriarAtividade.setVisibility(View.GONE);
        }else{
            btnCriarAtividade.setVisibility(View.VISIBLE);
        }

        btnCriarAtividade.setOnClickListener(view -> {
            Intent i = new Intent(AtividadesScreen.this,
                    CriarAtividadeScreen.class);
            startActivity(i);
        });

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



        listaAtividades.setAdapter(new AdapterAtividades(this, list, usuario));
        RecyclerView.LayoutManager layout = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        listaAtividades.setLayoutManager(layout);
    }
}