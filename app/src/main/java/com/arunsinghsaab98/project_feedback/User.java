package com.arunsinghsaab98.project_feedback;

public class User {

    String mobileNo,email,address,displayName;

    public User(String phoneNo, String email, String address, String displayName) {
        this.mobileNo = phoneNo;
        this.email = email;
        this.address = address;
        this.displayName = displayName;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public User() {
    }
}
