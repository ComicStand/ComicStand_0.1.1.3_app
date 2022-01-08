package com.s.video.musicas.scooby.adapter;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.s.video.musicas.scooby.Models.PrivacyModel;
import com.s.video.musicas.scooby.R;
import com.s.video.musicas.scooby.utils.AppStrings;
import com.s.video.musicas.scooby.utils.MySharedpreferences;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BottomSheetDialog extends BottomSheetDialogFragment {

    String status = "";
    TextView txtNearBy;
    TextView txtPublic, txtFriends;
    RecyclerView rvListItem, rvPlayBackList, rvPlayVoip;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable
            ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.setting_layout,
                container, false);
        rvListItem = v.findViewById(R.id.rvListItem);
        rvPlayBackList = v.findViewById(R.id.rvPlayBackList);
        rvPlayVoip = v.findViewById(R.id.rvPlayVoip);
        getPrivacy();
        return v;
    }

    private void getPrivacy() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://52.207.96.115/index.php/App/get_privacy/", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d("TAG", "onResponse: "+response);
                ArrayList<PrivacyModel> list = new ArrayList<>();
                ArrayList<PrivacyModel> playBack = new ArrayList<>();
                ArrayList<PrivacyModel> voip = new ArrayList<>();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("status").equalsIgnoreCase("Success")) {
                        JSONArray jsonArray = new JSONArray(jsonObject.getString("data"));
                        for (int i = 0; i < 4; i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            PrivacyModel privacyModel = new PrivacyModel(1, jsonObject1.getString("name"), jsonObject1.getString("icon"), jsonObject1.getString("status"));
                            list.add(privacyModel);
                        }


                        for (int i = 4; i < 8; i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            PrivacyModel privacyModel = new PrivacyModel(1, jsonObject1.getString("name"), jsonObject1.getString("icon"), jsonObject1.getString("status"));
                            playBack.add(privacyModel);
                        }


                        for (int i = 8; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            PrivacyModel privacyModel = new PrivacyModel(1, jsonObject1.getString("name"), jsonObject1.getString("icon"), jsonObject1.getString("status"));
                            voip.add(privacyModel);
                        }


                        sortData(list);
                        PrivacyAdapter videoAdapter = new PrivacyAdapter(getActivity(), list, BottomSheetDialog.this);
                        rvListItem.setAdapter(videoAdapter);


                        sortData(playBack);
                        PlayNAckAdapter playBackAdapter = new PlayNAckAdapter(getActivity(), playBack, BottomSheetDialog.this);
                        rvPlayBackList.setAdapter(playBackAdapter);


                        sortData(voip);
                        VoipAdapter play = new VoipAdapter(getActivity(), voip, BottomSheetDialog.this);
                        rvPlayVoip.setAdapter(play);
                        Log.d("TAG", "onResponse: " + list.size());

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.d("TAG", "onErrorResponse: " + error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("vedio_id", MySharedpreferences.getInstance().get(getContext(), AppStrings.VIDEO_ID));
                map.put("user_id", MySharedpreferences.getInstance().get(getContext(), AppStrings.userID));
                return map;
            }
        };
        Volley.newRequestQueue(getActivity()).add(stringRequest);
    }


    public void getPrivacyStatus() {
        getPrivacy();
    }


    private void sortData(List<PrivacyModel> list) {
        Collections.sort(list, new Comparator<PrivacyModel>() {
            @Override
            public int compare(PrivacyModel o1, PrivacyModel o2) {
                return o2.getStats().compareTo(o1.getStats());
            }
        });
    }
}
