package com.chicsol.marrymax.dialogs;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.chicsol.marrymax.R;
import com.chicsol.marrymax.widgets.faTextView;
import com.chicsol.marrymax.widgets.mTextView;


/**
 * Created by zeedr on 24/10/2016.
 */

public class dialogTermsConditions extends DialogFragment {

   // public onApplyPromoCodeListener mApplyPromoCodeListener;
    mTextView tvDesc, tvTitle;
    boolean withdrawCheck;

    String title, desc, params = null, btnTitle;
    Button btCancel, btSubscribe;
    String promocode = "";
    EditText etPromoCode;

    public static dialogTermsConditions newInstance() {
 /*       Members member, String userpath, boolean replyCheck, Members member2*/
        dialogTermsConditions frag = new dialogTermsConditions();
       // Bundle args = new Bundle();


        /*args.putString("name", title);
        args.putString("desc", desc);
        args.putString("params", params);
        args.putString("btnTitle", btnTitle);
        args.putBoolean("withdrawCheck", withdrawCheck);

        frag.setArguments(args);*/
        return frag;
    }

 /*   @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        try {


            if (getTargetFragment() != null) {
                mApplyPromoCodeListener = (onApplyPromoCodeListener) getTargetFragment();
            } else {
                mApplyPromoCodeListener = (onApplyPromoCodeListener) activity;
            }
        } catch (ClassCastException e) {
            throw new ClassCastException(e.toString() + " must implement OnCompleteListener");
        }
    }*/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
 /*       Bundle mArgs = getArguments();

        title = mArgs.getString("name");
        desc = mArgs.getString("desc");

        params = mArgs.getString("params");
        btnTitle = mArgs.getString("btnTitle");

        withdrawCheck = mArgs.getBoolean("withdrawCheck");*/


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.dialog_payment_terms, container, false);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        btCancel = (Button) rootView.findViewById(R.id.mButtonDialogClose);

   /*     mOkButton = (Button) rootView.findViewById(R.id.mButtonDialogRequest);
        btSubscribe = (Button) rootView.findViewById(R.id.mButtonDialogRequestSubscribe);
        etPromoCode = (EditText) rootView.findViewById(R.id.EditTextPromoCodeEnterPromoCode);*/


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

        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogTermsConditions.this.getDialog().cancel();
            }
        });

        faTextView cancelButton = (faTextView) rootView.findViewById(R.id.faButtonRequestdismiss);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(final View v) {
                //Toast.makeText(getActivity().getApplicationContext(), "clcieck", Toast.LENGTH_SHORT).show();
                dialogTermsConditions.this.getDialog().cancel();
            }
        });


        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();

        getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }


    public static interface onApplyPromoCodeListener {
        public abstract void onApplyPromoCode(String s);
    }


}


