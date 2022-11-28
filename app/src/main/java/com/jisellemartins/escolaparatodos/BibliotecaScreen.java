package com.jisellemartins.escolaparatodos;

import static com.jisellemartins.escolaparatodos.Utils.Utils.aluno;
import static com.jisellemartins.escolaparatodos.Utils.Utils.nomeDisciplina;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.jisellemartins.escolaparatodos.adapter.AdapterConteudo;
import com.jisellemartins.escolaparatodos.dialogs.DialogLoading;
import com.jisellemartins.escolaparatodos.dialogs.DialogNomeArquivo;
import com.jisellemartins.escolaparatodos.model.Conteudo;

import java.util.ArrayList;

public class BibliotecaScreen extends AppCompatActivity {
    ImageView imgVoltar, imgConfig;

    RecyclerView listaConteudos;
    Button btnAdcArquivo, tituloDisciplina;

    String usuario = aluno;
    String disciplinaTime;


    StorageReference storageRef;

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseStorage storage = FirebaseStorage.getInstance();


    Uri imageUri;
    DialogNomeArquivo dialogNomeArquivo = new DialogNomeArquivo();
    ArrayList<Conteudo> list = new ArrayList<>();

    DialogLoading loading = new DialogLoading();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_biblioteca_screen);
        imgVoltar = findViewById(R.id.imgVoltar);
        imgConfig = findViewById(R.id.imgConfig);
        listaConteudos = findViewById(R.id.listConteudos);
        btnAdcArquivo = findViewById(R.id.btnAdcArquivo);
        tituloDisciplina = findViewById(R.id.tituloDisciplina);

        tituloDisciplina.setText("Biblioteca - " + nomeDisciplina);

        loading.showDialog(this);
        signInAnonymously();

        SharedPreferences sharedPref = getSharedPreferences("chaves", MODE_PRIVATE);
        usuario = sharedPref.getString("usuario", aluno);
        disciplinaTime = sharedPref.getString("disciplina", "");



        if (usuario.equals(aluno)) {
            btnAdcArquivo.setVisibility(View.GONE);
        } else {
            btnAdcArquivo.setVisibility(View.VISIBLE);
        }

        imgVoltar.setOnClickListener(view -> {
            finish();
        });
        imgConfig.setOnClickListener(view -> {
            Intent i = new Intent(BibliotecaScreen.this, ConfiguracoesScreen.class);
            startActivity(i);
        });

        StorageReference listRef = storage.getReference().child(disciplinaTime);


        listRef.listAll()
                .addOnSuccessListener(listResult -> {
                    for (StorageReference item : listResult.getItems()) {
                        item.getMetadata().addOnSuccessListener(storageMetadata -> {
                            Conteudo conteudo = new Conteudo();
                            String type = storageMetadata.getContentType().substring(storageMetadata.getContentType().lastIndexOf("/"));
                            conteudo.setDescricao(storageMetadata.getName() + type.replace("/","."));
                            conteudo.setTamanho(bytesIntoHumanReadable(storageMetadata.getSizeBytes()));
                            if (storageMetadata.getContentType().contains("image")){
                                conteudo.setIcon(R.drawable.imagem);
                            }else{
                                conteudo.setIcon(R.drawable.documento_texto);
                            }
                            list.add(conteudo);
                            if (listResult.getItems().size() == list.size()){
                                showList();
                            }

                        }).addOnFailureListener(e -> {
                            loading.fecharLoading();
                            Log.d("TESTEXX", e.toString());
                        });
                    }
                    if (listResult.getItems().size() == 0){
                        loading.fecharLoading();
                    }


                })
                .addOnFailureListener(e -> {
                    loading.fecharLoading();
                    Log.d("TESTEXX", e.toString());

                });

        btnAdcArquivo.setOnClickListener(view -> {
            dialogNomeArquivo.showDialog(this);

        });


    }

    private void showList(){
        loading.fecharLoading();
        listaConteudos.setAdapter(new AdapterConteudo(this, list, usuario, storage));
        RecyclerView.LayoutManager layout = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        listaConteudos.setLayoutManager(layout);
    }
    private void signInAnonymously() {
        mAuth.signInAnonymously().addOnSuccessListener(this, authResult -> {
            Log.d("TESTEXX", "LOGOU ANONIMO");
        }).addOnFailureListener(this, exception -> Log.e("TESTEXX", "signInAnonymously:FAILURE", exception));
    }

    private void uploadImage(){
        DialogLoading alert = new DialogLoading();
        alert.showDialog(this);
        storageRef = FirebaseStorage.getInstance().getReference(disciplinaTime+"/" + dialogNomeArquivo.nameArquivo);
        storageRef.putFile(imageUri).addOnSuccessListener(taskSnapshot -> {
            alert.fecharLoading();
            Toast.makeText(BibliotecaScreen.this, "Arquivo adicionado com sucesso!", Toast.LENGTH_LONG).show();

        }).addOnFailureListener(e -> {
            alert.fecharLoading();
            Toast.makeText(BibliotecaScreen.this, "Erro: " + e, Toast.LENGTH_LONG).show();
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100 && data != null && data.getData() != null){
            imageUri = data.getData();
            uploadImage();

        }
    }
    public static String bytesIntoHumanReadable(long bytes) {
        long kilobyte = 1024;
        long megabyte = kilobyte * 1024;
        long gigabyte = megabyte * 1024;
        long terabyte = gigabyte * 1024;

        if ((bytes >= 0) && (bytes < kilobyte)) {
            return bytes + " B";

        } else if ((bytes >= kilobyte) && (bytes < megabyte)) {
            return (bytes / kilobyte) + " KB";

        } else if ((bytes >= megabyte) && (bytes < gigabyte)) {
            return (bytes / megabyte) + " MB";

        } else if ((bytes >= gigabyte) && (bytes < terabyte)) {
            return (bytes / gigabyte) + " GB";

        } else if (bytes >= terabyte) {
            return (bytes / terabyte) + " TB";

        } else {
            return bytes + " Bytes";
        }
    }
}