package com.s.video.musicas.scooby.nettwork.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public  class getLikeStatusModel {

    @Expose
    @SerializedName("data")
    private Data data;
    @Expose
    @SerializedName("status")
    private String status;
    @Expose
    @SerializedName("message")
    private String message;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
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

    public static class Data {
        @Expose
        @SerializedName("total_dislike")
        private int total_dislike;
        @Expose
        @SerializedName("total_like")
        private int total_like;
        @Expose
        @SerializedName("dislike_status")
        private String dislike_status;
        @Expose
        @SerializedName("like_status")
        private String like_status;

        public int getTotal_dislike() {
            return total_dislike;
        }

        public void setTotal_dislike(int total_dislike) {
            this.total_dislike = total_dislike;
        }

        public int getTotal_like() {
            return total_like;
        }

        public void setTotal_like(int total_like) {
            this.total_like = total_like;
        }

        public String getDislike_status() {
            return dislike_status;
        }

        public void setDislike_status(String dislike_status) {
            this.dislike_status = dislike_status;
        }

        public String getLike_status() {
            return like_status;
        }

        public void setLike_status(String like_status) {
            this.like_status = like_status;
        }
    }
}
