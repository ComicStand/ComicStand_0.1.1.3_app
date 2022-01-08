package com.s.video.musicas.scooby;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import com.s.video.musicas.scooby.nettwork.Api;
import com.s.video.musicas.scooby.nettwork.ApiClints;
import com.s.video.musicas.scooby.nettwork.model.PaymentModel;
import com.s.video.musicas.scooby.utils.AppStrings;
import com.s.video.musicas.scooby.utils.MySharedpreferences;

public class RazorpayActivity extends AppCompatActivity implements PaymentResultListener {

    private AlertDialog alertDialog;

    private double amount;
    double fAmount;
    String videoID;
    CompositeDisposable disposable = new CompositeDisposable();
    Api api = ApiClints.getClient().create(Api.class);

    String discount;
    String path,video_title;

    private String pageID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_razorpay);

        path=getIntent().getStringExtra("path");
        videoID=getIntent().getStringExtra("video_id");
        video_title=getIntent().getStringExtra("video_title");
        amount = Double.parseDouble(getIntent().getStringExtra("amount"));
        pageID=getIntent().getStringExtra("pageID");
        fAmount = 100 * amount;
        String number = MySharedpreferences.getInstance().get(this, AppStrings.mobile);
        startPayment(String.valueOf(fAmount), getString(R.string.app_name), number, "premium@scooby.live");

        //sendPayem("jfrgfurgd495847");
    }

    public void startPayment(final String amount, final String name, final String userNUmber, String mail) {
        final Activity activity = this;
        final Checkout co = new Checkout();

        try {
            JSONObject options = new JSONObject();
            options.put("name", name);
            options.put("description", video_title);
            options.put("image", "https://scooby.live/wp-content/uploads/2021/08/Scooby-Final-1.1.1-100x101.png");
            // options.put("image","http://anandimagicworld.com/Anandi%20Magic%20World%20Logo.png");
            options.put("currency", "INR");
            options.put("amount", amount);
            JSONObject preFill = new JSONObject();
            preFill.put("email", mail);
            preFill.put("contact", userNUmber);
            options.put("prefill", preFill);
            options.put("notes" , name);
            co.open(activity, options);
        } catch (Exception e) {
            Toast.makeText(activity, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT)
                    .show();
            e.printStackTrace();
            onBackIntent();
        }
    }

    @SuppressWarnings("unused")
    @Override
    public void onPaymentSuccess(String razorpayPaymentID) {
        try {
            System.out.println(razorpayPaymentID);


            sendPayem(razorpayPaymentID);

            //         Toast.makeText(this, "Payment Successful: " + razorpayPaymentID, Toast.LENGTH_SHORT).show();
            // senduserDetails(mysharedpreferences.getSaveUSERID(), razorpayPaymentID, String.valueOf(amt), "true");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unused")
    @Override
    public void onPaymentError(int code, String response) {
        try {
            Toast.makeText(this, "Transaction Error: Payment failed" , Toast.LENGTH_SHORT).show();
            //  senduserDetails(mysharedpreferences.getSaveUSERID(), "654654754", String.valueOf(amt), "false");
            onBackIntent();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void onBackIntent() {
        Intent intent=new Intent(RazorpayActivity.this,PremiumVideoActivity.class);
        startActivity(intent);
        finish();
    }


    private void sendPayem(String iID) {
        api.sendPayment(MySharedpreferences.getInstance().get(this, AppStrings.userID), videoID, "1",iID, String.valueOf(amount))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new SingleObserver<PaymentModel>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onSuccess(@NonNull PaymentModel paymentModel) {


                        Log.d("TAG", "onSuccess: "+paymentModel.getStatus());
                        if (paymentModel.getStatus().equalsIgnoreCase("success")) {
                            Intent intent = new Intent(RazorpayActivity.this, PriumumVideoPlayerActivity.class);
                            intent.putExtra("path",path);
                            intent.putExtra("video_id",videoID);
                            intent.putExtra("video_title",video_title);
                            intent.putExtra("pageID",pageID);
                            startActivity(intent);
                            finish();
                        }else {
                            onBackIntent();
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.d("TAG", "onError: "+e.getMessage());
                    }
                });
    }


}