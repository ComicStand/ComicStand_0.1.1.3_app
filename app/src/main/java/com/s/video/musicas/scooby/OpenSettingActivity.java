package com.s.video.musicas.scooby;

import androidx.annotation.DrawableRes;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;


import com.s.video.musicas.scooby.adapter.PriminumVideoAdapter;
import com.s.video.musicas.scooby.auth.AuthActivity;
import com.s.video.musicas.scooby.nettwork.Api;
import com.s.video.musicas.scooby.nettwork.ApiClints;
import com.s.video.musicas.scooby.nettwork.model.CheckVideoSubscriptionModel;
import com.s.video.musicas.scooby.nettwork.model.PremiumVideoModel;
import com.s.video.musicas.scooby.nettwork.model.UserDeatailsModel;
import com.s.video.musicas.scooby.utils.AppStrings;
import com.s.video.musicas.scooby.utils.MySharedpreferences;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Toast;

import com.s.video.musicas.scooby.nettwork.model.UpdateProfileModel;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URI;
import java.security.Permissions;


public class OpenSettingActivity extends AppCompatActivity {


    @BindView(R.id.imgBackground)
    ImageView imgBackground;

    @BindView(R.id.contInsta)
    ConstraintLayout contInsta;

    @BindView(R.id.constFace)
    ConstraintLayout constFace;

    @BindView(R.id.contTwitter)
    ConstraintLayout contTwitter;

    @BindView(R.id.contYoutube)
    ConstraintLayout contYoutube;

    @BindView(R.id.contRateNow)
    ConstraintLayout contRateNow;

    @BindView(R.id.imgAdd)
    ImageView imgAdd;

    @BindView(R.id.txtLogin)
    TextView txtLogin;

    @BindView(R.id.txtSign)
    TextView txtSign;

    @BindView(R.id.change_photo)
    TextView changePhoto;

    @BindView(R.id.imgProfile)
    CircleImageView imgProfile;

    @BindView(R.id.progress_circular)
    ProgressBar progress_circular;

    @BindView(R.id.BackPressedIconSetting)
    ImageView BackPressed;

    private static final int PERMISSION_ALL = 1;
    String[] PERMISSIONS;
    String imageUri = "1";
    Bitmap bitmap;


    Api api = ApiClints.getClient().create(Api.class);

    @BindView(R.id.txtLogout)
    TextView txtLogout;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_setting);
        ButterKnife.bind(this);
        Glide.with(this).load(R.drawable.blankpage).into(imgBackground);

        changePhoto.setOnClickListener(v -> pickImg());
        imgProfile.setOnClickListener(v -> pickImg());


        PERMISSIONS = new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE};
        BackPressed.setOnClickListener(v -> onBackPressed());


        getProfile();




        txtLogout.setOnClickListener(v -> signOut());

        if /*(MySharedpreferences.getInstance().get(this,AppStrings.userID) != null)*/
        (MySharedpreferences.getInstance().get(OpenSettingActivity.this, AppStrings.loginType).equalsIgnoreCase("guest")){
            /*getProfile();*/
            txtLogin.setText("Guest");
            /*txtSign.setVisibility(View.VISIBLE);*/
            txtLogout.setText(R.string.Login);
            /*txtLogout.setVisibility(View.VISIBLE);*/
        }else {
            getProfile();

            /*txtSign.setText("Guest Login");
            txtSign.setVisibility(View.VISIBLE);
            txtLogout.setText("Login");
            txtLogout.setVisibility(View.VISIBLE);*/
        }


        imgAdd.setOnClickListener(v -> openAdd());
        contInsta.setOnClickListener(v -> openBrowser("https://www.instagram.com/watchscooby"));

        constFace.setOnClickListener(v -> openBrowser("https://www.facebook.com/116890863477519"));

        contTwitter.setOnClickListener(v -> openBrowser("https://www.youtube.com/channel/UC5a5Q-QVK_31BN2Zs6AJtaw"));

        contYoutube.setOnClickListener(v -> openBrowser("https://scooby.live"));

        contRateNow.setOnClickListener(v -> openBrowser("https://play.google.com/store/apps/details?id=com.s.video.musicas.scooby"));
    }

