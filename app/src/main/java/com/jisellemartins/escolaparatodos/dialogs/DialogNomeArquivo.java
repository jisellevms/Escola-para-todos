package com.jisellemartins.escolaparatodos.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.util.Log;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.jisellemartins.escolaparatodos.BibliotecaScreen;
import com.jisellemartins.escolaparatodos.DisciplinesScreen;
import com.jisellemartins.escolaparatodos.R;

import java.util.HashMap;
import java.util.Map;

public class DialogNomeArquivo {

    Button btnSelecionar;
    EditText nomeArquivo;
    ImageView cancelar;
    Activity activityG;
    public String nameArquivo = "";


    public void showDialog(Activity activity){
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_nome_arquivo);
        activityG = activity;

        btnSelecionar = dialog.findViewById(R.id.btnSelecionar);
        nomeArquivo = dialog.findViewById(R.id.nomeArquivo);
        cancelar = dialog.findViewById(R.id.cancelar);
        cancelar.setOnClickListener(v -> dialog.dismiss());
        btnSelecionar.setOnClickListener(v -> {

            if (nomeArquivo.getText().toString().isEmpty()){
                Toast.makeText(activity, "Preencha o nome do arquivo", Toast.LENGTH_LONG).show();
            }else{
                nameArquivo = nomeArquivo.getText().toString();
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("*/*");
                String[] mimetypes = {"image/*", "application/pdf"};
                intent.putExtra(Intent.EXTRA_MIME_TYPES, mimetypes);
                activity.startActivityForResult(intent, 100);
                dialog.dismiss();
            }
        });
        dialog.show();
    }

}
