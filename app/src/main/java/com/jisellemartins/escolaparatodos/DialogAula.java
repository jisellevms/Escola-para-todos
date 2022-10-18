package com.jisellemartins.escolaparatodos;

import android.app.Activity;
import android.app.Dialog;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

public class DialogAula {
    public void showDialog(Activity activity){
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_aula);

        //Button dialogButton = (Button) dialog.findViewById(R.id.btnIniciarAula);
        ImageView cancelar = (ImageView) dialog.findViewById(R.id.cancelar);
        cancelar.setOnClickListener(v -> dialog.dismiss());

        dialog.show();

    }
}
