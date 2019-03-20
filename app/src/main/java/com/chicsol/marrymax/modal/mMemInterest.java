package com.chicsol.marrymax.modal;

import com.google.gson.annotations.SerializedName;

public class mMemInterest {


    @SerializedName("interested_id")
    public long interested_id;

    @SerializedName("my_id")
    public long my_id;

    @SerializedName("image_view")
    public long image_view;

    @SerializedName("phone_view")
    public long phone_view;

    @SerializedName("feedback_due")
    public int feedback_due;

    public long getInterested_id() {
        return interested_id;
    }

    public void setInterested_id(long interested_id) {
        this.interested_id = interested_id;
    }

    public long getMy_id() {
        return my_id;
    }

    public void setMy_id(long my_id) {
        this.my_id = my_id;
    }

    public long getImage_view() {
        return image_view;
    }

    public void setImage_view(long image_view) {
        this.image_view = image_view;
    }

    public long getPhone_view() {
        return phone_view;
    }

    public void setPhone_view(long phone_view) {
        this.phone_view = phone_view;
    }

    public int getFeedback_due() {
        return feedback_due;
    }

    public void setFeedback_due(int feedback_due) {
        this.feedback_due = feedback_due;
    }
}
