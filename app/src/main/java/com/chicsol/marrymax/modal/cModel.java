package com.chicsol.marrymax.modal;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Android on 1/25/2017.
 */

public class cModel {
    @SerializedName("id")
    String id;
    @SerializedName("name")
    String name;
    @SerializedName("code")
    String code;
    @SerializedName("date")
    String date;

    public cModel(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
