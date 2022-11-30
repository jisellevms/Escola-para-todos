package com.jisellemartins.escolaparatodos;

import static com.jisellemartins.escolaparatodos.CadastroScreen.getRandomNonRepeatingIntegers;
import static com.jisellemartins.escolaparatodos.Utils.Utils.aluno;
import static com.jisellemartins.escolaparatodos.Utils.Utils.atividade;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;
import com.jisellemartins.escolaparatodos.Utils.Utils;
import com.jisellemartins.escolaparatodos.adapter.AdapterQuestao;
import com.jisellemartins.escolaparatodos.dialogs.DialogLoading;
import com.jisellemartins.escolaparatodos.model.Questao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.internal.Util;

public class AtividadeQuestaoScreen extends AppCompatActivity {
    ImageView imgVoltar, imgConfig;
    RecyclerView listaQuestao;
    Button btnEnviarAtv;

    String usuario = aluno;
    String numeroUser;
    String disciplinaTime;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    DialogLoading loading = new DialogLoading();

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
        numeroUser = sharedPref.getString("numero", aluno);
        disciplinaTime = sharedPref.getString("disciplina", "");

        if (usuario.equals(aluno)) {
            btnEnviarAtv.setVisibility(View.VISIBLE);
        } else {
            btnEnviarAtv.setVisibility(View.GONE);
        }
        for (Questao questao : Utils.atividade.getQuestoes()) {
            questao.setItemSelecionado("A");
        }

        btnEnviarAtv.setOnClickListener(view -> {

            loading.showDialog(this);
            int itensAcertados = 0;
            int qtdQuestoes = atividade.getQuestoes().size();
            for (Questao questao : atividade.getQuestoes()) {
                if (questao.getItemCorreto().equals(questao.getItemSelecionado())) {
                    itensAcertados++;
                }
            }


            Map<String, Object> boletim = new HashMap<>();
            boletim.put("acertos", itensAcertados);
            boletim.put("aluno", numeroUser);
            boletim.put("descricao", atividade.getDescricao());
            boletim.put("disciplina", disciplinaTime);
            boletim.put("totalQuestao", qtdQuestoes);

            db.collection("Boletim").document(getRandomNonRepeatingIntegers(6, 0, 1000).toString())
                    .set(boletim)
                    .addOnSuccessListener(aVoid -> {
                        criarRelacaoADA();
                    })
                    .addOnFailureListener(e -> {
                        loading.fecharLoading();
                        Toast.makeText(this, "Erro: " + e, Toast.LENGTH_LONG).show();
                        Log.d("TESTEXX", "Erro:" + e);

                    });
        });

        listaQuestao.setAdapter(new AdapterQuestao(this, atividade.getQuestoes()));
        RecyclerView.LayoutManager layout = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        listaQuestao.setLayoutManager(layout);
    }

    public void criarRelacaoADA() {
        Map<String, Object> relacaoADA = new HashMap<>();
        relacaoADA.put("aluno", numeroUser);
        relacaoADA.put("disciplina", disciplinaTime);
        relacaoADA.put("atividade", atividade.getCodigoAtv());


        db.collection("RelacaoADA").document(getRandomNonRepeatingIntegers(6, 0, 1000).toString())
                .set(relacaoADA)
                .addOnSuccessListener(aVoid -> {
                    loading.fecharLoading();
                    Toast.makeText(this, "Nota de " + atividade.getDescricao() + " disponível em boletim!", Toast.LENGTH_LONG).show();
                    finish();

                })
                .addOnFailureListener(e -> {
                    loading.fecharLoading();
                    Toast.makeText(this, "Erro: " + e, Toast.LENGTH_LONG).show();
                    Log.d("TESTEXX", "Erro:" + e);

                });
    }
}