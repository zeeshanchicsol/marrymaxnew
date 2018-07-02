package com.chicsol.marrymax.modal;

import java.util.ArrayList;

/**
 * Created by macintoshhd on 12/26/17.
 */


public class mParentChild {



    String id;
    String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    ArrayList<mChild> child;

    public mParentChild() {
    }

    public ArrayList<mChild> getChild() {
        return child;
    }

    public void setChild(ArrayList<mChild> child) {
        this.child = child;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}