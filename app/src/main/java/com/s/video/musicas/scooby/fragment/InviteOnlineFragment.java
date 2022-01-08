package com.s.video.musicas.scooby.fragment;

import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.s.video.musicas.scooby.R;
import com.s.video.musicas.scooby.adapter.InviteOnlineConatactAdapter;
import com.s.video.musicas.scooby.adapter.SocialConatactAdapter;
import com.s.video.musicas.scooby.nettwork.Api;
import com.s.video.musicas.scooby.nettwork.ApiClints;
import com.s.video.musicas.scooby.nettwork.model.ModelClassData;
import com.s.video.musicas.scooby.nettwork.model.SocialContactModel;
import com.s.video.musicas.scooby.utils.AppStrings;
import com.s.video.musicas.scooby.utils.MySharedpreferences;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class InviteOnlineFragment extends Fragment implements InviteOnlineConatactAdapter.SendReq{

    String link, video_id, video_title;
    ArrayList<SocialContactModel.Datum> inviteList = new ArrayList<>();
    List<SocialContactModel.Datum> dataList;
    int items =0;

    public InviteOnlineFragment(String link, String video_id, String video_title){
        this.link = link;
        this.video_id = video_id;
        this.video_title = video_title;
    }

    @BindView(R.id.progress_circular)
    ProgressBar progress_circular;


    @BindView(R.id.rvContactList)
    RecyclerView rvContactList;
    @BindView(R.id.contacInvite)
    ConstraintLayout contacInvite;
    @BindView(R.id.txtInvite)
    TextView txtInvite;

    CompositeDisposable disposable = new CompositeDisposable();
    Api api = ApiClints.getClient().create(Api.class);

    InviteOnlineConatactAdapter subCatDetailsAdapter;


    @BindView(R.id.edSearch)
    EditText edSearch;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_invite_online, container, false);
        ButterKnife.bind(this, view);
        getData();
        contacInvite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                items = inviteList.size()-1;
                sendInvitation(inviteList.get(items).getUserId());
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
                            dataList = socialContactModel.getData();
                            subCatDetailsAdapter = new InviteOnlineConatactAdapter(getActivity(), dataList, InviteOnlineFragment.this::req);
                            rvContactList.setLayoutManager(new LinearLayoutManager(getContext()));
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
    public void req(boolean isChecked, int pos) {
        if(inviteList.contains(dataList.get(pos))){
            inviteList.remove(dataList.get(pos));
        }else {
            inviteList.add(dataList.get(pos));
        }

        if(isChecked){
            dataList.get(pos).setFrnd_status("1");
        }else {
            dataList.get(pos).setFrnd_status("0");
        }

        subCatDetailsAdapter.notifyItemChanged(pos);

        if(inviteList.size()>5){
            Toast.makeText(getContext(), "You can only invite 5 contact at a time.", Toast.LENGTH_SHORT).show();
            contacInvite.setVisibility(View.GONE);
        }else if(inviteList.size()<1){
            contacInvite.setVisibility(View.GONE);
        }else {
            contacInvite.setVisibility(View.VISIBLE);
        }

        txtInvite.setText("Send "+inviteList.size()+" invite");


    }


    private void sendInvitation(String userId) {
        Log.e("onSuccess", "sendInvitation , "+userId);
        api.sendInvitation(userId, link, video_id, video_title)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new SingleObserver<ModelClassData>() {
                    @Override
                    public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {

                    }


                    @Override
                    public void onSuccess(@io.reactivex.rxjava3.annotations.NonNull ModelClassData modelClassData) {
                        try {
                            if (modelClassData.getStatus().equalsIgnoreCase("success")) {
                                Log.e("onSuccess", modelClassData.getMessage()+" , "+userId);
                                Toast.makeText(getActivity(), modelClassData.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                            items--;
                            if(items!=-1){
                                sendInvitation(inviteList.get(items).getUserId());
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                        Log.e("onError", e.getMessage());

                    }
                });
    }
}