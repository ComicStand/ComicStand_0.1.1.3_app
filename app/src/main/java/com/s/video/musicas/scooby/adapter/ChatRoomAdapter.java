package com.s.video.musicas.scooby.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import com.s.video.musicas.scooby.PlayVieoActivity;
import com.s.video.musicas.scooby.R;
import com.s.video.musicas.scooby.nettwork.model.ModelClass;
import com.s.video.musicas.scooby.nettwork.model.ModelClassData;

public class ChatRoomAdapter extends RecyclerView.Adapter<ChatRoomAdapter.ViewHolder> {

    List<ModelClassData.Datum> list;
    List<ModelClass> listImage=new ArrayList<>();
    Context context;






    public ChatRoomAdapter(List<ModelClassData.Datum> list, Context context) {
        this.list = list;
        this.context = context;

    }

    @NonNull
    @Override
    public ChatRoomAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.chat_video_adapter, parent, false);
        return new ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(@NonNull ChatRoomAdapter.ViewHolder holder, int position) {

        ModelClassData.Datum obj = list.get(position);
        holder.txtTitle.setText(obj.getTitle());
        Glide.with(context).load("https://img.youtube.com/vi/" + obj.getVedioId() + "/sddefault.jpg").into(holder.imgThumblain);
        holder.txtType.setText(obj.getType());
        holder.itemView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PlayVieoActivity.class);
                intent.putExtra("video", obj.getVedioId());
                intent.putExtra("video_title", obj.getTitle());
                intent.putExtra("pageID", "1");
                intent.putExtra("groupID", obj.getGroup_id());
                context.startActivity(intent);
                ((Activity) context).finish();


            }
        });


        holder.rvUserList.setHasFixedSize(true);
        holder.rvUserList.addItemDecoration(new OverlapDecoration());
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        holder.rvUserList.setLayoutManager(layoutManager);



        listImage=new ArrayList<>();
        for (int i=0;i<obj.getUser().size();i++){
            ModelClass aClass=new ModelClass(obj.getUser().get(i).getPhoto());
            listImage.add(aClass);
        }

        if (!obj.getUser().isEmpty()){
            listImage.add(new ModelClass("data"));
            RecyclerViewAdapter videoAdapter = new RecyclerViewAdapter( context,listImage,"");
            holder.rvUserList.setAdapter(videoAdapter);

        }

    }

    @Override
    public int getItemCount() {
        return list.size();

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtDiscription, txtTitle,txtType;
        ImageView imgThumblain;
        ConstraintLayout constLayoutTwo;
        RecyclerView rvUserList;
        private RecyclerViewAdapter mAdapter;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgThumblain = itemView.findViewById(R.id.imgThumblain);
            txtDiscription = itemView.findViewById(R.id.txtDiscription);
            txtTitle = itemView.findViewById(R.id.txtTitle);
            constLayoutTwo = itemView.findViewById(R.id.constLayoutTwo);
            rvUserList = itemView.findViewById(R.id.rvUserList);
            txtType=itemView.findViewById(R.id.txtType);

        }
    }
}
