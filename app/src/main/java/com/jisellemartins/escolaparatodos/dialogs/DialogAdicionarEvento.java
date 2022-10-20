package com.jisellemartins.escolaparatodos.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.view.Window;
import android.widget.ImageView;

import com.jisellemartins.escolaparatodos.R;

public class DialogAdicionarEvento {
    public void showDialog(Activity activity){
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_adicionar_evento);

        //Button dialogButton = (Button) dialog.findViewById(R.id.btnIniciarAula);
        ImageView cancelar = (ImageView) dialog.findViewById(R.id.cancelar);
        cancelar.setOnClickListener(v -> dialog.dismiss());

        dialog.show();

    }
}
