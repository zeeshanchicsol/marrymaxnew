package com.chicsol.marrymax.modal;

import com.google.gson.annotations.SerializedName;

/**
 * Created by macintoshhd on 11/27/17.
 */

public class mMem {


    @SerializedName("id")
    public String id;

    @SerializedName("my_id")
    public int my_id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getMy_id() {
        return my_id;
    }

    public void setMy_id(int my_id) {
        this.my_id = my_id;
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

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    @SerializedName("memberid")
    public String memberid;

    @SerializedName("visting_memberid")
    public String visting_memberid;

    @SerializedName("path")
    public String path;

    @SerializedName("userpath")
    public String userpath;

    @SerializedName("notes")
    public String notes;


}

