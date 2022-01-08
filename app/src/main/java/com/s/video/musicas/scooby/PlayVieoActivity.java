package com.s.video.musicas.scooby;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;
import com.s.video.musicas.scooby.ChatModel.Message;
import com.s.video.musicas.scooby.Map.MapActivity;
import com.s.video.musicas.scooby.Models.PrivacyModel;
import com.s.video.musicas.scooby.adapter.BottomSheetDialog;
import com.s.video.musicas.scooby.adapter.ChatBoxAdapter;
import com.s.video.musicas.scooby.adapter.UserListAdapter;
import com.s.video.musicas.scooby.nettwork.Api;
import com.s.video.musicas.scooby.nettwork.ApiClints;
import com.s.video.musicas.scooby.nettwork.model.LiveUserModel;
import com.s.video.musicas.scooby.utils.AppStrings;
import com.s.video.musicas.scooby.utils.MySharedpreferences;
import com.sinch.android.rtc.PushPair;
import com.sinch.android.rtc.SinchClient;
import com.sinch.android.rtc.SinchError;
import com.sinch.android.rtc.calling.Call;
import com.sinch.android.rtc.calling.CallClient;
import com.sinch.android.rtc.calling.CallClientListener;
import com.sinch.android.rtc.calling.CallListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;


public class PlayVieoActivity extends AppCompatActivity {

    private static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 0;
    private int MIC_PERMISSION = 1;

    // String[] permissions = {"android.permission.RECORD_AUDIO","android.permission.READ_PHONE_STATE"};

    Activity activity;
    YouTubePlayerView youtubeView;
    YouTubePlayer youTubePlayer;
    private String video;
    AbstractYouTubePlayerListener listener;

    public RecyclerView myRecylerView;
    public List<Message> MessageList = new ArrayList<>();
    public ChatBoxAdapter chatBoxAdapter;
    public EditText messagetxt;
    public ImageView send, imgSearch;
    private Socket socket;
    public String Nickname, video_title, pageID, groupId;

    ImageView imgMUteMic, btSetting, btShare, btAdd;
    Boolean isMuted = false;
    String value = "1";
    int settingValue = 0;
    int publicValue = 0;
    int count = 0;
    int playPause = 0;
    int isSeeked = 0;


    private static final String APP_KEY = "b0983781-06e8-427f-a5f8-fdbf91f52652";
    private static final String APP_SECRET = "HZe5trUWykuuY/2vefgW9g==";
    private static final String ENVIRONMENT = "sandbox.sinch.com";

    private Call call;
    private TextView callState;
    private SinchClient sinchClient;

    Context context;
    ArrayList<PrivacyModel> list;
    BottomSheetDialog dialog;
    String publicSatus = "0";
    String nearBy = "0";
    String frinds = "0";
    String lock = "0";

    Map<String, String> map = new HashMap<>();

    ImageView imgAdd;
    DrawerLayout drawer_layout;
    RecyclerView rvUserLIst;

    CompositeDisposable disposable = new CompositeDisposable();
    Api api = ApiClints.getClient().create(Api.class);

    TextView txtTotalCount, SettingTabInfo;

    List<LiveUserModel.Data> joinUserlist;

    @BindView(R.id.btMap)
    ImageView btMap;

    @BindView(R.id.SearchYouTube)
    TextView SearchYouTube;

    @BindView(R.id.BackPressedYouTubePlayer)
    ImageView BackPressed;


    ConstraintLayout SettingTab, InviteIcon, PublicIcon, SettingDownButton;
    float currentDuration=0;
/*
    @BindView(R.id.IconEarth)
    ImageView PublicIcon;
    @BindView(R.id.InviteIcon)
    ImageView InviteIcon;*/
   /* @BindView(R.id.SettingButtonDown)
    ImageView SettingButtonDown;
    @BindView(R.id.SettingTab)
    ImageView SettingTab;*/


