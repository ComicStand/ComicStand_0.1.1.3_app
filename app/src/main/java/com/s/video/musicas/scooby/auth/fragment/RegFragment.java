package com.s.video.musicas.scooby.auth.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Looper;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Toast;

import com.android.volley.BuildConfig;
import com.s.video.musicas.scooby.OpenSettingActivity;
import com.s.video.musicas.scooby.ProfileChange;
import com.s.video.musicas.scooby.nettwork.model.OtpModel;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

import com.s.video.musicas.scooby.LiveChatRoomLIstActivity;
import com.s.video.musicas.scooby.R;
import com.s.video.musicas.scooby.nettwork.Api;
import com.s.video.musicas.scooby.nettwork.ApiClints;
import com.s.video.musicas.scooby.utils.AppStrings;
import com.s.video.musicas.scooby.utils.MySharedpreferences;


public class RegFragment extends Fragment {

    @BindView(R.id.edName)
    EditText edName;

    @BindView(R.id.edEmailID)
    EditText edEmailID;

    @BindView(R.id.edMobileNumber)
    EditText edMobileNumber;

    @BindView(R.id.edPassword)
    EditText edPassword;


   /* @BindView(R.id.edPicUpload)
    EditText edPicUpload;*/


    @BindView(R.id.btregister)
    Button btregister;

    @BindView(R.id.progress_circular)
    ProgressBar progress_circular;

    /*@BindView(R.id.imgUploded)
    ImageView imgUploded;*/

    @BindView(R.id.btnlogin)
    RadioButton btnlogin;

    private String tag = "Empty";

    /*String imageStatus = "1";
    private static int PERMISSION_ALL = 1;
    String[] PERMISSIONS;
    String imageUri = "1";
    Bitmap bitmap;
*/

    CompositeDisposable disposable = new CompositeDisposable();
    Api api = ApiClints.getClient().create(Api.class);


   /* private final static int REQUEST_CHECK_SETTINGS = 2000;
    private LocationRequest mLocationRequest;*/
    double latitude;
    double longitude;
   /* private String mLastUpdateTime;
    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;
    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = 5000;
    private FusedLocationProviderClient mFusedLocationClient;
    private SettingsClient mSettingsClient;
    private LocationSettingsRequest mLocationSettingsRequest;
    private LocationCallback mLocationCallback;
    private Location mCurrentLocation;
    private Boolean mRequestingLocationUpdates;*/
  /*  private Object LoginFragment;*/


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_reg, container, false);
        ButterKnife.bind(this, view);
        btregister.setOnClickListener(v -> register());

        /*PERMISSIONS = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE};*/

       /* edPicUpload.setOnClickListener(v -> chooseImage());*/
        btnlogin.setOnClickListener(v -> openlogin());

        edEmailID.setText("user@scooby");




        /*init();
        restoreValuesFromBundle(savedInstanceState);
        startLocationButtonClick();*/

        return view;
    }

    private void guestLogin() {
        int random = ThreadLocalRandom.current().nextInt(0, 1000);
        MySharedpreferences.getInstance().save(getContext(), AppStrings.userID, String.valueOf(random));
        goDashBoad();

    }
    private void openlogin() {
        Bundle args = new Bundle();
        Fragment fragmentt = new LoginFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        fragmentt.setArguments(args);
        transaction.replace(R.id.fragment, fragmentt);
        transaction.addToBackStack("reg").commit();
    }

   /* private void chooseImage() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!hasPermissions(getActivity(), PERMISSIONS)) {
                ActivityCompat.requestPermissions(getActivity(), PERMISSIONS, PERMISSION_ALL);
            } else {
                pickImg();
            }
        } else {
            pickImg();
        }
    }*/

