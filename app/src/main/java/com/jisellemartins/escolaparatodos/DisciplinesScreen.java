package com.jisellemartins.escolaparatodos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.jisellemartins.escolaparatodos.adapter.AdapterDisciplinas;
import com.jisellemartins.escolaparatodos.model.Disciplina;

import java.util.ArrayList;

public class DisciplinesScreen extends AppCompatActivity {
    RecyclerView listaDisciplinas;
    Button btnCriarDisciplina;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disciplinas_screen);
        listaDisciplinas = findViewById(R.id.listaDisciplinas);
        btnCriarDisciplina = findViewById(R.id.btnAdcAluno);

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


        listaDisciplinas.setAdapter(new AdapterDisciplinas(this, list));
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