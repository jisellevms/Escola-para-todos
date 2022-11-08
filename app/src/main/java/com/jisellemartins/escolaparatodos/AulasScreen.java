package com.jisellemartins.escolaparatodos;

import static com.jisellemartins.escolaparatodos.Utils.UtilAutenticacao.aluno;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.jisellemartins.escolaparatodos.adapter.AdapterAulas;
import com.jisellemartins.escolaparatodos.dialogs.DialogAula;
import com.jisellemartins.escolaparatodos.model.Aula;

import java.util.ArrayList;

public class AulasScreen extends AppCompatActivity {
    RecyclerView listaAulas;
    Button iniciarAula;
    LinearLayoutCompat cardAluno, cardProfessor;

    String usuario = aluno;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aulas_screen);

        listaAulas = findViewById(R.id.listaAulas);
        iniciarAula = findViewById(R.id.iniciarAula);
        cardAluno = findViewById(R.id.cardAluno);
        cardProfessor = findViewById(R.id.cardProfessor);

        SharedPreferences sharedPref = getSharedPreferences("chaves", MODE_PRIVATE);
        usuario = sharedPref.getString("usuario", aluno);

        if (usuario.equals(aluno)){
            cardAluno.setVisibility(View.VISIBLE);
            cardProfessor.setVisibility(View.GONE);
        }else{
            cardAluno.setVisibility(View.GONE);
            cardProfessor.setVisibility(View.VISIBLE);
        }



        iniciarAula.setOnClickListener(view -> {
            DialogAula alert = new DialogAula();
            alert.showDialog(this);
        });

        ArrayList<Aula> list = new ArrayList<>();
        Aula aula = new Aula();
        Aula aula2 = new Aula();
        Aula aula3 = new Aula();

        aula.setDescricao("Aula: Matriz");
        aula2.setDescricao("Aula: Geometria");
        aula3.setDescricao("Aula: Áreas");

        list.add(aula);
        list.add(aula2);
        list.add(aula3);


        listaAulas.setAdapter(new AdapterAulas(this, list, usuario));
        RecyclerView.LayoutManager layout = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        listaAulas.setLayoutManager(layout);
    }
}