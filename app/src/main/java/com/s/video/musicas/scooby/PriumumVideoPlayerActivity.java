package com.s.video.musicas.scooby;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.s.video.musicas.scooby.Map.MapActivity;
import com.s.video.musicas.scooby.adapter.BottomSheetDialog;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.potyvideo.library.AndExoPlayerView;
import com.s.video.musicas.scooby.adapter.UserListAdapter;
import com.s.video.musicas.scooby.nettwork.model.LiveUserModel;
import com.sinch.android.rtc.PushPair;
import com.sinch.android.rtc.Sinch;
import com.sinch.android.rtc.SinchClient;
import com.sinch.android.rtc.SinchError;
import com.sinch.android.rtc.calling.Call;
import com.sinch.android.rtc.calling.CallClient;
import com.sinch.android.rtc.calling.CallClientListener;
import com.sinch.android.rtc.calling.CallListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

import com.s.video.musicas.scooby.ChatModel.Message;
import com.s.video.musicas.scooby.adapter.ChatBoxAdapter;
import com.s.video.musicas.scooby.nettwork.Api;
import com.s.video.musicas.scooby.nettwork.ApiClints;
import com.s.video.musicas.scooby.nettwork.model.VideoLikeModel;
import com.s.video.musicas.scooby.nettwork.model.getLikeStatusModel;
import com.s.video.musicas.scooby.utils.AppStrings;
import com.s.video.musicas.scooby.utils.MySharedpreferences;


public class PriumumVideoPlayerActivity extends AppCompatActivity {

    String path;
    @BindView(R.id.video_view)
    AndExoPlayerView andExoPlayerView;

    @BindView(R.id.BackPressedPremium)
    ImageView BackPressed;

    @BindView(R.id.constLayoutTwo)
    ConstraintLayout constLayoutTwo;

    Activity activity;
 //   private String video;

    public RecyclerView myRecylerView;
    public List<Message> MessageList = new ArrayList<>();
    public ChatBoxAdapter chatBoxAdapter;
    public EditText messagetxt;
    public ImageView send;
    private Socket socket;
    public String Nickname, video_title, pageID;

    ImageView imgMUteMic;
    Boolean isMuted = false;
    String value = "1";


    private static final String APP_KEY = "b0983781-06e8-427f-a5f8-fdbf91f52652";
    private static final String APP_SECRET = "HZe5trUWykuuY/2vefgW9g==";
    private static final String ENVIRONMENT = "sandbox.sinch.com";

    private Call call;
    private TextView callState;
    private SinchClient sinchClient;

    private String likeDislike = "1";



    Handler mHandler;
    ImageView imgAdd;
    DrawerLayout drawer_layout;
    RecyclerView rvUserLIstt;
    TextView txtTotalCount;
    List<LiveUserModel.Data> joinUserlist;

    Map<String, String> map = new HashMap<>();
    ImageView btSetting, btShare, btAdd;
    CompositeDisposable disposable = new CompositeDisposable();
    Api api = ApiClints.getClient().create(Api.class);

    @BindView(R.id.txtLike)
    TextView txtLike;

    @BindView(R.id.txtDislike)
    TextView txtDislike;

    @BindView(R.id.imgLike)
    ImageView imgLike;

    @BindView(R.id.imgDislike)
    ImageView imgDislike;

    @BindView(R.id.imgSettingVideio)
    ImageView imgSettingVideio;

    String video_id;
    @BindView(R.id.progress_circular)
    ProgressBar progress_circular;

    @BindView(R.id.imgShare)
    ImageView imgShare;

