package com.chicsol.marrymax.modal;

import com.google.gson.annotations.SerializedName;

public class PhoneVerificationStatusUpdateEvent {

    @SerializedName("mMessage")
    public String mMessage;

    public PhoneVerificationStatusUpdateEvent(String message) {
        mMessage = message;
    }

    public String getMessage() {
        return mMessage;
    }
}