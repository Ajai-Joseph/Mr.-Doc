package com.example.mrdoclogin;

public class PatientsOfHospital {
    public String userEmail,userName,userId,docName;

    public PatientsOfHospital() {
    }

    public PatientsOfHospital(String userEmail, String userName, String userId,String docName) {
        this.userEmail = userEmail;
        this.userName = userName;
        this.userId = userId;
        this.docName=docName;
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

    public String getDocName() {
        return docName;
    }

    public void setDocName(String docName) {
        this.docName = docName;
    }
}
