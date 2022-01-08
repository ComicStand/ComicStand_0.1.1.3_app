package com.s.video.musicas.scooby.auth.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.goodiebag.pinview.Pinview;

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
import com.s.video.musicas.scooby.fragment.ForgotPassFragment;
import com.s.video.musicas.scooby.nettwork.Api;
import com.s.video.musicas.scooby.nettwork.ApiClints;
import com.s.video.musicas.scooby.nettwork.model.OtpModel;
import com.s.video.musicas.scooby.utils.AppStrings;
import com.s.video.musicas.scooby.utils.MySharedpreferences;


public class OtpFragment extends Fragment {

    @BindView(R.id.pinOtp)
    Pinview pinOtp;

    @BindView(R.id.txtVerificationCode)
    TextView txtVerificationCode;


    @BindView(R.id.btVerify)
    Button btVerify;

    String number;
    boolean isForgot;


    CompositeDisposable disposable = new CompositeDisposable();
    Api api = ApiClints.getClient().create(Api.class);



    @BindView(R.id.progress_circular)
    ProgressBar progress_circular;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_otp, container, false);
        ButterKnife.bind(this, view);

        api = ApiClints.getClient().create(Api.class);
        number = getArguments().getString("number");
        isForgot = getArguments().getBoolean("isForgotPass");
        sendOtp();
        txtVerificationCode.setText(getString(R.string.verification_code_sent_to, number));
        btVerify.setOnClickListener(v -> hitOtp());
        return view;
    }

    private void sendOtp(){
        showProgress();
        api.sendOtp(number).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<OtpModel>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onSuccess(@NonNull OtpModel otpModel) {
                        hideProgress();
                        Toast.makeText(getContext(), otpModel.getMessage(), Toast.LENGTH_SHORT).show();
                    }


                    @Override
                    public void onError(@NonNull Throwable e) {
                        hideProgress();
                    }
                });
    }

    private void hitOtp() {
        String otp = pinOtp.getValue();
        if (otp.length() < 5) {
            Toast.makeText(getContext(), "Wrong Otp", Toast.LENGTH_SHORT).show();
        } else {
            showProgress();
            String no = number;
            api.otpVery(no, otp).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new SingleObserver<OtpModel>() {
                        @Override
                        public void onSubscribe(@NonNull Disposable d) {

                        }

                        @Override
                        public void onSuccess(@NonNull OtpModel otpModel) {
                            hideProgress();
                            if (otpModel.getStatus().equalsIgnoreCase("success")) {
                                if (isForgot) {
                                    openForgotPass(number);
                                } else {
                                    MySharedpreferences.getInstance().save(getContext(), AppStrings.userID, otpModel.getData().get(0).getSignupId());
                                    Toast.makeText(getContext(), otpModel.getMessage(), Toast.LENGTH_SHORT).show();
                                    MySharedpreferences.getInstance().save(getContext(),AppStrings.loginType,"login");
                                    MySharedpreferences.getInstance().save(getContext(),AppStrings.name,otpModel.getData().get(0).getName());
                                    MySharedpreferences.getInstance().save(getContext(),AppStrings.mobile,otpModel.getData().get(0).getMobile());
                                    goDashBoad();
                                }

                            } else {
                                Toast.makeText(getContext(), otpModel.getMessage(), Toast.LENGTH_SHORT).show();

                            }
                        }


                        @Override
                        public void onError(@NonNull Throwable e) {
                            hideProgress();
                            Toast.makeText(getContext(), "Wrong OTP", Toast.LENGTH_SHORT).show();

                        }
                    });
        }
    }

    private void openForgotPass(String number) {
        Bundle args = new Bundle();
        Fragment fragmentt = new ForgotPassFragment();
        args.putString("number", number);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        fragmentt.setArguments(args);
        transaction.replace(R.id.fragment, fragmentt);
        transaction.addToBackStack("forgot").commit();
    }

    private void goDashBoad() {
        Intent intent = new Intent(getContext(), LiveChatRoomLIstActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        getActivity().finish();
    }

    private void showProgress() {
        progress_circular.setVisibility(View.VISIBLE);
    }


    private void hideProgress() {
        progress_circular.setVisibility(View.INVISIBLE);
    }

}