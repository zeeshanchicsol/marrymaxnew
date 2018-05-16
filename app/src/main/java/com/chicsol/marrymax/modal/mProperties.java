package com.chicsol.marrymax.modal;

import com.google.gson.annotations.SerializedName;

/**
 * Notificatio archive model
 */

public class mProperties {
    @SerializedName("alias")
    public String alias;
    @SerializedName("path")
    public String path;
    @SerializedName("userpath")
    public String userpath;
    @SerializedName("default_image")
    public String default_image;

    @SerializedName("type")
    public String type;

    @SerializedName("viewd")
    public long viewd;

    @SerializedName("name")
    public String name;

    @SerializedName("joined_date")
    public String joined_date;

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

    public String getDefault_image() {
        return default_image;
    }

    public void setDefault_image(String default_image) {
        this.default_image = default_image;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getViewd() {
        return viewd;
    }

    public void setViewd(long viewd) {
        this.viewd = viewd;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getJoined_date() {
        return joined_date;
    }

    public void setJoined_date(String joined_date) {
        this.joined_date = joined_date;
    }


}
