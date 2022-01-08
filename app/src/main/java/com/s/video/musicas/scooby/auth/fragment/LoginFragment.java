package com.s.video.musicas.scooby.auth.fragment;

import static androidx.core.app.ActivityCompat.finishAffinity;

import android.content.Intent;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

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
import com.s.video.musicas.scooby.fragment.ForgotPassFragment;
import com.s.video.musicas.scooby.nettwork.Api;
import com.s.video.musicas.scooby.nettwork.ApiClints;
import com.s.video.musicas.scooby.nettwork.model.LoginModel;
import com.s.video.musicas.scooby.utils.AppStrings;
import com.s.video.musicas.scooby.utils.MySharedpreferences;


public class LoginFragment extends Fragment {


    @BindView(R.id.SignUpHere)
    RadioButton SignUpHere;

    @BindView(R.id.btLogin)
    Button btLogin;

    @BindView(R.id.btSignInWithOtp)
    Button btSignInWithOtp;

    @BindView(R.id.btForgotPass)
    Button btForgotPass;

    @BindView(R.id.edUserName)
    EditText edUserName;

    @BindView(R.id.edPassword)
    EditText edPassword;

    @BindView(R.id.progress_circular)
    ProgressBar progress_circular;

   /* @BindView(R.id.showPassword)
    EditText showPassword;*/



    CompositeDisposable disposable = new CompositeDisposable();
    Api api = ApiClints.getClient().create(Api.class);

    @BindView(R.id.btGuestLogin)
    Button btGuestLogin;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        ButterKnife.bind(this, view);
        btLogin.setOnClickListener(v -> login());
        btSignInWithOtp.setOnClickListener(v -> loginWithOtp());
        btForgotPass.setOnClickListener(v -> forgotPass());
        SignUpHere.setOnClickListener(v -> intentLogin());
        btGuestLogin.setOnClickListener(v -> guestLogin());
        /*showPassword.setOnClickListener(v -> viewPassword());*/


        return view;
    }

    private void loginWithOtp() {
        String userID = edUserName.getText().toString().trim();

        if (userID.isEmpty()) {
            edUserName.setError("Empty");
        } else{
            openOTP(userID, false);
        }
    }

    private void forgotPass() {
        String userID = edUserName.getText().toString().trim();
        if (userID.isEmpty()) {
            edUserName.setError("Empty");
        } else{
            openOTP(userID, true);
        }
    }

    /*private void viewPassword(){
        edPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
    }*/


    private void openOTP(String number, boolean isForgot) {
        Bundle args = new Bundle();
        Fragment fragmentt = new OtpFragment();
        args.putString("number", number);
        args.putBoolean("isForgotPass", isForgot);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        fragmentt.setArguments(args);
        transaction.replace(R.id.fragment, fragmentt);
        transaction.addToBackStack("otp").commit();
    }


    private void login() {
        String userID = edUserName.getText().toString().trim();
        String password = edPassword.getText().toString().trim();

        if (userID.isEmpty()) {
            edUserName.setError("Empty");
        } else if (password.isEmpty()) {
            edPassword.setError("Empty");
        } else {
            showProgress();
            api.login(userID, password).observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(new SingleObserver<LoginModel>() {
                        @Override
                        public void onSubscribe(@NonNull Disposable d) {

                        }

                        @Override
                        public void onSuccess(@NonNull LoginModel loginModel) {
                            hideProgress();
                            if (loginModel.getStatus().equalsIgnoreCase("success")) {
                                MySharedpreferences.getInstance().save(getContext(), AppStrings.userID, loginModel.getData().get(0).getSignupId());
                                MySharedpreferences.getInstance().save(getContext(),AppStrings.loginType,"login");
                                MySharedpreferences.getInstance().save(getContext(),AppStrings.name,loginModel.getData().get(0).getName());
                                MySharedpreferences.getInstance().save(getContext(),AppStrings.mobile,loginModel.getData().get(0).getMobile());

                                //Toast.makeText(getContext(), MySharedpreferences.getInstance().get(getContext(),AppStrings.name)+"", Toast.LENGTH_SHORT).show();
                                Toast.makeText(getContext(), loginModel.getMessage(), Toast.LENGTH_SHORT).show();
                                goDashBoad();
                            } else {
                                Toast.makeText(getContext(), loginModel.getMessage(), Toast.LENGTH_SHORT).show();

                            }
                        }

                        @Override
                        public void onError(@NonNull Throwable e) {
                            hideProgress();
                            Toast.makeText(getContext(), "Invalid id or Password", Toast.LENGTH_SHORT).show();

                        }
                    });
        }

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
        progress_circular.setVisibility(View.GONE);
    }

    private void intentLogin() {
        Bundle args = new Bundle();
        Fragment fragmentt = new RegFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        fragmentt.setArguments(args);
        transaction.replace(R.id.fragment, fragmentt);
        transaction.addToBackStack("reg").commit();

    }




    private void guestLogin() {
        int random = ThreadLocalRandom.current().nextInt(0, 10000000);
        MySharedpreferences.getInstance().save(getContext(), AppStrings.userID, String.valueOf(random));
        MySharedpreferences.getInstance().save(getContext(),AppStrings.loginType,"guest");
        MySharedpreferences.getInstance().save(getContext(),AppStrings.name,"Guest Login");

        goDashBoad();

    }

}