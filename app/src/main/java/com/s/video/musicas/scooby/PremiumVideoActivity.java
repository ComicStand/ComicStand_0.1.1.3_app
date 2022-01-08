package com.s.video.musicas.scooby;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

import com.bumptech.glide.Glide;
import com.s.video.musicas.scooby.adapter.PriminumVideoAdapter;
import com.s.video.musicas.scooby.auth.AuthActivity;
import com.s.video.musicas.scooby.nettwork.Api;
import com.s.video.musicas.scooby.nettwork.ApiClints;
import com.s.video.musicas.scooby.nettwork.model.CheckVideoSubscriptionModel;
import com.s.video.musicas.scooby.nettwork.model.PremiumVideoModel;
import com.s.video.musicas.scooby.nettwork.model.UpdateProfileModel;
import com.s.video.musicas.scooby.nettwork.model.UserDeatailsModel;
import com.s.video.musicas.scooby.utils.AppStrings;
import com.s.video.musicas.scooby.utils.MySharedpreferences;
import android.os.*;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;


public class PremiumVideoActivity extends AppCompatActivity implements PriminumVideoAdapter.Check {


    @BindView(R.id.rcLIst)
    RecyclerView rcLIst;

    @BindView(R.id.imgAdd)
    ImageView imgAdd;

    @BindView(R.id.imgSetting)
    ImageView imgSetting;

    @BindView(R.id.lobby)
    RadioButton lobby;


    /*@BindView(R.id.imgBack)
    ImageView imgBack;*/

    CompositeDisposable disposable = new CompositeDisposable();
    Api api = ApiClints.getClient().create(Api.class);

    /*@BindView(R.id.imgProfile)
    CircleImageView imgProfile;*/

    private static int PERMISSION_ALL = 1;
    String[] PERMISSIONS;
    String imageUri = "1";
    Bitmap bitmap;

    @BindView(R.id.progress_circular)
    ProgressBar progress_circular;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_premium_video);
        ButterKnife.bind(this);

        /*imgBack.setOnClickListener(v -> onBackPressed());*/

        imgSetting.setOnClickListener(v -> openSetting());

        imgAdd.setOnClickListener(v -> openAdd());

        lobby.setOnClickListener(v -> openlobby());




        /*imgProfile.setOnClickListener(v -> uploadImg());*/

        PERMISSIONS = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE};


        getList();
        isOnline();
        /*getProfile();*/
    }

    private void openSetting() {
        Intent intent = new Intent(PremiumVideoActivity.this, OpenSettingActivity.class);
        startActivity(intent);
    }

    private void openlobby() {
        Intent intent = new Intent(PremiumVideoActivity.this, LiveChatRoomLIstActivity.class);
        startActivity(intent);
    }


    private void openAdd() {
        Intent intent = new Intent(this, ContactActivity.class);
        startActivity(intent);
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
                        Intent intent = new Intent(PremiumVideoActivity.this, PremiumVideoActivity.class);
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




    private void getList() {
        Log.d("TAG", "getList: " + MySharedpreferences.getInstance().get(this, AppStrings.userID));
        ProgressDialog dialog = ProgressDialog.show(PremiumVideoActivity.this, "",
                ". Please wait...", true);
        api.getVideoList(MySharedpreferences.getInstance().get(this, AppStrings.userID)).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new SingleObserver<PremiumVideoModel>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onSuccess(@NonNull PremiumVideoModel premiumVideoModel) {
                        dialog.dismiss();
                        Log.d("TAG", "onSuccess: " + premiumVideoModel.getStatus());
                        if (premiumVideoModel.getStatus().equalsIgnoreCase("success")) {


                            LinearLayoutManager mLayoutManager = new LinearLayoutManager(PremiumVideoActivity.this);
                            rcLIst.setLayoutManager(mLayoutManager);
                            PriminumVideoAdapter videoAdapter = new PriminumVideoAdapter(PremiumVideoActivity.this, premiumVideoModel.getData());
                            rcLIst.setAdapter(videoAdapter);

                        }


                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        dialog.dismiss();
                        Log.d("TAG", "onSuccess: " + e.getMessage());

                    }
                });


    }

    @Override
    public void check(String id, String path, String amount,String video_title) {
        if (!MySharedpreferences.getInstance().get(PremiumVideoActivity.this, AppStrings.loginType).equalsIgnoreCase("login")) {
            Intent intent = new Intent(PremiumVideoActivity.this, AuthActivity.class);
            startActivity(intent);
            finish();
        } else {
            ProgressDialog dialog = ProgressDialog.show(PremiumVideoActivity.this, "",
                    ". Please wait...", true);
            api.chekVideoSuScription(MySharedpreferences.getInstance().get(this, AppStrings.userID), id)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new SingleObserver<CheckVideoSubscriptionModel>() {
                        @Override
                        public void onSubscribe(@NonNull Disposable d) {

                        }

                        @Override
                        public void onSuccess(@NonNull CheckVideoSubscriptionModel checkVideoSubscriptionModel) {

                            Log.d("TAG", "onSuccess: " + checkVideoSubscriptionModel.getStatus());
                            dialog.dismiss();
                            if (checkVideoSubscriptionModel.getStatus().equalsIgnoreCase("success")) {
                                Intent intent = new Intent(PremiumVideoActivity.this, PriumumVideoPlayerActivity.class);
                                intent.putExtra("path", path);
                                intent.putExtra("video_id", id);
                                intent.putExtra("video_title",video_title);
                                intent.putExtra("pageID","2");
                                startActivity(intent);

                            } else {
                                Intent intent = new Intent(PremiumVideoActivity.this, RazorpayActivity.class);
                                intent.putExtra("path", path);
                                intent.putExtra("amount", amount);
                                intent.putExtra("video_id", id);
                                intent.putExtra("video_title",video_title);
                                intent.putExtra("pageID","2");
                                startActivity(intent);
                                finish();

                            }
                        }

                        @Override
                        public void onError(@NonNull Throwable e) {
                            Log.d("TAG", "onError: " + e.getMessage());
                            dialog.dismiss();
                        }
                    });

        }

    }

    @Override
    public void onBackPressed() {

        Intent intent = new Intent(PremiumVideoActivity.this, LiveChatRoomLIstActivity.class);
        startActivity(intent);

        /*finishAffinity();
        *//*exit = true;*/
    }}
