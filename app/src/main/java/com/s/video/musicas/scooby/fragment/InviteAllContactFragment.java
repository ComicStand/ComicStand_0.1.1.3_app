package com.s.video.musicas.scooby.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.s.video.musicas.scooby.Models.Contacts;
import com.s.video.musicas.scooby.R;
import com.s.video.musicas.scooby.adapter.InvoteContactAdapter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;


public class InviteAllContactFragment extends Fragment {


    public static final int REQUEST_READ_CONTACTS = 79;
    InvoteContactAdapter contactAdapter;
    List<Contacts> contactsList;
    List<String> addInvite;

    @BindView(R.id.rvContact)
    RecyclerView rvContact;

    @BindView(R.id.progress_circular)
    ProgressBar progress_circular;


    @BindView(R.id.edSearch)
    EditText edSearch;

    @BindView(R.id.contacInvite)
    ConstraintLayout contacInvite;

    @BindView(R.id.txtInvite)
    TextView txtInvite;

    int counter=0;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_invite_all_contact, container, false);
        ButterKnife.bind(this, view);
        contactsList = new ArrayList<>();
        addInvite=new ArrayList<>();

        contacInvite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (txtInvite.getText().toString().trim().equalsIgnoreCase("Return to the scooby")){
                    Objects.requireNonNull(getActivity()).onBackPressed();
                }else {
                    if (!addInvite.isEmpty()){
                        openSmsMsgAppFnc(addInvite);
                    }
                }

            }
        });

        edSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (contactAdapter != null) {
                    contactAdapter.getFilter().filter(s);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        new Handler().postDelayed(new Runnable() {
            public void run() {
                if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
                    getContactList();
                } else {
                    requestStoragePermission();
                }
            }
        }, 1000);
        return view;
    }
    private void requestStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.READ_CONTACTS)) {

        } else {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
    }




    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getContext(), "Permission GRANTED", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Permission DENIED", Toast.LENGTH_SHORT).show();
            }
        }
    }



    private void showProgress() {
        progress_circular.setVisibility(View.VISIBLE);
    }

    private void hideProgress() {
        progress_circular.setVisibility(View.GONE);
    }


    private static final String[] PROJECTION = new String[]{
            ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
            ContactsContract.Contacts.DISPLAY_NAME,
            ContactsContract.CommonDataKinds.Phone.NUMBER

    };

    private void getContactList() {
        showProgress();
        ContentResolver cr = getActivity().getContentResolver();

        Cursor cursor = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, PROJECTION, null, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");
        if (cursor != null) {
            HashSet<String> mobileNoSet = new HashSet<String>();
            try {
                final int nameIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
                final int numberIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);

                String name, number;
                while (cursor.moveToNext()) {
                    name = cursor.getString(nameIndex);
                    number = cursor.getString(numberIndex);
                    number = number.replace(" ", "");
                    if (!mobileNoSet.contains(number)) {
                        Contacts contacts = new Contacts();
                        contacts.setContactName(name);
                        contacts.setContactNumber(number);
                        contacts.setChecked(false);
                        contactsList.add(contacts);
                        mobileNoSet.add(number);
                    }
                }
            } finally {
                cursor.close();
            }
        }
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        rvContact.setLayoutManager(mLayoutManager);
        contactAdapter = new InvoteContactAdapter(getContext(),contactsList,InviteAllContactFragment.this);
        rvContact.setAdapter(contactAdapter);
        Log.d("TAG", "getAllContacts: " + contactsList.get(0).getContactNumber());
        hideProgress();
    }

    @SuppressLint("SetTextI18n")
    public void getNumber(){
      //  openSmsMsgAppFnc(number,"Come watch videos with me on Scooby!");
        addInvite=new ArrayList<>();
         counter=0;
        if (!contactsList.isEmpty()){
            for (int i=0;i<contactsList.size();i++){
                if (contactsList.get(i).isChecked()){
                    counter++;
                    addInvite.add(contactsList.get(i).getContactNumber());
                    Log.d("TAG", "getNumber: "+counter);
                    txtInvite.setText("SEND "+counter+" INVITE");
                }
            }

         /*   txtInvite.setText("Return to the scooby");
            Log.d("TAG", "getNumber: "+counter);*/
        }

        if (addInvite.isEmpty()){
            txtInvite.setText("");
        }
    }

    @SuppressLint("IntentReset")
    void openSmsMsgAppFnc(List<String> list)
    {
        StringBuilder uri = new StringBuilder("sms:");
        for (int i = 0; i < list.size(); i++) {
            uri.append(list.get(i));
            uri.append(", ");
        }
        Intent smsIntent = new Intent(Intent.ACTION_VIEW);
        smsIntent.setType("vnd.android-dir/mms-sms");
        smsIntent.setData(Uri.parse(uri.toString()));
        smsIntent.putExtra("sms_body", "Come watch videos with me on Scooby!");
        startActivity(smsIntent);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onResume() {
        super.onResume();
        txtInvite.setText("Return to the scooby");
    }
}