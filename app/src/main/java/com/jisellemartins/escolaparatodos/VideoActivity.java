package com.jisellemartins.escolaparatodos;

import static com.jisellemartins.escolaparatodos.Utils.UtilAutenticacao.entreiComoAluno;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.Map;

import io.agora.rtc.Constants;
import io.agora.rtc.IRtcEngineEventHandler;
import io.agora.rtc.RtcEngine;
import io.agora.rtc.models.ChannelMediaOptions;
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
        } else {
            iv.setSelected(true);
            iv.setColorFilter(getResources().getColor(R.color.black), PorterDuff.Mode.MULTIPLY);
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


        channelName = "com.jisellemartins.escolaparatodos";

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
        } else {
            iv.setSelected(true);
            iv.setColorFilter(getResources().getColor(R.color.black), PorterDuff.Mode.MULTIPLY);
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

    private void fetchToken(int uid, String channelName, int tokenRole) {
        // Prepare the Url
        String URLString = serverUrl + "/rtc/" + channelName + "/" + tokenRole + "/"
                + "uid" + "/" + uid + "/?expiry=" + tokenExpireTime;

        OkHttpClient client = new OkHttpClient();

        // Instantiate the RequestQueue.
        Request request = new Request.Builder()
                .url(URLString)
                .header("Content-Type", "application/json; charset=UTF-8")
                .get()
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("IOException", e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    Gson gson = new Gson();
                    String result = response.body().string();
                    Map map = gson.fromJson(result, Map.class);
                    String _token = map.get("rtcToken").toString();
                    setToken(_token);
                    Log.i("Token Received", token);
                }
            }
        });
    }

    void setToken(String newValue) {
        token = newValue;
        //if (!isJoined) { // Join a channel
            //ChannelMediaOptions options = new ChannelMediaOptions();

            // For a Video call, set the channel profile as COMMUNICATION.
            //options.channelProfile = Constants.CHANNEL_PROFILE_COMMUNICATION;
            // Set the client role as BROADCASTER or AUDIENCE according to the scenario.
            //options.clientRoleType = Constants.CLIENT_ROLE_BROADCASTER;
            // Start local preview.
            mRtcEngine.startPreview();

            // Join the channel with a token.
            mRtcEngine.joinChannel(token, channelName, null, 0);
        /*} else { // Already joined, renew the token by calling renewToken
            mRtcEngine.renewToken(token);
            Log.i("i", "Token renewed");
        }*/
    }


    /*@Override
    public void onTokenPrivilegeWillExpire(String token) {
        Log.i("i", "Token Will expire");
        fetchToken(uid, channelName, tokenRole);
        super.onTokenPrivilegeWillExpire(token);
    }

    public void joinChannel(View view) {
        //channelName = editChannelName.getText().toString();
        if (channelName.length() == 0) {
            //showMessage("Type a channel name");
            Log.i("TESTEXX: ", "Type a channel name");
            return;
        } else if (!serverUrl.contains("http")) {
            Log.i("TESTEXX: ", "Invalid token server URL");
            //showMessage("Invalid token server URL");
            return;
        }
        tokenRole = Constants.CLIENT_ROLE_BROADCASTER;
        // Display LocalSurfaceView.
        setupLocalVideo();
        //localSurfaceView.setVisibility(View.VISIBLE);
        fetchToken(uid, channelName, tokenRole);

    }*/
}

