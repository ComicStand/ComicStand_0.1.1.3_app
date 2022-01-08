package com.s.video.musicas.scooby.Models;

import android.content.Intent;
import android.os.Handler;

import com.s.video.musicas.scooby.LiveChatRoomLIstActivity;
import com.s.video.musicas.scooby.SplashActivity;
import com.s.video.musicas.scooby.auth.AuthActivity;
import com.s.video.musicas.scooby.utils.AppStrings;
import com.s.video.musicas.scooby.utils.MySharedpreferences;

public class Contacts {
    private String ContactImage;
    private String ContactName;
    private String ContactNumber;
    private boolean isChecked;


    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public String getContactImage() {
        return ContactImage;
    }

    public void setContactImage(String contactImage) {
        ContactImage = contactImage;
    }

    public String getContactName() {
        return ContactName;
    }

    public void setContactName(String contactName) {
        ContactName = contactName;
    }

    public String getContactNumber() {
        return ContactNumber;
    }

    public void setContactNumber(String contactNumber) {
        ContactNumber = contactNumber;
    }
}


