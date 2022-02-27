package com.example.mrdoclogin;

public class PatientServicesDoctorHomeRequestProfile {

    public String userEmail,userName,userId,userAddress,userDate,userTime;

    public PatientServicesDoctorHomeRequestProfile(){

    }

    public PatientServicesDoctorHomeRequestProfile(String userEmail, String userName, String userId, String userAddress, String userDate, String userTime) {
        this.userEmail = userEmail;
        this.userName = userName;
        this.userId = userId;
        this.userAddress = userAddress;
        this.userDate = userDate;
        this.userTime = userTime;
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

    public String getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
    }

    public String getUserDate() {
        return userDate;
    }

    public void setUserDate(String userDate) {
        this.userDate = userDate;
    }

    public String getUserTime() {
        return userTime;
    }

    public void setUserTime(String userTime) {
        this.userTime = userTime;
    }
}
