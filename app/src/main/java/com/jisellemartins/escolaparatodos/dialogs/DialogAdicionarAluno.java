package com.jisellemartins.escolaparatodos.dialogs;

import static android.content.Context.MODE_PRIVATE;
import static com.jisellemartins.escolaparatodos.Utils.UtilAutenticacao.aluno;

import android.app.Activity;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jisellemartins.escolaparatodos.R;
import com.jisellemartins.escolaparatodos.model.Aluno;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DialogAdicionarAluno {

    FirebaseFirestore db = FirebaseFirestore.getInstance();


    public void showDialog(Activity activity) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_adicionar_aluno);

        //Button dialogButton = (Button) dialog.findViewById(R.id.btnIniciarAula);
        ImageView cancelar = dialog.findViewById(R.id.cancelar);
        EditText campoTelefone = dialog.findViewById(R.id.campoTelefone);
        Button btnSalvar = dialog.findViewById(R.id.btnSalvar);
        cancelar.setOnClickListener(v -> dialog.dismiss());

        btnSalvar.setOnClickListener(view -> {
            if (!campoTelefone.getText().toString().isEmpty()) {

                // verificar se aluno está cadastrado no aplicativo, se não precisa cadastrar

                db.collection("Aluno")
                        .whereEqualTo("codArea", Integer.valueOf(campoTelefone.getText().toString().substring(0, 2)))
                        .whereEqualTo("telefone", Integer.valueOf(campoTelefone.getText().toString().substring(2, 11)))
                        .get()
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful() && task.getResult().size() > 0) {
                                String nomeAluno = task.getResult().getDocuments().get(0).get("nome").toString();
                                verificaUsuarioExiste(activity, campoTelefone.getText().toString(), nomeAluno, dialog);
                            } else if (task.getResult().size() == 0) {
                                Toast.makeText(activity, "Esse aluno não está cadastrado", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(activity, "Erro: " + task.getException(), Toast.LENGTH_LONG).show();
                            }
                        });


            } else {
                Toast.makeText(activity, "Preencha o campo", Toast.LENGTH_LONG).show();
            }
        });

        dialog.show();

    }

    private void verificaUsuarioExiste(Activity activity, String telefoneALuno, String nomeAluno, Dialog dialog) {
        // se aluno existir, vai dar um update na tabela de disciplinas adicionando o aluno no json

        SharedPreferences sharedPref = activity.getSharedPreferences("chaves", MODE_PRIVATE);
        String disciplinaTime = sharedPref.getString("disciplina", "");

        CollectionReference complaintsRef = db.collection("Disciplina");
        complaintsRef.whereEqualTo("dataCriacao", disciplinaTime).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Map<Object, String> map = new HashMap<>();
                    Gson gson = new Gson();
                    String jsonAntigo = document.get("alunos").toString();
                    ArrayList<Aluno> lstObject = new ArrayList<>();
                    Aluno aluno = new Aluno();
                    aluno.setNome(nomeAluno);
                    aluno.setTelefone(telefoneALuno);
                    if (jsonAntigo.equals("{}")) {
                        lstObject.add(aluno);
                    } else {
                        lstObject = gson.fromJson(jsonAntigo, new TypeToken<List<Aluno>>() {
                        }.getType());
                        lstObject.add(aluno);
                    }

                    String novoJson = gson.toJson(lstObject);
                    map.put("alunos", novoJson);
                    complaintsRef.document(document.getId()).set(map, SetOptions.merge());

                    Toast.makeText(activity, "Aluno cadastrado no disciplina", Toast.LENGTH_LONG).show();
                    dialog.dismiss();


                }
            }
        });
    }

}
