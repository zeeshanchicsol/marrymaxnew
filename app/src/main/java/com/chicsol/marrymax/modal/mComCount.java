package com.chicsol.marrymax.modal;

import com.google.gson.annotations.SerializedName;

/**
 * Created by macintoshhd on 3/14/18.
 */

public class mComCount {


    public long getNew_messages_count() {
        return new_messages_count;
    }

    public void setNew_messages_count(long new_messages_count) {
        this.new_messages_count = new_messages_count;
    }

    public long getNew_interests_count() {
        return new_interests_count;
    }

    public void setNew_interests_count(long new_interests_count) {
        this.new_interests_count = new_interests_count;
    }

    public long getNew_requests_count() {
        return new_requests_count;
    }

    public void setNew_requests_count(long new_requests_count) {
        this.new_requests_count = new_requests_count;
    }

    public long getNew_questions_count() {
        return new_questions_count;
    }

    public void setNew_questions_count(long new_questions_count) {
        this.new_questions_count = new_questions_count;
    }

    public String getFeedback_pending() {
        return feedback_pending;
    }

    public void setFeedback_pending(String feedback_pending) {
        this.feedback_pending = feedback_pending;
    }


    @SerializedName("feedback_pending")
    public String feedback_pending;

    @SerializedName("new_messages_count")
    public long new_messages_count;
    @SerializedName("new_interests_count")
    public long new_interests_count;
    @SerializedName("new_requests_count")
    public long new_requests_count;
    @SerializedName("new_questions_count")
    public long new_questions_count;


}
