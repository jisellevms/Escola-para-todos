package com.jisellemartins.escolaparatodos;

import static com.jisellemartins.escolaparatodos.CadastroScreen.getRandomNonRepeatingIntegers;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.jisellemartins.escolaparatodos.Utils.Mask;
import com.jisellemartins.escolaparatodos.Utils.Utils;
import com.jisellemartins.escolaparatodos.adapter.AdapterItemQuestao;
import com.jisellemartins.escolaparatodos.model.Questao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CriarAtividadeScreen extends AppCompatActivity {

    ImageView imgVoltar, imgConfig;
    RecyclerView listaQuestoes;
    ArrayList<Questao> list = new ArrayList<>();
    Button btnQtdQuestoes, criarAtv;
    EditText qtdQuestoes, descricaoAtv, dataAtividade;
    int quantidadeQuestoes = 0;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_criar_atividade_screen);
        imgVoltar = findViewById(R.id.imgVoltar);
        imgConfig = findViewById(R.id.imgConfig);
        btnQtdQuestoes = findViewById(R.id.btnQtdQuestoes);
        qtdQuestoes = findViewById(R.id.qtdQuestoes);
        descricaoAtv = findViewById(R.id.descricaoAtv);
        dataAtividade = findViewById(R.id.dataAtividade);
        criarAtv = findViewById(R.id.criarAtv);

        listaQuestoes = findViewById(R.id.listaQuestoes);

        imgVoltar.setOnClickListener(view -> {
            finish();
        });
        imgConfig.setOnClickListener(view -> {
            Intent i = new Intent(CriarAtividadeScreen.this, ConfiguracoesScreen.class);
            startActivity(i);
        });


        dataAtividade.addTextChangedListener(Mask.insert("##/##/####", dataAtividade));

        btnQtdQuestoes.setOnClickListener(view -> {
            Utils.listQuestoes = new ArrayList<>();
            if(!qtdQuestoes.getText().toString().isEmpty()){
                quantidadeQuestoes = Integer.parseInt(qtdQuestoes.getText().toString());
                int count = 0;
                list.clear();
                while(list.size() < quantidadeQuestoes){
                    count++;
                    Questao questao = new Questao();
                    questao.setDesc("Insira uma descrição");
                    questao.setItemA("Item A");
                    questao.setItemB("Item B");
                    questao.setItemC("Item C");
                    questao.setItemD("Item D");
                    questao.setItemE("Item E");
                    questao.setQtdItens(5);
                    questao.setNumeroQuestao(count);
                    list.add(questao);
                }

                listaQuestoes.setAdapter(new AdapterItemQuestao(this, list));
                RecyclerView.LayoutManager layout = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
                listaQuestoes.setLayoutManager(layout);

                listaQuestoes.setVisibility(View.VISIBLE);

            }else{
                Toast.makeText(this, "Digite um número", Toast.LENGTH_SHORT).show();
            }

        });

        criarAtv.setOnClickListener(view -> {

            if (!qtdQuestoes.getText().toString().isEmpty() && !descricaoAtv.getText().toString().isEmpty()
                    && !dataAtividade.getText().toString().isEmpty()){

                Gson gson = new Gson();
                String stringJson = gson.toJson(Utils.listQuestoes);
                Map<String, Object> atividade = new HashMap<>();
                atividade.put("descricao", descricaoAtv.getText().toString());
                atividade.put("data", dataAtividade.getText().toString());
                atividade.put("qtdQuestoes", Utils.listQuestoes.size());
                atividade.put("status", false);
                atividade.put("jsonQuestoes", stringJson);

                db.collection("Atividade").document(getRandomNonRepeatingIntegers(6,0,1000).toString())
                        .set(atividade)
                        .addOnSuccessListener(aVoid -> {

                            Toast.makeText(this, "Atividade cadastrada com sucesso!", Toast.LENGTH_LONG).show();
                            finish();


                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(this, "Erro: " + e, Toast.LENGTH_LONG).show();
                            Log.d("TESTEXX", "Erro:" + e);

                        });

            }else{
                Toast.makeText(this, "É necessário preencher todos os campos", Toast.LENGTH_LONG).show();
            }
        });





    }

}