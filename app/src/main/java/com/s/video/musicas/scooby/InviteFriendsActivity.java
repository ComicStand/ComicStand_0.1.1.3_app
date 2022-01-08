package com.s.video.musicas.scooby;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.s.video.musicas.scooby.fragment.InviteAllContactFragment;
import com.s.video.musicas.scooby.fragment.InviteOnlineFragment;
import com.s.video.musicas.scooby.nettwork.Api;
import com.s.video.musicas.scooby.nettwork.ApiClints;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class InviteFriendsActivity extends AppCompatActivity {

    @BindView(R.id.imgBAckground)
    ImageView imgBAckground;

    @BindView(R.id.progress_circular)
    ProgressBar progress_circular;


    @BindView(R.id.tabs)
    TabLayout tabs;


    @BindView(R.id.tab_viewpager)
    ViewPager tab_viewpager;

    @BindView(R.id.btShare)
    ImageView btShare;
    @BindView(R.id.ivBack)
    ImageView ivBack;

    String link, video_id, video_title;




    CompositeDisposable disposable = new CompositeDisposable();
    Api api = ApiClints.getClient().create(Api.class);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_friends);

        ButterKnife.bind(this);

        tabs.setupWithViewPager(tab_viewpager);
        link = getIntent().getStringExtra("link");
        video_id = getIntent().getStringExtra("video_id");
        video_title = getIntent().getStringExtra("video_title");
        setupViewPager(tab_viewpager);

        Glide.with(this).load(R.drawable.blankpage).into(imgBAckground);

        btShare.setOnClickListener(v -> shareCode());
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
    private void setupViewPager(ViewPager viewPager) {
        ContactActivity.ViewPagerAdapter adapter = new ContactActivity.ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new InviteOnlineFragment(link, video_id, video_title), getString(R.string.app_name));
        adapter.addFragment(new InviteAllContactFragment(), "Contact");
        viewPager.setAdapter(adapter);

    }

    private void shareCode() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT,   "https://play.google.com/store/apps/details?id=" + getPackageName());
        intent.putExtra(Intent.EXTRA_SUBJECT, "Code");
        startActivity(intent);
    }


   public static class ViewPagerAdapter extends FragmentPagerAdapter {
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



    private void showProgress() {
        progress_circular.setVisibility(View.VISIBLE);
    }


    private void hideProgress() {
        progress_circular.setVisibility(View.INVISIBLE);
    }



}