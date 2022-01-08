package com.s.video.musicas.scooby.auth;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.widget.ProgressBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.s.video.musicas.scooby.R;
import com.s.video.musicas.scooby.auth.fragment.LoginFragment;

public class AuthActivity extends AppCompatActivity {

    @BindView(R.id.progress_circular)
    ProgressBar progress_circular;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        ButterKnife.bind(this);
        openDashBoard();

    }

    private void openDashBoard() {
        Bundle args = new Bundle();
        Fragment fragmentt = new LoginFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        fragmentt.setArguments(args);
        transaction.replace(R.id.fragment, fragmentt);
        transaction.commit();
    }
}