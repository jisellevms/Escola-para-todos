package com.jisellemartins.escolaparatodos.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.jisellemartins.escolaparatodos.CriarDisciplinaScreen;
import com.jisellemartins.escolaparatodos.DisciplinesScreen;
import com.jisellemartins.escolaparatodos.R;
import com.jisellemartins.escolaparatodos.Utils.UtilAutenticacao;

import java.util.concurrent.Executor;

public class DialogCodVerificacao {

    Button btnVerificar;
    EditText codigo;
    ImageView cancelar;
    Activity activityG;
    String mVerificationId, code;
    public void showDialog(Activity activity, String mVerificationId, FirebaseAuth auth){
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_codigo_verificacao);
        activityG = activity;

        btnVerificar = (Button) dialog.findViewById(R.id.btnVerificar);
        codigo = (EditText) dialog.findViewById(R.id.codigo);
        cancelar = (ImageView) dialog.findViewById(R.id.cancelar);
        cancelar.setOnClickListener(v -> dialog.dismiss());
        btnVerificar.setOnClickListener(v -> {
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, codigo.getText().toString());
            this.mVerificationId = mVerificationId;
            code = codigo.getText().toString();
            signInWithPhoneAuthCredential(credential, auth);

        });
        dialog.show();
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential, FirebaseAuth auth) {
        auth.signInWithCredential(credential)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d("TESTE", "signInWithCredential:success");

                        //UtilAutenticacao.phoneAuthCredential = credential;

                        SharedPreferences sharedPref = activityG.getPreferences(Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString("verificationId",mVerificationId);
                        editor.apply();

                        editor.putString("code", code);
                        editor.apply();

                        FirebaseUser user = task.getResult().getUser();
                        Intent i = new Intent(activityG,
                                DisciplinesScreen.class);
                        activityG.startActivity(i);

                    } else {
                        Log.w("TESTE", "signInWithCredential:failure", task.getException());
                        if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                        }
                    }
                });
    }

}
