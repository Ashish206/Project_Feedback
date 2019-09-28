package com.arunsinghsaab98.project_feedback.model;

public class Complaint {

    String user_name,time_slot,product,issue,imageUrl;

    public Complaint(String user_name,String time_slot, String product, String issue,String imageUrl) {
        this.user_name = user_name;
        this.time_slot = time_slot;
        this.product = product;
        this.issue = issue;
        this.imageUrl = imageUrl;

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

    public Complaint() {
    }

    public String getTime_slot() {
        return time_slot;
    }

    public void setTime_slot(String time_slot) {
        this.time_slot = time_slot;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getIssue() {
        return issue;
    }

    public void setIssue(String issue) {
        this.issue = issue;
    }
}
