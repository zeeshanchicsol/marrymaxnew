package com.chicsol.marrymax.fragments.AccountSetting;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chicsol.marrymax.R;
import com.chicsol.marrymax.preferences.SharedPreferenceManager;

public class MyProfileStatusFragment extends Fragment {

    LinearLayout llCompleteProfile, llVeriyEmail, llVerifyPhone, llAdminReview;
    TextView  tvTitle,tvDesc;
    private Context context;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public MyProfileStatusFragment() {
        // Required empty public constructor
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       /* if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }*/
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_myprofilestatus, container, false);

        initilize(rootView);
        //  setListeners();
        return rootView;
    }


    private void initilize(View view) {
        llCompleteProfile = (LinearLayout) view.findViewById(R.id.LinearLayoutMyProfileStatusCompleteProfile);
        llVerifyPhone = (LinearLayout) view.findViewById(R.id.LinearLayoutMyProfileStatusVerifyPhone);
        llVeriyEmail = (LinearLayout) view.findViewById(R.id.LinearLayoutMyProfileStatusVerifyEmail);
        llAdminReview = (LinearLayout) view.findViewById(R.id.LinearLayoutMyProfileStatusAdminApproval);
        String compUptoSSeventyText = "Dear <b> <font color=#216917>" + SharedPreferenceManager.getUserObject(context).getAlias() + "</font></b>, your profile is <b> <font color=#9a0606>Not Live </font></b> ";

    }


    @Override
    public void onDetach() {
        super.onDetach();

    }


}
