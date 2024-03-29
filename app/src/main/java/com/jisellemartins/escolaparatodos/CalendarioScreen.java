package com.jisellemartins.escolaparatodos;

import static com.jisellemartins.escolaparatodos.Utils.Utils.aluno;
import static com.jisellemartins.escolaparatodos.Utils.Utils.nomeDisciplina;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.jisellemartins.escolaparatodos.adapter.AdapterEvento;
import com.jisellemartins.escolaparatodos.dialogs.DialogAdicionarEvento;
import com.jisellemartins.escolaparatodos.model.Evento;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class CalendarioScreen extends AppCompatActivity {

    ImageView imgVoltar, imgConfig;

    RecyclerView listaEventos;
    Button btnAdcEvento, tituloDisciplina;
    CalendarView calendar;
    String disciplinaTime = "";


    String usuario = aluno;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String dataSelecionada = "";
    ArrayList<Evento> list = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendario);
        imgVoltar = findViewById(R.id.imgVoltar);
        imgConfig = findViewById(R.id.imgConfig);
        listaEventos = findViewById(R.id.listaEventos);
        btnAdcEvento = findViewById(R.id.btnAdcEvento);
        calendar = findViewById(R.id.calendar);
        tituloDisciplina = findViewById(R.id.tituloDisciplina);

        SharedPreferences sharedPref = getSharedPreferences("chaves", MODE_PRIVATE);
        usuario = sharedPref.getString("usuario", aluno);
        disciplinaTime = sharedPref.getString("disciplina", "");

        tituloDisciplina.setText("Calendário - " + nomeDisciplina);


        if (usuario.equals(aluno)) {
            btnAdcEvento.setVisibility(View.GONE);
        } else {
            btnAdcEvento.setVisibility(View.VISIBLE);
        }

        imgVoltar.setOnClickListener(view -> {
            finish();
        });
        imgConfig.setOnClickListener(view -> {
            Intent i = new Intent(CalendarioScreen.this, ConfiguracoesScreen.class);
            startActivity(i);
        });






        calendar.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            list.clear();
            dataSelecionada = year + "/" + (month + 1) + "/" + dayOfMonth;
            SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
            try {
                Date date = format.parse(dataSelecionada);
                procurarEventos(date.getTime());

            } catch (ParseException e) {
                e.printStackTrace();
            }
        });

        btnAdcEvento.setOnClickListener(view -> {
            DialogAdicionarEvento alert = new DialogAdicionarEvento();
            alert.showDialog(this, this);
        });

    }
    private String getDate(long time) {
        Calendar cal = Calendar.getInstance(Locale.getDefault());
        cal.setTimeInMillis(time);
        String date = DateFormat.format("yyyy/MM/dd", cal).toString();
        return date;
    }

    public void procurarEventos(Long date) {
        db.collection("Evento")
                .whereEqualTo("disciplina", disciplinaTime)
                .whereEqualTo("dateEvento", String.valueOf(date))
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        verificaEventoExiste(task);
                    } else {
                        Toast.makeText(this, "Erro: " + task.getException(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    public void verificaEventoExiste(Task<QuerySnapshot> task) {
        if (task.getResult().size() == 0) {
            listaEventos.setAdapter(new AdapterEvento(this, list));
            RecyclerView.LayoutManager layout = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            listaEventos.setLayoutManager(layout);
            Toast.makeText(this, "Não existe evento para a data selecionada", Toast.LENGTH_SHORT).show();
        } else {
            for (QueryDocumentSnapshot document : task.getResult()) {
                Evento evento = new Evento();
                evento.setData(document.get("dateEvento").toString());
                evento.setDescricao(document.get("descricao").toString());
                list.add(evento);
            }

            listaEventos.setAdapter(new AdapterEvento(this, list));
            RecyclerView.LayoutManager layout = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            listaEventos.setLayoutManager(layout);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        buscarEventosHoje();
    }

    public void buscarEventosHoje(){
        list.clear();
        String eventosHoje = getDate(calendar.getDate());
        SimpleDateFormat formatDate = new SimpleDateFormat("yyyy/MM/dd");
        Date dateHoje = null;
        try {
            dateHoje = formatDate.parse(eventosHoje);
            procurarEventos(dateHoje.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}

