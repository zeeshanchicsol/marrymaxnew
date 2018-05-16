package com.chicsol.marrymax.modal;

import com.google.gson.annotations.SerializedName;

/**
 * Created by macintoshhd on 10/23/17.
 */

public class mLfm {
    @SerializedName("id")
    public int id;
    @SerializedName("id2")
    public int id2;
    @SerializedName("default_image")
    public String default_image;
    @SerializedName("memberid")
    public long memberid;
    @SerializedName("visting_memberid")
    public long visting_memberid;
    @SerializedName("name")
    public String name;
    @SerializedName("type")
    public String type;
    @SerializedName("text")
    public String text;
    @SerializedName("path")
    public String path;
    @SerializedName("userpath")
    public String userpath;
    @SerializedName("status")
    public String status;
    @SerializedName("date")
    public String date;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId2() {
        return id2;
    }

    public void setId2(int id2) {
        this.id2 = id2;
    }

    public long getMemberid() {
        return memberid;
    }

    public void setMemberid(long memberid) {
        this.memberid = memberid;
    }

    public long getVisting_memberid() {
        return visting_memberid;
    }

    public void setVisting_memberid(long visting_memberid) {
        this.visting_memberid = visting_memberid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDefault_image() {
        return default_image;
    }

    public void setDefault_image(String default_image) {
        this.default_image = default_image;
    }


}
