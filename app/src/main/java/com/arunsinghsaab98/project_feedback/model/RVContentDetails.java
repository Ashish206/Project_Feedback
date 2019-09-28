package com.arunsinghsaab98.project_feedback.model;

public class RVContentDetails {
    String imageUrl,user_name;

    public RVContentDetails(String image, String user_name) {
        this.imageUrl = image;
        this.user_name = user_name;
    }

    public RVContentDetails() {
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }
}
