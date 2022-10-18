package com.jisellemartins.escolaparatodos.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jisellemartins.escolaparatodos.R;
import com.jisellemartins.escolaparatodos.adapter.AdapterBoletim;
import com.jisellemartins.escolaparatodos.model.Boletim;

import java.util.ArrayList;

public class DialogBoletim {
    public void showDialog(Activity activity){
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_boletim);

        //Button dialogButton = (Button) dialog.findViewById(R.id.btnIniciarAula);
        ImageView cancelar = (ImageView) dialog.findViewById(R.id.cancelar);
        TextView aluno = (TextView) dialog.findViewById(R.id.aluno);
        TextView numero = (TextView) dialog.findViewById(R.id.numero);
        RecyclerView listaBoletimDialog = (RecyclerView) dialog.findViewById(R.id.listaBoletimDialog);


        ArrayList<Boletim> list = new ArrayList<>();
        Boletim boletim = new Boletim();
        Boletim boletim2 = new Boletim();
        Boletim boletim3 = new Boletim();

        boletim.setDescricao("Prova Matriz");
        boletim.setAcertos(7);
        boletim.setTotal(10);

        boletim2.setDescricao("Atividade Vetores");
        boletim2.setAcertos(5);
        boletim2.setTotal(10);

        boletim3.setDescricao("Atividade Matemática");
        boletim3.setAcertos(10);
        boletim3.setTotal(10);

        list.add(boletim);
        list.add(boletim2);
        list.add(boletim3);


        listaBoletimDialog.setAdapter(new AdapterBoletim(activity, list));
        RecyclerView.LayoutManager layout = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);

        listaBoletimDialog.setLayoutManager(layout);


        cancelar.setOnClickListener(v -> dialog.dismiss());

        dialog.show();

    }
}
