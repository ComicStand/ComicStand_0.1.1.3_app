package com.s.video.musicas.scooby;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterViewAnimator;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;


import com.google.firebase.messaging.FirebaseMessaging;
import com.s.video.musicas.scooby.adapter.ChatRoomAdapter;
import com.s.video.musicas.scooby.nettwork.Api;
import com.s.video.musicas.scooby.nettwork.ApiClints;
import com.s.video.musicas.scooby.nettwork.model.ModelClassData;
import com.s.video.musicas.scooby.nettwork.model.UserDeatailsModel;
import com.s.video.musicas.scooby.utils.AppStrings;
import com.s.video.musicas.scooby.utils.MySharedpreferences;
import com.squareup.picasso.Picasso;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;


public class LiveChatRoomLIstActivity extends AppCompatActivity {
    @BindView(R.id.rvVideo)
    RecyclerView rvVideo;

    public static final int REQUEST_RECORD_AUDIO = 79;
    @BindView(R.id.imgAdd)
    ImageView imgAdd;

    @BindView(R.id.fb_open_video_cat)
    FloatingActionButton fb_open_video_cat;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    List<ModelClassData> list;

    @BindView(R.id.imgSetting)
    ImageView imgSetting;

    @BindView(R.id.premium)
    RadioButton premium;

    Handler mHandler;


