package com.jisellemartins.escolaparatodos.dialogs;

import static android.content.Context.MODE_PRIVATE;
import static com.jisellemartins.escolaparatodos.CadastroScreen.getRandomNonRepeatingIntegers;
import static com.jisellemartins.escolaparatodos.Utils.UtilAutenticacao.aluno;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.jisellemartins.escolaparatodos.DisciplineScreen;
import com.jisellemartins.escolaparatodos.DisciplinesScreen;
import com.jisellemartins.escolaparatodos.R;
import com.jisellemartins.escolaparatodos.model.Aluno;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DialogCriarDisciplina {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String numeroProf;


    public void showDialog(Activity activity){
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_criar_disciplina);

        //Button dialogButton = (Button) dialog.findViewById(R.id.btnIniciarAula);
        ImageView cancelar = dialog.findViewById(R.id.cancelar);
        Button btnCriarDisciplina = dialog.findViewById(R.id.btnCriarDisciplina);
        EditText nomeDisciplina = dialog.findViewById(R.id.nomeDisciplina);

        SharedPreferences sharedPref = activity.getSharedPreferences("chaves", MODE_PRIVATE);
        numeroProf = sharedPref.getString("numero", "");


        btnCriarDisciplina.setOnClickListener(view -> {
            Long timeInLong = System.currentTimeMillis()/1000;
            String timestamp = timeInLong.toString();

            /*Gson gson = new Gson();
            ArrayList<Aluno> list = new ArrayList<>();
            Aluno aluno = new Aluno();
            Aluno aluno2 = new Aluno();
            Aluno aluno3 = new Aluno();

            aluno.setNome("ANA CLARA MEDEIROS 1");
            aluno2.setNome("ANA CLARA MEDEIROS 2");
            aluno3.setNome("ANA CLARA MEDEIROS 3");

            aluno.setTelefone("85986373679");
            aluno2.setTelefone("85986373678");
            aluno3.setTelefone("85986373677");

            list.add(aluno);
            list.add(aluno2);
            list.add(aluno3);


            String json = gson.toJson(list);*/

            Map<String, Object> disciplina = new HashMap<>();
            disciplina.put("nome", nomeDisciplina.getText().toString());
            disciplina.put("dataCriacao", timestamp);
            disciplina.put("numeroProf", numeroProf);
            //disciplina.put("alunos",json);
            disciplina.put("alunos", "{}");

            db.collection("Disciplina").document(getRandomNonRepeatingIntegers(6,0,1000).toString())
                    .set(disciplina)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(activity, "Disciplina cadastrada com sucesso!", Toast.LENGTH_LONG).show();
                        Intent i = new Intent(activity, DisciplineScreen.class);
                        activity.startActivity(i);

                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(activity, "Erro: " + e, Toast.LENGTH_LONG).show();
                        Log.d("TESTEXX", "Erro:" + e);

                    });
        });
        cancelar.setOnClickListener(v -> dialog.dismiss());

        dialog.show();

    }
}