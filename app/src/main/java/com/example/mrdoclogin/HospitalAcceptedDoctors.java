package com.example.mrdoclogin;

public class HospitalAcceptedDoctors {
    public String userEmail,userName,userId,hosId;

    public HospitalAcceptedDoctors() {
    }

    public HospitalAcceptedDoctors(String userEmail, String userName, String userId,String hosId) {
        this.userEmail = userEmail;
        this.userName = userName;
        this.userId = userId;
        this.hosId=hosId;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getHosId() {
        return hosId;
    }

    public void setHosId(String hosId) {
        this.hosId = hosId;
    }
}
