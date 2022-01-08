package com.s.video.musicas.scooby.nettwork.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public  class LiveUserModel implements Serializable {

    @Expose
    @SerializedName("data")
    private List<Data> data;
    @Expose
    @SerializedName("status")
    private String status;
    @Expose
    @SerializedName("message")
    private String message;

    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static class Data implements Serializable{
        @Expose
        @SerializedName("signup_id")
        private String signup_id;
        @Expose
        @SerializedName("lat_long")
        private String lat_long;
        @Expose
        @SerializedName("photo")
        private String photo;
        @Expose
        @SerializedName("user_type")
        private String user_type;
        @Expose
        @SerializedName("name")
        private String name;

        public String getUser_type() {
            return user_type;
        }

        public void setUser_type(String user_type) {
            this.user_type = user_type;
        }

        public String getSignup_id() {
            return signup_id;
        }

        public void setSignup_id(String signup_id) {
            this.signup_id = signup_id;
        }

        public String getLat_long() {
            return lat_long;
        }

        public void setLat_long(String lat_long) {
            this.lat_long = lat_long;
        }

        public String getPhoto() {
            return photo;
        }

        public void setPhoto(String photo) {
            this.photo = photo;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