    /*@BindView(R.id.gifMUteMic)
    GifImageView gifMUteMic;*/
    //YouTubePlayerFragment youtubeFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_vieo);
        ButterKnife.bind(this);
        youtubeView = findViewById(R.id.youtubeView);
        //getLifecycle().addObserver(youtubeView);
        imgMUteMic = findViewById(R.id.imgMUteMic);
        btSetting = findViewById(R.id.btSetting);
        imgSearch = findViewById(R.id.imgSearch);
        btShare = findViewById(R.id.btShare);
        btAdd = findViewById(R.id.btAdd);
        imgAdd = findViewById(R.id.imgAdd);
        drawer_layout = findViewById(R.id.drawer_layout);
        rvUserLIst = findViewById(R.id.rvUserLIstt);
        txtTotalCount = findViewById(R.id.txtTotalCount);

        SettingTab = findViewById(R.id.SettingTab);
        InviteIcon = findViewById(R.id.InviteIcon);
        PublicIcon = findViewById(R.id.PublicIcon);
        SettingDownButton = findViewById(R.id.SettingButtonDown);
        SettingTabInfo = findViewById(R.id.SettingTabInfo);

        imgAdd.setOnClickListener(v -> openDrawer());
        btSetting.setOnClickListener(v -> openSettingTab());
        imgSearch.setOnClickListener(v -> openDashboard());

        joinUserlist = new ArrayList<>();
        video = getIntent().getStringExtra("video");
        video_title = getIntent().getStringExtra("video_title");
        pageID = getIntent().getStringExtra("pageID");
        groupId = getIntent().getStringExtra("groupID");
        BackPressed.setOnClickListener(v -> onBackPressed());


        MySharedpreferences.getInstance().save(this, AppStrings.VIDEO_ID, video);

        send = findViewById(R.id.btSend);
        messagetxt = findViewById(R.id.messagetxt);

        btMap.setOnClickListener(v -> openMap());
        btAdd.setOnClickListener(v -> openInviteFriends());
        SettingDownButton.setOnClickListener(v -> SettingTabInvisible());

        activity = this;

        InviteIcon.setAlpha(0.4f);
        InviteIcon.setOnClickListener(v -> InviteClick());
        PublicIcon.setOnClickListener(v -> PublicClick());


        if (MySharedpreferences.getInstance().get(activity, AppStrings.loginType).equalsIgnoreCase("guest")) {
            Nickname = "Guest Login";
        } else {
            Nickname = MySharedpreferences.getInstance().get(activity, AppStrings.name);
        }


        // next video from youtube by salman

        SearchYouTube.setOnClickListener(v -> openYouTube());


        btShare.setOnClickListener(v -> shareCode());
        /* microPhone();*/
        map.put("id", video);

/*
        sinchClient = Sinch.getSinchClientBuilder()
                .context(this)
                .userId(video)
                .applicationKey(APP_KEY)
                .applicationSecret(APP_SECRET)
                .environmentHost(ENVIRONMENT)
                .build();

        sinchClient.setSupportCalling(true);
        sinchClient.startListeningOnActiveConnection();
        sinchClient.start();
        sinchClient.getCallClient().addCallClientListener(new SinchCallClientListener());*/

        Glide.with(this).load(R.drawable.ic_baseline_mic_off_24).into(imgMUteMic);

