package com.chicsol.marrymax.modal;

import com.google.gson.annotations.SerializedName;

/**
 * Created by macintoshhd on 11/27/17.
 */

public class mMemList {

    @SerializedName("id")
    public String id;

    @SerializedName("my_id")
    public String my_id;

    @SerializedName("name")
    public String name;

    @SerializedName("total_member_count")
    public String total_member_count;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMy_id() {
        return my_id;
    }

    public void setMy_id(String my_id) {
        this.my_id = my_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTotal_member_count() {
        return total_member_count;
    }

    public void setTotal_member_count(String total_member_count) {
        this.total_member_count = total_member_count;
    }
}
