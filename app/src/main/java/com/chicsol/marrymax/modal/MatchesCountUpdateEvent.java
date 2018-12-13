package com.chicsol.marrymax.modal;

public class MatchesCountUpdateEvent {

    public String mMessage;

    public MatchesCountUpdateEvent(String message) {
        mMessage = message;
    }

    public String getMessage() {
        return mMessage;
    }
}