        imgMUteMic.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {

                Toast.makeText(activity, "VoIP Coming Soon", Toast.LENGTH_SHORT).show();
/*

                if (ContextCompat.checkSelfPermission(PlayVieoActivity.this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {


                   */
                /* setMicOn();*//*


                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            AudioManager audioManager = (AudioManager) activity.getSystemService(Context.AUDIO_SERVICE);
                            audioManager.setSpeakerphoneOn(true);


                            //mic image change code here

                            if (value.equals("1")) {
                                Glide.with(activity).load(R.drawable.ic_baseline_mic_24).into(imgMUteMic);
                                value = "2";
                                call = sinchClient.getCallClient().callConference("id", map);
                                call.addCallListener(new SinchCallListener());
                                audioManager.setMicrophoneMute(false);

                                */
                /*microPhone();*//*


                            } else {
                               */
                /* microPhone();*//*


                                audioManager.setMicrophoneMute(true);
                                value = "1";
                                Glide.with(activity).load(R.drawable.ic_baseline_mic_off_24).into(imgMUteMic);
                                call.hangup();

                            }

                        }


                    }, 1);
                } else {


                   */
                /* RequestMicPermission();*//*

                }
*/


                /*setMicOn();*/
            }
        });

        try {
            socket = IO.socket("http://52.207.96.115:3000/");
            socket.connect();
            if(groupId==null){
                groupId = System.currentTimeMillis()+video.toString();
            }
            socket.emit("join", Nickname, video, video_title, MySharedpreferences.getInstance().get(activity, AppStrings.userID),
                    "Public", groupId);
            socket.emit("messagedetection", Nickname, Nickname + " : Join", MySharedpreferences.getInstance().get(PlayVieoActivity.this, AppStrings.userID), video, "", MySharedpreferences.getInstance().get(activity, AppStrings.image), groupId);
            getUserList();
        } catch (URISyntaxException e) {
            e.printStackTrace();

        }

        myRecylerView = (RecyclerView) findViewById(R.id.myRecylerView);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        myRecylerView.setLayoutManager(mLayoutManager);
        chatBoxAdapter = new ChatBoxAdapter(activity, MessageList, MySharedpreferences.getInstance().get(this, AppStrings.userID));
        myRecylerView.setAdapter(chatBoxAdapter);


        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //retrieve the nickname and the message content and fire the event messagedetection
                DateFormat dateFormat = new SimpleDateFormat("hh:mm a");
                String formattedDate = dateFormat.format(new Date());


                if (!messagetxt.getText().toString().trim().isEmpty()) {
                    socket.emit("messagedetection", Nickname, messagetxt.getText().toString(), MySharedpreferences.getInstance().get(PlayVieoActivity.this, AppStrings.userID), video, formattedDate, MySharedpreferences.getInstance().get(activity, AppStrings.image), groupId);
                    messagetxt.setText(" ");
                }
            }
        });


        socket.on("userjoinedthechat", new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //String data = (String) args[0];

                        String json = String.valueOf(args[0]);
                        //      {"user":"Guest Login","message":"has joined this room "}

                        try {
                            JSONObject jsonObject = new JSONObject(json);
                            String msz = jsonObject.getString("user");
                            String message = jsonObject.getString("message");


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        getUserList();

                    }
                });
            }
        });
        socket.on("userdisconnect", new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String json = String.valueOf(args[0]);
                        //      {"user":"Guest Login","message":"has joined this room "}

                        try {
                            getUserList();
                            JSONObject jsonObject = new JSONObject(json);
                            String msz = jsonObject.getString("user");
                            String message = jsonObject.getString("message");
                            if (jsonObject.getString("status").equalsIgnoreCase("Admin")) {
                                // jsonObject.getString("status").replaceAll("user","Admin");

                                //  Toast.makeText(activity, "Admin closed group", Toast.LENGTH_SHORT).show();
                            } else {

                                /* Toast.makeText(activity, msz + " " + message, Toast.LENGTH_SHORT).show();*/
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();

                        }

                    }
                });
            }
        });

        socket.on("message", new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {


                        String json = String.valueOf(args[0]);
                        JSONObject jsonObject = null;

                        try {
                            jsonObject = new JSONObject(json);

                            String nickname = null;
                            String message = null;
                            Message m = null;

                            nickname = jsonObject.getString("senderNickname");
                            message = jsonObject.getString("message");
                            String user_id = jsonObject.getString("user_id");


                            m = new Message(nickname, message, jsonObject.getString("Time"), user_id, jsonObject.getString("image"));
                            MessageList.add(m);
                            getUserList();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                        chatBoxAdapter.notifyItemChanged(MessageList.size() - 1);
                        myRecylerView.scrollToPosition(chatBoxAdapter.getItemCount() - 1);

                    }
                });
            }
        });
    }

    private void openDashboard() {
        Intent intent = new Intent(this, NextVideoYouTube.class);
        intent.putExtra("changeVideo", true);
        startActivityForResult(intent, 101);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==101 && resultCode==RESULT_OK){
            video = data.getStringExtra("video");
            video_title = data.getStringExtra("video_title");
            youTubePlayer.loadVideo(video, 0);
            socket.emit("join", Nickname, video, video_title, MySharedpreferences.getInstance().get(activity, AppStrings.userID),
                    "Public", groupId);

        }
    }

    //new Mic permission imported by salman.


    private void openYouTube() {
        Intent intent = new Intent(this, NextVideoYouTube.class);
        startActivity(intent);

    }

   /* private void RequestMicPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.RECORD_AUDIO)) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, MIC_PERMISSION);



            *//*new AlertDialog.Builder(this)
                    .setTitle("Permission Required")
                    .setMessage("Allow Permission to Voice talk")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ActivityCompat.requestPermissions(PlayVieoActivity.this,new String[]
                                    {Manifest.permission.RECORD_AUDIO},MIC_PERMISSION);
                        }
                    })


                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int which) {
                            dialogInterface.dismiss();
                        }
                    })
                    .create().show();*//*

        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, MIC_PERMISSION);
        }
    }
*/

   /* @Override
    public void onRequestPermissionsResult(int requestCode, @androidx.annotation.NonNull String[] permissions, @androidx.annotation.NonNull int[] grantResults) {
        if (requestCode == MIC_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                *//*setMicOn();*//*

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        //mic image change code here

                        if (value.equals("1")) {
                            Glide.with(activity).load(R.drawable.ic_baseline_mic_24).into(imgMUteMic);
                            value = "2";
                            call = sinchClient.getCallClient().callConference("id", map);
                            call.addCallListener(new SinchCallListener());
                            microPhone();
                        } else {
                            microPhone();
                            value = "1";
                            Glide.with(activity).load(R.drawable.ic_baseline_mic_off_24).into(imgMUteMic);
                            call.hangup();

                        }

                    }


                }, 1);
            } else {
                Toast.makeText(this, "Permission DENIED", Toast.LENGTH_SHORT).show();
            }

        }
    }
*/
  /*  public void setMicOn() {


        //mic image change code here

        if (value.equals("100")) {
            Glide.with(activity).load(R.drawable.ic_baseline_mic_24).into(imgMUteMic);
            value = "200";
            call = sinchClient.getCallClient().callConference("id", map);
            call.addCallListener(new SinchCallListener());
            microPhone();
        } else {
            value = "100";
            Glide.with(activity).load(R.drawable.ic_baseline_mic_off_24).into(imgMUteMic);
            call.hangup();
            microPhone();


        }

    }*/

   /* private void microPhone() {

        AudioManager audioManager = (AudioManager) activity.getSystemService(Context.AUDIO_SERVICE);
        audioManager.setSpeakerphoneOn(true);
        if (!isMuted) {
            audioManager.setMicrophoneMute(true);
            isMuted = true;

        } else {
            audioManager.setMicrophoneMute(false);
            isMuted = false;
        }
    }*/






   /* private void openSettings() {
        Intent intent = new Intent();
        intent.setAction(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package",
                BuildConfig.APPLICATION_ID, null);
        intent.setData(uri);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }*/


