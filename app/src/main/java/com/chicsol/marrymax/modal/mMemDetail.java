package com.chicsol.marrymax.modal;

import com.google.gson.annotations.SerializedName;

public class mMemDetail {

    @SerializedName("info_id")
    public long info_id;

    @SerializedName("residence")
    public String residence;

    @SerializedName("parents")
    public String parents;

    @SerializedName("siblings")
    public String siblings;

    @SerializedName("academic")
    public String academic;

    @SerializedName("social")
    public String social;


    @SerializedName("jobinfo")
    public String jobinfo;


    public String getJobinfo() {
        return jobinfo;
    }

    public void setJobinfo(String jobinfo) {
        this.jobinfo = jobinfo;
    }

    public long getInfo_id() {
        return info_id;
    }

    public void setInfo_id(long info_id) {
        this.info_id = info_id;
    }

    public String getResidence() {
        return residence;
    }

    public void setResidence(String residence) {
        this.residence = residence;
    }

    public String getParents() {
        return parents;
    }

    public void setParents(String parents) {
        this.parents = parents;
    }

    public String getSiblings() {
        return siblings;
    }

    public void setSiblings(String siblings) {
        this.siblings = siblings;
    }

    public String getAcademic() {
        return academic;
    }

    public void setAcademic(String academic) {
        this.academic = academic;
    }

    public String getSocial() {
        return social;
    }

    public void setSocial(String social) {
        this.social = social;
    }
}
