package com.s.video.musicas.scooby.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.List;

import com.s.video.musicas.scooby.R;
import com.s.video.musicas.scooby.nettwork.model.ModelClass;
import com.s.video.musicas.scooby.nettwork.model.ModelClassData;
import com.s.video.musicas.scooby.utils.AppStrings;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private static final int TYPE_ITEM = 1;
    private static final int TYPE_FOOTER = 2;
    private static final int TYPE_HEADER = 0;
    private String mHeaderTitle;

    private OnHeaderClickListener mHeaderClickListener;


    private Context mContext;
    private List<ModelClass> modelList;

    private OnItemClickListener mItemClickListener;


    public RecyclerViewAdapter(Context context, List<ModelClass> modelList, String headerTitle) {
        this.mContext = context;
        this.modelList = modelList;
        this.mHeaderTitle = headerTitle;
    }

    public void updateList(List<ModelClass> modelList) {
        this.modelList = modelList;
        notifyDataSetChanged();

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEADER) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler_header, parent, false);
            return new HeaderViewHolder(v);
        } else if (viewType == TYPE_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler_list, parent, false);
            return new ViewHolder(v);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof HeaderViewHolder) {
            //          HeaderViewHolder headerHolder = (HeaderViewHolder) holder;

//           headerHolder.txtHeader.setText(mHeaderTitle);

        } else if (holder instanceof ViewHolder) {

            final ModelClass model = getItem(position);
            ViewHolder genericViewHolder = (ViewHolder) holder;

            Transformation transformation = new RoundedTransformationBuilder()
                    .borderColor(Color.WHITE)
                    .borderWidthDp(0)
                    .oval(true)
                    .build();
            String img = model.getImage();
            //
            Log.d("TAG", "onBindViewHolder: " + AppStrings.ideo_path + img);
            Picasso.with(mContext)
                    .load(AppStrings.ideo_path + img)
                    .transform(transformation)
                    .fit()
                    .centerCrop()
                    .into(genericViewHolder.imgUser);



        }
    }

    //    need to override this method
    @Override
    public int getItemViewType(int position) {

        if (isPositionHeader(position)) {
            return TYPE_HEADER;
        }
        return TYPE_ITEM;

    }

    private boolean isPositionFooter(int position) { return position == modelList.size(); }

    private boolean isPositionHeader(int position) { return position == 0; }


    @Override
    public int getItemCount() {

        if (modelList.size() > 20) {
            return 20;
        } else {
            return modelList.size();
        }

        //

        //   return modelList.size() + 1;
    }

    public void SetOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    public void SetOnHeaderClickListener(final OnHeaderClickListener headerClickListener) {
        this.mHeaderClickListener = headerClickListener;
    }

    private ModelClass getItem(int position) {
        return modelList.get(position - 1);
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position, ModelClassData.User model);
    }

    public interface OnHeaderClickListener {
        void onHeaderClick(View view, String headerTitle);
    }

    class HeaderViewHolder extends RecyclerView.ViewHolder {
        TextView txtHeader;


        @SuppressLint("SetTextI18n")
        public HeaderViewHolder(final View itemView) {
            super(itemView);
            /*  */

            Log.d("TAG", "HeaderViewHolder: " + modelList.size());
            txtHeader = (TextView) itemView.findViewById(R.id.notify_badge);
            String size = String.valueOf(modelList.size() - 1);
            if (modelList.size() > 20) {
                txtHeader.setText("+" + size);
            } else {
                //  txtHeader.setText("+" + modelList.size());
                txtHeader.setVisibility(View.GONE);
            }

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //        mHeaderClickListener.onHeaderClick(itemView, "");

                }
            });

        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView imgUser;


        public ViewHolder(final View itemView) {
            super(itemView);
            this.imgUser = (ImageView) itemView.findViewById(R.id.img_user);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                        //  mItemClickListener.onItemClick(itemView, getAdapterPosition() - 1, modelList.get(getAdapterPosition() - 1));


                }
            });

        }
    }

}