// permission imported till here.

    private void openDrawer() {
        String value = "1";
        if (value.equalsIgnoreCase("1")) {
            drawer_layout.openDrawer(GravityCompat.END);
            value = "2";
        } else {
            drawer_layout.openDrawer(GravityCompat.START);
            value = "1";
        }
    }

    private void playVideo(String timerValue, float time) {
        youtubeView.enableBackgroundPlayback(true);

        listener = new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@androidx.annotation.NonNull com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer uTubePlayer) {
                youTubePlayer = uTubePlayer;
                if(timerValue.equalsIgnoreCase("stop")){
                    youTubePlayer.loadVideo(video, time);
                }else {
                    youTubePlayer.loadVideo(video, 0);
                }
                if (timerValue.equalsIgnoreCase("start"))
                    handler(youTubePlayer);
                if(pageID.equals("1")){
                    getUserList();
                }
            }

            @Override
            public void onCurrentSecond(@androidx.annotation.NonNull YouTubePlayer youTubePlayer, float second) {
                super.onCurrentSecond(youTubePlayer, second);
                currentDuration = second;
            }

            @Override
            public void onStateChange(@androidx.annotation.NonNull YouTubePlayer youTubePlayer,
                                      @androidx.annotation.NonNull PlayerConstants.PlayerState state) {
                super.onStateChange(youTubePlayer, state);
                if(state == PlayerConstants.PlayerState.PAUSED){
                    if(pageID.equals("2")){
                        playPause =0;
                        MySharedpreferences.getInstance().save(PlayVieoActivity.this,AppStrings.VIDEOPLAYSTATUS,currentDuration+"");
                    }else {
                        if(playPause==1){
                            youTubePlayer.play();
                        }
                    }
                }else if(state == PlayerConstants.PlayerState.PLAYING){
                    if(pageID.equals("2")){
                        playPause=1;
                        MySharedpreferences.getInstance().save(PlayVieoActivity.this,AppStrings.VIDEOPLAYSTATUS,currentDuration+"");
                    }
                }
            }
        };

        youtubeView.addYouTubePlayerListener(listener);
        if(pageID.equals("2")){
            youtubeView.getPlayerUiController().setCustomAction1(getDrawable(R.drawable.exo_icon_rewind), new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    isSeeked = 1;
                    youTubePlayer.seekTo(currentDuration-5);
                }
            });
            youtubeView.getPlayerUiController().setCustomAction2(getDrawable(R.drawable.exo_icon_fastforward), new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    isSeeked = 1;
                    youTubePlayer.seekTo(currentDuration+5);

                }
            });

            youtubeView.getPlayerUiController().showCustomAction1(true);
            youtubeView.getPlayerUiController().showCustomAction2(true);
        }else {
            handler1();
        }


    }



    private void conentionClose() {
        socket.emit("leave", Nickname, video, MySharedpreferences.getInstance().get(activity, AppStrings.userID), "Admin");
        socket.emit("leave", Nickname, video, MySharedpreferences.getInstance().get(activity, AppStrings.userID), "user");

    }

    private class SinchCallListener implements CallListener {
        @Override
        public void onCallEnded(Call endedCall) {
            call = null;
            SinchError a = endedCall.getDetails().getError();
            setVolumeControlStream(AudioManager.USE_DEFAULT_STREAM_TYPE);
        }

        @Override
        public void onCallEstablished(Call establishedCall) {

            /* setVolumeControlStream(AudioManager.STREAM_VOICE_CALL);*/
        }

        @Override
        public void onCallProgressing(Call progressingCall) {
        }

        @Override
        public void onShouldSendPushNotification(Call call, List<PushPair> pushPairs) {
        }
    }

    private class SinchCallClientListener implements CallClientListener {
        @Override
        public void onIncomingCall(CallClient callClient, Call incomingCall) {
            call = incomingCall;
            call.answer();
            call.addCallListener(new SinchCallListener());
        }
    }

    private void initKeyBoardListener() {
        final int MIN_KEYBOARD_HEIGHT_PX = 150;
        final View decorView = getWindow().getDecorView();
        decorView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            private final Rect windowVisibleDisplayFrame = new Rect();
            private int lastVisibleDecorViewHeight;

            @Override
            public void onGlobalLayout() {
                decorView.getWindowVisibleDisplayFrame(windowVisibleDisplayFrame);
                final int visibleDecorViewHeight = windowVisibleDisplayFrame.height();

                if (lastVisibleDecorViewHeight != 0) {
                    if (lastVisibleDecorViewHeight > visibleDecorViewHeight + MIN_KEYBOARD_HEIGHT_PX) {
                        Log.d("Pasha", "SHOW");
                        send.setVisibility(View.VISIBLE);
                        btSetting.setVisibility(View.GONE);
                        btShare.setVisibility(View.GONE);
                        btAdd.setVisibility(View.GONE);
                        btMap.setVisibility(View.GONE);
                        myRecylerView.scrollToPosition(chatBoxAdapter.getItemCount() - 1);
                    } else if (lastVisibleDecorViewHeight + MIN_KEYBOARD_HEIGHT_PX < visibleDecorViewHeight) {
                        Log.d("Pasha", "HIDE");
                        send.setVisibility(View.VISIBLE);
                        btSetting.setVisibility(View.VISIBLE);
                        btShare.setVisibility(View.GONE);
                        btAdd.setVisibility(View.VISIBLE);
                        btMap.setVisibility(View.GONE);


                    }
                }
                lastVisibleDecorViewHeight = visibleDecorViewHeight;
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        initKeyBoardListener();
        send.setVisibility(View.VISIBLE);
        getUserList();

        if (pageID.equalsIgnoreCase("2")) {
            playVideo("start", 0);
            MySharedpreferences.getInstance().save(PlayVieoActivity.this, AppStrings.CHECKSETTINGSTATUS, "1");
        } else {
            MySharedpreferences.getInstance().save(PlayVieoActivity.this, AppStrings.CHECKSETTINGSTATUS, "2");
            getGroupList(1);
        }

        // getUserCheck();
        //      playVideo();
    }


    private void shareCode() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, video_title + "\n" + "" + "https://play.google.com/store/apps/details?id=" + getPackageName());
        intent.putExtra(Intent.EXTRA_SUBJECT, "Code");
        startActivity(intent);
    }


    @Override
    protected void onStop() {
        super.onStop();
        if (call != null) {
            call.hangup();
        }
    }


    private void getUserList() {
        api.getUserList(video).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new SingleObserver<LiveUserModel>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                    }

                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onSuccess(@NonNull LiveUserModel liveUserModel) {

                        if (liveUserModel.getStatus().equalsIgnoreCase("success")) {
                            joinUserlist = new ArrayList<>();
                            joinUserlist = liveUserModel.getData();
                            LinearLayoutManager mLayoutManager = new LinearLayoutManager(activity);
                            rvUserLIst.setLayoutManager(mLayoutManager);
                            UserListAdapter subCatDetailsAdapter = new UserListAdapter(activity, liveUserModel.getData());
                            rvUserLIst.setAdapter(subCatDetailsAdapter);
                            txtTotalCount.setText(liveUserModel.getData().size() + "");
                            count = liveUserModel.getData().size();

                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }
                });
    }


    private void openSettingTab() {

        if (settingValue == 0) {
            SettingTab.setVisibility(View.VISIBLE);
            settingValue = 1;
            myRecylerView.scrollToPosition(chatBoxAdapter.getItemCount() - 1);
        } else {
            SettingTab.setVisibility(View.GONE);
            settingValue = 0;
        }

    }

    private void SettingTabInvisible() {
        SettingTab.setVisibility(View.GONE);
        settingValue = 0;
    }


   /* private void SettingTab(){

        if (PublicValue==0) {
            PublicIcon.setAlpha(0.5f);
            PublicValue = 1;
        }
        else {
            InviteIcon.setAlpha(0.5f);
            PublicValue=0;
        }

    }*/
