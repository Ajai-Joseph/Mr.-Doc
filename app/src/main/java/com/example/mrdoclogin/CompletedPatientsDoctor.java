package com.example.mrdoclogin;

public class CompletedPatientsDoctor {
    public String patientName,patientEmail,patientId;

    public CompletedPatientsDoctor() {
    }

    public CompletedPatientsDoctor(String patientName, String patientEmail, String patientId) {
        this.patientName = patientName;
        this.patientEmail = patientEmail;
        this.patientId = patientId;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getPatientEmail() {
        return patientEmail;
    }

    public void setPatientEmail(String patientEmail) {
        this.patientEmail = patientEmail;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }
}
