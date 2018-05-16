package com.chicsol.marrymax.activities;

/**
 * Created by Android on 12/5/2016.
 */


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.LinearLayout;

import com.chicsol.marrymax.R;
import com.chicsol.marrymax.adapters.ExpandableListAdapter;
import com.chicsol.marrymax.widgets.mTextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FaqActivity extends AppCompatActivity {

    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    LinearLayout lastHeaderLL;
    mTextView lastHeaderTextView;
    private int lastExpandedPosition = -1;
    View lastview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq);
        lastHeaderLL = null;
        lastHeaderTextView = null;
        lastview = null;
          /*  Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_faq);
            setSupportActionBar(toolbar);*/

        // get the listview
        expListView = (ExpandableListView) findViewById(R.id.lvExp);
        expListView.setChildIndicator(null);
        expListView.setChildDivider(getResources().getDrawable(R.color.colorWhite));
        // preparing list data
        prepareListData();

        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);

        // setting list adapter
        expListView.setAdapter(listAdapter);

        // Listview Group click listener
        expListView.setOnGroupClickListener(new OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {


                if (lastview != null) {
                    lastHeaderTextView.setTextColor(getResources().getColor(R.color.colorBlack));
                    lastHeaderLL.setBackgroundColor(getResources().getColor(R.color.colorWhite));


                }

                lastHeaderTextView = (mTextView) v.findViewById(R.id.lblListHeader);
                lastHeaderTextView.setTextColor(getResources().getColor(R.color.colorWhite));


                lastHeaderLL = (LinearLayout) v.findViewById(R.id.LinearLayoutListHeader);
                lastHeaderLL.setBackgroundColor(getResources().getColor(R.color.colorDefaultGreen));

                lastview = v;
       /*       Toast.makeText(getApplicationContext(),
                "Group Clicked " + listDataHeader.get(groupPosition),
                 Toast.LENGTH_SHORT).show();*/
                return false;
            }
        });

        // Listview Group expanded listener
        expListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
/* if (lastExpandedPosition != -1
                        && groupPosition != lastExpandedPosition) {
                    expListView.collapseGroup(lastExpandedPosition);
                }
                lastExpandedPosition = groupPosition;*/
            }
        });

        // Listview Group collasped listener
      /*  expListView.setOnGroupCollapseListener(new OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {
                Toast.makeText(getApplicationContext(),
                        listDataHeader.get(groupPosition) + " Collapsed",
                        Toast.LENGTH_SHORT).show();

            }
        });*/

        // Listview on child click listener
     /*   expListView.setOnChildClickListener(new OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                // TODO Auto-generated method stub
                Toast.makeText(
                        getApplicationContext(),
                        listDataHeader.get(groupPosition)
                                + " : "
                                + listDataChild.get(
                                listDataHeader.get(groupPosition)).get(
                                childPosition), Toast.LENGTH_SHORT)
                        .show();
                return false;
            }
        });*/
    }

    /*
     * Preparing the list data
     */
    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        // Adding child data
        listDataHeader.add("Can i register a matrimonial account on behalf of a relative or friend ?");
        listDataHeader.add("Now Showing");
        listDataHeader.add("Coming Soon..");

        // Adding child data
        List<String> top250 = new ArrayList<String>();
        top250.add("We understand that it may become tedious to fill a long form, however; matrimony is a serious matter. It very important to convey your detailed information to interested members. The more information you provide about yourself the more likely you become.");


        List<String> nowShowing = new ArrayList<String>();
        nowShowing.add("We understand that it may become tedious to fill a long form, however; matrimony is a serious matter. It very important to convey your detailed information to interested members. The more information you provide about yourself the more likely you become.");


        List<String> comingSoon = new ArrayList<String>();
        comingSoon.add("We understand that it may become tedious to fill a long form, however; matrimony is a serious matter. It very important to convey your detailed information to interested members. The more information you provide about yourself the more likely you become.");


        listDataChild.put(listDataHeader.get(0), top250); // Header, Child data
        listDataChild.put(listDataHeader.get(1), nowShowing);
        listDataChild.put(listDataHeader.get(2), comingSoon);
    }
}