    String[] PERMISSIONS;
    CompositeDisposable disposable = new CompositeDisposable();
    Api api = ApiClints.getClient().create(Api.class);

    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_chat_room_l_ist);

        this.mHandler = new Handler();
        m_Runnable.run();

        ButterKnife.bind(this);
        list = new ArrayList<>();

        getProfile();

        PERMISSIONS = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE};
        getChatRoomList();
        fb_open_video_cat.setOnClickListener(v -> openYouTube());


        /*if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
        } else {
            requestStoragePermission();
        }*/
        imgSetting.setOnClickListener(v -> openSetting());

        imgAdd.setOnClickListener(v -> openAdd());

        premium.setOnClickListener(v -> openPremiumvideo());

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w("TAG", "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token
                        String token = task.getResult();

                        // Log and toast
                        Log.d("FCM Token", token);
                        Log.d("FCM Token", MySharedpreferences.getInstance().get(LiveChatRoomLIstActivity.this, AppStrings.userID));
                        sendDeviceToken(MySharedpreferences.getInstance().get(LiveChatRoomLIstActivity.this, AppStrings.userID), token);
                    }
                });


        // refresh layout

        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                RefreshLayout();
                isOnline();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        isOnline();

    }

    //method to detect if device is connected by any means
    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

        // are we connected to the net(wifi or phone)
        if ( cm.getNetworkInfo(0).getState() == NetworkInfo.State.CONNECTED ||
                //cm.getNetworkInfo(0).getState() == NetworkInfo.State.CONNECTING ||
                //cm.getNetworkInfo(1).getState() == NetworkInfo.State.CONNECTING ||
                cm.getNetworkInfo(1).getState() == NetworkInfo.State.CONNECTED ) {
            Log.e("Testing Internet Connection", "We have internet");
            return true;

        } else if (cm.getNetworkInfo(0).getState() == NetworkInfo.State.DISCONNECTED
                ||  cm.getNetworkInfo(1).getState() == NetworkInfo.State.DISCONNECTED){
            showNoInternetConnectionDialog();
            Log.e("Testing Internet Connection", "We dont have internet");
            return false;
        }
        return false;

    }
    //Showing the No internet connection Custom Dialog =)
    public void showNoInternetConnectionDialog(){
        Log.e("Testing Internet Connection", "Entering showNoInternetConnectionDialog Method");
        final AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.MyDialogTheme);
        builder.setMessage("Whoops! Its seems you don't have internet connection.")
                .setTitle("No Internet Connection")
                .setCancelable(false)
                .setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(LiveChatRoomLIstActivity.this, LiveChatRoomLIstActivity.class);
                        startActivity(intent);
                        isOnline();
                    }
                })
                .setNegativeButton("Exit", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        finishAffinity();
                        System.exit(0);
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
        Log.e("Testing Internet Connection", "Showed NoIntenetConnectionDialog");


    }



    private final Runnable m_Runnable = new Runnable() {
        public void run() {
            LiveChatRoomLIstActivity.this.mHandler.postDelayed(m_Runnable, 30000);
            RefreshLayout();
        }

    };


    private void openYouTube() {
        Intent intent = new Intent(this, DashboardActivity.class);
        startActivity(intent);
        finish();
    }

    private void openSetting() {


        Intent intent = new Intent(LiveChatRoomLIstActivity.this, OpenSettingActivity.class);
        startActivity(intent);
    }



   /* private void openVideoCat() {
        Intent intent = new Intent(this, ChooseVideoCatActivity.class);
        startActivity(intent);
        finish();
    }*/

    /*private void requestStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(LiveChatRoomLIstActivity.this, Manifest.permission.RECORD_AUDIO)) {

        } else {
            ActivityCompat.requestPermissions(LiveChatRoomLIstActivity.this,
                    new String[]{Manifest.permission.RECORD_AUDIO}, REQUEST_RECORD_AUDIO);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_RECORD_AUDIO) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(LiveChatRoomLIstActivity.this, "Permission GRANTED", Toast.LENGTH_SHORT).show();
            } else {
                    Toast.makeText(LiveChatRoomLIstActivity.this, "Permission DENIED", Toast.LENGTH_SHORT).show();
                }
        }
    }
*/
    private void openAdd() {
        Intent intent = new Intent(this, ContactActivity.class);
        startActivity(intent);
    }

    private void openPremiumvideo() {
        Intent intent = new Intent(this, PremiumVideoActivity.class);
        startActivity(intent);
    }


    private void sendDeviceToken(String userId, String token) {
        api.sendDeviceToken(userId, token)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new SingleObserver<ModelClassData>() {
                    @Override
                    public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {

                    }


                    @Override
                    public void onSuccess(@io.reactivex.rxjava3.annotations.NonNull ModelClassData modelClassData) {
                        if (modelClassData.getStatus().equalsIgnoreCase("success")) {
                            Log.e("onSuccess", modelClassData.getMessage());


                        }

                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                        Log.e("onError", e.getMessage());

                    }
                });
    }

    private void getChatRoomList() {
        progressBar.setVisibility(View.VISIBLE);
        api.getGroupLIst()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new SingleObserver<ModelClassData>() {
                    @Override
                    public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {

                    }


                    @Override
                    public void onSuccess(@io.reactivex.rxjava3.annotations.NonNull ModelClassData modelClassData) {
                        if (modelClassData.getStatus().equalsIgnoreCase("success")) {
                            LinearLayoutManager mLayoutManager = new LinearLayoutManager(LiveChatRoomLIstActivity.this);
                            rvVideo.setLayoutManager(mLayoutManager);

                            //
                            Collections.reverse(modelClassData.getData());
                            ChatRoomAdapter videoAdapter = new ChatRoomAdapter(modelClassData.getData(), LiveChatRoomLIstActivity.this);
                            rvVideo.setAdapter(videoAdapter);


                        }
                        progressBar.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                });
    }


    private void RefreshLayout() {

        isOnline();
        api.getGroupLIst()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new SingleObserver<ModelClassData>() {
                    @Override
                    public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {

                    }

                    @Override
                    public void onSuccess(@io.reactivex.rxjava3.annotations.NonNull ModelClassData modelClassData) {
                        if (modelClassData.getStatus().equalsIgnoreCase("success")) {
                            LinearLayoutManager mLayoutManager = new LinearLayoutManager(LiveChatRoomLIstActivity.this);
                            rvVideo.setLayoutManager(mLayoutManager);

                            //
                            Collections.reverse(modelClassData.getData());
                            ChatRoomAdapter videoAdapter = new ChatRoomAdapter(modelClassData.getData(), LiveChatRoomLIstActivity.this);
                            rvVideo.setAdapter(videoAdapter);


                        }

                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {

                    }
                });
    }


    @Override
    protected void onResume() {

        super.onResume();
        getChatRoomList();


    }


    private void getProfile() {
        api.userProfiledatils(MySharedpreferences.getInstance().get(this, AppStrings.userID))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new SingleObserver<UserDeatailsModel>() {
                    @Override
                    public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {

                    }

                    @Override
                    public void onSuccess(@io.reactivex.rxjava3.annotations.NonNull UserDeatailsModel userDeatailsModel) {


                        Log.d("TAG", "onSuccess: " + userDeatailsModel.getData().get(0).getPhoto());
                        if (userDeatailsModel.getStatus().equalsIgnoreCase("success")) {
                            Glide.with(LiveChatRoomLIstActivity.this).load(AppStrings.ideo_path + userDeatailsModel.getData().get(0).getPhoto());
                            MySharedpreferences.getInstance().save(LiveChatRoomLIstActivity.this, AppStrings.image, AppStrings.ideo_path + userDeatailsModel.getData().get(0).getPhoto());
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                        Log.d("TAG", "onError: " + e.getMessage());
                    }
                });
    }


    @Override
    public void onBackPressed() {
        finishAffinity();
    }


}

