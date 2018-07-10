package com.chicsol.marrymax.dialogs;


import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;

import com.chicsol.marrymax.R;
import com.chicsol.marrymax.modal.Members;
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
    Members member;

    public static dialogContactDetails newInstance(String params, String alias) {
 /*       Members member, String userpath, boolean replyCheck, Members member2*/
        dialogContactDetails frag = new dialogContactDetails();
        Bundle args = new Bundle();


        // args.putString("name", name);
        args.putString("alias", alias);
        args.putString("params", params);
        /*args.putString("param", String.valueOf(member2.get_phone_view()));
        args.putString("my_id", String.valueOf(member2.get_my_id()));
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
        Type type = new TypeToken<Members>() {
        }.getType();
        member = (Members) gson.fromJson(params, type);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.dialog_contact_details, container, false);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        String desc = "Here is the contact detail of  <b> <font color=#216917>" + alias + "</font></b>" ;

        tvTitle = (mTextView) rootView.findViewById(R.id.TextViewContactDialogTitle);

        mTextView contactName = (mTextView) rootView.findViewById(R.id.TextViewContactDialogContactName);
        mTextView contactPhone = (mTextView) rootView.findViewById(R.id.TextViewContactDialogMobileNumber);
        mTextView contactRelationShip = (mTextView) rootView.findViewById(R.id.TextViewContactDialogRelationship);
        mTextView contactCalltime = (mTextView) rootView.findViewById(R.id.TextViewContactDialogPrefferedCallTime);
        mTextView contactCountryName = (mTextView) rootView.findViewById(R.id.TextViewContactDialogCountryName);

        contactName.setText(""+member.get_personal_name());

        contactPhone.setText(""+member.get_phone_mobile());


        contactRelationShip.setText(""+member.get_profile_owner());

        contactCalltime.setText(""+member.get_notes());

        contactCountryName.setText(""+member.get_country_name());


        Button mOkButton = (Button) rootView.findViewById(R.id.ButtonContactDialogCancel);


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