/*

    private void Checkprivacy() {
        if (MySharedpreferences.getInstance().get(context,AppStrings.CHECKSETTINGSTATUS) != null){
            if (MySharedpreferences.getInstance().get(context,AppStrings.CHECKSETTINGSTATUS).equalsIgnoreCase("1")){
                if (publicValue==0) {
                    publicSatus = "1";
                }
                else if (publicValue==1){
                    lock="1";
                }
            }
        }


        sendStatus();
    }


    private void sendStatus(){
        StringRequest stringRequest=new StringRequest(Request.Method.POST, "http://52.207.96.115/index.php/App/update_privacy", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d("TAG", "onResponse: "+response);
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    Toast.makeText(context, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    dialog.getPrivacyStatus();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.d("TAG", "onErrorResponse: "+error.getMessage());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String >map=new HashMap<>();
                map.put("vedio_id", MySharedpreferences.getInstance().get(context, AppStrings.VIDEO_ID));
                map.put("user_id",MySharedpreferences.getInstance().get(context,AppStrings.userID));
                map.put("public_status", publicSatus);
                map.put("frnd_status",frinds);
                map.put("nearby_status", nearBy);
                map.put("invite_status",lock);
                return map;
            }
        };
        Volley.newRequestQueue(context).add(stringRequest);
    }
*/

    private void InviteClick() {

        if (pageID.equals("2")) {
            PublicIcon.setAlpha(0.4f);
            InviteIcon.setAlpha(1f);
            SettingTabInfo.setText("Only Invited can join");
            //  publicValue = 1;
            // Checkprivacy();

        } else {
            Toast.makeText(activity, "Only Admin can change setting", Toast.LENGTH_SHORT).show();
        }
    }

    private void PublicClick() {

        if (pageID.equals("2")) {
            PublicIcon.setAlpha(1f);
            InviteIcon.setAlpha(0.4f);
            SettingTabInfo.setText("Anyone can join");
            //  publicValue = 0;
            // Checkprivacy();
        } else {
            Toast.makeText(activity, "Only Admin can change setting", Toast.LENGTH_SHORT).show();
        }
    }



