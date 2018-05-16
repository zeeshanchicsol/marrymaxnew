package com.chicsol.marrymax.modal;

import com.google.gson.annotations.SerializedName;

/**
 * Created by muneeb on 7/31/17.
 */

public class mRequestPermission {

    @SerializedName("privilege_type_id")
    public int privilege_type_id;
    @SerializedName("type")
    public String type;
    @SerializedName("name")
    public String name;
    @SerializedName("request_id")
    public long request_id;
    @SerializedName("member_status")
    public long member_status;
    @SerializedName("self")
    public long self;

    public int getPrivilege_type_id() {
        return privilege_type_id;
    }

    public void setPrivilege_type_id(int privilege_type_id) {
        this.privilege_type_id = privilege_type_id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getRequest_id() {
        return request_id;
    }

    public void setRequest_id(long request_id) {
        this.request_id = request_id;
    }

    public long getMember_status() {
        return member_status;
    }

    public void setMember_status(long member_status) {
        this.member_status = member_status;
    }

    public long getSelf() {
        return self;
    }

    public void setSelf(long self) {
        this.self = self;
    }


}
