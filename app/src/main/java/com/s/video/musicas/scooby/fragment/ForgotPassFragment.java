package com.s.video.musicas.scooby.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.s.video.musicas.scooby.DashboardActivity;
import com.s.video.musicas.scooby.PremiumVideoActivity;
import com.s.video.musicas.scooby.R;
import com.s.video.musicas.scooby.auth.fragment.LoginFragment;
import com.s.video.musicas.scooby.nettwork.Api;
import com.s.video.musicas.scooby.nettwork.ApiClints;
import com.s.video.musicas.scooby.nettwork.model.OtpModel;
import com.s.video.musicas.scooby.utils.AppStrings;
import com.s.video.musicas.scooby.utils.MySharedpreferences;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ForgotPassFragment extends Fragment {
    Api api = ApiClints.getClient().create(Api.class);

    @BindView(R.id.progress_circular)
    ProgressBar progress_circular;
    @BindView(R.id.edPassword)
    EditText edPassword;
    @BindView(R.id.edCPassword)
    EditText edCPassword;
    @BindView(R.id.btChange)
    Button btChange;
    String number;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_forgot_pass, container, false);
        ButterKnife.bind(this, view);

        api = ApiClints.getClient().create(Api.class);
        number = getArguments().getString("number");
        btChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updatePassword();
            }
        });
        return view;
    }


    private void updatePassword() {
        String pass = edPassword.getText().toString().trim();
        String cPass = edCPassword.getText().toString().trim();
        if (pass.isEmpty()) {
            edPassword.setError("Empty!");
        } else if(cPass.isEmpty()){
            edCPassword.setError("Empty!");
        } else if(!pass.equals(cPass)){
            Toast.makeText(getContext(), "Password not matched.", Toast.LENGTH_SHORT).show();
        }else {
            showProgress();
            api.updatePassword(number, pass).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new SingleObserver<OtpModel>() {
                        @Override
                        public void onSubscribe(@NonNull Disposable d) {

                        }

                        @Override
                        public void onSuccess(@NonNull OtpModel otpModel) {
                            hideProgress();
                            Toast.makeText(getContext(), otpModel.getMessage(), Toast.LENGTH_SHORT).show();
                            getActivity().onBackPressed();

                           /* Intent intent = new Intent(getActivity(), LoginFragment.class);
                            startActivity(intent);*/

                        }


                        @Override
                        public void onError(@NonNull Throwable e) {
                            hideProgress();
                            Toast.makeText(getContext(), "Wrong OTP", Toast.LENGTH_SHORT).show();

                        }
                    });
        }
    }



    private void showProgress() {
        progress_circular.setVisibility(View.VISIBLE);
    }


    private void hideProgress() {
        progress_circular.setVisibility(View.INVISIBLE);
    }
}