    @BindView(R.id.btMap)
    ImageView btMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_priumum_video_player);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        ButterKnife.bind(this);
        activity = this;
        imgMUteMic = findViewById(R.id.imgMUteMic);
        btSetting = findViewById(R.id.btSetting);
        btShare = findViewById(R.id.btShare);
        btAdd = findViewById(R.id.btAdd);

        imgShare.setOnClickListener(v -> shareCode());
        btShare.setOnClickListener(v -> shareCode());

        btSetting.setOnClickListener(v -> openSheet());

        btMap.setOnClickListener(v -> openMap());

        imgAdd = findViewById(R.id.imgAdd);
        drawer_layout = findViewById(R.id.drawer_layout);
        rvUserLIstt = findViewById(R.id.rvUserLIstt);
        txtTotalCount = findViewById(R.id.txtTotalCount);
        imgAdd.setOnClickListener(v -> openDrawer());
        joinUserlist = new ArrayList<>();
        BackPressed.setOnClickListener(v -> onBackPressed());


        pageID = getIntent().getStringExtra("pageID");
        video_id = getIntent().getStringExtra("video_id");
        video_title=getIntent().getStringExtra("video_title");
        send = findViewById(R.id.btSend);
        messagetxt = findViewById(R.id.messagetxt);

        btAdd.setOnClickListener(v -> openInviteFriends());
        imgSettingVideio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMenu(v,"1");
            }
        });


        getUserList();


        if (MySharedpreferences.getInstance().get(activity, AppStrings.loginType).equalsIgnoreCase("guest")) {
            Nickname = "Guest Login";
        } else {
            Nickname = MySharedpreferences.getInstance().get(activity, AppStrings.name);
        }


        map.put("id", video_id);


       /* sinchClient = Sinch.getSinchClientBuilder()
                .context(this)
                .userId(video_id)
                .applicationKey(APP_KEY)
                .applicationSecret(APP_SECRET)
                .environmentHost(ENVIRONMENT)
                .build();

        sinchClient.setSupportCalling(true);
        sinchClient.startListeningOnActiveConnection();
        sinchClient.start();

        sinchClient.getCallClient().addCallClientListener(new SinchCallClientListener());*/


        getLikeStatus();

        imgLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                likeDislike = "2";
                likeDislike("like");
                Glide.with(PriumumVideoPlayerActivity.this).load(R.drawable.ic_baseline_thumb_down_alt_24).into(imgDislike);
                Glide.with(PriumumVideoPlayerActivity.this).load(R.drawable.ic_baseline_thumb_up_24_blavk).into(imgLike);
            }
        });


        imgDislike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                likeDislike = "1";
                likeDislike("dislike");
                Glide.with(PriumumVideoPlayerActivity.this).load(R.drawable.ic_baseline_thumb_down_alt_24_black).into(imgDislike);
                Glide.with(PriumumVideoPlayerActivity.this).load(R.drawable.ic_baseline_thumb_up_24).into(imgLike);

            }
        });



       /* mute();*/


        Glide.with(this).load(R.drawable.ic_baseline_mic_off_24).into(imgMUteMic);

       /* imgMUteMic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (value.equals("1")) {
                    Glide.with(activity).load(R.drawable.ic_baseline_mic_24).into(imgMUteMic);
                    value = "2";
                    mute();
                } else {
                    mute();
                    value = "1";
                    Glide.with(activity).load(R.drawable.ic_baseline_mic_off_24).into(imgMUteMic);

                }
            }
        });*/
        try {
            socket = IO.socket("http://52.207.96.115:3000/");
            socket.connect();
            socket.emit("join", Nickname, video_id, video_title,MySharedpreferences.getInstance().get(activity, AppStrings.userID),"Private");
            socket.emit("messagedetection", Nickname, Nickname + " : Join", MySharedpreferences.getInstance().get(PriumumVideoPlayerActivity.this, AppStrings.userID), video_id, "", MySharedpreferences.getInstance().get(activity, AppStrings.image));
            getUserList();

            //     socket.emit("senderId", MySharedpreferences.getInstance().get(this,AppStrings.userID));
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
                    socket.emit("messagedetection", Nickname, messagetxt.getText().toString(), MySharedpreferences.getInstance().get(PriumumVideoPlayerActivity.this, AppStrings.userID), video_id, formattedDate, MySharedpreferences.getInstance().get(activity, AppStrings.image));
                    messagetxt.setText(" ");
                    getUserList();
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
                            getUserList();
                           /* Toast.makeText(activity, msz + " " + message, Toast.LENGTH_SHORT).show();*/
                        } catch (JSONException e) {
                            e.printStackTrace();
                            getUserList();
                        }


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
                            JSONObject jsonObject = new JSONObject(json);
                            String msz = jsonObject.getString("user");
                            String message = jsonObject.getString("message");
                            getUserList();
                           /* Toast.makeText(activity, msz + " " + message, Toast.LENGTH_SHORT).show();*/
                        } catch (JSONException e) {
                            e.printStackTrace();
                            getUserList();

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


                            Log.d("TAG", "run: "+nickname);
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


    private final Runnable m_Runnable = new Runnable() {
        public void run() {
            PriumumVideoPlayerActivity.this.mHandler.postDelayed(m_Runnable, 1000);
            getUserList();
        }

    };

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

   /* @Override
    public void onBackPressed() {
        conentionClose();
        andExoPlayerView.stopPlayer();
        super.onBackPressed();

    }*/

    private void conentionClose() {

        socket.emit("leave", Nickname, video_id, MySharedpreferences.getInstance().get(activity, AppStrings.userID),"Admin");
        socket.emit("leave", Nickname, video_id, MySharedpreferences.getInstance().get(activity, AppStrings.userID),"user");


       // socket.emit("leave", Nickname, video);
       /* if (pageID.equalsIgnoreCase("2")){
            socket.emit("leave", Nickname, video_id, MySharedpreferences.getInstance().get(activity, AppStrings.userID),"Admin");
        }else {
            socket.emit("leave", Nickname, video_id, MySharedpreferences.getInstance().get(activity, AppStrings.userID),"user");
        }*/
    }


  /*  private class SinchCallListener implements CallListener {
        @Override
        public void onCallEnded(Call endedCall) {
            call = null;
            SinchError a = endedCall.getDetails().getError();
            //   setVolumeControlStream(AudioManager.USE_DEFAULT_STREAM_TYPE);
        }

        @Override
        public void onCallEstablished(Call establishedCall) {
            // setVolumeControlStream(AudioManager.STREAM_VOICE_CALL);
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
*/

    private void likeDislike(String dislike) {
        api.sendLikeDislike(MySharedpreferences.getInstance().get(this, AppStrings.userID), video_id, dislike)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<VideoLikeModel>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onSuccess(@NonNull VideoLikeModel videoLikeModel) {

                        getLikeStatus();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {


                    }
                });
    }


    private void getLikeStatus() {
        api.getLikeStatus(MySharedpreferences.getInstance().get(this, AppStrings.userID), video_id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new SingleObserver<getLikeStatusModel>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onSuccess(@NonNull getLikeStatusModel getLikeStatusModel) {
                        if (getLikeStatusModel.getStatus().equals("success")) {


                            try {
                                txtLike.setText(getLikeStatusModel.getData().getTotal_like() + "");
                            } catch (Exception e) {
                                e.getMessage();
                            }

                            try {
                                txtDislike.setText(getLikeStatusModel.getData().getTotal_dislike() + "");
                            } catch (Exception e) {
                                e.getMessage();
                            }

                        }

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }
                });
    }

    private void shareCode() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, video_title + "\n" + "" + "https://play.google.com/store/apps/details?id=" + getPackageName());
        intent.putExtra(Intent.EXTRA_SUBJECT, "Code");
        startActivity(intent);
    }

