package com.chicsol.marrymax.modal;

import com.google.gson.annotations.SerializedName;

public class mDoc {

    @SerializedName("doc_id")
    public String doc_id;
    @SerializedName("member_id")
    public String member_id;


    @SerializedName("doc_type")
    public String doc_type;


    @SerializedName("doc_url")
    public String doc_url;

    @SerializedName("notes")
    public String notes;

    @SerializedName("review")
    public int review;

    @SerializedName("admin")
    public int admin;

    @SerializedName("date")
    public String date;

    public String getDoc_id() {
        return doc_id;
    }

    public void setDoc_id(String doc_id) {
        this.doc_id = doc_id;
    }

    public String getMember_id() {
        return member_id;
    }

    public void setMember_id(String member_id) {
        this.member_id = member_id;
    }

    public String getDoc_type() {
        return doc_type;
    }

    public void setDoc_type(String doc_type) {
        this.doc_type = doc_type;
    }

    public String getDoc_url() {
        return doc_url;
    }

    public void setDoc_url(String doc_url) {
        this.doc_url = doc_url;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public int getReview() {
        return review;
    }

    public void setReview(int review) {
        this.review = review;
    }

    public int getAdmin() {
        return admin;
    }

    public void setAdmin(int admin) {
        this.admin = admin;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
