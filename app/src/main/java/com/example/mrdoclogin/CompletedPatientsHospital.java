package com.example.mrdoclogin;

public class CompletedPatientsHospital {
    public String patientName,patientEmail,doctorName,patientId;

    public CompletedPatientsHospital() {
    }

    public CompletedPatientsHospital(String patientName, String patientEmail,String doctorName,String patientId) {
        this.patientName = patientName;
        this.patientEmail=patientEmail;
        this.doctorName = doctorName;
        this.patientId=patientId;

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

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }
}
