package com.jisellemartins.escolaparatodos;

import static com.jisellemartins.escolaparatodos.Utils.UtilAutenticacao.entreiComoAluno;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.Map;

import io.agora.rtc.Constants;
import io.agora.rtc.IRtcEngineEventHandler;
import io.agora.rtc.RtcEngine;
import io.agora.rtc.video.VideoCanvas;
import io.agora.rtc.video.VideoEncoderConfiguration;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class VideoActivity extends AppCompatActivity {

    private RtcEngine mRtcEngine;
    private String channelName;
    private int channelProfile;
    String token;
    private String appId = "94ec9d428e304275b5ea66c7b9be6eb5";

    FirebaseFirestore db = FirebaseFirestore.getInstance();


    private int tokenRole; // The token role: Broadcaster or Audience
    private String serverUrl = "https://agora-token-service-production-7f4d.up.railway.app/"; // The base URL to your token server, for example, "https://agora-token-service-production-92ff.up.railway.app".
    private int tokenExpireTime = 40; // Expire time in Seconds.

    private IRtcEngineEventHandler mRtcEventHandler = new IRtcEngineEventHandler() {

        @Override
        public void onFirstRemoteVideoDecoded(final int uid, int width, int height, int elapsed) {
            runOnUiThread(() -> setupRemoteVideo(uid));
        }

        @Override
        public void onUserOffline(int uid, int reason) {
            runOnUiThread(() -> onRemoteUserLeft());
        }

        @Override
        public void onUserMuteVideo(final int uid, final boolean muted) {
            runOnUiThread(() -> onRemoteUserVideoMuted(uid, muted));
        }
    };

    public void onLocalAudioMuteClicked(View view) {
        ImageView iv = (ImageView) view;
        if (iv.isSelected()) {
            iv.setSelected(false);
            iv.clearColorFilter();
            iv.setColorFilter(getResources().getColor(R.color.green), PorterDuff.Mode.MULTIPLY);
            iv.setImageDrawable(getResources().getDrawable(R.drawable.microfone));
        } else {
            iv.setSelected(true);
            iv.setColorFilter(getResources().getColor(R.color.red), PorterDuff.Mode.MULTIPLY);
            iv.setImageDrawable(getResources().getDrawable(R.drawable.nomicrofone));

        }

        mRtcEngine.muteLocalAudioStream(iv.isSelected());
    }

    private void onRemoteUserVideoMuted(int uid, boolean muted) {
        FrameLayout container = findViewById(R.id.remote_video_view_container);

        SurfaceView surfaceView = (SurfaceView) container.getChildAt(0);

        Object tag = surfaceView.getTag();
        if (tag != null && (Integer) tag == uid) {
            surfaceView.setVisibility(muted ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                channelName= null;
            } else {
                channelName= extras.getString("nomeDaAula");
            }
        } else {
            channelName= (String) savedInstanceState.getSerializable("nomeDaAula");
        }


        //channelName = "com.jisellemartins.escolaparatodos";

        if (entreiComoAluno){
            channelProfile = Constants.CLIENT_ROLE_BROADCASTER;
        }else{
            channelProfile = Constants.CLIENT_ROLE_BROADCASTER;
        }

        if (channelProfile == -1) {
            Log.e("TAG: ", "No profile");
        }

        initAgoraEngineAndJoinChannel();
    }

    public void onLocalVideoMuteClicked(View view) {
        ImageView iv = (ImageView) view;
        if (iv.isSelected()) {
            iv.setSelected(false);
            iv.clearColorFilter();
            iv.setColorFilter(getResources().getColor(R.color.green), PorterDuff.Mode.MULTIPLY);
            iv.setImageDrawable(getResources().getDrawable(R.drawable.video));
        } else {
            iv.setSelected(true);
            iv.setColorFilter(getResources().getColor(R.color.red), PorterDuff.Mode.MULTIPLY);
            iv.setImageDrawable(getResources().getDrawable(R.drawable.novideo));
        }

        mRtcEngine.muteLocalVideoStream(iv.isSelected());

        FrameLayout container = findViewById(R.id.local_video_view_container);
        SurfaceView surfaceView = (SurfaceView) container.getChildAt(0);
        surfaceView.setZOrderMediaOverlay(!iv.isSelected());
        surfaceView.setVisibility(iv.isSelected() ? View.GONE : View.VISIBLE);
    }

    private void setupRemoteVideo(int uid) {
        FrameLayout container = findViewById(R.id.remote_video_view_container);
//        if (container.getChildCount() > 1) {
//            return;
//        }

        SurfaceView surfaceView = RtcEngine.CreateRendererView(getBaseContext());
        container.addView(surfaceView);
        mRtcEngine.setupRemoteVideo(new VideoCanvas(surfaceView, VideoCanvas.RENDER_MODE_FIT, uid));
    }

    private void onRemoteUserLeft() {
        FrameLayout container = findViewById(R.id.remote_video_view_container);
        container.removeAllViews();
    }

    private void initAgoraEngineAndJoinChannel() {
        initalizeAgoraEngine();
        mRtcEngine.setChannelProfile(Constants.CHANNEL_PROFILE_LIVE_BROADCASTING);
        mRtcEngine.setClientRole(channelProfile);
        mRtcEngine.enableLocalVideo(true);
        setupVideoProfile();
        setupLocalVideo();
        joinChannel();
    }

    private void initalizeAgoraEngine() {
        try {
            mRtcEngine = RtcEngine.create(getBaseContext(), "bddccf81b58d4da48eb1b8429fe6c86a", mRtcEventHandler);
        } catch (Exception e) {
            Log.i("TESTEXX: ", e.toString());
            e.printStackTrace();
        }
    }

    private void setupVideoProfile() {
        mRtcEngine.enableVideo();

        mRtcEngine.setVideoEncoderConfiguration(new VideoEncoderConfiguration(VideoEncoderConfiguration.VD_640x480, VideoEncoderConfiguration.FRAME_RATE.FRAME_RATE_FPS_15,
                VideoEncoderConfiguration.STANDARD_BITRATE,
                VideoEncoderConfiguration.ORIENTATION_MODE.ORIENTATION_MODE_FIXED_PORTRAIT));
    }

    private void setupLocalVideo() {
        FrameLayout container = findViewById(R.id.local_video_view_container);
        SurfaceView surfaceView = RtcEngine.CreateRendererView(getBaseContext());
        surfaceView.setZOrderMediaOverlay(true);
        container.addView(surfaceView);
        mRtcEngine.setupLocalVideo(new VideoCanvas(surfaceView, VideoCanvas.RENDER_MODE_FIT, 0));
    }

    private void joinChannel() {
        //fetchToken();
        mRtcEngine.joinChannel(null, channelName, "Optional Data", 0);
    }

    private void leaveChannel() {
        mRtcEngine.leaveChannel();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (!entreiComoAluno){
            deleteAula();
        }
        leaveChannel();
        RtcEngine.destroy();
        mRtcEngine = null;
    }

    public void onSwitchCameraClicked(View view) {
        mRtcEngine.switchCamera();
    }

    public void onEndCallClicked(View view) {
        finish();
    }

    public void deleteAula(){

        SharedPreferences sharedPref = getSharedPreferences("chaves", MODE_PRIVATE);
        String disciplinaTime = sharedPref.getString("disciplina", "");

        // PROCURAR A AULA
        db.collection("Aula")
                .whereEqualTo("disciplina", disciplinaTime)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult().size() > 0) {

                        // DELETAR A AULA
                        db.collection("Aula").document(task.getResult().getDocuments().get(0).getId())
                                .delete()
                                .addOnSuccessListener(aVoid -> Log.d("TESTEXX", "A aula foi apagada!"))
                                .addOnFailureListener(e -> Log.w("TESTEXX", "Error ao apagar aula ", e));

                    } else if(task.getResult().size() == 0){
                       Log.i("TESTEXX","A aula não foi encontrada");
                    }else {
                        Log.i("TESTEXX","ERRO: " + task.getException());
                    }
                });


    }


}

