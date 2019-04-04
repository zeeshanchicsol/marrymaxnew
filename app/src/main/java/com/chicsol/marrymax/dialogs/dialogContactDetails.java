package com.chicsol.marrymax.dialogs;


import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.CardView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.chicsol.marrymax.R;
import com.chicsol.marrymax.activities.directive.MainDirectiveActivity;
import com.chicsol.marrymax.modal.Members;
import com.chicsol.marrymax.modal.mMemPhone;
import com.chicsol.marrymax.other.MarryMax;
import com.chicsol.marrymax.preferences.SharedPreferenceManager;
import com.chicsol.marrymax.widgets.faTextView;
import com.chicsol.marrymax.widgets.mCheckBox;
import com.chicsol.marrymax.widgets.mTextView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;


/**
 * Created by zeedr on 24/10/2016.
 */

public class dialogContactDetails extends DialogFragment {
    // private ListView lv_mycontacts;
    //  public onCompleteListener mCompleteListener;
    String title, desc, params = null;
    mCheckBox cbAllowPhone, cbAllowPics;
    mTextView tvDesc, tvTitle;
    String userpath, alias;
    boolean replyCheck;
    mMemPhone member;

    private int feedback_due;
    private AppCompatButton btGiveFeedbackInterest;
    private CardView cvFeedbackPending;
    private TextView tvFeedbackPending;
    private AppCompatButton btGiveFeedback;

    public static dialogContactDetails newInstance(String params, String alias, int feedback_due) {
        /*       Members member, String userpath, boolean replyCheck, Members member2*/
        dialogContactDetails frag = new dialogContactDetails();
        Bundle args = new Bundle();


        // args.putString("name", name);
        args.putString("alias", alias);
        args.putString("params", params);
        args.putInt("feedback_due", feedback_due);

        /*args.putString("param", String.valueOf(member2.getPhone_view()));
        args.putString("my_id", String.valueOf(member2.getMy_id()));
        args.putString("checkedTextView", member.getAlias());
        args.putString("userpath", userpath);
*/

        frag.setArguments(args);
        return frag;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle mArgs = getArguments();
        //    replyCheck = mArgs.getBoolean("replyCheck");
        //  name = mArgs.getString("name");
        alias = mArgs.getString("alias");
        params = mArgs.getString("params");
        feedback_due = mArgs.getInt("feedback_due");
      /*  my_id = mArgs.getString("my_id");
        userpath = mArgs.getString("userpath");
        checkedTextView = mArgs.getString("checkedTextView");*/

     /*   Log.e("name Fragment", name);
        Log.e("desc Fragment", desc);
        Log.e("param Fragment", param);
        Log.e("my_id Fragment", my_id);*/

        Gson gson;
        GsonBuilder gsonBuilder = new GsonBuilder();

        gson = gsonBuilder.create();
        Type type = new TypeToken<mMemPhone>() {
        }.getType();
        member = (mMemPhone) gson.fromJson(params, type);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.dialog_contact_details, container, false);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);


        tvFeedbackPending = (TextView) rootView.findViewById(R.id.TextViewDashMainFeedbackPending);
        cvFeedbackPending = (CardView) rootView.findViewById(R.id.CardViewDashMainFeedbackPending);
        btGiveFeedback = (AppCompatButton) rootView.findViewById(R.id.ButtonDashMainFeedbackPending);
        btGiveFeedback.setVisibility(View.GONE);
        btGiveFeedbackInterest = (AppCompatButton) rootView.findViewById(R.id.mButtonInterestGiveFeedback);

        String desc = "Here is the contact detail of  <b> <font color=#216917>" + alias + "</font></b>";

        tvTitle = (mTextView) rootView.findViewById(R.id.TextViewContactDialogTitle);

        mTextView contactName = (mTextView) rootView.findViewById(R.id.TextViewContactDialogContactName);
        mTextView contactPhone = (mTextView) rootView.findViewById(R.id.TextViewContactDialogMobileNumber);
        mTextView contactRelationShip = (mTextView) rootView.findViewById(R.id.TextViewContactDialogRelationship);
        mTextView contactCalltime = (mTextView) rootView.findViewById(R.id.TextViewContactDialogPrefferedCallTime);
        mTextView contactCountryName = (mTextView) rootView.findViewById(R.id.TextViewContactDialogCountryName);

        contactName.setText("" + member.getPersonal_name());

        contactPhone.setText("" + member.getPhone_mobile());


        contactRelationShip.setText("" + member.getProfile_owner());

        contactCalltime.setText("" + member.getNotes());

        contactCountryName.setText("" + member.getCountry_name());


        Button mOkButton = (Button) rootView.findViewById(R.id.ButtonContactDialogCancel);



    /*    String aliass= SharedPreferenceManager.getUserObject(getContext()).getAlias();
        String aliasn = "<font color='#9a0606'>" + aliass + "!</font><br>";
        if (feedback_due == 1) {

            String text = "Dear " + "<b>" + aliasn.toUpperCase() + "</b> your Feedback is Pending. To continue viewing more phone numbers or sending interest you need to provide feedback.";
            tvFeedbackPending.setText(Html.fromHtml(text));
            btGiveFeedbackInterest.setVisibility(View.VISIBLE);
            cvFeedbackPending.setVisibility(View.VISIBLE);

        } else if (feedback_due == 2) {
            String text = "Dear " + "<b>" + aliasn.toUpperCase() + "</b> Your Feedback are due. To continue viewing more phone numbers or sending interest you need to provide feedback.";
            tvFeedbackPending.setBackgroundColor(Color.parseColor("#fff5d7"));
            tvFeedbackPending.setText(Html.fromHtml(text));
            btGiveFeedbackInterest.setVisibility(View.VISIBLE);
            cvFeedbackPending.setVisibility(View.VISIBLE);

        } else {
            btGiveFeedbackInterest.setVisibility(View.GONE);
            cvFeedbackPending.setVisibility(View.GONE);
        }*/


        MarryMax max = new MarryMax(null);
        if (feedback_due == 0) {
            cvFeedbackPending.setVisibility(View.GONE);
            btGiveFeedbackInterest.setVisibility(View.GONE);

        } else {

            String desca = max.getFeedbackText(feedback_due, getContext());
            tvFeedbackPending.setText(Html.fromHtml(desca));
            cvFeedbackPending.setVisibility(View.VISIBLE);
            btGiveFeedbackInterest.setVisibility(View.VISIBLE);
        }








        //  tvTitle.setText(title);


        if (Build.VERSION.SDK_INT >= 24) {
            // for 24 api and more
            tvTitle.setText(Html.fromHtml(desc, Html.FROM_HTML_MODE_LEGACY));
        } else {
            tvTitle.setText(Html.fromHtml(desc));
        }


        mOkButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(final View v) {

                dialogContactDetails.this.getDialog().cancel();

            }
        });
        btGiveFeedbackInterest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getContext(), MainDirectiveActivity.class);
                in.putExtra("type", 25);
                // in.putExtra("subtype", "received");
                startActivity(in);
            }
        });


        faTextView cancelButton = (faTextView) rootView.findViewById(R.id.faButtonContactDialogDismiss);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(final View v) {
                //Toast.makeText(getActivity().getApplicationContext(), "clcieck", Toast.LENGTH_SHORT).show();
                dialogContactDetails.this.getDialog().cancel();
            }
        });


        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }


}


