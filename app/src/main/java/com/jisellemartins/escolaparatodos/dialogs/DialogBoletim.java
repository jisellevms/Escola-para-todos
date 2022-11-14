package com.jisellemartins.escolaparatodos.dialogs;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.gson.reflect.TypeToken;
import com.jisellemartins.escolaparatodos.R;
import com.jisellemartins.escolaparatodos.adapter.AdapterBoletim;
import com.jisellemartins.escolaparatodos.adapter.AdapterBoletimProfessor;
import com.jisellemartins.escolaparatodos.model.Aluno;
import com.jisellemartins.escolaparatodos.model.Boletim;

import java.util.ArrayList;
import java.util.List;

public class DialogBoletim {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ArrayList<Boletim> list = new ArrayList<>();

    public void showDialog(Activity activity, String telefoneAluno, String nomeAluno){
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_boletim);

        //Button dialogButton = (Button) dialog.findViewById(R.id.btnIniciarAula);
        ImageView cancelar = (ImageView) dialog.findViewById(R.id.cancelar);
        TextView aluno = (TextView) dialog.findViewById(R.id.aluno);
        TextView numero = (TextView) dialog.findViewById(R.id.numero);
        RecyclerView listaBoletimDialog = (RecyclerView) dialog.findViewById(R.id.listaBoletimDialog);

        aluno.setText(nomeAluno);
        numero.setText(telefoneAluno);

        SharedPreferences sharedPref = activity.getSharedPreferences("chaves", MODE_PRIVATE);
        String disciplinaTime = sharedPref.getString("disciplina", "");

        CollectionReference complaintsRef = db.collection("Boletim");
        complaintsRef.whereEqualTo("disciplina", disciplinaTime).whereEqualTo("aluno", telefoneAluno).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                if (task.getResult().size() > 0){
                    for (QueryDocumentSnapshot document : task.getResult()) {

                        Boletim boletim = new Boletim();
                        boletim.setDescricao(document.get("descricao").toString());
                        boletim.setAcertos(Integer.parseInt(document.get("acertos").toString()));
                        boletim.setTotal(Integer.parseInt(document.get("totalQuestao").toString()));
                        list.add(boletim);
                    }

                    listaBoletimDialog.setAdapter(new AdapterBoletim(activity, list));
                    RecyclerView.LayoutManager layout = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);

                    listaBoletimDialog.setLayoutManager(layout);
                }else{
                    Toast.makeText(activity, "Não existe boletim para o aluno selecionado", Toast.LENGTH_LONG).show();
                }

            }else{
                Toast.makeText(activity, "Erro: " + task.getException(), Toast.LENGTH_LONG).show();
            }
        });




        cancelar.setOnClickListener(v -> dialog.dismiss());

        dialog.show();

    }
}
