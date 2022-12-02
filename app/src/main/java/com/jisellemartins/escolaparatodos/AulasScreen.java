package com.jisellemartins.escolaparatodos;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.os.Build.VERSION.SDK_INT;
import static com.jisellemartins.escolaparatodos.Utils.Utils.TAG_EPT;
import static com.jisellemartins.escolaparatodos.Utils.Utils.aluno;
import static com.jisellemartins.escolaparatodos.Utils.Utils.entreiComoAluno;
import static com.jisellemartins.escolaparatodos.Utils.Utils.nomeDisciplina;

import androidx.annotation.Nullable;
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
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.jisellemartins.escolaparatodos.adapter.AdapterAulas;
import com.jisellemartins.escolaparatodos.dialogs.DialogAula;
import com.jisellemartins.escolaparatodos.dialogs.DialogLoading;
import com.jisellemartins.escolaparatodos.model.Aula;

import java.util.ArrayList;

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
                            Log.i(TAG_EPT, "A aula não foi encontrada");

                        } else {
                            cardAluno.setVisibility(View.GONE);
                            Log.i(TAG_EPT, "ERRO: " + task.getException());
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


        if (!usuario.equals("aluno")) {
            if (SDK_INT >= Build.VERSION_CODES.R) {
                if (!Environment.isExternalStorageManager()) {
                    requestPermission();
                }
            }
        }


    }


    private void requestPermission() {
        if (SDK_INT >= Build.VERSION_CODES.R) {
            try {
                Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                intent.addCategory("android.intent.category.DEFAULT");
                intent.setData(Uri.parse(String.format("package:%s", getApplicationContext().getPackageName())));
                startActivityForResult(intent, 2296);
            } catch (Exception e) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                startActivityForResult(intent, 2296);
            }
        } else {
            //below android 11
            ActivityCompat.requestPermissions(AulasScreen.this, new String[]{WRITE_EXTERNAL_STORAGE}, 2296);
        }
    }

    public void entrarNaAula() {
        Intent intent = new Intent(this, VideoActivity.class);
        intent.putExtra("nomeDaAula", nomeAulaParaChanel);
        startActivity(intent);
    }


    public void listarAulas() {
        list.clear();
        StorageReference listRef = storage.getReference().child(disciplinaTime);

        listRef.listAll()
                .addOnSuccessListener(listResult -> {
                    if (listResult.getPrefixes().size() > 0) {
                        for (StorageReference prefix : listResult.getPrefixes()) {
                            Log.d(TAG_EPT, prefix.getName());
                            Aula aula = new Aula();
                            aula.setDescricao(prefix.getName());
                            aula.setAudio(listRef.getName() + "/" + prefix.getName() + "/" + "audio");
                            aula.setTexto(listRef.getName() + "/" + prefix.getName() + "/" + "texto");
                            list.add(aula);
                        }

                        listaAulas.setAdapter(new AdapterAulas(this, list, usuario, this));
                        RecyclerView.LayoutManager layout = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

                        listaAulas.setLayoutManager(layout);
                        dialogLoading.fecharLoading();

                    } else {
                        Log.d(TAG_EPT, "não possui aulas");
                        dialogLoading.fecharLoading();
                    }

                })
                .addOnFailureListener(e -> {
                    Log.d(TAG_EPT, e.getMessage());
                    dialogLoading.fecharLoading();
                });


    }

    @Override
    protected void onResume() {
        super.onResume();
        new Handler().postDelayed(() -> listarAulas(), 3000);

    }
}