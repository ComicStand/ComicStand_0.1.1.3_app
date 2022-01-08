package com.s.video.musicas.scooby.nettwork.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ChatRoomVideoModel {
    @SerializedName("obj")
    @Expose
    private List<Obj> obj = null;

    public List<Obj> getObj() {
        return obj;
    }

    public void setObj(List<Obj> obj) {
        this.obj = obj;
    }

    public class Obj {

        @SerializedName("username")
        @Expose
        private String username;
        @SerializedName("vedio_id")
        @Expose
        private String vedioId;
        @SerializedName("title")
        @Expose
        private String title;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
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

    }
}
