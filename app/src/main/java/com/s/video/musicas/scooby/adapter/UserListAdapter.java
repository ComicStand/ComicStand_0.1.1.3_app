package com.s.video.musicas.scooby.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.s.video.musicas.scooby.R;
import com.s.video.musicas.scooby.nettwork.model.LiveUserModel;
import com.s.video.musicas.scooby.utils.AppStrings;

import java.util.List;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.ViewHolder> {

    Context context;
    List<LiveUserModel.Data> list;

    public UserListAdapter(Context context, List<LiveUserModel.Data> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public UserListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.user_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserListAdapter.ViewHolder holder, int position) {
        LiveUserModel.Data data = list.get(position);
        Glide.with(context)
                .load(AppStrings.ideo_path + data.getPhoto())
                .placeholder(R.drawable.avtar)
                .centerCrop()
                .into(holder.imgProfile);
        if(data.getUser_type().equals("Admin")){
            holder.txtUserType.setVisibility(View.VISIBLE);
        }else {
            holder.txtUserType.setVisibility(View.GONE);
        }

        holder.txtUserName.setText(data.getName());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtUserName;
        TextView txtUserType;
        ImageView imgProfile;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProfile = itemView.findViewById(R.id.imgProfile);
            txtUserName = itemView.findViewById(R.id.txtUserName);
            txtUserType = itemView.findViewById(R.id.txtUserType);
        }
    }
}
