package com.chicsol.marrymax.adapters;

/**
 * Created by Android on 11/22/2016.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.chicsol.marrymax.R;
import com.chicsol.marrymax.modal.mProperties;

import java.util.ArrayList;
import java.util.List;

public class NotificationSpinnerAdapter extends ArrayAdapter<mProperties> {

    // declaring our ArrayList of items
    private ArrayList<mProperties> items;

    /* here we must override the constructor for ArrayAdapter
    * the only variable we care about now is ArrayList<ItemNotificaitonDashMain> items,
    * because it is the list of items we want to display.
    */
    public NotificationSpinnerAdapter(Context context, int textViewResourceId, ArrayList<mProperties> objects) {
        super(context, textViewResourceId, objects);
        this.items = objects;
    }

    /*
     * we are overriding the getView method here - this is what defines how each
     * list item_slider will look.
     */
    public View getView(int position, View convertView, ViewGroup parent) {

        // assign the view we are converting to a local variable
        View v = convertView;

        // first check to see if the view is null. if so, we have to inflate it.
        // to inflate it basically means to render, or show, the view.
        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.item_spinner_notifications, null);
        }

		/*
         * Recall that the variable position is sent in as an argument to this method.
		 * The variable simply refers to the position of the current object in the list. (The ArrayAdapter
		 * iterates through the list we sent it)
		 *
		 * Therefore, i refers to the current ItemNotificaitonDashMain object.
		 */
        mProperties i = items.get(position);

        if (i != null) {

            // This is how you obtain a reference to the TextViews.
            // These TextViews are created in the XML files we defined.

            TextView tv_userName = (TextView) v.findViewById(R.id.TextViewDashNotificationUserName);
            TextView tv_userNotiDesc = (TextView) v.findViewById(R.id.TextViewDashNotificationDescription);


            // check to see if each individual textview is null.
            // if not, assign some text!
            if (tv_userName != null) {
                tv_userName.setText(i.getAlias());
            }
            if (tv_userNotiDesc != null) {
                tv_userNotiDesc.setText(i.getName());
            }

        }

        // the view must be returned to our activity
        return v;

    }
    public void addAll(List<mProperties> lst) {
        items.clear();
        items.addAll(lst);
        notifyDataSetChanged();
    }

}