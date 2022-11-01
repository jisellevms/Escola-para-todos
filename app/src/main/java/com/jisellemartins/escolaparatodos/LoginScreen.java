package com.jisellemartins.escolaparatodos;

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

public class LoginScreen extends AppCompatActivity {

    ImageView imgVoltar;
    Button btnLogin;
    EditText campoTelefone;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);


        imgVoltar = findViewById(R.id.imgVoltar);
        btnLogin = findViewById(R.id.btnLogin);
        campoTelefone = findViewById(R.id.campoTelefone);

        imgVoltar.setOnClickListener(view -> {
            finish();
        });

        btnLogin.setOnClickListener(view -> {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

            if (user == null ){
                Toast.makeText(this, "Esse dispositivo não foi verificado, faça o cadastro!", Toast.LENGTH_LONG).show();
            }

            if (user.getPhoneNumber().equals("+55" + campoTelefone.getText().toString())){
                Intent i = new Intent(LoginScreen.this,
                        DisciplinesScreen.class);
                startActivity(i);
            }else{
                Toast.makeText(this, "O número está incorreto", Toast.LENGTH_LONG).show();
            }
        });
    }
}