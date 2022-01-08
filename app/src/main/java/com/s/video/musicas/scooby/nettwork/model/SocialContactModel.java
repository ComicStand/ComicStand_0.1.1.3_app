package com.s.video.musicas.scooby.nettwork.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SocialContactModel {
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("data")
    @Expose
    private List<Datum> data = null;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Datum> getData() {
        return data;
    }

    public void setData(List<Datum> data) {
        this.data = data;
    }

    public class Datum {

        @SerializedName("user_id")
        @Expose
        private String userId;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("my_user_id")
        @Expose
        private String myUserId;
        @SerializedName("status")
        @Expose
        private String status;
        @SerializedName("my_frnds_user_id")
        @Expose
        private String myFrndsUserId;


        @SerializedName("frnd_status")
        @Expose
        private String frnd_status;


        public String getFrnd_status() {
            return frnd_status;
        }

        public void setFrnd_status(String frnd_status) {
            this.frnd_status = frnd_status;
        }

        @SerializedName("photo")
        @Expose
        private String photo;

        public String getPhoto() {
            return photo;
        }

        public void setPhoto(String photo) {
            this.photo = photo;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getMyUserId() {
            return myUserId;
        }

        public void setMyUserId(String myUserId) {
            this.myUserId = myUserId;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getMyFrndsUserId() {
            return myFrndsUserId;
        }

        public void setMyFrndsUserId(String myFrndsUserId) {
            this.myFrndsUserId = myFrndsUserId;
        }

    }
}