/*
    @Override
    public void onBackPressed() {
          Intent intent=new Intent(OpenSettingActivity.this,LiveChatRoomLIstActivity.class);
          startActivity(intent);


    }*/


    /* private void uploadImg() {
         if (!hasPermissions(OpenSettingActivity.this, PERMISSIONS)) {
             ActivityCompat.requestPermissions(OpenSettingActivity.this, PERMISSIONS, PERMISSION_ALL);
             if (PERMISSION_ALL == PackageManager.PERMISSION_GRANTED);{

                 pickImg();
             }
         } else {
             pickImg();
         }
     }


    public boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                } else {
                    Toast.makeText(OpenSettingActivity.this, "Select Photo", Toast.LENGTH_SHORT).show();
                }
            }
        }
        return true;
    }*/

    private void pickImg() {
        if (MySharedpreferences.getInstance().get(OpenSettingActivity.this, AppStrings.loginType).equalsIgnoreCase("guest")) {
            Intent intent = new Intent(OpenSettingActivity.this, AuthActivity.class);
            startActivity(intent);
        } else {
            Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(pickPhoto, 1);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            Uri uri = data.getData();

            InputStream inputStream;
            try {
                inputStream = getApplicationContext().getContentResolver().openInputStream(uri);
                bitmap = BitmapFactory.decodeStream(inputStream);
              //  Bitmap resized = Bitmap.createScaledBitmap(bitmap, 200, 200, true);

                //   Glide.with(MyProfileActivity.this).load(bitmap).into(profile_image);
                imageUri = imageTostring(bitmap);

                picUpload(imageUri);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

    }

    private String imageTostring(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 25, outputStream);
        byte[] imageByte = outputStream.toByteArray();
        return Base64.encodeToString(imageByte, Base64.DEFAULT);


    }


    private void picUpload(String path) {
        showProgress();
        api.updateProfile(MySharedpreferences.getInstance().get(OpenSettingActivity.this, AppStrings.userID), path)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<UpdateProfileModel>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onSuccess(@NonNull UpdateProfileModel updateProfileModel) {
                        Log.d("TAG", "onSuccess: " + updateProfileModel.getStatus());
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                hideProgress();
                                Toast.makeText(OpenSettingActivity.this, updateProfileModel.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        },2000);
                        getProfile();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        hideProgress();
                        Log.d("TAG", "onSuccess: " + e.getMessage());
                    }
                });



    }

    private void showProgress() {
        progress_circular.setVisibility(View.VISIBLE);
    }


    private void hideProgress() {
        progress_circular.setVisibility(View.INVISIBLE);
    }



    private void getProfile() {
        api.userProfiledatils(MySharedpreferences.getInstance().get(this, AppStrings.userID))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new SingleObserver<UserDeatailsModel>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onSuccess(@NonNull UserDeatailsModel userDeatailsModel) {

                        txtLogin.setText(userDeatailsModel.getData().get(0).getName());

                        Log.d("TAG", "onSuccess: " + userDeatailsModel.getData().get(0).getPhoto());
                        if (userDeatailsModel.getStatus().equalsIgnoreCase("success")) {
                            Glide.with(OpenSettingActivity.this).load(AppStrings.ideo_path + userDeatailsModel.getData().get(0).getPhoto()).into(imgProfile);
                            MySharedpreferences.getInstance().save(OpenSettingActivity.this, AppStrings.image, AppStrings.ideo_path + userDeatailsModel.getData().get(0).getPhoto());
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                        Log.d("TAG", "onError: " + e.getMessage());
                    }
                });
    }




    private void openBrowser(String url){

        try {
            Intent i = new Intent("android.intent.action.MAIN");
            i.setComponent(ComponentName.unflattenFromString("com.android.chrome/com.android.chrome.Main"));
            i.addCategory("android.intent.category.LAUNCHER");
            i.setData(Uri.parse(url));
            startActivity(i);
        }
        catch(ActivityNotFoundException e) {
            // Chrome is not installed
            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(i);
        }
    }


    private void openRate(){
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id="+getPackageName())));
    }
    private void openAdd() {
        Intent intent = new Intent(this, ContactActivity.class);
        startActivity(intent);
    }


/*
    private void getProfile() {
        api.userProfiledatils(MySharedpreferences.getInstance().get(this, AppStrings.userID))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new SingleObserver<UserDeatailsModel>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onSuccess(@NonNull UserDeatailsModel userDeatailsModel) {

                        txtLogin.setText(userDeatailsModel.getData().get(0).getName());

                 //       Log.d("TAG", "onSuccess: " + userDeatailsModel);



                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                        Log.d("TAG", "onError: " + e.getMessage());
                    }
                });
    }*/

    private void signOut(){
        MySharedpreferences.getInstance().removeAll(OpenSettingActivity.this);
        Intent intent=new Intent(OpenSettingActivity.this, AuthActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);

    }


   }