package com.chicsol.marrymax.modal;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Gunaseelan on 31-12-2015.
 */
public class WebCSC {
    @SerializedName("cid")
    private String cid;
    @SerializedName("sid")
    private String sid;
    @SerializedName("id")
    private String id;
    @SerializedName("name")
    private String name;

    private boolean isSelected;


    public WebCSC(String id, String cid, String sid, String name) {
        this.cid = cid;
        this.sid = sid;
        this.id = id;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }
}
