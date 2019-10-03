package com.chicsol.marrymax.modal;

import com.google.gson.annotations.SerializedName;

/**
 * Created by muneeb on 4/21/17.
 */

public class mInstallment {

    @SerializedName("item_name")
    public String item_name;

    @SerializedName("pkr_price")
    public String pkr_price;

    @SerializedName("usd_price")
    public String usd_price;

    @SerializedName("phone")
    public String phone;

    @SerializedName("contact")
    public String contact;

    @SerializedName("duration")
    public String duration;

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public String getPkr_price() {
        return pkr_price;
    }

    public void setPkr_price(String pkr_price) {
        this.pkr_price = pkr_price;
    }

    public String getUsd_price() {
        return usd_price;
    }

    public void setUsd_price(String usd_price) {
        this.usd_price = usd_price;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }
}
