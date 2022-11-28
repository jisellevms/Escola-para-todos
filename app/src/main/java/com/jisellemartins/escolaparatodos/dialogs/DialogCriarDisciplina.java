package com.jisellemartins.escolaparatodos.dialogs;

import static android.content.Context.MODE_PRIVATE;
import static com.jisellemartins.escolaparatodos.CadastroScreen.getRandomNonRepeatingIntegers;

import android.app.Activity;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;
import com.jisellemartins.escolaparatodos.R;

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

            Map<String, Object> disciplina = new HashMap<>();
            disciplina.put("nome", nomeDisciplina.getText().toString());
            disciplina.put("dataCriacao", timestamp);
            disciplina.put("numeroProf", numeroProf);
            disciplina.put("alunos", "{}");

            db.collection("Disciplina").document(getRandomNonRepeatingIntegers(6,0,1000).toString())
                    .set(disciplina)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(activity, "Disciplina cadastrada com sucesso!", Toast.LENGTH_LONG).show();
                        dialog.dismiss();
                        activity.startActivityForResult(activity.getIntent(), 10);

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
