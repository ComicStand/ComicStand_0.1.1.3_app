package com.s.video.musicas.scooby.nettwork.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public  class ModelClassData implements Serializable {

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

    public class User {

        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("photo")
        @Expose
        private String photo;
        @SerializedName("lat_long")
        @Expose
        private String latLong;
        @SerializedName("signup_id")
        @Expose
        private String signupId;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPhoto() {
            return photo;
        }

        public void setPhoto(String photo) {
            this.photo = photo;
        }

        public String getLatLong() {
            return latLong;
        }

        public void setLatLong(String latLong) {
            this.latLong = latLong;
        }

        public String getSignupId() {
            return signupId;
        }

        public void setSignupId(String signupId) {
            this.signupId = signupId;
        }

    }

    public class Datum {

        @SerializedName("vedio_id")
        @Expose
        private String vedioId;
        @SerializedName("group_id")
        @Expose
        private String group_id;
        @SerializedName("title")
        @Expose
        private String title;
        @SerializedName("user")
        @Expose
        private List<User> user = null;

        @SerializedName("type")
        @Expose
        private String type;

        public String getGroup_id() {
            return group_id;
        }

        public void setGroup_id(String group_id) {
            this.group_id = group_id;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getVedioId() {
            return vedioId;
        }

        public void setVedioId(String vedioId) {
            this.vedioId = vedioId;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public List<User> getUser() {
            return user;
        }

        public void setUser(List<User> user) {
            this.user = user;
        }

    }
}
