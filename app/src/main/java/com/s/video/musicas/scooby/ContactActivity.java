package com.s.video.musicas.scooby;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
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
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayout;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import com.s.video.musicas.scooby.auth.AuthActivity;
import com.s.video.musicas.scooby.fragment.ContactListFragment;
import com.s.video.musicas.scooby.fragment.OnlineConatctFragment;
import com.s.video.musicas.scooby.nettwork.Api;
import com.s.video.musicas.scooby.nettwork.ApiClints;
import com.s.video.musicas.scooby.nettwork.model.UpdateProfileModel;
import com.s.video.musicas.scooby.nettwork.model.UserDeatailsModel;
import com.s.video.musicas.scooby.utils.AppStrings;
import com.s.video.musicas.scooby.utils.MySharedpreferences;

public class ContactActivity extends AppCompatActivity {


    @BindView(R.id.tabs)
    TabLayout tabs;


    @BindView(R.id.tab_viewpager)
    ViewPager tab_viewpager;

    @BindView(R.id.imgPicUpload)
    CircleImageView imgPicUpload;

    @BindView(R.id.btShare)
    ImageView btShare;

    @BindView(R.id.BackPressedIconContact)
    ImageView BackPressed;


    private static int PERMISSION_ALL = 1;
    String[] PERMISSIONS;
    String imageUri = "1";
    Bitmap bitmap;


    @BindView(R.id.imgBAckground)
    ImageView imgBAckground;

    @BindView(R.id.progress_circular)
    ProgressBar progress_circular;


    CompositeDisposable disposable = new CompositeDisposable();
    Api api = ApiClints.getClient().create(Api.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        ButterKnife.bind(this);
        setupViewPager(tab_viewpager);
        tabs.setupWithViewPager(tab_viewpager);

        imgPicUpload.setOnClickListener(v -> openSetting());
        BackPressed.setOnClickListener(v -> onBackPressed());

        PERMISSIONS = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE};

        Glide.with(this).load(R.drawable.blankpage).into(imgBAckground);

        getProfile();

    }


    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new OnlineConatctFragment(), getString(R.string.app_name));
        adapter.addFragment(new ContactListFragment(), "Contact");
        viewPager.setAdapter(adapter);
        btShare.setOnClickListener(v -> shareCode());

    }

    private void shareCode() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT,   "https://play.google.com/store/apps/details?id=" + getPackageName());
        intent.putExtra(Intent.EXTRA_SUBJECT, "Code");
        startActivity(intent);
    }

    static class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);

            mFragmentTitleList.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();

        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    private void uploadImg() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!hasPermissions(ContactActivity.this, PERMISSIONS)) {
                ActivityCompat.requestPermissions(ContactActivity.this, PERMISSIONS, PERMISSION_ALL);
            } else {
                pickImg();
            }
        } else {
            pickImg();
        }
    }

    private void openSetting(){
        Intent intent = new Intent(this, OpenSettingActivity.class);
        startActivity(intent);
    }


    private void pickImg() {
        if (MySharedpreferences.getInstance().get(ContactActivity.this, AppStrings.loginType).equalsIgnoreCase("guest")) {
            Intent intent = new Intent(ContactActivity.this, AuthActivity.class);
            startActivity(intent);
        } else {
            Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(pickPhoto, 1);
        }
    }

    public boolean hasPermissions(Context context, String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                } else {
                    Toast.makeText(ContactActivity.this, "Upload Photo", Toast.LENGTH_SHORT).show();
                }
            }
        }
        return true;
    }


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
        api.updateProfile(MySharedpreferences.getInstance().get(ContactActivity.this, AppStrings.userID), path)
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
                        Toast.makeText(ContactActivity.this, updateProfileModel.getMessage(), Toast.LENGTH_SHORT).show();
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


                        if (userDeatailsModel.getStatus().equalsIgnoreCase("success")) {
                            Glide.with(ContactActivity.this).load(AppStrings.ideo_path + userDeatailsModel.getData().get(0).getPhoto()).into(imgPicUpload);
                            MySharedpreferences.getInstance().save(ContactActivity.this, AppStrings.image, AppStrings.ideo_path + userDeatailsModel.getData().get(0).getPhoto());

                            Log.d("TAG", "onSuccess: "+AppStrings.ideo_path + userDeatailsModel.getData().get(0).getPhoto());
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }
                });
    }
}
