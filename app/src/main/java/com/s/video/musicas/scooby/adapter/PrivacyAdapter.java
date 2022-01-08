package com.s.video.musicas.scooby.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.s.video.musicas.scooby.Models.PrivacyModel;
import com.s.video.musicas.scooby.R;
import com.s.video.musicas.scooby.utils.AppStrings;
import com.s.video.musicas.scooby.utils.MySharedpreferences;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PrivacyAdapter  extends RecyclerView.Adapter<PrivacyAdapter.ViewHolder> {
    Context context;
    ArrayList<PrivacyModel> list;
    BottomSheetDialog dialog;
    String publicSatus="0";
    String nearBy="0";
    String frinds="0";
    String lock="0";





    public PrivacyAdapter(Context context, ArrayList<PrivacyModel> list, BottomSheetDialog dialog) {
        this.context = context;
        this.list = list;
        this.dialog = dialog;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        View view = LayoutInflater.from(context).inflate(R.layout.item_option, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        PrivacyModel itemModel = list.get(position);
        holder.tvItem.setText(itemModel.getName());
        Glide.with(context).load(itemModel.getImage()).into(holder.imgLogo);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MySharedpreferences.getInstance().get(context,AppStrings.CHECKSETTINGSTATUS) != null){
                    if (MySharedpreferences.getInstance().get(context,AppStrings.CHECKSETTINGSTATUS).equalsIgnoreCase("1")){
                        if (itemModel.getName().equalsIgnoreCase("Public")) {
                            publicSatus = "1";
                        }
                        else if (itemModel.getName().equalsIgnoreCase("Friend Only")){
                            frinds="1";
                        }else if (itemModel.getName().equalsIgnoreCase("Near By")){
                            nearBy="1";
                        }else if (itemModel.getName().equalsIgnoreCase("Invite Only")){
                            lock="1";
                        }
                    }
                }


                sendStatus();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvItem;
        ImageView imgLogo;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvItem = itemView.findViewById(R.id.tvItem);
            imgLogo=itemView.findViewById(R.id.imgLogo);
        }
    }
    private void sendStatus(){
        StringRequest stringRequest=new StringRequest(Request.Method.POST, "http://52.207.96.115/index.php/App/update_privacy", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d("TAG", "onResponse: "+response);
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    Toast.makeText(context, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    dialog.getPrivacyStatus();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.d("TAG", "onErrorResponse: "+error.getMessage());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String >map=new HashMap<>();
                map.put("vedio_id", MySharedpreferences.getInstance().get(context, AppStrings.VIDEO_ID));
                map.put("user_id",MySharedpreferences.getInstance().get(context,AppStrings.userID));
                map.put("public_status", publicSatus);
                map.put("frnd_status",frinds);
                map.put("nearby_status", nearBy);
                map.put("invite_status",lock);
                return map;
            }
        };
        Volley.newRequestQueue(context).add(stringRequest);
    }
}