package com.chicsol.marrymax.modal;

import com.google.gson.annotations.SerializedName;

/**
 * Created by muneeb on 4/25/17.
 */

public class mAsEmailNotifications {
    @SerializedName("id")
    String id;
    @SerializedName("type")
    String type;
    @SerializedName("isedit")
    String isedit;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIsedit() {
        return isedit;
    }

    public void setIsedit(String isedit) {
        this.isedit = isedit;
    }
}
