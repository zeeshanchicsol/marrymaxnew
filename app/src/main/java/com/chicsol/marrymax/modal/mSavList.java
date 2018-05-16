package com.chicsol.marrymax.modal;

import com.google.gson.annotations.SerializedName;

/**
 * Created by macintoshhd on 11/30/17.
 */

public class mSavList {


    @SerializedName("id")
    public String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getPrefvalue1() {
        return prefvalue1;
    }

    public void setPrefvalue1(String prefvalue1) {
        this.prefvalue1 = prefvalue1;
    }

    @SerializedName("path")
    public String path;




    @SerializedName("alias")
    public String alias;

    @SerializedName("prefvalue1")
    public String prefvalue1;


}
