package com.chicsol.marrymax.modal;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Android on 12/15/2016.
 */

public class WebArdType {
    @SerializedName("id")
    String id;
    @SerializedName("type")
    String type;

    public WebArdType(String id, String name) {
        this.id = id;
        this.type = name;
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
}
