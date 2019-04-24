package com.chicsol.marrymax.modal;

import com.google.gson.annotations.SerializedName;

public class mMemPhone {


    @SerializedName("profile_owner")
    public String profile_owner;

    @SerializedName("personal_name")
    public String personal_name;

    @SerializedName("phone_mobile")
    public String phone_mobile;

    @SerializedName("photo_name")
    public String photo_name;

    @SerializedName("notes")
    public String notes;

    @SerializedName("about_member_id")
    public long about_member_id;

    @SerializedName("other_info")
    public String other_info;

    @SerializedName("country_name")
    public String country_name;

    @SerializedName("feedback_due")
    public int feedback_due;

    @SerializedName("phone_landline")
    public String phone_landline;


    public String getPhone_landline() {
        return phone_landline;
    }

    public void setPhone_landline(String phone_landline) {
        this.phone_landline = phone_landline;
    }


    public String getProfile_owner() {
        return profile_owner;
    }

    public void setProfile_owner(String profile_owner) {
        this.profile_owner = profile_owner;
    }

    public String getPersonal_name() {
        return personal_name;
    }

    public void setPersonal_name(String personal_name) {
        this.personal_name = personal_name;
    }

    public String getPhone_mobile() {
        return phone_mobile;
    }

    public void setPhone_mobile(String phone_mobile) {
        this.phone_mobile = phone_mobile;
    }

    public String getPhoto_name() {
        return photo_name;
    }

    public void setPhoto_name(String photo_name) {
        this.photo_name = photo_name;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public long getAbout_member_id() {
        return about_member_id;
    }

    public void setAbout_member_id(long about_member_id) {
        this.about_member_id = about_member_id;
    }

    public String getOther_info() {
        return other_info;
    }

    public void setOther_info(String other_info) {
        this.other_info = other_info;
    }

    public String getCountry_name() {
        return country_name;
    }

    public void setCountry_name(String country_name) {
        this.country_name = country_name;
    }

    public int getFeedback_due() {
        return feedback_due;
    }

    public void setFeedback_due(int feedback_due) {
        this.feedback_due = feedback_due;
    }


}
