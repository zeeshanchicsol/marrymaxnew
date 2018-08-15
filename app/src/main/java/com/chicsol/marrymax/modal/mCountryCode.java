package com.chicsol.marrymax.modal;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Android on 12/15/2016.
 */

public class mCountryCode {
    @SerializedName("id")
    String id;
    @SerializedName("name")
    String name;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @SerializedName("code")
    String code;
    private boolean isSelected;

    public mCountryCode(String id, String name,String code) {
        this.id = id;
        this.name = name;
        this.code = code;
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

}
