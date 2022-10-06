package com.jisellemartins.escolaparatodos;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jisellemartins.escolaparatodos.adapter.AdapterConteudo;
import com.jisellemartins.escolaparatodos.model.Conteudo;

import java.util.ArrayList;

public class LibraryScreen extends AppCompatActivity {
    RecyclerView listaConteudos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library_screen);
        listaConteudos = findViewById(R.id.listConteudos);

        ArrayList<Conteudo> list = new ArrayList<>();
        Conteudo conteudo = new Conteudo();
        conteudo.setDescricao("matriz.pdf");
        conteudo.setTamanho("200KB");
        conteudo.setIcon(R.drawable.documento_texto);


        Conteudo conteudo2 = new Conteudo();
        conteudo2.setDescricao("matriz.pdf");
        conteudo2.setTamanho("200KB");
        conteudo2.setIcon(R.drawable.imagem);


        Conteudo conteudo3 = new Conteudo();
        conteudo3.setDescricao("matriz.pdf");
        conteudo3.setTamanho("200KB");
        conteudo3.setIcon(R.drawable.documento_texto);

        list.add(conteudo);
        list.add(conteudo2);
        list.add(conteudo3);



        listaConteudos.setAdapter(new AdapterConteudo(this, list));
        RecyclerView.LayoutManager layout = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        listaConteudos.setLayoutManager(layout);
    }
}