/*
    private void openSheet() {
        BottomSheetDialog bottomSheet = new BottomSheetDialog();
        bottomSheet.show(getSupportFragmentManager(), "ModalBottomSheet");
    }*/

    private void openMap() {

        if (joinUserlist.isEmpty()) {
            Toast.makeText(activity, "No location found", Toast.LENGTH_SHORT).show();
        } else {
            Intent intent = new Intent(PlayVieoActivity.this, MapActivity.class);
            intent.putExtra("joinUserlist", (Serializable) joinUserlist);
            startActivity(intent);

        }

    }


    private void openInviteFriends() {
        Intent intent = new Intent(PlayVieoActivity.this, InviteFriendsActivity.class);
        intent.putExtra("link", groupId);
        intent.putExtra("video_id", video);
        intent.putExtra("video_title", video_title);
        startActivity(intent);
    }

    private void handler(YouTubePlayer youTubePlayer) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    long millis = (long) currentDuration;
                    value = String.valueOf(millis);
                    socket.emit("time", video, currentDuration, playPause, groupId, isSeeked);
                    isSeeked=0;
                    handler(youTubePlayer);

                } catch (Exception e) {
                    e.getMessage();

                }

            }
        }, 1000);
    }

    private void handler1(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    getGroupList(0);
                    handler1();
                } catch (Exception e) {
                    e.getMessage();

                }

            }
        }, 1000);

    }

    private void getGroupList(int play) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://52.207.96.115:3000/group_time", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("getGroupList", "onResponse: " + response);
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        if (object.has(groupId)) {
                            JSONObject group = object.getJSONObject(groupId);
                            float time = group.getInt("time");
                            int playPause = group.getInt("play_pause");
                            int isSeeked = group.getInt("back_forword");
                            if(play==1){
                                playVideo("stop", time);
                            }
                            if(youTubePlayer!=null){
                                if(isSeeked==1){
                                    youTubePlayer.seekTo(time+2);
                                }
                            }
                            if(playPause==1){
                                if(youTubePlayer!=null){
                                    youTubePlayer.play();
                                }
                            }else {
                                if(youTubePlayer!=null){
                                    youTubePlayer.pause();
                                }
                            }
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("TAG", "onResponse: " + error.getMessage());
            }
        });
        Volley.newRequestQueue(this).add(stringRequest);
    }


    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this, R.style.MyDialogTheme)
                .setTitle("Leaving the room")
                .setMessage("Are you sure you want to leave this room?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
                        conentionClose();
                        if (pageID.equalsIgnoreCase("2")) {
                            Toast.makeText(activity, "You closed the group", Toast.LENGTH_SHORT).show();
                            /* youtubeFragment.onDestroy();*/
                        }
                        if (pageID.equals("2")) {
                            /*youtubeFragment.onDestroy();*/
                            socket.emit("messagedetection", Nickname, Nickname + " : Admin Left",
                                    MySharedpreferences.getInstance().get(PlayVieoActivity.this, AppStrings.userID), video, "", MySharedpreferences.getInstance().get(activity, AppStrings.image), groupId);
                            Intent intent = new Intent(PlayVieoActivity.this, LiveChatRoomLIstActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            socket.emit("messagedetection", Nickname, Nickname + " : Left", MySharedpreferences.getInstance().get(PlayVieoActivity.this, AppStrings.userID), video, "", MySharedpreferences.getInstance().get(activity, AppStrings.image), groupId);
                            Intent intent = new Intent(PlayVieoActivity.this, LiveChatRoomLIstActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        PlayVieoActivity.super.onBackPressed();
                    }
                }).create().show();


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        youTubePlayer.removeListener(listener);
        youtubeView.release();
    }
}