package com.jisellemartins.escolaparatodos;


import static com.jisellemartins.escolaparatodos.Utils.Utils.TAG_EPT;
import static com.jisellemartins.escolaparatodos.Utils.Utils.entreiComoAluno;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import io.agora.rtc.Constants;
import io.agora.rtc.IRtcEngineEventHandler;
import io.agora.rtc.RtcEngine;
import io.agora.rtc.video.VideoCanvas;
import io.agora.rtc.video.VideoEncoderConfiguration;


public class VideoActivity extends AppCompatActivity {

    private RtcEngine mRtcEngine;
    private String channelName;
    private int channelProfile;


    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private MediaRecorder recorder = null;

    private MediaPlayer player = null;

    // Requesting permission to RECORD_AUDIO
    private boolean permissionToRecordAccepted = false;
    private String[] permissions = {Manifest.permission.RECORD_AUDIO};
    private File audioFile;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    StorageReference storageRef;
    String disciplinaTime;


    public static File dir = new File(new File(Environment.getExternalStorageDirectory(), "bleh"), "bleh");

    public String aulaFalada;
    File textoFile;


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

        SharedPreferences sharedPref = getSharedPreferences("chaves", MODE_PRIVATE);
        disciplinaTime = sharedPref.getString("disciplina", "");

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                channelName = null;
            } else {
                channelName = extras.getString("nomeDaAula");
            }
        } else {
            channelName = (String) savedInstanceState.getSerializable("nomeDaAula");
        }


        if (entreiComoAluno) {
            channelProfile = Constants.CLIENT_ROLE_BROADCASTER;
        } else {
            channelProfile = Constants.CLIENT_ROLE_BROADCASTER;
        }

        if (channelProfile == -1) {
            Log.e("TAG: ", "No profile");
        }

        initAgoraEngineAndJoinChannel();


        if (!entreiComoAluno) {
            audioFile = new File(Environment.getExternalStorageDirectory(), channelName + ".3gp");
            ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_PERMISSION);
            iniciarGravacao();
            //abrirMicrofone();
        }


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
            Log.i(TAG_EPT, e.toString());
            e.printStackTrace();
        }
    }

    private void setupVideoProfile() {
        mRtcEngine.enableVideo();

        mRtcEngine.setVideoEncoderConfiguration(new VideoEncoderConfiguration(VideoEncoderConfiguration.VD_640x480, VideoEncoderConfiguration.FRAME_RATE.FRAME_RATE_FPS_15, VideoEncoderConfiguration.STANDARD_BITRATE, VideoEncoderConfiguration.ORIENTATION_MODE.ORIENTATION_MODE_FIXED_PORTRAIT));
    }

    private void setupLocalVideo() {
        FrameLayout container = findViewById(R.id.local_video_view_container);
        SurfaceView surfaceView = RtcEngine.CreateRendererView(getBaseContext());
        surfaceView.setZOrderMediaOverlay(true);
        container.addView(surfaceView);
        mRtcEngine.setupLocalVideo(new VideoCanvas(surfaceView, VideoCanvas.RENDER_MODE_FIT, 0));
    }

    private void joinChannel() {
        mRtcEngine.joinChannel(null, channelName, "Optional Data", 0);
    }

    private void leaveChannel() {
        mRtcEngine.leaveChannel();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();


        if (!entreiComoAluno) {
            deleteAula();
            stopRecording();
            uploadAudio();
            //uploadTexto();
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

    public void deleteAula() {

        SharedPreferences sharedPref = getSharedPreferences("chaves", MODE_PRIVATE);
        String disciplinaTime = sharedPref.getString("disciplina", "");

        // PROCURAR A AULA
        db.collection("Aula").whereEqualTo("disciplina", disciplinaTime).get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult().size() > 0) {

                // DELETAR A AULA
                db.collection("Aula").document(task.getResult().getDocuments().get(0).getId()).delete().addOnSuccessListener(aVoid -> Log.d(TAG_EPT, "A aula foi apagada!")).addOnFailureListener(e -> Log.w(TAG_EPT, "Error ao apagar aula ", e));

            } else if (task.getResult().size() == 0) {
                Log.i(TAG_EPT, "A aula não foi encontrada");
            } else {
                Log.i(TAG_EPT, "ERRO: " + task.getException());
            }
        });
    }

    // GRAVAR VOZ DA AULA


    public void iniciarGravacao() {
        boolean mStartRecording = true;
        onRecord(mStartRecording);
        if (mStartRecording) {
            Log.i("AUDIO", "Stop recording");
        } else {
            Log.i("AUDIO", "Start recording");
        }
        mStartRecording = !mStartRecording;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_RECORD_AUDIO_PERMISSION:
                permissionToRecordAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                break;
        }
        if (!permissionToRecordAccepted) finish();

    }

    private void onRecord(boolean start) {
        if (start) {
            startRecording();
        } else {
            stopRecording();
        }
    }

    private void startRecording() {
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setOutputFile(audioFile.getAbsolutePath());
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            recorder.prepare();
            recorder.start();
        } catch (IOException e) {
            Log.e(TAG_EPT, "" + e);
        }


    }

    private void stopRecording() {
        if (recorder != null) {
            recorder.stop();
            recorder.release();
            recorder = null;
        }

    }

    @Override
    public void onStop() {
        super.onStop();
        if (recorder != null) {
            recorder.release();
            recorder = null;
        }

        if (player != null) {
            player.release();
            player = null;
        }
    }

    private void uploadAudio() {
        storageRef = FirebaseStorage.getInstance().getReference(disciplinaTime + "/" + channelName + "/audio");
        storageRef.putFile(Uri.fromFile(audioFile)).addOnSuccessListener(taskSnapshot -> {
            Toast.makeText(getApplicationContext(), "Audio adicionado com sucesso!", Toast.LENGTH_LONG).show();


        }).addOnFailureListener(e -> {
            Toast.makeText(getApplicationContext(), "Erro: " + e, Toast.LENGTH_LONG).show();
        });
    }

    // VOZ PARA TEXTO

    public void abrirMicrofone() {
        Intent intent = new Intent(
                RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "pt-BR");

        try {
            startActivityForResult(intent, 1);
            //txtText.setText("");
        } catch (ActivityNotFoundException a) {
            Toast t = Toast.makeText(getApplicationContext(),
                    "Opps! Your device doesn't support Speech to Text",
                    Toast.LENGTH_SHORT);
            t.show();
        }
    }

    public boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("PDF", "Permission is granted");
                return true;
            } else {

                Log.v("PDF", "Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v("PDF", "Permission is granted");
            return true;
        }
    }

    public void askForPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (!Environment.isExternalStorageManager()) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                startActivity(intent);
                return;
            }
            createDir();
        }
    }

    public void createDir() {
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    private void savepdf() {
        Document doc = new Document();
        //String mfile=new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(System.currentTimeMillis());
        textoFile = new File(Environment.getExternalStorageDirectory(), "/" + "texto" + ".pdf");
        Font smallBold = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD);
        try {
            PdfWriter.getInstance(doc, new FileOutputStream(textoFile));
            doc.open();
            String mtext = aulaFalada;
            doc.addAuthor("Jiselle Martins");
            doc.add(new Paragraph(mtext, smallBold));
            doc.close();
            Toast.makeText(this, "texto" + ".pdf" + " foi salvo em " + textoFile, Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Log.i("PDF", e.getMessage());
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 1: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> text = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

                    aulaFalada = text.get(0);
                    savepdf();
                }
                break;
            }

        }
    }

    private void uploadTexto() {
        storageRef = FirebaseStorage.getInstance().getReference(disciplinaTime + "/" + channelName + "/texto");
        storageRef.putFile(Uri.fromFile(textoFile)).addOnSuccessListener(taskSnapshot -> {
            Toast.makeText(getApplicationContext(), "Texto adicionado com sucesso!", Toast.LENGTH_LONG).show();

        }).addOnFailureListener(e -> {
            Toast.makeText(getApplicationContext(), "Erro: " + e, Toast.LENGTH_LONG).show();
        });
    }





}

