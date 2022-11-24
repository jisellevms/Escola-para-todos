package com.jisellemartins.escolaparatodos;

import static com.jisellemartins.escolaparatodos.BibliotecaScreen.bytesIntoHumanReadable;
import static com.jisellemartins.escolaparatodos.Utils.UtilAutenticacao.aluno;
import static com.jisellemartins.escolaparatodos.Utils.UtilAutenticacao.nomeDisciplina;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.jisellemartins.escolaparatodos.adapter.AdapterAulas;
import com.jisellemartins.escolaparatodos.dialogs.DialogAula;
import com.jisellemartins.escolaparatodos.dialogs.DialogLoading;
import com.jisellemartins.escolaparatodos.model.Aula;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class AulasScreen extends AppCompatActivity {
    ImageView imgVoltar, imgConfig;
    RecyclerView listaAulas;
    Button iniciarAula, btnEntrarAula, tituloDisciplina;
    LinearLayoutCompat cardAluno, cardProfessor;
    TextView descricaoAula;

    String usuario = aluno;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String nomeAulaParaChanel;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    String disciplinaTime;

    ArrayList<Aula> list = new ArrayList<>();
    DialogLoading dialogLoading = new DialogLoading();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aulas_screen);
        imgVoltar = findViewById(R.id.imgVoltar);
        imgConfig = findViewById(R.id.imgConfig);

        listaAulas = findViewById(R.id.listaAulas);
        iniciarAula = findViewById(R.id.iniciarAula);
        cardAluno = findViewById(R.id.cardAluno);
        cardProfessor = findViewById(R.id.cardProfessor);
        btnEntrarAula = findViewById(R.id.btnEntrarAula);
        descricaoAula = findViewById(R.id.descricaoAula);
        tituloDisciplina = findViewById(R.id.tituloDisciplina);

        dialogLoading.showDialog(this);

        SharedPreferences sharedPref = getSharedPreferences("chaves", MODE_PRIVATE);
        usuario = sharedPref.getString("usuario", aluno);
        disciplinaTime = sharedPref.getString("disciplina", "");

        tituloDisciplina.setText("Aulas - " + nomeDisciplina);

        imgVoltar.setOnClickListener(view -> {
            finish();
        });
        imgConfig.setOnClickListener(view -> {
            Intent i = new Intent(AulasScreen.this, ConfiguracoesScreen.class);
            startActivity(i);
        });
        if (usuario.equals(aluno)) {
            cardProfessor.setVisibility(View.GONE);

            // PROCURAR A AULA
            db.collection("Aula")
                    .whereEqualTo("disciplina", disciplinaTime)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful() && task.getResult().size() > 0) {
                            nomeAulaParaChanel = task.getResult().getDocuments().get(0).get("nomeAula").toString();
                            descricaoAula.setText(nomeAulaParaChanel);
                            cardAluno.setVisibility(View.VISIBLE);
                        } else if (task.getResult().size() == 0) {
                            cardAluno.setVisibility(View.GONE);
                            Log.i("TESTEXX", "A aula não foi encontrada");

                        } else {
                            cardAluno.setVisibility(View.GONE);
                            Log.i("TESTEXX", "ERRO: " + task.getException());
                        }
                    });


        } else {
            cardAluno.setVisibility(View.GONE);
            cardProfessor.setVisibility(View.VISIBLE);
        }


        iniciarAula.setOnClickListener(view -> {
            DialogAula alert = new DialogAula();
            alert.showDialog(this);
        });

        btnEntrarAula.setOnClickListener(view -> {

            int MY_PERMISSIONS_REQUEST_CAMERA = 0;
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO}, MY_PERMISSIONS_REQUEST_CAMERA);
            } else {
                entrarNaAula();
            }
        });

        listarAulas();
    }

    public void entrarNaAula() {
        Intent intent = new Intent(this, VideoActivity.class);
        intent.putExtra("nomeDaAula", nomeAulaParaChanel);
        startActivity(intent);
    }


    public void listarAulas() {

        StorageReference listRef = storage.getReference().child(disciplinaTime);

        listRef.listAll()
                .addOnSuccessListener(listResult -> {
                    if (listResult.getPrefixes().size() > 0) {
                        for (StorageReference prefix : listResult.getPrefixes()) {
                            Log.d("testexx", prefix.getName());
                            Aula aula = new Aula();
                            aula.setDescricao(prefix.getName());
                            aula.setAudio(listRef.getName() + "/" + prefix.getName() + "/" + "audio");
                            aula.setTexto(listRef.getName() + "/" + prefix.getName() + "/" + "texto");
                            list.add(aula);
                        }

                        listaAulas.setAdapter(new AdapterAulas(this, list, usuario));
                        RecyclerView.LayoutManager layout = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

                        listaAulas.setLayoutManager(layout);
                        dialogLoading.fecharLoading();

                    }else{
                        Log.d("testexx", "não possui aulas");
                        dialogLoading.fecharLoading();
                    }

                })
                .addOnFailureListener(e -> {
                    Log.d("testexx", e.getMessage());
                    dialogLoading.fecharLoading();
                });



    }
}