package com.jisellemartins.escolaparatodos;

import static com.jisellemartins.escolaparatodos.Utils.UtilAutenticacao.aluno;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.jisellemartins.escolaparatodos.adapter.AdapterConteudo;
import com.jisellemartins.escolaparatodos.adapter.AdapterEvento;
import com.jisellemartins.escolaparatodos.dialogs.DialogAdicionarEvento;
import com.jisellemartins.escolaparatodos.dialogs.DialogAula;
import com.jisellemartins.escolaparatodos.model.Conteudo;
import com.jisellemartins.escolaparatodos.model.Evento;

import java.util.ArrayList;

public class CalendarioScreen extends AppCompatActivity {

    RecyclerView listaEventos;
    Button btnAdcEvento;


    String usuario = aluno;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendario);

        listaEventos = findViewById(R.id.listaEventos);
        btnAdcEvento = findViewById(R.id.btnAdcEvento);

        SharedPreferences sharedPref = getSharedPreferences("chaves", MODE_PRIVATE);
        usuario = sharedPref.getString("usuario", aluno);

        if (usuario.equals(aluno)){
            btnAdcEvento.setVisibility(View.GONE);
        }else{
            btnAdcEvento.setVisibility(View.VISIBLE);
        }

        btnAdcEvento.setOnClickListener(view -> {
            DialogAdicionarEvento alert = new DialogAdicionarEvento();
            alert.showDialog(this);
        });

        ArrayList<Evento> list = new ArrayList<>();

        Evento evento = new Evento();
        evento.setData("09/06");
        evento.setDescricao("Aula extra de revisão");

        Evento evento2 = new Evento();
        evento2.setData("09/06");
        evento2.setDescricao("Aula extra de revisão");

        list.add(evento);
        list.add(evento2);

        listaEventos.setAdapter(new AdapterEvento(this, list));
        RecyclerView.LayoutManager layout = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        listaEventos.setLayoutManager(layout);
    }
}