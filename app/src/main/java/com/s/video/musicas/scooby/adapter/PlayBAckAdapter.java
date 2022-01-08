package com.s.video.musicas.scooby.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.s.video.musicas.scooby.Models.PrivacyModel;
import com.s.video.musicas.scooby.R;

import java.util.ArrayList;

public class PlayBAckAdapter extends RecyclerView.Adapter<PlayBAckAdapter.ViewHolder>  {

    Context context;
    ArrayList<PrivacyModel> list;
    BottomSheetDialog dialog;


    public PlayBAckAdapter(Context context, ArrayList<PrivacyModel> list, BottomSheetDialog dialog) {
        this.context = context;
        this.list = list;
        this.dialog = dialog;
    }

    @NonNull
    @Override
    public PlayBAckAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_option, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlayBAckAdapter.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
