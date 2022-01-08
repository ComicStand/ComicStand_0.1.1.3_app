package com.s.video.musicas.scooby.Models;

public class PrivacyModel {
    private int id;
    private String name;
    private String image;
   private String stats;


    public PrivacyModel(int id, String name, String image, String stats) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.stats = stats;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getStats() {
        return stats;
    }

    public void setStats(String stats) {
        this.stats = stats;
    }
}
