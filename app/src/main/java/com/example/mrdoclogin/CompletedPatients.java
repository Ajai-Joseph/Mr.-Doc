package com.example.mrdoclogin;

public class CompletedPatients {
    public String docName,docEmail,docId;

    public CompletedPatients() {
    }

    public CompletedPatients(String docName, String docEmail, String docId) {
        this.docName = docName;
        this.docEmail = docEmail;
        this.docId = docId;
    }

    public String getDocName() {
        return docName;
    }

    public void setDocName(String docName) {
        this.docName = docName;
    }

    public String getDocEmail() {
        return docEmail;
    }

    public void setDocEmail(String docEmail) {
        this.docEmail = docEmail;
    }

    public String getDocId() {
        return docId;
    }

    public void setDocId(String docId) {
        this.docId = docId;
    }
}
