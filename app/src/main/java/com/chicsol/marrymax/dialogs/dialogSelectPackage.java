package com.chicsol.marrymax.dialogs;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.RadioGroup;

import com.chicsol.marrymax.R;
import com.chicsol.marrymax.modal.Subscription;
import com.chicsol.marrymax.utils.ViewGenerator;
import com.chicsol.marrymax.widgets.faTextView;
import com.chicsol.marrymax.widgets.mTextView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;


/**
 * Created by zeedr on 24/10/2016.
 */

public class dialogSelectPackage extends DialogFragment {

    public onChangeSubscriptionPlanListener mCompleteListener;
    mTextView tvDesc, tvTitle;
    boolean withdrawCheck = false;
    RadioGroup radioGroup;
    String title, item_id, params = null, btnTitle;
    Button mOkButton, btSubscribe;

    String responseArray = null;

    public static dialogSelectPackage newInstance(String responseArray, String item_id, String desc, String btnTitle, boolean withdrawCheck) {
 /*       Members member, String userpath, boolean replyCheck, Members member2*/
        dialogSelectPackage frag = new dialogSelectPackage();
        Bundle args = new Bundle();


        args.putString("item_id", item_id);
        args.putBoolean("withdrawCheck", withdrawCheck);
        args.putString("responseArray", responseArray);
      /*    args.putString("params", params);
        args.putString("btnTitle", btnTitle);

*/
        frag.setArguments(args);
        return frag;
    }

    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        try {


            if (getTargetFragment() != null) {
                mCompleteListener = (onChangeSubscriptionPlanListener) getTargetFragment();
            } else {
                mCompleteListener = (onChangeSubscriptionPlanListener) activity;
            }
        } catch (ClassCastException e) {
            throw new ClassCastException(e.toString() + " must implement OnCompleteListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle mArgs = getArguments();

        item_id = mArgs.getString("item_id");
        responseArray = mArgs.getString("responseArray");
        withdrawCheck = mArgs.getBoolean("withdrawCheck");

     /*    desc = mArgs.getString("desc");

        params = mArgs.getString("params");
        btnTitle = mArgs.getString("btnTitle");

       */


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.dialog_select_package, container, false);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        radioGroup = (RadioGroup) rootView.findViewById(R.id.RadioGroupSubscriptionSelectDialog);
        mOkButton = (Button) rootView.findViewById(R.id.mButtonDialogRequest);
        btSubscribe = (Button) rootView.findViewById(R.id.mButtonDialogRequestSubscribe);


        Gson gson;
        GsonBuilder gsonBuilderc = new GsonBuilder();
        gson = gsonBuilderc.create();
        Type listType = new TypeToken<List<Subscription>>() {
        }.getType();

        List<Subscription> sDataList = (List<Subscription>) gson.fromJson(responseArray.toString(), listType);

        ViewGenerator viewGenerator = new ViewGenerator(getContext());
        viewGenerator.generateDynamicRadiosForSubscriptionDialog(sDataList, radioGroup, withdrawCheck);

        radioGroup.check(Integer.parseInt(item_id));







      /*  tvTitle = (mTextView) rootView.findViewById(R.id.TextViewRequestDialogTitle);

        tvDesc = (mTextView) rootView.findViewById(R.id.TextViewRequestDialogDetails);

*/


      /*  if (Build.VERSION.SDK_INT >= 24) {
            // for 24 api and more
            tvDesc.setText(Html.fromHtml(desc, Html.FROM_HTML_MODE_LEGACY));
        } else {
            tvDesc.setText(Html.fromHtml(desc));
        }
        btSubscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MarryMax marryMax = new MarryMax(getActivity());
                marryMax.subscribe();
            }
        });*/
//637 319 255     5655
        mOkButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(final View v) {
                //  Log.e("checked id",   radioGroup.getCheckedRadioButtonId()+"");
                View radioButton = radioGroup.findViewById(radioGroup.getCheckedRadioButtonId());
                Subscription subscription = (Subscription) radioButton.getTag();
                if (withdrawCheck) {
                    Log.e("radio ids are", subscription.getItem_id() + "  " + subscription.getOther_item_id());

                }
          mCompleteListener.onChangeSubscriptionPackage(subscription.getItem_id()+"", subscription.getOther_item_id());

                dialogSelectPackage.this.getDialog().cancel();

            }
        });

        faTextView cancelButton = (faTextView) rootView.findViewById(R.id.faButtonRequestdismiss);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(final View v) {
                //Toast.makeText(getActivity().getApplicationContext(), "clcieck", Toast.LENGTH_SHORT).show();
                dialogSelectPackage.this.getDialog().cancel();
            }
        });


        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();

        getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }


    public static interface onChangeSubscriptionPlanListener {
        public abstract void onChangeSubscriptionPackage(String itemid,String otherItem_id);
    }


}


