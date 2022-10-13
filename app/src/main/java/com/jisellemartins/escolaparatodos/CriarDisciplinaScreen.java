package com.jisellemartins.escolaparatodos;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.jisellemartins.escolaparatodos.adapter.AdapterAtividades;
import com.jisellemartins.escolaparatodos.adapter.AdapterNumeros;
import com.jisellemartins.escolaparatodos.model.Atividade;
import com.jisellemartins.escolaparatodos.model.Numero;

import java.util.ArrayList;

public class CriarDisciplinaScreen extends AppCompatActivity {

    RecyclerView listaNumeros;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_criar_disciplina_screen);

        listaNumeros = findViewById(R.id.listaNumeros);


        ArrayList<Numero> list = new ArrayList<>();

        Numero numero = new Numero();
        numero.setNumero("(85) 986373679");

        Numero numero2 = new Numero();
        numero2.setNumero("(85) 986373679");

        Numero numero3 = new Numero();
        numero3.setNumero("(85) 986373679");



        list.add(numero);
        list.add(numero2);
        list.add(numero3);



        listaNumeros.setAdapter(new AdapterNumeros(this, list));
        RecyclerView.LayoutManager layout = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        listaNumeros.setLayoutManager(layout);

    }
}