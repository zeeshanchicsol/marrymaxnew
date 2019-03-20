package com.chicsol.marrymax.modal;

import com.google.gson.annotations.SerializedName;

public class MatchesCountUpdateEvent {

    @SerializedName("mMessage")
    public String mMessage;

    public MatchesCountUpdateEvent(String message) {
        mMessage = message;
    }

    public String getMessage() {
        return mMessage;
    }
}