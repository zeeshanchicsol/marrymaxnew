package com.chicsol.marrymax.modal;

import com.google.gson.annotations.SerializedName;

/**
 * Created by muneeb on 8/29/17.
 */

public class mPayments {

    @SerializedName("transpath")
    public String transpath;
    @SerializedName("card_name")
    public String card_name;
    @SerializedName("card_number")
    public long card_number;
    @SerializedName("card_type")
    public String card_type;
    @SerializedName("ccv_number")
    public int ccv_number;
    @SerializedName("month")
    public int month;
    @SerializedName("year")
    public int year;
    @SerializedName("amount")
    public float amount;
    @SerializedName("email")
    public String email;
    @SerializedName("alias")
    public String alias;
    @SerializedName("personal_name")
    public String personal_name;
    @SerializedName("description")
    public String description;
    @SerializedName("help_person")
    public String help_person;

    public String getTranspath() {
        return transpath;
    }

    public void setTranspath(String transpath) {
        this.transpath = transpath;
    }

    public String getCard_name() {
        return card_name;
    }

    public void setCard_name(String card_name) {
        this.card_name = card_name;
    }

    public long getCard_number() {
        return card_number;
    }

    public void setCard_number(long card_number) {
        this.card_number = card_number;
    }

    public String getCard_type() {
        return card_type;
    }

    public void setCard_type(String card_type) {
        this.card_type = card_type;
    }

    public int getCcv_number() {
        return ccv_number;
    }

    public void setCcv_number(int ccv_number) {
        this.ccv_number = ccv_number;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getPersonal_name() {
        return personal_name;
    }

    public void setPersonal_name(String personal_name) {
        this.personal_name = personal_name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getHelp_person() {
        return help_person;
    }

    public void setHelp_person(String help_person) {
        this.help_person = help_person;
    }


}
