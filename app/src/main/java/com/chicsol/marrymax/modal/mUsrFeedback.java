package com.chicsol.marrymax.modal;

import com.google.gson.annotations.SerializedName;

public class mUsrFeedback {

    @SerializedName("id")
    public String id;

    @SerializedName("alias")
    public String alias;

    @SerializedName("memberid")
    public String memberid;

    @SerializedName("visting_memberid")
    public String visting_memberid;

    @SerializedName("path")
    public String path;

    @SerializedName("userpath")
    public String userpath;

    @SerializedName("default_image")
    public String default_image;

    @SerializedName("country_flag")
    public String country_flag;

    @SerializedName("country_name")
    public String country_name;

    @SerializedName("age")
    public int age;

    @SerializedName("education_type")
    public String education_type;

    @SerializedName("marital_type")
    public String marital_type;

    @SerializedName("ethnic_type")
    public String ethnic_type;

    @SerializedName("religious_type")
    public String religious_type;

    @SerializedName("notes")
    public String notes;

    @SerializedName("rating")
    public int rating;

    @SerializedName("date")
    public String date;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getMemberid() {
        return memberid;
    }

    public void setMemberid(String memberid) {
        this.memberid = memberid;
    }

    public String getVisting_memberid() {
        return visting_memberid;
    }

    public void setVisting_memberid(String visting_memberid) {
        this.visting_memberid = visting_memberid;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getUserpath() {
        return userpath;
    }

    public void setUserpath(String userpath) {
        this.userpath = userpath;
    }

    public String getDefault_image() {
        return default_image;
    }

    public void setDefault_image(String default_image) {
        this.default_image = default_image;
    }

    public String getCountry_flag() {
        return country_flag;
    }

    public void setCountry_flag(String country_flag) {
        this.country_flag = country_flag;
    }

    public String getCountry_name() {
        return country_name;
    }

    public void setCountry_name(String country_name) {
        this.country_name = country_name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getEducation_type() {
        return education_type;
    }

    public void setEducation_type(String education_type) {
        this.education_type = education_type;
    }

    public String getMarital_type() {
        return marital_type;
    }

    public void setMarital_type(String marital_type) {
        this.marital_type = marital_type;
    }

    public String getEthnic_type() {
        return ethnic_type;
    }

    public void setEthnic_type(String ethnic_type) {
        this.ethnic_type = ethnic_type;
    }

    public String getReligious_type() {
        return religious_type;
    }

    public void setReligious_type(String religious_type) {
        this.religious_type = religious_type;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