/*
    private String imageTostring(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        byte[] imageByte = outputStream.toByteArray();
        String encode = Base64.encodeToString(imageByte, Base64.DEFAULT);
        return encode;


    }


    private void pickImg() {
        Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhoto, 1);
    }

    public boolean hasPermissions(Context context, String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                } else {
                    Toast.makeText(getContext(), "Permission done...", Toast.LENGTH_SHORT).show();
                }
            }
        }
        return true;
    }*/


    private void register() {
        String name = edName.getText().toString().trim();
        String mail = edEmailID.getText().toString().trim();
        String mobb = edMobileNumber.getText().toString().trim();
        String pass = edPassword.getText().toString().trim();


        if (name.isEmpty()) {
            edName.setError(tag);
        } else if (mail.isEmpty()) {
            edEmailID.setError(tag);
        } else if (mobb.isEmpty()) {
            edMobileNumber.setError(tag);
        } else if (pass.isEmpty()) {
            edPassword.setError(tag);
        } else {
            showProgress();
            latitude = (19.3897639);
            longitude = (73.0707252);

            Log.d("TAG", "register: " + latitude + "," + longitude);
            api.register(name, mail, mobb, pass,latitude + "," + longitude).observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(new SingleObserver<OtpModel>() {
                        @Override
                        public void onSubscribe(@NonNull Disposable d) {

                        }

                        @Override
                        public void onSuccess(@NonNull OtpModel otpModel) {
                            hideProgress();
                            if (otpModel.getStatus().equalsIgnoreCase("success")) {
                                // openOTP(edMobileNumber.getText().toString().trim());

                                MySharedpreferences.getInstance().save(getContext(), AppStrings.userID, otpModel.getData().get(0).getSignupId());
                                Toast.makeText(getContext(), otpModel.getMessage(), Toast.LENGTH_SHORT).show();
                                MySharedpreferences.getInstance().save(getContext(), AppStrings.loginType, "login");
                                MySharedpreferences.getInstance().save(getContext(), AppStrings.name, otpModel.getData().get(0).getName());
                                MySharedpreferences.getInstance().save(getContext(), AppStrings.mobile, otpModel.getData().get(0).getMobile());
                                goDashBoad();
                            } else {
                                Toast.makeText(getContext(), otpModel.getMessage() + "", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onError(@NonNull Throwable e) {
                            hideProgress();
                        }
                    });
        }


    }

    private void showProgress() {
        progress_circular.setVisibility(View.VISIBLE);
    }

    private void hideProgress() {
        progress_circular.setVisibility(View.GONE);
    }



    private void goDashBoad() {
        Intent intent = new Intent(getContext(), ProfileChange.class);
        startActivity(intent);
        getActivity().finish();
    }

/*

    @SuppressLint("RestrictedApi")
    private void init() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
        mSettingsClient = LocationServices.getSettingsClient(getActivity());

        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                mCurrentLocation = locationResult.getLastLocation();
                mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
                hideProgress();

            }
        };
        mRequestingLocationUpdates = false;
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        mLocationSettingsRequest = builder.build();
    }

    private void restoreValuesFromBundle(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey("is_requesting_updates")) {
                mRequestingLocationUpdates = savedInstanceState.getBoolean("is_requesting_updates");
            }

            if (savedInstanceState.containsKey("last_known_location")) {
                mCurrentLocation = savedInstanceState.getParcelable("last_known_location");
            }

            if (savedInstanceState.containsKey("last_updated_on")) {
                mLastUpdateTime = savedInstanceState.getString("last_updated_on");
            }
        }
    }

    public void startLocationButtonClick() {
        Dexter.withActivity(getActivity())
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        mRequestingLocationUpdates = true;
                        startLocationUpdates();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        if (response.isPermanentlyDenied()) {
                            openSettings();
                        }
                    }


                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        break;
                    case Activity.RESULT_CANCELED:
                        mRequestingLocationUpdates = false;
                        break;
                }
                break;
        }
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

    private void startLocationUpdates() {
        showProgress();
        mSettingsClient
                .checkLocationSettings(mLocationSettingsRequest)
                .addOnSuccessListener(getActivity(), new OnSuccessListener<LocationSettingsResponse>() {
                    @SuppressLint("MissingPermission")
                    @Override
                    public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                        mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                                mLocationCallback, Looper.myLooper());
                    }
                })
                .addOnFailureListener(getActivity(), new OnFailureListener() {
                    @Override
                    public void onFailure(@androidx.annotation.NonNull Exception e) {
                        int statusCode = ((ApiException) e).getStatusCode();
                        switch (statusCode) {
                            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                try {
                                    ResolvableApiException rae = (ResolvableApiException) e;
                                    rae.startResolutionForResult(getActivity(), REQUEST_CHECK_SETTINGS);
                                } catch (IntentSender.SendIntentException sie) {
                                }
                                break;
                            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                String errorMessage = "Location settings are inadequate, and cannot be " +
                                        "fixed here. Fix in Settings.";

                                Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

*/

}