package com.s.video.musicas.scooby;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.s.video.musicas.scooby.utils.AppStrings;
import com.s.video.musicas.scooby.utils.MySharedpreferences;

public class ChooseVideoCatActivity extends AppCompatActivity {


    @BindView(R.id.imgYoutube)
    ImageView imgYoutube;

    @BindView(R.id.imgPreminum)
    ImageView imgPreminum;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_video_cat);
        ButterKnife.bind(this);
        Log.d("TAG", "onCreate: "+ MySharedpreferences.getInstance().get(this, AppStrings.userID));
        imgPreminum.setOnClickListener(v -> openPreminumVideo());
        imgYoutube.setOnClickListener(v -> openYouTu());
    }

    private void openPreminumVideo() {
        Intent intent = new Intent(this, PremiumVideoActivity.class);
        startActivity(intent);
        finish();
    }

    private void openYouTu() {
        Intent intent = new Intent(this, DashboardActivity.class);
        startActivity(intent);
        finish();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, LiveChatRoomLIstActivity.class);
        startActivity(intent);
        finish();

    }
}