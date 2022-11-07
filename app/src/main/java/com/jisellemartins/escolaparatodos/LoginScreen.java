package com.jisellemartins.escolaparatodos;

import static com.jisellemartins.escolaparatodos.Utils.UtilAutenticacao.aluno;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;
import com.jisellemartins.escolaparatodos.dialogs.DialogAula;
import com.jisellemartins.escolaparatodos.dialogs.DialogCodVerificacao;
import com.jisellemartins.escolaparatodos.dialogs.DialogLoading;

public class LoginScreen extends AppCompatActivity {

    ImageView imgVoltar;
    Button btnLogin;
    EditText campoTelefone, campoSenha;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String usuario = aluno;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);

        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);



        imgVoltar = findViewById(R.id.imgVoltar);
        btnLogin = findViewById(R.id.btnLogin);
        campoTelefone = findViewById(R.id.campoTelefone);
        campoSenha = findViewById(R.id.campoSenha);

        imgVoltar.setOnClickListener(view -> {
            finish();
        });

        btnLogin.setOnClickListener(view -> {
            DialogLoading alert = new DialogLoading();
            alert.showDialog(this);

            usuario = sharedPref.getString("usuario", aluno);

            if(campoTelefone.getText().toString().isEmpty() && campoSenha.getText().toString().isEmpty()){
                Toast.makeText(this, "Preencha todos os campos!", Toast.LENGTH_LONG).show();
            }else{
                String colecao = usuario.equals(aluno) ?  "Aluno" :  "Professor";
                db.collection(colecao)
                        .whereEqualTo("codArea", Integer.valueOf(campoTelefone.getText().toString().substring(0,2)))
                        .whereEqualTo("telefone", Integer.valueOf(campoTelefone.getText().toString().substring(2,11)))
                        .whereEqualTo("senha", campoSenha.getText().toString())
                        .get()
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful() && task.getResult().size() > 0) {
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