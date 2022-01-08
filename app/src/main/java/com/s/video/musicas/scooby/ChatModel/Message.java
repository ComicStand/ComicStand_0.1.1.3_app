package com.s.video.musicas.scooby.ChatModel;

public class Message {
    private String nickname;
    private String message,time,userID,image ;

    public  Message(){

    }

    public Message(String nickname, String message, String time, String userID, String image) {
        this.nickname = nickname;
        this.message = message;
        this.time = time;
        this.userID = userID;
        this.image = image;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
