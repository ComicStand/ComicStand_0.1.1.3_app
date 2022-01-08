package com.s.video.musicas.scooby.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import com.s.video.musicas.scooby.R;
import com.s.video.musicas.scooby.adapter.SocialConatactAdapter;
import com.s.video.musicas.scooby.auth.AuthActivity;
import com.s.video.musicas.scooby.nettwork.Api;
import com.s.video.musicas.scooby.nettwork.ApiClints;
import com.s.video.musicas.scooby.nettwork.model.AddFriendModel;
import com.s.video.musicas.scooby.nettwork.model.SocialContactModel;
import com.s.video.musicas.scooby.utils.AppStrings;
import com.s.video.musicas.scooby.utils.MySharedpreferences;

public class OnlineConatctFragment extends Fragment implements SocialConatactAdapter.SendReq {

    @BindView(R.id.progress_circular)
    ProgressBar progress_circular;


    @BindView(R.id.rvContactList)
    RecyclerView rvContactList;

    CompositeDisposable disposable = new CompositeDisposable();
    Api api = ApiClints.getClient().create(Api.class);

    SocialConatactAdapter subCatDetailsAdapter;


    @BindView(R.id.edSearch)
    EditText edSearch;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_online_conatct, container, false);
        ButterKnife.bind(this, view);
        getData();

        edSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (subCatDetailsAdapter != null) {
                    subCatDetailsAdapter.getFilter().filter(s);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        return view;
    }


    private void getData() {
        showProgress();
        Log.d("TAG", "getData: " + MySharedpreferences.getInstance().get(getContext(), AppStrings.userID));
        api.getSocialContactList(MySharedpreferences.getInstance().get(getContext(), AppStrings.userID))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<SocialContactModel>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onSuccess(@NonNull SocialContactModel socialContactModel) {

                        if (socialContactModel.getStatus().equalsIgnoreCase("success")) {
                            subCatDetailsAdapter = new SocialConatactAdapter(getActivity(), socialContactModel.getData(), OnlineConatctFragment.this::req);
                            rvContactList.setLayoutManager(new GridLayoutManager(getContext(), 3));
                            rvContactList.setAdapter(subCatDetailsAdapter);
                        }
                        hideProgress();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        hideProgress();
                    }
                });
    }

    private void showProgress() {
        progress_circular.setVisibility(View.VISIBLE);
    }

    private void hideProgress() {
        progress_circular.setVisibility(View.GONE);
    }

    @Override
    public void req(String id, int pos) {
        if (MySharedpreferences.getInstance().get(getContext(), AppStrings.loginType).equalsIgnoreCase("guest")) {
            Intent intent = new Intent(getContext(), AuthActivity.class);
            startActivity(intent);
        } else {
            sendReq(id, pos);
        }

    }


    private void sendReq(String id, int pos) {
        showProgress();
        api.addFriend(id, MySharedpreferences.getInstance().get(getContext(), AppStrings.userID))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new SingleObserver<AddFriendModel>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onSuccess(@NonNull AddFriendModel addFriendModel) {

                        if (addFriendModel.getStatus().equals("success"))
                            hideProgress();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        hideProgress();
                    }
                });
    }
}