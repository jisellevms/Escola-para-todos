package com.jisellemartins.escolaparatodos;

import static com.jisellemartins.escolaparatodos.Utils.UtilAutenticacao.aluno;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;

public class DisciplineScreen extends AppCompatActivity {
    Button btnLibrary, btnTasks, btnCalendar, btnLessons, btnReportCard, btnStudents;

    String usuario = aluno;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disciplina_screen);

        btnLibrary = findViewById(R.id.btnLibrary);
        btnTasks = findViewById(R.id.btnTasks);
        btnCalendar = findViewById(R.id.btnAdcAluno);
        btnLessons = findViewById(R.id.btnLessons);
        btnReportCard = findViewById(R.id.btnReportCard);
        btnStudents = findViewById(R.id.btnStudents);

        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        usuario = sharedPref.getString("usuario", aluno);




        btnLibrary.setOnClickListener(view -> {
            Intent i = new Intent(DisciplineScreen.this,
                    BibliotecaScreen.class);
            startActivity(i);
        });
        btnTasks.setOnClickListener(view -> {
            Intent i = new Intent(DisciplineScreen.this,
                    AtividadesScreen.class);
            startActivity(i);
        });
        btnCalendar.setOnClickListener(view -> {
            Intent i = new Intent(DisciplineScreen.this,
                    CalendarioScreen.class);
            startActivity(i);
        });
        btnLessons.setOnClickListener(view -> {
            Intent i = new Intent(DisciplineScreen.this,
                    AulasScreen.class);
            startActivity(i);
        });
        btnReportCard.setOnClickListener(view -> {
            Intent i = new Intent(DisciplineScreen.this,
                    BoletimScreen.class);
            startActivity(i);
        });
        btnStudents.setOnClickListener(view -> {
            Intent i = new Intent(DisciplineScreen.this,
                    GerenciarAlunosScreen.class);
            startActivity(i);
        });
    }
}