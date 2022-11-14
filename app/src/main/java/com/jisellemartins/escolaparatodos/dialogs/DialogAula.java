package com.jisellemartins.escolaparatodos.dialogs;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.jisellemartins.escolaparatodos.R;
import com.jisellemartins.escolaparatodos.VideoActivity;

import io.agora.agorauikit.manager.AgoraRTC;
import io.agora.rtc.Constants;

public class DialogAula {
    public void showDialog(Activity activity){
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_aula);

        //Button dialogButton = (Button) dialog.findViewById(R.id.btnIniciarAula);
        ImageView cancelar = (ImageView) dialog.findViewById(R.id.cancelar);
        Button btnIniciarAula = (Button) dialog.findViewById(R.id.btnIniciarAula);
        cancelar.setOnClickListener(v -> dialog.dismiss());

        btnIniciarAula.setOnClickListener(view -> {
            int MY_PERMISSIONS_REQUEST_CAMERA = 0;
            if (ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(activity, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO}, MY_PERMISSIONS_REQUEST_CAMERA);
            }

            int channelProfile = Constants.CLIENT_ROLE_BROADCASTER;
            // channelProfile = Constants.CLIENT_ROLE_AUDIENCE;

            //EditText channel = (EditText) findViewById(R.id.channel);
            String channelName = "teste";
            Intent intent = new Intent(activity, VideoActivity.class);
            intent.putExtra("channelMessage", channelName);
            intent.putExtra("profileMessage", channelProfile);
            activity.startActivity(intent);

            dialog.dismiss();
        });

        dialog.show();

    }
}
