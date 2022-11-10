package com.jisellemartins.escolaparatodos.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.view.Window;
import android.widget.ImageView;

import com.jisellemartins.escolaparatodos.R;

public class DialogLoading {

    Dialog dialog;


    public void showDialog(Activity activity){
        final Dialog dialog;
        dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_loading);

        this.dialog = dialog;
        dialog.show();

    }

    public void fecharLoading(){
        dialog.dismiss();
    }
}
