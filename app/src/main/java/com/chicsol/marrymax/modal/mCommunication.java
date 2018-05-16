package com.chicsol.marrymax.modal;

import com.google.gson.annotations.SerializedName;

/**
 * Created by muneeb on 5/3/17.
 */

public class mCommunication {
    @SerializedName("id")
    public long id;
    @SerializedName("message_id")
    public String message_id;
    @SerializedName("path")
    public String path;
    @SerializedName("userpath")
    public String userpath;
    @SerializedName("to_member_id")
    public long to_member_id;
    @SerializedName("to_member_desc_id")
    public String to_member_desc_id;
    @SerializedName("from_member_id")
    public long from_member_id;
    @SerializedName("alias")
    public String alias;
    @SerializedName("gender")
    public String gender;
    @SerializedName(value = "ethnic_background_type", alternate = {"ethnic_background_types"})
    public String ethnic_background_type;
    @SerializedName(value = "religious_sect_type", alternate = {"religious_sec_type"})
    public String religious_sect_type;
    @SerializedName("answered")
    public long answered;
    @SerializedName("answer_viewed")
    public long answer_viewed;
    @SerializedName("subject")
    public String subject;
    @SerializedName("message")
    public String message;
    @SerializedName("created_date")
    public String created_date;
    @SerializedName("message_date")
    public String message_date;
    @SerializedName("read_status")
    public long read_status;
    @SerializedName("read_quota")
    public long read_quota;
    @SerializedName("write_quota")
    public long write_quota;
    @SerializedName(value = "country_name", alternate = {"member_country_name"})
    public String country_name;
    @SerializedName("country_flag")
    public String country_flag;
    @SerializedName("default_image")
    public String default_image;
    @SerializedName("age")
    public long age;
    @SerializedName("education_types")
    public String education_types;
    @SerializedName("count")
    public long count;
    @SerializedName("self")
    public long self;
    @SerializedName("request_type_id")
    public long request_type_id;
    @SerializedName("occupation_types")
    public String occupation_types;
    @SerializedName("request_id")
    public String request_id;
    @SerializedName("interested_members_count")
    public long interested_members_count;
    @SerializedName("requesting_members_count")
    public long requesting_members_count;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getMessage_id() {
        return message_id;
    }

    public void setMessage_id(String message_id) {
        this.message_id = message_id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getUserpath() {
        return userpath;
    }

    public void setUserpath(String userpath) {
        this.userpath = userpath;
    }

    public long getTo_member_id() {
        return to_member_id;
    }

    public void setTo_member_id(long to_member_id) {
        this.to_member_id = to_member_id;
    }

    public String getTo_member_desc_id() {
        return to_member_desc_id;
    }

    public void setTo_member_desc_id(String to_member_desc_id) {
        this.to_member_desc_id = to_member_desc_id;
    }

    public long getFrom_member_id() {
        return from_member_id;
    }

    public void setFrom_member_id(long from_member_id) {
        this.from_member_id = from_member_id;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEthnic_background_type() {
        return ethnic_background_type;
    }

    public void setEthnic_background_type(String ethnic_background_type) {
        this.ethnic_background_type = ethnic_background_type;
    }

    public String getReligious_sect_type() {
        return religious_sect_type;
    }

    public void setReligious_sect_type(String religious_sect_type) {
        this.religious_sect_type = religious_sect_type;
    }

    public long getAnswered() {
        return answered;
    }

    public void setAnswered(long answered) {
        this.answered = answered;
    }

    public long getAnswer_viewed() {
        return answer_viewed;
    }

    public void setAnswer_viewed(long answer_viewed) {
        this.answer_viewed = answer_viewed;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCreated_date() {
        return created_date;
    }

    public void setCreated_date(String created_date) {
        this.created_date = created_date;
    }

    public String getMessage_date() {
        return message_date;
    }

    public void setMessage_date(String message_date) {
        this.message_date = message_date;
    }

    public long getRead_status() {
        return read_status;
    }

    public void setRead_status(long read_status) {
        this.read_status = read_status;
    }

    public long getRead_quota() {
        return read_quota;
    }

    public void setRead_quota(long read_quota) {
        this.read_quota = read_quota;
    }

    public long getWrite_quota() {
        return write_quota;
    }

    public void setWrite_quota(long write_quota) {
        this.write_quota = write_quota;
    }

    public String getCountry_name() {
        return country_name;
    }

    public void setCountry_name(String country_name) {
        this.country_name = country_name;
    }

    public String getCountry_flag() {
        return country_flag;
    }

    public void setCountry_flag(String country_flag) {
        this.country_flag = country_flag;
    }

    public String getDefault_image() {
        return default_image;
    }

    public void setDefault_image(String default_image) {
        this.default_image = default_image;
    }

    public long getAge() {
        return age;
    }

    public void setAge(long age) {
        this.age = age;
    }

    public String getEducation_types() {
        return education_types;
    }

    public void setEducation_types(String education_types) {
        this.education_types = education_types;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public long getSelf() {
        return self;
    }

    public void setSelf(long self) {
        this.self = self;
    }

    public long getRequest_type_id() {
        return request_type_id;
    }

    public void setRequest_type_id(long request_type_id) {
        this.request_type_id = request_type_id;
    }

    public String getOccupation_types() {
        return occupation_types;
    }

    public void setOccupation_types(String occupation_types) {
        this.occupation_types = occupation_types;
    }

    public String getRequest_id() {
        return request_id;
    }

    public void setRequest_id(String request_id) {
        this.request_id = request_id;
    }

    public long getInterested_members_count() {
        return interested_members_count;
    }

    public void setInterested_members_count(long interested_members_count) {
        this.interested_members_count = interested_members_count;
    }

    public long getRequesting_members_count() {
        return requesting_members_count;
    }

    public void setRequesting_members_count(long requesting_members_count) {
        this.requesting_members_count = requesting_members_count;
    }

}
