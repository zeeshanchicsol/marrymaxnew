package com.chicsol.marrymax.dialogs;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.AppCompatButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.chicsol.marrymax.R;
import com.chicsol.marrymax.activities.MatchAidActivity;
import com.chicsol.marrymax.widgets.faTextView;

import org.json.JSONArray;


/**
 * Created by zeedr on 24/10/2016.
 */

public class dialogMatchAidUnderProcess extends DialogFragment {


    String userpath, jsarray;

    TextView tvDesc1, tvDesc2;
    int response;

    public static dialogMatchAidUnderProcess newInstance(JSONArray jsArray, String userpath, int response) {

        dialogMatchAidUnderProcess frag = new dialogMatchAidUnderProcess();
        Bundle args = new Bundle();
        args.putString("jsArray", jsArray.toString());
        args.putString("userpath", userpath);
        args.putInt("response", response);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle mArgs = getArguments();

        jsarray = mArgs.getString("jsArray");
        userpath = mArgs.getString("userpath");
        response = mArgs.getInt("response");

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.dialog_match_aid_underprocess, container, false);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);




        Button mOkButton = (Button) rootView.findViewById(R.id.mButtonDialogMatchAidUPViewProgress);
        mOkButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(final View v) {
                dialogMatchAidUnderProcess.this.getDialog().cancel();
                Intent intent = new Intent(getActivity(), MatchAidActivity.class);
                startActivity(intent);

            }
        });


        faTextView cancelButton = (faTextView) rootView.findViewById(R.id.dismissBtnMatchAidUPCC);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(final View v) {

                dialogMatchAidUnderProcess.this.getDialog().cancel();
            }
        });

        AppCompatButton cancelButton1 = (AppCompatButton) rootView.findViewById(R.id.mButtonDialogMatchAidUPCancel);
        cancelButton1.setOnClickListener(new View.OnClickListener() {
            public void onClick(final View v) {

                dialogMatchAidUnderProcess.this.getDialog().cancel();
            }
        });

        tvDesc1 = (TextView) rootView.findViewById(R.id.TextViewMAUnderProgressDesc1);
        tvDesc2 = (TextView) rootView.findViewById(R.id.TextViewMAUnderProgressDesc2);

        if(response==-1){
            tvDesc1.setText("Please wait few more days, before you can submit a new Match-Aid request.");
            tvDesc2.setText("Only one Match-Aid request is served within ten days.");


        }




        return rootView;
    }


    @Override
    public void onStart() {
        super.onStart();

        getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }


}


