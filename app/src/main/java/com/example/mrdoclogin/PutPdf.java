package com.example.mrdoclogin;
public class PutPdf {
    public String name,url,hname,hemail,hid;

    public PutPdf() {
    }

    public PutPdf(String name, String url, String hname, String hemail,String hid) {
        this.name = name;
        this.url = url;
        this.hname=hname;
        this.hemail=hemail;
        this.hid=hid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getHname() {
        return hname;
    }

    public void setHname(String hname) {
        this.hname = hname;
    }

    public String getHemail() {
        return hemail;
    }

    public void setHemail(String hemail) {
        this.hemail = hemail;
    }

    public String getHid() {
        return hid;
    }

    public void setHid(String hid) {
        this.hid = hid;
    }
}
