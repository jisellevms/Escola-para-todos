package com.jisellemartins.escolaparatodos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.jisellemartins.escolaparatodos.dialogs.DialogCodVerificacao;

import java.util.concurrent.TimeUnit;

public class CadastroScreen extends AppCompatActivity {

    EditText cadTelefone;;
    Button btnCadastrar;

    String mVerificationId;
    PhoneAuthProvider.ForceResendingToken mResendToken;

    Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_screen);

        cadTelefone = findViewById(R.id.cadTelefone);
        btnCadastrar = findViewById(R.id.btnCadastrar);

        activity = this;

        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.setLanguageCode("pt");

        btnCadastrar.setOnClickListener(view -> {
            PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                @Override
                public void onVerificationCompleted(PhoneAuthCredential credential) {

                    Log.d("TESTE", "onVerificationCompleted:" + credential);

                    final String code = credential.getSmsCode();
                    if (code != null){
                        Toast.makeText(activity, "Esse usuário já existe", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onVerificationFailed(FirebaseException e) {

                    Log.w("TESTE", "onVerificationFailed", e);

                    Toast.makeText(activity, "Verificação falhou, tente novamente.", Toast.LENGTH_LONG).show();


                    /*if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    } else if (e instanceof FirebaseTooManyRequestsException) {
                    }*/

                }

                @Override
                public void onCodeSent(@NonNull String verificationId,
                                       @NonNull PhoneAuthProvider.ForceResendingToken token) {

                    Log.d("TESTE", "onCodeSent:" + verificationId);

                    mVerificationId = verificationId;
                    mResendToken = token;

                    DialogCodVerificacao dialogCodVerificacao = new DialogCodVerificacao();
                    dialogCodVerificacao.showDialog(activity, mVerificationId, auth);
                }
            };


            if (auth.getCurrentUser() != null &&
                    !FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber().isEmpty() &&
                    FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber().equals(cadTelefone.getText().toString())) {
                Toast.makeText(activity, "O número informado já foi verificado, faça login!", Toast.LENGTH_LONG).show();
            } else {
                PhoneAuthOptions options =
                        PhoneAuthOptions.newBuilder(auth)
                                .setPhoneNumber("+55" + cadTelefone.getText().toString())       // Phone number to verify
                                .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                                .setActivity(this)                 // Activity (for callback binding)
                                .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                                .build();
                PhoneAuthProvider.verifyPhoneNumber(options);

            }
        });

    }
}