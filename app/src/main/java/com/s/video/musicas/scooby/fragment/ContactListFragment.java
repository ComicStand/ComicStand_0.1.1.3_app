package com.s.video.musicas.scooby.fragment;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.s.video.musicas.scooby.Models.Contacts;
import com.s.video.musicas.scooby.R;
import com.s.video.musicas.scooby.adapter.AllContactsAdapter;

public class ContactListFragment extends Fragment {


    public static final int REQUEST_READ_CONTACTS = 79;
    AllContactsAdapter contactAdapter;
    List<Contacts> contactsList;

    @BindView(R.id.rvContact)
    RecyclerView rvContact;

    @BindView(R.id.progress_circular)
    ProgressBar progress_circular;


    @BindView(R.id.edSearch)
    EditText edSearch;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_contact_list, container, false);


        ButterKnife.bind(this, view);
        contactsList = new ArrayList<>();

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


/*

        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    performSearch();
                    return true;
                }
                return false;
            }
        });
*/


        new Handler().postDelayed(new Runnable() {
            public void run() {
                // do something...


                if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
                    getContactList();

                } else {
                    Log.d("TAG", "run: " + "1");
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
        contactAdapter = new AllContactsAdapter(contactsList, getContext(),ContactListFragment.this);
        rvContact.setAdapter(contactAdapter);
        Log.d("TAG", "getAllContacts: " + contactsList.get(0).getContactNumber());
        hideProgress();
    }

    public void getNumber(String number){
        openSmsMsgAppFnc(number,"Come watch videos with me on Scooby!");
    }

    void openSmsMsgAppFnc(String mblNumVar, String smsMsgVar)
    {
        Intent smsMsgAppVar = new Intent(Intent.ACTION_VIEW);
        smsMsgAppVar.setData(Uri.parse("sms:" +  mblNumVar));
        smsMsgAppVar.putExtra("sms_body", smsMsgVar);
        startActivity(smsMsgAppVar);
    }
}