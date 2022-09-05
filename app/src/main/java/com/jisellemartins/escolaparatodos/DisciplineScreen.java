package com.jisellemartins.escolaparatodos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class DisciplineScreen extends AppCompatActivity {
    Button btnLibrary, btnTasks, btnCalendar, btnLessons, btnReportCard, btnStudents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discipline_screen);

        btnLibrary = findViewById(R.id.btnLibrary);
        btnTasks = findViewById(R.id.btnTasks);
        btnCalendar = findViewById(R.id.btnCalendar);
        btnLessons = findViewById(R.id.btnLessons);
        btnReportCard = findViewById(R.id.btnReportCard);
        btnStudents = findViewById(R.id.btnStudents);

        btnLibrary.setOnClickListener(view -> {
            Intent i = new Intent(DisciplineScreen.this,
                    LibraryScreen.class);
            startActivity(i);
        });
        btnTasks.setOnClickListener(view -> {
            /*Intent i = new Intent(DisciplineScreen.this,
                    LibraryScreen.class);
            startActivity(i);*/
        });
        btnCalendar.setOnClickListener(view -> {
            /*Intent i = new Intent(DisciplineScreen.this,
                    LibraryScreen.class);
            startActivity(i);*/
        });
        btnLessons.setOnClickListener(view -> {
            Intent i = new Intent(DisciplineScreen.this,
                    LessonScreen.class);
            startActivity(i);
        });
        btnReportCard.setOnClickListener(view -> {
            Intent i = new Intent(DisciplineScreen.this,
                    ReportCardScreen.class);
            startActivity(i);
        });
        btnStudents.setOnClickListener(view -> {
            /*Intent i = new Intent(DisciplineScreen.this,
                    LibraryScreen.class);
            startActivity(i);*/
        });
    }
}