package com.chicsol.marrymax.modal;

import com.google.gson.annotations.SerializedName;

/**
 * Created by muneeb on 5/15/17.
 */

public class mContacts {

    @SerializedName("alias")
    public String alias;
    @SerializedName("path")
    public String path;
    @SerializedName("userpath")
    public String userpath;
    @SerializedName("phone_request_id")
    public long phone_request_id;
    @SerializedName("country_name")
    public String country_name;
    @SerializedName("country_flag")
    public String country_flag;
    @SerializedName("other_info")
    public String other_info;
    @SerializedName("phone_mobile")
    public String phone_mobile;
    @SerializedName("start_date")
    public String start_date;
    @SerializedName("default_image")
    public String default_image;
    @SerializedName("min_age")
    public long min_age;
    @SerializedName("match_id")
    public long match_id;
    @SerializedName("feedback_id")
    public int feedback_id;

    public long getMatch_id() {
        return match_id;
    }

    public void setMatch_id(long match_id) {
        this.match_id = match_id;
    }

    public int getFeedback_id() {
        return feedback_id;
    }

    public void setFeedback_id(int feedback_id) {
        this.feedback_id = feedback_id;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
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

    public long getPhone_request_id() {
        return phone_request_id;
    }

    public void setPhone_request_id(long phone_request_id) {
        this.phone_request_id = phone_request_id;
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

    public String getOther_info() {
        return other_info;
    }

    public void setOther_info(String other_info) {
        this.other_info = other_info;
    }

    public String getPhone_mobile() {
        return phone_mobile;
    }

    public void setPhone_mobile(String phone_mobile) {
        this.phone_mobile = phone_mobile;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getDefault_image() {
        return default_image;
    }

    public void setDefault_image(String default_image) {
        this.default_image = default_image;
    }

    public long getMin_age() {
        return min_age;
    }

    public void setMin_age(long min_age) {
        this.min_age = min_age;
    }


}
