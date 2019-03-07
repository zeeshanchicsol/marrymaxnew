package com.chicsol.marrymax.fragments.matches;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.chicsol.marrymax.R;
import com.chicsol.marrymax.dialogs.dialogMatchingAttributeFragment;

public abstract class BaseMatchesFragment extends Fragment {
    private LinearLayout llMatchPreference;

    private Fragment childFragment;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanseState) {
        View view = provideYourFragmentView(inflater, parent, savedInstanseState);
        childFragment = getChildFragment();

        initialize(view);
        setListeners();


        return view;
    }

    public void initialize(View view) {

        llMatchPreference = (LinearLayout) view.findViewById(R.id.LinearLayoutMatchesMatchPreference);

    }

    public void setListeners() {
        llMatchPreference.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                dialogMatchingAttributeFragment dialogFragment = dialogMatchingAttributeFragment.newInstance("asd");
                dialogFragment.setTargetFragment(childFragment, 0);
                dialogFragment.show(getFragmentManager(), "dialog");
            }
        });
    }


    public abstract View provideYourFragmentView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState);

    public abstract Fragment getChildFragment();

}