package com.example.mrdoclogin;

public class PatientProfile {
    public String userEmail;
    public String userName;
    public String userType;
    public String userId;
    public String userPicUrl;


    public PatientProfile(String userEmail, String userName,String userType,String userId,String userPicUrl) {
        this.userEmail = userEmail;
        this.userName =userName;
        this.userType=userType;
        this.userId=userId;
        this.userPicUrl=userPicUrl;

    }
    public PatientProfile()
    {

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

    public void setUserName(String userName) { this.userName =userName; }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) { this.userType = userType; }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) { this.userId = userId;}

    public String getUserPicUrl() {
        return userPicUrl;
    }

    public void setUserPicUrl(String userPicUrl) {
        this.userPicUrl = userPicUrl;
    }
}
