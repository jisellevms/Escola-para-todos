package com.jisellemartins.escolaparatodos;

import static com.jisellemartins.escolaparatodos.Utils.Utils.TAG_EPT;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.jisellemartins.escolaparatodos.Utils.Mask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class CadastroScreen extends AppCompatActivity {
    ImageView imgVoltar, imgConfig;
    EditText nomeUsuario, telefoneUsuario, senhaUsuario, idadeAluno, formacaoProf;
    Button btnCadastrar;
    TextView idade, formacao;

    RadioButton radioAluno, radioProf;
    RadioGroup radioGroup;

    Activity activity;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    boolean aluno = true;
    String usuario = "Aluno";

    String telefone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_screen);
        imgVoltar = findViewById(R.id.imgVoltar);
        imgConfig = findViewById(R.id.imgConfig);

        nomeUsuario = findViewById(R.id.nomeUsuario);
        telefoneUsuario = findViewById(R.id.telefoneUsuario);
        senhaUsuario = findViewById(R.id.senhaUsuario);
        idadeAluno = findViewById(R.id.idadeAluno);
        formacaoProf = findViewById(R.id.formacaoProf);



        idade = findViewById(R.id.idade);
        formacao = findViewById(R.id.formacao);

        radioGroup = findViewById(R.id.radioGroup);
        radioAluno = findViewById(R.id.radioAluno);
        radioProf = findViewById(R.id.radioProf);


        btnCadastrar = findViewById(R.id.btnCadastrar);

        telefoneUsuario.addTextChangedListener(Mask.insert("(##)#####-####", telefoneUsuario));


        activity = this;

        auth.setLanguageCode("pt");
        imgVoltar.setOnClickListener(view -> {
            finish();
        });
        imgConfig.setOnClickListener(view -> {
            Intent i = new Intent(CadastroScreen.this, ConfiguracoesScreen.class);
            startActivity(i);
        });
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            switch(checkedId){
                case R.id.radioAluno:
                    idade.setVisibility(View.VISIBLE);
                    idadeAluno.setVisibility(View.VISIBLE);

                    formacao.setVisibility(View.GONE);
                    formacaoProf.setVisibility(View.GONE);

                    aluno = true;

                    break;
                case R.id.radioProf:
                    idade.setVisibility(View.GONE);
                    idadeAluno.setVisibility(View.GONE);

                    formacao.setVisibility(View.VISIBLE);
                    formacaoProf.setVisibility(View.VISIBLE);

                    aluno = false;
                    break;
            }
        });


        btnCadastrar.setOnClickListener(view -> {
            cadastrar();

        });

    }

    public void cadastrar(){

        telefone = telefoneUsuario.getText().toString().replace("(","").replace(")","").replace("-","");


        if(!nomeUsuario.getText().toString().isEmpty() &&
        !telefoneUsuario.getText().toString().isEmpty() &&
        !senhaUsuario.getText().toString().isEmpty() &&
                ((aluno && !idadeAluno.getText().toString().isEmpty()) || (!aluno && !formacaoProf.getText().toString().isEmpty()))){


            if (aluno){
                usuario = "Aluno";
            }else{
                usuario = "Professor";
            }

            db.collection(usuario)
                    .whereEqualTo("codArea", Integer.valueOf(telefone.substring(0,2)))
                    .whereEqualTo("telefone", Integer.valueOf(telefone.substring(2,11)))
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            verificaUsuarioExiste(task.getResult().size());
                        } else {
                            Toast.makeText(this, "Erro: " + task.getException(), Toast.LENGTH_LONG).show();
                        }
                    });


        }else {
            Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_LONG).show();
        }
    }

    public void verificaUsuarioExiste(int size){
        SharedPreferences sharedPref = getSharedPreferences("chaves", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        if(size ==  0){
            if (aluno) {

                Map<String, Object> aluno = new HashMap<>();
                aluno.put("idade", Integer.valueOf(idadeAluno.getText().toString()));
                aluno.put("nome", nomeUsuario.getText().toString());
                aluno.put("codArea", Integer.valueOf(telefone.substring(0,2)));
                aluno.put("telefone", Integer.valueOf(telefone.substring(2,11)));
                aluno.put("senha", senhaUsuario.getText().toString());

                db.collection("Aluno").document(getRandomNonRepeatingIntegers(6,0,1000).toString())
                        .set(aluno)
                        .addOnSuccessListener(aVoid -> {
                            editor.putString("usuario","aluno");
                            editor.commit();
                            Toast.makeText(this, "Usuário cadastrado com sucesso!", Toast.LENGTH_LONG).show();
                            finish();
                            Intent i = new Intent(this, DisciplinesScreen.class);
                            startActivity(i);

                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(this, "Erro: " + e, Toast.LENGTH_LONG).show();
                            Log.d(TAG_EPT, "Erro:" + e);

                        });

            }else {
                Map<String, Object> professor = new HashMap<>();
                professor.put("formacao", formacaoProf.getText().toString());
                professor.put("nome", nomeUsuario.getText().toString());
                professor.put("codArea", Integer.valueOf(telefone.substring(0,2)));
                professor.put("telefone", Integer.valueOf(telefone.substring(2,11)));
                professor.put("senha", senhaUsuario.getText().toString());

                db.collection("Professor").document(getRandomNonRepeatingIntegers(6,0,1000).toString())
                        .set(professor)
                        .addOnSuccessListener(aVoid -> {
                            editor.putString("usuario","professor");
                            editor.commit();
                            Toast.makeText(this, "Usuário cadastrado com sucesso!", Toast.LENGTH_LONG).show();
                            finish();
                            Intent i = new Intent(this, DisciplinesScreen.class);
                            startActivity(i);

                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(this, "Erro: " + e, Toast.LENGTH_LONG).show();

                        });
            }

            editor.putString("numero",telefone);
            editor.commit();
        }else{
            Toast.makeText(this, "O usuário já está cadastrado", Toast.LENGTH_LONG).show();
        }
    }


    public static int getRandomInt(int min, int max) {
        Random random = new Random();

        return random.nextInt((max - min) + 1) + min;
    }

    public static ArrayList<Integer> getRandomNonRepeatingIntegers(int size, int min,
                                                                   int max) {
        ArrayList<Integer> numbers = new ArrayList<>();

        while (numbers.size() < size) {
            int random = getRandomInt(min, max);

            if (!numbers.contains(random)) {
                numbers.add(random);
            }
        }
        return numbers;
    }
}