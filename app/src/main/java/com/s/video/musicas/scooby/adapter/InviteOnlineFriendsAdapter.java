package com.s.video.musicas.scooby.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.s.video.musicas.scooby.R;

public class InviteOnlineFriendsAdapter extends RecyclerView.Adapter<InviteOnlineFriendsAdapter.ViewHolder> implements Filterable {

    Context context;

    @Override
    public Filter getFilter() {
        return null;
    }

    @NonNull
    @Override
    public InviteOnlineFriendsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.inive_online, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InviteOnlineFriendsAdapter.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox checkbox;
        TextView txtUserName;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            checkbox=itemView.findViewById(R.id.checkbox);
            txtUserName=itemView.findViewById(R.id.txtUserName);
        }
    }
}
