package com.chicsol.marrymax.modal;

import com.google.gson.annotations.SerializedName;

/**
 * Created by macintoshhd on 12/6/17.
 */

public class mPermission {

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

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getReligious_sect_type() {
        return religious_sect_type;
    }

    public void setReligious_sect_type(String religious_sect_type) {
        this.religious_sect_type = religious_sect_type;
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

    public String getEducation_type() {
        return education_type;
    }

    public void setEducation_type(String education_type) {
        this.education_type = education_type;
    }

    public String getOccupation_type() {
        return occupation_type;
    }

    public void setOccupation_type(String occupation_type) {
        this.occupation_type = occupation_type;
    }

    public String getDefault_image() {
        return default_image;
    }

    public void setDefault_image(String default_image) {
        this.default_image = default_image;
    }

    public int getPhoto_privilege() {
        return photo_privilege;
    }

    public void setPhoto_privilege(int photo_privilege) {
        this.photo_privilege = photo_privilege;
    }

    public int getProfile_privilege() {
        return profile_privilege;
    }

    public void setProfile_privilege(int profile_privilege) {
        this.profile_privilege = profile_privilege;
    }

    public int getPhone_privilege() {
        return phone_privilege;
    }

    public void setPhone_privilege(int phone_privilege) {
        this.phone_privilege = phone_privilege;
    }

    @SerializedName("path")
    public String path;
    @SerializedName("userpath")
    public String userpath;
    @SerializedName("alias")
    public String alias;
    @SerializedName("age")
    public String age;
    @SerializedName("religious_sect_type")
    public String religious_sect_type;
    @SerializedName("country_flag")
    public String country_flag;
    @SerializedName("country_name")
    public String country_name;
    @SerializedName("education_type")
    public String education_type;
    @SerializedName("occupation_type")
    public String occupation_type;
    @SerializedName("default_image")
    public String default_image;
    @SerializedName("photo_privilege")
    public int photo_privilege;
    @SerializedName("profile_privilege")
    public int profile_privilege;
    @SerializedName("phone_privilege")
    public int phone_privilege;


}
