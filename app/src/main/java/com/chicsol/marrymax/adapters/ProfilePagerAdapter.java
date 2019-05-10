package com.chicsol.marrymax.adapters;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.util.Log;

import com.chicsol.marrymax.activities.UserProfileActivityFragment;
import com.chicsol.marrymax.modal.Members;

import java.util.ArrayList;
import java.util.List;

public class ProfilePagerAdapter extends FragmentStatePagerAdapter {
    private static final int NUM_ITEMS = 10;
    private ArrayList<String> page_indexes;


    public ProfilePagerAdapter(FragmentManager fm) {
        super(fm);

        page_indexes = new ArrayList<>();
       /* for (int i = 0; i < NUM_ITEMS; i++) {
            page_indexes.add(i);
        }*/
    }

    @Override
    public int getCount() {
        return page_indexes.size();
    }

    @Override
    public Fragment getItem(int position) {
        String userpath = page_indexes.get(position);
        return UserProfileActivityFragment.newInstance(userpath);
    }

    void deletePage(int position) {
        if (canDelete()) {
            page_indexes.remove(position);
            notifyDataSetChanged();
        }
    }

    boolean canDelete() {
        return page_indexes.size() > 0;
    }

    // This is called when notifyDataSetChanged() is called
    @Override
    public int getItemPosition(Object object) {
        // refresh all fragments when data set changed
        return PagerAdapter.POSITION_NONE;
    }

    public void addAll(ArrayList<String> lst) {
        page_indexes.clear();
        page_indexes.addAll(lst);
        notifyDataSetChanged();
        //Log.e("item size in adapter", page_indexes.size() + "");
    }

    public void addItemMore(ArrayList<String> lst, boolean addBackward) {


        if (addBackward) {
            page_indexes.addAll(0, lst);
        } else {
            page_indexes.addAll(lst);
        }

        notifyDataSetChanged();

        //   notifyItemRangeChanged(0, items.size());
    }

    @Override
    public Parcelable saveState() {
        return null;
    }




   /* public void addToTail() {
        int start = page_indexes.size();
        int last = page_indexes.size() + 10;
        for (int i = start; i < last; i++) {
            page_indexes.add(i);
        }

        notifyDataSetChanged();
    }


    public void addToHead() {



        int start = page_indexes.get(0);
        Log.e("start start", start + "");
        int last = start - 10;
        Log.e("last last", last + "");

        for (int i = start; i >= last; i--) {
            Log.e("start", i + "");
            page_indexes.add(0, i);
        }

      *//*  for (int i = 0; i < page_indexes.size(); i++) {
            Log.e("page_indexes", page_indexes.get(i) + "");
        }*//*

        notifyDataSetChanged();

    }*/
}