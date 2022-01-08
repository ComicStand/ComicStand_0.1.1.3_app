package com.s.video.musicas.scooby;

import android.Manifest;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.android.volley.BuildConfig;
import com.bumptech.glide.Glide;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.s.video.musicas.scooby.auth.AuthActivity;
import com.s.video.musicas.scooby.utils.AppStrings;
import com.s.video.musicas.scooby.utils.MySharedpreferences;

public class SplashActivity extends AppCompatActivity {

    TextView textView2;
    Animation splash_screen_animation;

    @BindView(R.id.imageView)
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ButterKnife.bind(this);
        Glide.with(this).load(R.drawable.logo).into(imageView);

        startLocationButtonClick();

       /* getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);*/


        ConstraintLayout constraintLayout = findViewById(R.id.splash);
        AnimationDrawable animationDrawable = (AnimationDrawable) constraintLayout.getBackground();
        animationDrawable.setEnterFadeDuration(0);
        animationDrawable.setExitFadeDuration(125);
        animationDrawable.start();


        splash_screen_animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.splash_screen_animation);
        textView2 = findViewById(R.id.textView2);
        textView2.startAnimation(splash_screen_animation);


    }

    public void startLocationButtonClick() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (MySharedpreferences.getInstance().get(SplashActivity.this, AppStrings.userID) != null) {
                    Intent intent = new Intent(SplashActivity.this, PremiumVideoActivity.class);
                    startActivity(intent);
                    finish();

                } else {
                    Intent intent = new Intent(SplashActivity.this, AuthActivity.class);
                    startActivity(intent);
                    finish();

                }
            }
        }, 2000);
    }
}

/*

    public void startLocationButtonClick() {
        Dexter.withActivity(this)
                .withPermissions(Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.RECORD_AUDIO,Manifest.permission.READ_PHONE_STATE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {


                        if (report.areAllPermissionsGranted()) {
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    if (MySharedpreferences.getInstance().get(SplashActivity.this, AppStrings.userID) != null) {
                                        Intent intent = new Intent(SplashActivity.this, LiveChatRoomLIstActivity.class);
                                        startActivity(intent);
                                        finish();

                                    } else {
                                        Intent intent = new Intent(SplashActivity.this, AuthActivity.class);
                                        startActivity(intent);
                                        finish();

                                    }
                                }
                            }, 2000);
                        }else {
                            openSettings();

                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    if (MySharedpreferences.getInstance().get(SplashActivity.this, AppStrings.userID) != null) {
                                        Intent intent = new Intent(SplashActivity.this, LiveChatRoomLIstActivity.class);
                                        startActivity(intent);
                                        finish();

                                    } else {
                                        Intent intent = new Intent(SplashActivity.this, AuthActivity.class);
                                        startActivity(intent);
                                        finish();

                                    }
                                }
                            }, 2000);
                        }


                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {

                    }
                }).check();

    }

    private void openSettings() {
        Intent intent = new Intent();
        intent.setAction(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package",
                BuildConfig.APPLICATION_ID, null);
        intent.setData(uri);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }


}*/
