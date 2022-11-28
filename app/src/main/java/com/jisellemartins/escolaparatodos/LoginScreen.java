package com.jisellemartins.escolaparatodos;

import static com.jisellemartins.escolaparatodos.Utils.Utils.aluno;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;
import com.jisellemartins.escolaparatodos.Utils.Mask;
import com.jisellemartins.escolaparatodos.dialogs.DialogLoading;

public class LoginScreen extends AppCompatActivity {

    ImageView imgVoltar, imgConfig;
    Button btnLogin;
    EditText campoTelefone, campoSenha;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String usuario = aluno;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);

        SharedPreferences sharedPref = getSharedPreferences("chaves", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();



        imgVoltar = findViewById(R.id.imgVoltar);
        imgConfig = findViewById(R.id.imgConfig);
        btnLogin = findViewById(R.id.btnLogin);
        campoTelefone = findViewById(R.id.campoTelefone);
        campoSenha = findViewById(R.id.campoSenha);

        campoTelefone.addTextChangedListener(Mask.insert("(##)#####-####", campoTelefone));

        imgVoltar.setOnClickListener(view -> {
            finish();
        });
        imgConfig.setOnClickListener(view -> {
            Intent i = new Intent(LoginScreen.this, ConfiguracoesScreen.class);
            startActivity(i);
        });

        btnLogin.setOnClickListener(view -> {
            DialogLoading alert = new DialogLoading();
            alert.showDialog(this);

            usuario = sharedPref.getString("usuario", aluno);

            if(campoTelefone.getText().toString().isEmpty() && campoSenha.getText().toString().isEmpty()){
                Toast.makeText(this, "Preencha todos os campos!", Toast.LENGTH_LONG).show();
            }else{

                String telefone = campoTelefone.getText().toString().replace("(","").replace(")","").replace("-","");

                String colecao = usuario.equals(aluno) ?  "Aluno" :  "Professor";
                db.collection(colecao)
                        .whereEqualTo("codArea", Integer.valueOf(telefone.substring(0,2)))
                        .whereEqualTo("telefone", Integer.valueOf(telefone.substring(2,11)))
                        .whereEqualTo("senha", campoSenha.getText().toString())
                        .get()
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful() && task.getResult().size() > 0) {
                                editor.putString("numero",telefone);
                                editor.commit();
                                alert.fecharLoading();
                                finish();
                                Intent i = new Intent(LoginScreen.this, DisciplinesScreen.class);
                                startActivity(i);
                            } else if(task.getResult().size() == 0){
                                alert.fecharLoading();
                                Toast.makeText(this, "Esse usuário não está cadastrado", Toast.LENGTH_LONG).show();
                            }else {
                                alert.fecharLoading();
                                Toast.makeText(this, "Erro: " + task.getException(), Toast.LENGTH_LONG).show();
                            }
                        });
            }
        });
    }
}