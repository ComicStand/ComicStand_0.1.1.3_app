package com.s.video.musicas.scooby.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import com.s.video.musicas.scooby.R;
import com.s.video.musicas.scooby.nettwork.model.SocialContactModel;
import com.s.video.musicas.scooby.utils.AppStrings;

public class SocialConatactAdapter extends RecyclerView.Adapter<SocialConatactAdapter.ViewHolder> implements Filterable {

    Context context;
    private List<SocialContactModel.Datum> contactListFiltered;
    List<SocialContactModel.Datum> list;
    SendReq req;

    public SocialConatactAdapter(Context context, List<SocialContactModel.Datum> list, SendReq req) {
        this.context = context;
        this.list = list;
        this.contactListFiltered = list;
        this.req = req;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.social_conatct, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SocialContactModel.Datum datum = contactListFiltered.get(position);
        holder.txtName.setText(datum.getName());
        Glide.with(context).load(AppStrings.ideo_path + datum.getPhoto()).placeholder(R.drawable.avtar).into(holder.ivUserAvtar);


        if (datum.getFrnd_status() == null) {
            Glide.with(context).load(R.drawable.userwithplush).into(holder.friendStatus);
        } else if (datum.getFrnd_status().equals("0")) {
            Glide.with(context).load(R.drawable.userwithplush).into(holder.friendStatus);
        } else if (datum.getFrnd_status().equals("1")) {
            holder.friendStatus.setVisibility(View.INVISIBLE);
        } else {
            Glide.with(context).load(R.drawable.ic_baseline_hourglass_full_24).into(holder.friendStatus);
        }

        holder.friendStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                req.req(datum.getUserId(), holder.getAdapterPosition());
                Glide.with(context).load(R.drawable.ic_baseline_hourglass_full_24).into(holder.friendStatus);

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
        ImageView friendStatus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivUserAvtar = itemView.findViewById(R.id.ivUserAvtar);
            txtName = itemView.findViewById(R.id.textView5);
            friendStatus = itemView.findViewById(R.id.imageView3);

        }
    }


    public interface SendReq {
        void req(String id, int pos);
    }
}
