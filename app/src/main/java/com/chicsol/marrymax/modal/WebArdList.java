package com.chicsol.marrymax.modal;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Android on 12/15/2016.
 */

public class WebArdList {
    @SerializedName("id")
    String id;
    @SerializedName("memberid")
    String memberid;
    @SerializedName("visting_memberid")
    String visting_memberid;
    @SerializedName("alias")
    String alias;
    @SerializedName("path")
    String path;
    @SerializedName("userpath")
    String userpath;
    @SerializedName("start_date")
    String start_date;
    @SerializedName("deactivate_reason")
    String deactivate_reason;


    public WebArdList(String id, String memberid, String visting_memberid, String alias, String path, String userpath, String start_date, String deactivate_reason) {
        this.id = id;
        this.memberid = memberid;
        this.visting_memberid = visting_memberid;
        this.alias = alias;
        this.path = path;
        this.userpath = userpath;
        this.start_date = start_date;
        this.deactivate_reason = deactivate_reason;
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

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getDeactivate_reason() {
        return deactivate_reason;
    }

    public void setDeactivate_reason(String deactivate_reason) {
        this.deactivate_reason = deactivate_reason;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
