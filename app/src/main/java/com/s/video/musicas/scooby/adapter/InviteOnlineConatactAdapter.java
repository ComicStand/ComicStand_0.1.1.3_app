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

import com.bumptech.glide.Glide;
import com.s.video.musicas.scooby.R;
import com.s.video.musicas.scooby.nettwork.model.SocialContactModel;
import com.s.video.musicas.scooby.utils.AppStrings;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class InviteOnlineConatactAdapter extends RecyclerView.Adapter<InviteOnlineConatactAdapter.ViewHolder> implements Filterable {

    Context context;
    private List<SocialContactModel.Datum> contactListFiltered;
    List<SocialContactModel.Datum> list;
    SendReq req;

    public InviteOnlineConatactAdapter(Context context, List<SocialContactModel.Datum> list, SendReq req) {
        this.context = context;
        this.list = list;
        this.contactListFiltered = list;
        this.req = req;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.invite_online_contact, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SocialContactModel.Datum datum = contactListFiltered.get(position);
        holder.txtName.setText(datum.getName());
        Glide.with(context).load(AppStrings.ideo_path + datum.getPhoto()).placeholder(R.drawable.avtar).into(holder.ivUserAvtar);
        holder.friendStatus.setEnabled(false);

        try {
            if(datum.getFrnd_status()==null){
                holder.friendStatus.setChecked(false);
            }else if (datum.getFrnd_status().equals("0")) {
                holder.friendStatus.setChecked(true);
            } else if (datum.getFrnd_status().equals("1")) {
                holder.friendStatus.setChecked(false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                req.req(holder.friendStatus.isChecked(), holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return contactListFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    contactListFiltered = list;
                } else {
                    List<SocialContactModel.Datum> filteredList = new ArrayList<>();
                    for (SocialContactModel.Datum row : list) {


                        if (row.getName().toLowerCase().contains(charString.toLowerCase()) || row.getName().contains(charSequence.toString())) {
                            filteredList.add(row);

                        }
                    }

                    contactListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = contactListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                contactListFiltered = (ArrayList<SocialContactModel.Datum>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView ivUserAvtar;
        TextView txtName;
        CheckBox friendStatus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivUserAvtar = itemView.findViewById(R.id.ivUserAvtar);
            txtName = itemView.findViewById(R.id.txtUserName);
            friendStatus = itemView.findViewById(R.id.checkbox);

        }
    }


    public interface SendReq {
        void req(boolean isChecked, int pos);
    }
}
