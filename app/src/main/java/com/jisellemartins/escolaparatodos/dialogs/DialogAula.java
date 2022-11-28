package com.jisellemartins.escolaparatodos.dialogs;

import static android.content.Context.MODE_PRIVATE;
import static com.jisellemartins.escolaparatodos.CadastroScreen.getRandomNonRepeatingIntegers;
import static com.jisellemartins.escolaparatodos.Utils.Utils.entreiComoAluno;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.util.Log;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.firestore.FirebaseFirestore;
import com.jisellemartins.escolaparatodos.R;
import com.jisellemartins.escolaparatodos.VideoActivity;

import java.util.HashMap;
import java.util.Map;

public class DialogAula {

    EditText descAula;
    Activity activity;
    String disciplinaTime, numeroUsuario;
    Dialog dialog;

    FirebaseFirestore db = FirebaseFirestore.getInstance();


    public void showDialog(Activity activity) {
        dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_aula);
        this.activity = activity;

        ImageView cancelar = dialog.findViewById(R.id.cancelar);
        Button btnIniciarAula = dialog.findViewById(R.id.btnIniciarAula);
        descAula = dialog.findViewById(R.id.descAula);
        cancelar.setOnClickListener(v -> dialog.dismiss());

        btnIniciarAula.setOnClickListener(view -> {
            int MY_PERMISSIONS_REQUEST_CAMERA = 0;
            if (ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(activity, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO}, MY_PERMISSIONS_REQUEST_CAMERA);
            } else {
                iniciarAula();
            }
            entreiComoAluno = false;

        });

        dialog.show();

    }

    public void iniciarAula() {
        SharedPreferences sharedPref = activity.getSharedPreferences("chaves", MODE_PRIVATE);
        disciplinaTime = sharedPref.getString("disciplina", "");
        numeroUsuario = sharedPref.getString("numero", "");

        if (!descAula.getText().toString().isEmpty()) {


            Map<String, Object> aula = new HashMap<>();
            aula.put("aulaAoVivo", true);
            aula.put("disciplina", disciplinaTime);
            aula.put("nomeAula", descAula.getText().toString());
            aula.put("professor", numeroUsuario);

            db.collection("Aula").document(getRandomNonRepeatingIntegers(6, 0, 1000).toString())
                    .set(aula)
                    .addOnSuccessListener(aVoid -> {
                        Log.i("TESTEXX", "Aula cadastrada com sucesso.");
                        dialog.dismiss();
                        Intent intent = new Intent(activity, VideoActivity.class);
                        intent.putExtra("nomeDaAula", descAula.getText().toString());
                        activity.startActivity(intent);
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(activity, "Algo deu errado, tente novamente. " + e, Toast.LENGTH_LONG).show();
                        Log.d("TESTEXX", "Erro:" + e);
                    });

        } else {
            Toast.makeText(activity, "Preencha o campo!", Toast.LENGTH_SHORT).show();
        }
    }
}
