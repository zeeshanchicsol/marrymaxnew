package com.chicsol.marrymax.modal;

import com.google.gson.annotations.SerializedName;

/**
 * Created by macintoshhd on 12/7/17.
 */

public class mDshCount {


    @SerializedName("name")
    public String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getReceived_count() {
        return received_count;
    }

    public void setReceived_count(String received_count) {
        this.received_count = received_count;
    }

    public String getSent_count() {
        return sent_count;
    }

    public void setSent_count(String sent_count) {
        this.sent_count = sent_count;
    }

    public String getTypesent() {
        return typesent;
    }

    public void setTypesent(String typesent) {
        this.typesent = typesent;
    }

    public String getTypereceived() {
        return typereceived;
    }

    public void setTypereceived(String typereceived) {
        this.typereceived = typereceived;
    }

    @SerializedName("received_count")
    public String received_count;

    @SerializedName("sent_count")
    public String sent_count;

    @SerializedName("typesent")
    public String typesent;

    @SerializedName("typereceived")
    public String typereceived;


}
