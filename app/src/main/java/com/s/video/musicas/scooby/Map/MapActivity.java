package com.s.video.musicas.scooby.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.s.video.musicas.scooby.R;
import com.s.video.musicas.scooby.nettwork.model.LiveUserModel;
import com.s.video.musicas.scooby.utils.AppStrings;
import com.s.video.musicas.scooby.utils.MySharedpreferences;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    List<LiveUserModel.Data> joinUserlist;

    private GoogleMap mMap;
    LatLng customMarkerLocationTwo;
    static final float COORDINATE_OFFSET = 0.00002f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map2);

        joinUserlist=new ArrayList<>();
        joinUserlist= (List<LiveUserModel.Data>) getIntent().getSerializableExtra("joinUserlist");

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        mMap.getUiSettings().setMapToolbarEnabled(false);

        mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                Double latitude, longitude;
                for (int i=0;i<joinUserlist.size();i++){


                    Log.d("TAG", "onMapLoaded: "+joinUserlist.get(i).getLat_long());

                    String[] team1 = joinUserlist.get(i).getLat_long().split(",");
                    latitude = Double.valueOf(team1[0].trim())  + (Math.random() -.5) / 1500;;
                    longitude = Double.valueOf(team1[1].trim())  + (Math.random() -.5) / 1500;;
                    if (MySharedpreferences.getInstance().get(MapActivity.this,AppStrings.userID).equals(joinUserlist.get(i).getSignup_id())){
                         customMarkerLocationTwo = new LatLng(latitude, longitude);
                    }

                    LatLng customMarkerLocationOne = new LatLng(latitude, longitude);

                    String name=joinUserlist.get(i).getName();
                    Glide.with(getApplicationContext()).asBitmap()
                            .placeholder(R.drawable.avtar)
                            .load(AppStrings.ideo_path+joinUserlist.get(i).getPhoto())
                            .into(new SimpleTarget<Bitmap>() {
                                @Override
                                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {

//                                    mMap.addMarker(new MarkerOptions().position(customMarkerLocationOne).title(name).icon(BitmapDescriptorFactory.fromBitmap(bitmappros(resource))));
//                                    LatLngBounds.Builder builder = new LatLngBounds.Builder();
//                                    builder.include(customMarkerLocationTwo); //Taking Point A (First LatLng)
//                                    LatLngBounds bounds = builder.build();
//                                    CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 200);
//                                    mMap.moveCamera(cu);
//                                    mMap.animateCamera(CameraUpdateFactory.zoomTo(14), 2000, null);


                                }
                            });
                }

            }
        });




        //When Map Loads Successfully
       /* mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {

                LatLng customMarkerLocationOne = new LatLng(28.583911, 77.319116);
                LatLng customMarkerLocationTwo = new LatLng(28.583078, 77.313744);
                LatLng customMarkerLocationThree = new LatLng(28.580903, 77.317408);
                LatLng customMarkerLocationFour = new LatLng(28.580108, 77.315271);






                mMap.addMarker(new MarkerOptions().position(new LatLng(28.583911,77.319116)).
                        icon(BitmapDescriptorFactory.fromBitmap(createCustomMarker(MapActivity.this,"http://52.207.96.115/uploads/920590081.png" ,"Manish")))).setTitle("iPragmatech Solutions Pvt Lmt");
               *//* mMap.addMarker(new MarkerOptions().position(customMarkerLocationTwo).
                        icon(BitmapDescriptorFactory.fromBitmap(
                                createCustomMarker(MapActivity.this,R.drawable.logo,"Narender")))).setTitle("Hotel Nirulas Noida");

                mMap.addMarker(new MarkerOptions().position(customMarkerLocationThree).
                        icon(BitmapDescriptorFactory.fromBitmap(
                                createCustomMarker(MapActivity.this,R.drawable.logo,"Neha")))).setTitle("Acha Khao Acha Khilao");
                mMap.addMarker(new MarkerOptions().position(customMarkerLocationFour).
                        icon(BitmapDescriptorFactory.fromBitmap(
                                createCustomMarker(MapActivity.this,R.drawable.logo,"Nupur")))).setTitle("Subway Sector 16 Noida");
              *//*

                //LatLngBound will cover all your marker on Google Maps
                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                builder.include(customMarkerLocationOne); //Taking Point A (First LatLng)
                builder.include(customMarkerLocationThree); //Taking Point B (Second LatLng)
                LatLngBounds bounds = builder.build();
                CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 200);
                mMap.moveCamera(cu);
                mMap.animateCamera(CameraUpdateFactory.zoomTo(14), 2000, null);
            }
        });*/
    }

    private Bitmap bitmappros(Bitmap res){
        View marker = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.layout, null);

        CircleImageView markerImage = (CircleImageView) marker.findViewById(R.id.user_dp);

        markerImage  = marker.findViewById(R.id.user_dp);
        markerImage.setImageBitmap(res);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        marker.setLayoutParams(new ViewGroup.LayoutParams(52, ViewGroup.LayoutParams.WRAP_CONTENT));
        marker.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
        marker.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
        marker.buildDrawingCache();
        Bitmap bitmap2 = Bitmap.createBitmap(marker.getMeasuredWidth(), marker.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap2);
        marker.draw(canvas);

        return bitmap2;
    }

}