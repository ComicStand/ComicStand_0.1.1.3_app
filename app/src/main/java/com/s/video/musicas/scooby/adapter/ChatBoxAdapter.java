package com.s.video.musicas.scooby.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import com.s.video.musicas.scooby.ChatModel.Message;
import com.s.video.musicas.scooby.R;

public class ChatBoxAdapter   extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<Message> MessageList;
    String name;
    private final int SENDER = 0, REC = 1;
    Context context;

    public ChatBoxAdapter(Context context,List<Message> messageList, String name) {
        MessageList = messageList;
        this.name = name;
        this.context=context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == SENDER) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_sender, parent, false);
            return new senderChat(view);

        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
            return new MyViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == SENDER) {
            ((senderChat) holder).setDATA(MessageList.get(position));
        } else {
            ((MyViewHolder) holder).setDATA(MessageList.get(position));
        }

    }

    @Override
    public int getItemViewType(int position) {
        int viewType;
        if (MessageList.get(position).getUserID().equals(name)) {
            viewType = SENDER;
        } else {
            viewType = REC;
        }
        return viewType;
    }
    @Override
    public int getItemCount() {
        return MessageList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        //   public TextView nickname;
        public TextView txtUser, txtTime;
        CircleImageView imgLogo;
        public MyViewHolder(@NonNull View view) {
            super(view);
            //nickname = (TextView) view.findViewById(R.id.nickname);
            txtUser = (TextView) view.findViewById(R.id.text);
            txtTime=view.findViewById(R.id.txtTime);
            imgLogo=view.findViewById(R.id.imgLogo);
            // text = view.findViewById(R.id.txtSender);

        }

        private void setDATA(Message model) {
            txtUser.setText(model.getMessage());
            Glide.with(context).load(model.getImage()).into(imgLogo);
          //  txtTime.setText(model.getTime());



        }


    }


    public class senderChat extends RecyclerView.ViewHolder {
        //   public TextView nickname;
        // public TextView txtUser, txtSender;
        TextView text,txtTime;
        CircleImageView imgLogo;

        public senderChat(@NonNull View view) {
            super(view);
            //nickname = (TextView) view.findViewById(R.id.nickname);
            text = view.findViewById(R.id.text);
            txtTime=view.findViewById(R.id.txtTime);
            imgLogo=view.findViewById(R.id.imgLogo);
            //   txtSender = view.findViewById(R.id.txtSender);
        }

        private void setDATA(Message model) {
            text.setText(model.getMessage());
            Glide.with(context).load(model.getImage()).into(imgLogo);

           // txtTime.setText(model.getTime());

        }


    }

}