/*
    private Boolean exit = false;
    @Override
    public void onBackPressed() {
        if (exit) {
            finishAffinity(); // finish activity
        } else {
            Toast.makeText(this, "Press Back again to Exit.",
                    Toast.LENGTH_SHORT).show();
            exit = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    exit = false;
                }
            }, 3 * 1000);

        }

    }}*/


/*
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(PremiumVideoActivity.this, LiveChatRoomLIstActivity.class);
        startActivity(intent);
        finish();
    }}*/

    /*private void uploadImg() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!hasPermissions(PremiumVideoActivity.this, PERMISSIONS)) {
                ActivityCompat.requestPermissions(PremiumVideoActivity.this, PERMISSIONS, PERMISSION_ALL);
            } else {
                pickImg();
            }
        } else {
            pickImg();
        }
    }*/


/*
    public boolean hasPermissions(Context context, String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                } else {
                    Toast.makeText(PremiumVideoActivity.this, "Permission done...", Toast.LENGTH_SHORT).show();
                }
            }
        }
        return true;
    }*/

   /* private void pickImg() {
        if (MySharedpreferences.getInstance().get(PremiumVideoActivity.this, AppStrings.loginType).equalsIgnoreCase("guest")) {
            Intent intent = new Intent(PremiumVideoActivity.this, AuthActivity.class);
            startActivity(intent);
        } else {
            Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(pickPhoto, 1);
        }
    }*/

/*
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            Uri uri = data.getData();

            InputStream inputStream = null;
            try {
                inputStream = getApplicationContext().getContentResolver().openInputStream(uri);
                bitmap = BitmapFactory.decodeStream(inputStream);
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
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        byte[] imageByte = outputStream.toByteArray();
        String encode = Base64.encodeToString(imageByte, Base64.DEFAULT);
        return encode;


    }

    private void picUpload(String path) {
        showProgress();
        api.updateProfile(MySharedpreferences.getInstance().get(PremiumVideoActivity.this, AppStrings.userID), path)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<UpdateProfileModel>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onSuccess(@NonNull UpdateProfileModel updateProfileModel) {

                        Log.d("TAG", "onSuccess: " + updateProfileModel.getStatus());
                        hideProgress();
                        getProfile();
                        Toast.makeText(PremiumVideoActivity.this, updateProfileModel.getMessage(), Toast.LENGTH_SHORT).show();
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

                        Log.d("TAG", "onSuccess: " + userDeatailsModel.getData().get(0).getPhoto());
                        if (userDeatailsModel.getStatus().equalsIgnoreCase("success")) {
                            Glide.with(PremiumVideoActivity.this).load(AppStrings.ideo_path + userDeatailsModel.getData().get(0).getPhoto()).into(imgProfile);
                            MySharedpreferences.getInstance().save(PremiumVideoActivity.this, AppStrings.image, AppStrings.ideo_path + userDeatailsModel.getData().get(0).getPhoto());
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                        Log.d("TAG", "onError: " + e.getMessage());
                    }
                });
    }


}*/

