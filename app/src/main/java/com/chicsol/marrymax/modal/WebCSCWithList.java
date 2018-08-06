package com.chicsol.marrymax.modal;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Gunaseelan on 31-12-2015.
 */
public class WebCSCWithList {

    @SerializedName("id")
    private String id;


    @SerializedName("name")
    private String name;


    List<WebCSC> list;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<WebCSC> getList() {
        return list;
    }

    public void setList(List<WebCSC> list) {
        this.list = list;
    }
    /*
    public WebCSCWithList(String id, String cid, String sid, String name) {
        this.cid = cid;
        this.sid = sid;
        this.id = id;
        this.name = name;
    }*/


}
