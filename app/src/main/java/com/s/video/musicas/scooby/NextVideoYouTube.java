package com.s.video.musicas.scooby;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.view.View.LAYER_TYPE_SOFTWARE;

public class NextVideoYouTube extends AppCompatActivity {
    WebView webView;
    ProgressBar progressBar;
    boolean isChangeVideo = false;
    @BindView(R.id.BackPressedYoutubeWebView)
    ImageView BackPressed;


    /*public static final int REQUEST_READ_CONTACTS = 79;*/
    /*@BindView(R.id.imgAdd)
    ImageView imgAdd;*/

    /*@BindView(R.id.fb_open_video_cat)
    FloatingActionButton fb_open_video_cat;*/

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        ButterKnife.bind(this);
        webView = findViewById(R.id.webView);
        progressBar = findViewById(R.id.progressBar);
        isChangeVideo = getIntent().getBooleanExtra("changeVideo", false);
        BackPressed.setOnClickListener(v -> onBackPressed());

        // improve webView performance by Salman

        webView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        webView.getSettings().setAppCacheEnabled(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);





        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        webView.setWebChromeClient(new WebChromeClient());
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setAppCacheEnabled(true);

        // few more here by salman

        webSettings.setDomStorageEnabled(true);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webSettings.setUseWideViewPort(true);
        webSettings.setSavePassword(true);
        webSettings.setSaveFormData(true);

/*
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {

        } else {

            requestStoragePermission();
        }*/

        /* imgAdd.setOnClickListener(v -> openAdd());*/

        /*fb_open_video_cat.setOnClickListener(v -> openVideoCat());*/

        webView.loadUrl("https://m.youtube.com");


        webView.setWebChromeClient(new WebChromeClient() {

            @Override
            public void onProgressChanged(WebView view, int newProgress) {

                if (Build.VERSION.SDK_INT >= 11)
                    view.setLayerType(View.LAYER_TYPE_NONE, null);

                if (newProgress == 100) {

                    progressBar.setVisibility(View.INVISIBLE);
                    webView.setVisibility(View.VISIBLE);
                    goIntent(webView.getUrl());
                    // goIntent(webView.getUrl());
                    Log.d("TAG", "onProgressChanged: " + webView.getUrl());
                }

                super.onProgressChanged(view, newProgress);
            }
        });


    }
   /* private void openAdd() {
        Intent intent = new Intent(this, ContactActivity.class);
        startActivity(intent);
    }*/

   /* private void openVideoCat() {
        Intent intent = new Intent(this, ChooseVideoCatActivity.class);
        startActivity(intent);
    }
*/

    private void goIntent(String words) {

        webView.setVisibility(View.VISIBLE);
        String singleWord = "watch";
        String search = "#searching";

        if (words.toLowerCase().contains(singleWord.toLowerCase())) {
            String ss = words;
            String[] strr = ss.split("=");  //now str[0] is "hello" and str[1] is "goodmorning,2,1"
            String str2 = strr[1];

            webView.setVisibility(View.GONE);
            webView.loadUrl("https://m.youtube.com");

          /*  Intent intent = new Intent(DashboardActivity.this, PlayVieoActivity.class);
            intent.putExtra("video", str2);
            startActivity(intent);
            finish();*/


            getTitle(str2);
            Log.d("TAG", "goIntent: " + str2);
        } else if (words.toLowerCase().contains(search.toLowerCase())) {
            webView.setVisibility(View.VISIBLE);
            Log.d("TAG", "no: " + words);
        } else {
            webView.setVisibility(View.VISIBLE);
            // webView.goBack();
        }
    }


    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
           /* Intent intent=new Intent(DashboardActivity.this,LiveChatRoomLIstActivity.class);
            webView.clearCache(true);
            startActivity(intent);*/
            finish();
        }
    }

    /*private void requestStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(DashboardActivity.this, Manifest.permission.READ_CONTACTS)) {

        } else {
            ActivityCompat.requestPermissions(DashboardActivity.this,
                    new String[]{Manifest.permission.READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(DashboardActivity.this, "Permission GRANTED", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(DashboardActivity.this, "Permission DENIED", Toast.LENGTH_SHORT).show();
            }
        }
    }*/


    private void getTitle(String title) {
        ProgressDialog pd = new ProgressDialog(NextVideoYouTube.this);
        pd.setMessage("loading");
        webView.setVisibility(View.INVISIBLE);
        pd.show();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://www.googleapis.com/youtube/v3/videos?part=id%2C+snippet&id=" + title + "&key=AIzaSyCn-NRyyDT2wGkD0kkeXQcgqfIW88t5_1E", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String data = jsonObject.getString("items");
                    JSONArray jsonArray=new JSONArray(data);
                    for (int i=0;i<1;i++){
                        JSONObject jsonObjectt = jsonArray.getJSONObject(i);
                        String titleObject=jsonObjectt.getString("snippet");
                        JSONObject object=new JSONObject(titleObject);
                        String  titlee=object.getString("title");

                        pd.dismiss();
                        webView.clearCache(true);
                        if(isChangeVideo){
                            Intent returnIntent = new Intent();
                            returnIntent.putExtra("video",title);
                            returnIntent.putExtra("video_title",titlee);
                            setResult(Activity.RESULT_OK,returnIntent);
                            finish();
                        }else {
                            Intent intent = new Intent(NextVideoYouTube.this, PlayVieoActivity.class);
                            intent.putExtra("video", title);
                            intent.putExtra("video_title",titlee);
                            intent.putExtra("pageID","2");
                            startActivity(intent);
                            finish();
                        }


                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pd.dismiss();
                webView.setVisibility(View.VISIBLE);
                Log.d("TAG", "onErrorResponse: " + error.getMessage());
            }
        });
        Volley.newRequestQueue(NextVideoYouTube.this).add(stringRequest);
    }



}