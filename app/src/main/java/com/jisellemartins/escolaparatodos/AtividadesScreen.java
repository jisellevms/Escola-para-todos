package com.jisellemartins.escolaparatodos;

import static com.jisellemartins.escolaparatodos.Utils.Utils.aluno;
import static com.jisellemartins.escolaparatodos.Utils.Utils.nomeDisciplina;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jisellemartins.escolaparatodos.adapter.AdapterAtividades;
import com.jisellemartins.escolaparatodos.dialogs.DialogLoading;
import com.jisellemartins.escolaparatodos.model.Atividade;
import com.jisellemartins.escolaparatodos.model.Questao;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class AtividadesScreen extends AppCompatActivity {
    ImageView imgVoltar, imgConfig;
    RecyclerView listaAtividades;
    Button btnCriarAtividade, tituloDisciplina;

    String usuario = aluno;
    String disciplinaTime;
    String numeroUser;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ArrayList<Atividade> list = new ArrayList<>();
    DialogLoading loading = new DialogLoading();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atividades_screen);
        imgVoltar = findViewById(R.id.imgVoltar);
        imgConfig = findViewById(R.id.imgConfig);
        listaAtividades = findViewById(R.id.listaAtividades);
        btnCriarAtividade = findViewById(R.id.btnCriarAtividade);
        tituloDisciplina = findViewById(R.id.tituloDisciplina);

        tituloDisciplina.setText("Atividades - " + nomeDisciplina);

        loading.showDialog(this);

        SharedPreferences sharedPref = getSharedPreferences("chaves", MODE_PRIVATE);
        usuario = sharedPref.getString("usuario", aluno);
        disciplinaTime = sharedPref.getString("disciplina", "");
        numeroUser = sharedPref.getString("numero", aluno);


        if (usuario.equals(aluno)) {
            btnCriarAtividade.setVisibility(View.GONE);
        } else {
            btnCriarAtividade.setVisibility(View.VISIBLE);
        }
        imgVoltar.setOnClickListener(view -> {
            finish();
        });
        imgConfig.setOnClickListener(view -> {
            Intent i = new Intent(AtividadesScreen.this, ConfiguracoesScreen.class);
            startActivity(i);
        });

        btnCriarAtividade.setOnClickListener(view -> {
            Intent i = new Intent(AtividadesScreen.this,
                    CriarAtividadeScreen.class);
            startActivity(i);
        });


    }

    public void listarAtividades() {
        list = new ArrayList<>();
        Gson gson = new Gson();

        CollectionReference complaintsRef = db.collection("Atividade");
        complaintsRef.whereEqualTo("disciplina", disciplinaTime).get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult().size() > 0) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    String jsonQuestoes = document.get("jsonQuestoes").toString();
                    ArrayList<Questao> listQuestoes = gson.fromJson(jsonQuestoes, new TypeToken<List<Questao>>() {
                    }.getType());

                    Atividade atividade = new Atividade();
                    atividade.setCodigoAtv(document.get("atividade").toString());
                    atividade.setStatus("Visualização");
                    atividade.setDescricao(document.get("descricao").toString());
                    atividade.setData(document.get("data").toString());
                    atividade.setQtdQuestoes(document.get("qtdQuestoes").toString());

                    atividade.setQuestoes(listQuestoes);
                    list.add(atividade);
                }
                loading.fecharLoading();
                listaAtividades.setAdapter(new AdapterAtividades(this, list, usuario));
                RecyclerView.LayoutManager layout = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
                listaAtividades.setLayoutManager(layout);
            } else {
                loading.fecharLoading();
                Toast.makeText(this, "Não existe atividades cadastradas", Toast.LENGTH_LONG).show();
            }


        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        listarAtividades();

    }
}