/*
    @Override
    protected void onStop() {
        super.onStop();
        if (call != null) {
            call.hangup();
        }
    }*/

    @Override
    protected void onResume() {
        super.onResume();
        initKeyBoardListener();
        send.setVisibility(View.VISIBLE);
        getUserList();

        /*if (sinchClient.isStarted()) {
            if (call == null) {
                call = sinchClient.getCallClient().callConference("id", map);
                call.addCallListener(new SinchCallListener());
            } else {
                call.hangup();
            }
        }*/


        if (pageID.equalsIgnoreCase("2")) {
            playVideo("start",0);
            MySharedpreferences.getInstance().save(PriumumVideoPlayerActivity.this,AppStrings.CHECKSETTINGSTATUS,"1");

        } else {
            MySharedpreferences.getInstance().save(PriumumVideoPlayerActivity.this,AppStrings.CHECKSETTINGSTATUS,"2");

            getGroupList();
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
                        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) andExoPlayerView.getLayoutParams();
                        params.verticalBias = 0f; // here is one modification for example. modify anything else you want :)
                        andExoPlayerView.setLayoutParams(params); // request the view to use the new modified params


                    } else if (lastVisibleDecorViewHeight + MIN_KEYBOARD_HEIGHT_PX < visibleDecorViewHeight) {
                        Log.d("Pasha", "HIDE");
                        send.setVisibility(View.VISIBLE);
                        btSetting.setVisibility(View.GONE);
                        btShare.setVisibility(View.GONE);
                        btAdd.setVisibility(View.GONE);
                        btMap.setVisibility(View.GONE);
                        myRecylerView.scrollToPosition(chatBoxAdapter.getItemCount() - 1);
                        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) andExoPlayerView.getLayoutParams();
                        params.verticalBias = 0.1f;// here is one modification for example. modify anything else you want :)
                        andExoPlayerView.setLayoutParams(params); // request the view to use the new modified params


                    }
                }
                // Сохраняем текущую высоту view до следующего вызова.
                // Save current decor view height for the next call.
                lastVisibleDecorViewHeight = visibleDecorViewHeight;
            }
        });
    }
