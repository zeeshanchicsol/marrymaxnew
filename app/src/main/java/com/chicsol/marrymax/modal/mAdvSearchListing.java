package com.chicsol.marrymax.modal;


import android.support.v4.app.Fragment;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Android on 11/24/2016.
 */


public class mAdvSearchListing {
    @SerializedName("icon")
    public int icon;
    @SerializedName("icon_selected")
    public int icon_selected;
    @SerializedName("title")
    public String title;
    @SerializedName("fragment")
    public Fragment fragment;


    @SerializedName("dataSelection")
    public boolean dataSelection;


    public mAdvSearchListing(int icon, int icon_selected, String title, Fragment frg, boolean dataSelection) {
        super();
        this.icon = icon;
        this.title = title;
        this.fragment = frg;
        this.icon_selected = icon_selected;
        this.dataSelection = dataSelection;

    }

    public int getIcon_selected() {
        return icon_selected;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Fragment getFragment() {
        return fragment;
    }


    public boolean isDataSelection() {
        return dataSelection;
    }

    public void setDataSelection(boolean dataSelection) {
        this.dataSelection = dataSelection;
    }

}