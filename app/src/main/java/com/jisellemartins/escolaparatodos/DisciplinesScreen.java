package com.jisellemartins.escolaparatodos;

import static com.jisellemartins.escolaparatodos.Utils.UtilAutenticacao.aluno;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.jisellemartins.escolaparatodos.adapter.AdapterDisciplinas;
import com.jisellemartins.escolaparatodos.model.Disciplina;

import java.util.ArrayList;

public class DisciplinesScreen extends AppCompatActivity {
    RecyclerView listaDisciplinas;
    Button btnCriarDisciplina;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    String usuario = aluno;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disciplinas_screen);
        listaDisciplinas = findViewById(R.id.listaDisciplinas);
        btnCriarDisciplina = findViewById(R.id.btnAdcAluno);

        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        usuario = sharedPref.getString("usuario", aluno);

        if (usuario.equals(aluno)){
            btnCriarDisciplina.setVisibility(View.GONE);
        }else{
            btnCriarDisciplina.setVisibility(View.VISIBLE);
        }

        ArrayList<Disciplina> list = new ArrayList<>();
        Disciplina disciplina = new Disciplina();
        Disciplina disciplina2 = new Disciplina();
        Disciplina disciplina3 = new Disciplina();
        disciplina.setNomeDisciplina("Matemática");
        disciplina2.setNomeDisciplina("Português");
        disciplina3.setNomeDisciplina("Geografia");

        list.add(disciplina);
        list.add(disciplina2);
        list.add(disciplina3);


        listaDisciplinas.setAdapter(new AdapterDisciplinas(this, list, usuario));
        RecyclerView.LayoutManager layout = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        listaDisciplinas.setLayoutManager(layout);

        btnCriarDisciplina.setOnClickListener(view -> {

            // LER SUBCOLEÇÕES

            db.collectionGroup("Aluno").get()
                    .addOnCompleteListener(queryDocumentSnapshots -> {
                        if (queryDocumentSnapshots.isSuccessful()) {
                            for (QueryDocumentSnapshot document : queryDocumentSnapshots.getResult()) {
                                Log.d("TESTEXX", document.getId() + " => " + document.getData());
                            }
                        } else {
                            Log.d("TESTEXX", "Error getting documents: ", queryDocumentSnapshots.getException());
                        }
                    });

            // LER COLEÇÕES PAI

            /*db.collection("Professor")
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("TESTEXX", document.getId() + " => " + document.getData());
                            }
                        } else {
                            Log.d("TESTEXX", "Error getting documents: ", task.getException());
                        }
                    });*/

            /*Intent i = new Intent(DisciplinesScreen.this,
                    CriarDisciplinaScreen.class);
            startActivity(i);*/
        });

    }
}