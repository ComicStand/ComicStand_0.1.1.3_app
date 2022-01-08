package com.s.video.musicas.scooby.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import com.s.video.musicas.scooby.Models.Contacts;
import com.s.video.musicas.scooby.R;
import com.s.video.musicas.scooby.fragment.ContactListFragment;

public class AllContactsAdapter extends RecyclerView.Adapter<AllContactsAdapter.ViewHolder> implements Filterable {

    private List<Contacts> contactsList;
    private Context activity;
    private List<Contacts> contactListFiltered;

    ContactListFragment fragment;


    public AllContactsAdapter(List<Contacts> contactsList, Context activity,ContactListFragment fragment) {
        this.contactsList = contactsList;
        this.activity = activity;
        this.contactListFiltered = contactsList;
        this.fragment=fragment;
    }

    @NonNull
    @Override
    public AllContactsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        View view = LayoutInflater.from(activity).inflate(R.layout.contacts_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AllContactsAdapter.ViewHolder holder, int position) {

        Contacts contacts=contactListFiltered.get(position);
        holder.txtName.setText(contacts.getContactName());
        holder.txtNumber.setText(contacts.getContactNumber());

        holder.itemView.setOnClickListener(v -> fragment.getNumber(contacts.getContactNumber()));

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
                    contactListFiltered = contactsList;
                } else {
                    List<Contacts> filteredList = new ArrayList<>();
                    for (Contacts row : contactsList) {


                        if (row.getContactName().toLowerCase().contains(charString.toLowerCase()) || row.getContactName().contains(charSequence.toString())) {
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
                contactListFiltered = (ArrayList<Contacts>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtName,txtNumber;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName=itemView.findViewById(R.id.txtName);
            txtNumber=itemView.findViewById(R.id.txtNumber);
        }
    }
}
