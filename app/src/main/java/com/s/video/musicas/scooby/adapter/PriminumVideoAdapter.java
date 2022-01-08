package com.s.video.musicas.scooby.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import io.reactivex.rxjava3.disposables.CompositeDisposable;
import com.s.video.musicas.scooby.R;
import com.s.video.musicas.scooby.nettwork.Api;
import com.s.video.musicas.scooby.nettwork.ApiClints;
import com.s.video.musicas.scooby.nettwork.model.PremiumVideoModel;
import com.s.video.musicas.scooby.utils.AppStrings;

public class PriminumVideoAdapter extends RecyclerView.Adapter<PriminumVideoAdapter.ViewHolder> {

    Context context;
    List<PremiumVideoModel.Datum> list;
    Check check;


    CompositeDisposable disposable = new CompositeDisposable();
    Api api = ApiClints.getClient().create(Api.class);

    public PriminumVideoAdapter(Context context, List<PremiumVideoModel.Datum> list) {
        this.context = context;
        this.list = list;
        this.check= (Check) context;
    }

    @NonNull
    @Override
    public PriminumVideoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.video_adapter, parent, false);
        return new ViewHolder(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull PriminumVideoAdapter.ViewHolder holder, int position) {
        PremiumVideoModel.Datum datum = list.get(position);
        holder.txtTitle.setText(datum.getTitle());
        if (datum.getPaymentStatus()== null){
            holder.txtAmount.setText("Rs."+datum.getAmount());
            holder.txtSubScribe.setText("Buy");

        }else {
            holder.txtAmount.setText("Rs. "+datum.getAmount());
            holder.txtSubScribe.setText("Play");
            holder.txtSubScribe.setBackgroundColor(context.getResources().getColor(R.color.green));
           // holder.txtAmount.setTextColor(context.getResources().getColor(R.color.green));

        }

        holder.txtDiscription.setText(datum.getDis());
        Glide.with(context).load(AppStrings.ideo_path+datum.getThumbnail()).into(holder.imgThumblain);

        holder.constLayoutTwo.setOnClickListener(v -> check.check(datum.getVideoId(),datum.getUrl(),datum.getAmount(),datum.getTitle()));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView  txtDiscription, txtTitle;
        ImageView imgThumblain;
        Button txtAmount,txtSubScribe;
        ConstraintLayout constLayoutTwo;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtAmount = itemView.findViewById(R.id.txtAmount);
            imgThumblain = itemView.findViewById(R.id.imgThumblain);
            txtDiscription = itemView.findViewById(R.id.txtDiscription);
            txtTitle = itemView.findViewById(R.id.txtTitle);
            constLayoutTwo=itemView.findViewById(R.id.constLayoutTwo);
            txtSubScribe=itemView.findViewById(R.id.txtSubScribe);
        }
    }
    public interface Check{
        void check(String id,String path,String amount,String video_title);
    }
   }
