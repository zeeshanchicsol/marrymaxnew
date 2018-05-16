package com.chicsol.marrymax.modal;

import com.google.gson.annotations.SerializedName;

/**
 * Created by macintoshhd on 12/4/17.
 */

public class mMemListDetail {

    @SerializedName("alias")
    public String alias;

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getCountry_name() {
        return country_name;
    }

    public void setCountry_name(String country_name) {
        this.country_name = country_name;
    }

    public String getCountry_flag() {
        return country_flag;
    }

    public void setCountry_flag(String country_flag) {
        this.country_flag = country_flag;
    }

    public String getEducation_types() {
        return education_types;
    }

    public void setEducation_types(String education_types) {
        this.education_types = education_types;
    }

    public String getMarital_status_types() {
        return marital_status_types;
    }

    public void setMarital_status_types(String marital_status_types) {
        this.marital_status_types = marital_status_types;
    }

    public String getOccupation_types() {
        return occupation_types;
    }

    public void setOccupation_types(String occupation_types) {
        this.occupation_types = occupation_types;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getDefault_image() {
        return default_image;
    }

    public void setDefault_image(String default_image) {
        this.default_image = default_image;
    }

    public String getMin_age() {
        return min_age;
    }

    public void setMin_age(String min_age) {
        this.min_age = min_age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    @SerializedName("country_name")
    public String country_name;

    @SerializedName("country_flag")
    public String country_flag;

    @SerializedName("education_types")
    public String education_types;

    @SerializedName("marital_status_types")
    public String marital_status_types;

    @SerializedName("occupation_types")
    public String occupation_types;

    @SerializedName("path")
    public String path;

    @SerializedName("default_image")
    public String default_image;

    @SerializedName("min_age")
    public String min_age;

    @SerializedName("name")
    public String name;

    @SerializedName("count")
    public String count;


    @SerializedName("userpath")
    public String userpath;


    public String getUserpath() {
        return userpath;
    }

    public void setUserpath(String userpath) {
        this.userpath = userpath;
    }

}
