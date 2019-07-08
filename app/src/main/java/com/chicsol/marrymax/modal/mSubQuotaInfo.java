package com.chicsol.marrymax.modal;

import com.google.gson.annotations.SerializedName;

/**
 * Created by macintoshhd on 12/7/17.
 */

public class mSubQuotaInfo {


    @SerializedName("contacts")
    public String contacts;


    @SerializedName("contacts_count")
    public String contacts_count;

    @SerializedName("messages")
    public String messages;

    @SerializedName("messages_count")
    public String messages_count;

    public String getContacts() {
        return contacts;
    }

    public void setContacts(String contacts) {
        this.contacts = contacts;
    }

    public String getContacts_count() {
        return contacts_count;
    }

    public void setContacts_count(String contacts_count) {
        this.contacts_count = contacts_count;
    }

    public String getMessages() {
        return messages;
    }

    public void setMessages(String messages) {
        this.messages = messages;
    }

    public String getMessages_count() {
        return messages_count;
    }

    public void setMessages_count(String messages_count) {
        this.messages_count = messages_count;
    }
}
