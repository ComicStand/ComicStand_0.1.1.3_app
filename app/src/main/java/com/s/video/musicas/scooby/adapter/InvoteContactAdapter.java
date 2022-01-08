package com.s.video.musicas.scooby.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.s.video.musicas.scooby.Models.Contacts;
import com.s.video.musicas.scooby.R;
import com.s.video.musicas.scooby.fragment.InviteAllContactFragment;

import java.util.List;

public class InvoteContactAdapter extends RecyclerView.Adapter<InvoteContactAdapter.ViewHolder> implements Filterable {

    Context context;
    private List<Contacts> contactsList;
    InviteAllContactFragment fragment;

    public InvoteContactAdapter(Context context, List<Contacts> contactsList, InviteAllContactFragment fragment) {
        this.context = context;
        this.contactsList = contactsList;
        this.fragment = fragment;
    }

    public InvoteContactAdapter(Context context, List<Contacts> contactsList) {
        this.context = context;
        this.contactsList = contactsList;
    }

    @Override
    public Filter getFilter() {
        return null;
    }

    @NonNull
    @Override
    public InvoteContactAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.online_invte, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InvoteContactAdapter.ViewHolder holder, int position) {
        Contacts contacts=contactsList.get(position);
        holder.txtName.setText(contacts.getContactName());
        holder.txtNumber.setText(contacts.getContactNumber());

        holder.checkbox.setOnCheckedChangeListener(null);
        holder.checkbox.setChecked(contactsList.get(position).isChecked());

        holder.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                contactsList.get(holder.getAdapterPosition()).setChecked(isChecked);
                fragment.getNumber();
            }
        });
     //   holder.itemView.setOnClickListener(v -> fragment.getNumber(contacts.getContactNumber()));
    }

    @Override
    public int getItemCount() {
        return contactsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox checkbox;
        TextView txtName,txtNumber;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.setIsRecyclable(false);
            checkbox=itemView.findViewById(R.id.imgAdd);
            txtName=itemView.findViewById(R.id.txtName);
            txtNumber=itemView.findViewById(R.id.txtNumber);
        }
    }
}