/*

    private void mute() {
        AudioManager audioManager = (AudioManager) activity.getSystemService(Context.AUDIO_SERVICE);
        audioManager.setSpeakerphoneOn(true);
        if (!isMuted) {
            audioManager.setMicrophoneMute(true);
            isMuted = true;
        } else {
            audioManager.setMicrophoneMute(false);
            isMuted = false;
        }
    }
*/


    private void openMenu(View view,String id){
        PopupMenu popup = new PopupMenu(PriumumVideoPlayerActivity.this,view);
        //inflating menu from xml resource
        popup.inflate(R.menu.action_bar);
        //adding click listener
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.seven:
                        andExoPlayerView.pausePlayer();
                        progress_circular.setVisibility(View.VISIBLE);


                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                progress_circular.setVisibility(View.GONE);
                               // andExoPlayerView.
                                andExoPlayerView.setPlayWhenReady(true);
                            }
                        },2000);

                        break;

                    case R.id.one:
                        andExoPlayerView.pausePlayer();
                        progress_circular.setVisibility(View.VISIBLE);

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                progress_circular.setVisibility(View.GONE);
                                andExoPlayerView.setPlayWhenReady(true);

                            }
                        },2000);
                        break;
                }
                return false;
            }
        });
        popup.show();
    }


    private void openSheet() {
        BottomSheetDialog bottomSheet = new BottomSheetDialog();
        bottomSheet.show(getSupportFragmentManager(), "ModalBottomSheet");

    }


    private void openMap() {
        Intent intent=new Intent(PriumumVideoPlayerActivity.this, MapActivity.class);
        startActivity(intent);
    }



    private void getGroupList() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://52.207.96.115:3000/group_time", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        if (object.has(video_id)) {
                            playVideo("stop", Integer.parseInt(object.getString(video_id)));
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        Volley.newRequestQueue(this).add(stringRequest);
    }

    private void getUserList() {
        api.getUserList(video_id).observeOn(AndroidSchedulers.mainThread())
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
                            rvUserLIstt.setLayoutManager(mLayoutManager);
                            UserListAdapter subCatDetailsAdapter = new UserListAdapter(activity, liveUserModel.getData());
                            rvUserLIstt.setAdapter(subCatDetailsAdapter);
                            txtTotalCount.setText(liveUserModel.getData().size() + "");
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }
                });
    }


    private void playVideo(String timerValue, int parseInt) {

        path = getIntent().getStringExtra("path");
        andExoPlayerView.setSource(AppStrings.ideo_path + path);
        andExoPlayerView.setShowController(true);
        andExoPlayerView.setPlayWhenReady(true);


        if (timerValue.equalsIgnoreCase("start")){
            handler(andExoPlayerView);
        }


    }






    private void handler(AndExoPlayerView andExoPlayerView) {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    handler(andExoPlayerView);
                } catch (Exception e) {
                    e.getMessage();

                }

            }
        }, 1000);
    }
    private void openInviteFriends() {
        Intent intent = new Intent(PriumumVideoPlayerActivity.this, InviteFriendsActivity.class);
        startActivity(intent);
    }


    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this,R.style.MyDialogTheme)
                .setTitle("Leaving the room")
                .setMessage("Are you sure you want to leave this room?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
                        conentionClose();
                        andExoPlayerView.stopPlayer();
                        socket.emit("messagedetection", Nickname, Nickname + " : Left", MySharedpreferences.getInstance().get(PriumumVideoPlayerActivity.this, AppStrings.userID), video_id, "", MySharedpreferences.getInstance().get(activity, AppStrings.image));

                        if (pageID.equals("2")) {

                            Intent intent = new Intent(PriumumVideoPlayerActivity.this, PremiumVideoActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Intent intent = new Intent(PriumumVideoPlayerActivity.this, PremiumVideoActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        PriumumVideoPlayerActivity.super.onBackPressed();
                    }
                }).create().show();


    }




    @Override
    protected void onDestroy() {
        super.onDestroy();
        conentionClose();
    }
}
