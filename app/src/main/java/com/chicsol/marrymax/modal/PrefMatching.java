package com.chicsol.marrymax.modal;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Android on 12/15/2016.
 */

public class PrefMatching {
    @SerializedName("id")
    String id;
    @SerializedName("type")
    String type;
    @SerializedName("about_type_id")
    int about_type_id;

    public PrefMatching(String id, String type, int about_type_id) {
        this.id = id;
        this.type = type;
        this.about_type_id = about_type_id;
    }


    public String getType() {
        return type;
    }

    public void setType(String name) {
        this.type = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public int getAbout_type_id() {
        return about_type_id;
    }

    public void setAbout_type_id(int about_type_id) {
        this.about_type_id = about_type_id;
    }
}
