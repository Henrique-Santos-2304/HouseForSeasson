package com.example.multitech.houseforseasson.database.models;

import com.example.multitech.houseforseasson.database.repository.FirebaseHelper;
import com.google.firebase.database.DatabaseReference;

public class Announcement {
    private String id;
    private String title;
    private String urlImg;
    private String description;
    private Integer bethroom;
    private Integer bedroom;
    private Integer garage;
    private boolean disponibility;

    public Announcement() {
        DatabaseReference dbRef = FirebaseHelper.getDbReference();
        String id = dbRef.push().getKey();
        this.setId(id);
    }

    public boolean isDisponibility() {
        return disponibility;
    }

    public void setDisponibility(boolean disponibility) {
        this.disponibility = disponibility;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getBethroom() {
        return bethroom;
    }

    public void setBethroom(Integer bethroom) {
        this.bethroom = bethroom;
    }

    public Integer getBedroom() {
        return bedroom;
    }

    public void setBedroom(Integer bedroom) {
        this.bedroom = bedroom;
    }

    public Integer getGarage() {
        return garage;
    }

    public void setGarage(Integer garage) {
        this.garage = garage;
    }

    public String getUrlImg() {
        return urlImg;
    }

    public void setUrlImg(String urlImg) {
        this.urlImg = urlImg;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
