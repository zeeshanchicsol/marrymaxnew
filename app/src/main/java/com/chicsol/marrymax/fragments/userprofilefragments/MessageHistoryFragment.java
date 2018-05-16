package com.chicsol.marrymax.fragments.userprofilefragments;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chicsol.marrymax.R;
import com.chicsol.marrymax.utils.Constants;


public class MessageHistoryFragment extends Fragment {
    private TextView tv_deleteMessage;
    private Typeface typeface;

    public MessageHistoryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Log.e("jsonn  ", getArguments().getString("json"));
        String json = getArguments().getString("json");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_userprofile_message_history, container, false);
        initilize(rootView);
        return rootView;
    }

    private void initilize(View view) {
        typeface = Typeface.createFromAsset(getContext().getAssets(), Constants.font_centurygothic_italic);
        tv_deleteMessage = (TextView) view.findViewById(R.id.TextViewUserProfileDeleteMessage);
        tv_deleteMessage.setTypeface(typeface);
    }

}
