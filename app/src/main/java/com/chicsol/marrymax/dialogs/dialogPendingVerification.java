package com.chicsol.marrymax.dialogs;


import android.content.Context;
import android.content.Intent;
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
import android.widget.TableRow;
import android.widget.TextView;

import com.chicsol.marrymax.R;
import com.chicsol.marrymax.activities.directive.MainDirectiveActivity;
import com.chicsol.marrymax.modal.mMemPhone;
import com.chicsol.marrymax.other.MarryMax;
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

public class dialogPendingVerification extends DialogFragment {

    private onCompleteListener mCompleteListener;

    mTextView tvTitle;
    String desc, tag;


    private AppCompatButton ButtonDialogPhoneVerificationPending, ButtonDialogEmailVerificationPending;

    public static dialogPendingVerification newInstance(String tag, String desc) {
        /*       Members member, String userpath, boolean replyCheck, Members member2*/
        dialogPendingVerification frag = new dialogPendingVerification();
        Bundle args = new Bundle();


        args.putString("tag", tag);
        args.putString("desc", desc);


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
        tag = mArgs.getString("tag");
        desc = mArgs.getString("desc");


      /*  my_id = mArgs.getString("my_id");
        userpath = mArgs.getString("userpath");
        checkedTextView = mArgs.getString("checkedTextView");*/

     /*   Log.e("name Fragment", name);
        Log.e("desc Fragment", desc);
        Log.e("param Fragment", param);
        Log.e("my_id Fragment", my_id);*/


    }

    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        try {


            if (getTargetFragment() != null) {
                mCompleteListener = (onCompleteListener) getTargetFragment();
            } else {
                mCompleteListener = (onCompleteListener) activity;
            }
        } catch (ClassCastException e) {
            throw new ClassCastException(e.toString() + " must implement OnCompleteListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.dialog_verification_pending, container, false);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);


        ButtonDialogPhoneVerificationPending = (AppCompatButton) rootView.findViewById(R.id.ButtonDialogPhoneVerificationPending);
        ButtonDialogEmailVerificationPending = (AppCompatButton) rootView.findViewById(R.id.ButtonDialogEmailVerificationPending);


        if (tag.equals("email")) {
            ButtonDialogPhoneVerificationPending.setVisibility(View.GONE);

        } else if (tag.equals("phone")) {
            ButtonDialogEmailVerificationPending.setVisibility(View.GONE);
        } else {
            ButtonDialogEmailVerificationPending.setVisibility(View.VISIBLE);
            ButtonDialogPhoneVerificationPending.setVisibility(View.VISIBLE);
        }


        tvTitle = (mTextView) rootView.findViewById(R.id.TextViewContactDialogTitle);


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


        //  tvTitle.setText(title);


        if (Build.VERSION.SDK_INT >= 24) {
            // for 24 api and more
            tvTitle.setText(Html.fromHtml(desc, Html.FROM_HTML_MODE_LEGACY));
        } else {
            tvTitle.setText(Html.fromHtml(desc));
        }


        mOkButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(final View v) {

                dialogPendingVerification.this.getDialog().cancel();

            }
        });
        ButtonDialogPhoneVerificationPending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCompleteListener.onComplete(1);
         /*       Intent in = new Intent(getContext(), MainDirectiveActivity.class);
                in.putExtra("type", 25);
                // in.putExtra("subtype", "received");
                startActivity(in);*/
            }
        });


        ButtonDialogEmailVerificationPending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCompleteListener.onComplete(2);
          /*      Intent in = new Intent(getContext(), MainDirectiveActivity.class);
                in.putExtra("type", 25);
                // in.putExtra("subtype", "received");
                startActivity(in);*/
            }
        });


        faTextView cancelButton = (faTextView) rootView.findViewById(R.id.faButtonContactDialogDismiss);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(final View v) {
                //Toast.makeText(getActivity().getApplicationContext(), "clcieck", Toast.LENGTH_SHORT).show();
                dialogPendingVerification.this.getDialog().cancel();
            }
        });


        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    public static interface onCompleteListener {
        public abstract void onComplete(int s);
    }

}


