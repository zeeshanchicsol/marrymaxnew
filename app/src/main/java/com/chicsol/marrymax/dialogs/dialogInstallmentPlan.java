package com.chicsol.marrymax.dialogs;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;


import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.chicsol.marrymax.R;

import com.chicsol.marrymax.modal.mInstallment;

import com.chicsol.marrymax.widgets.faTextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by zeedr on 24/10/2016.
 */

public class dialogInstallmentPlan extends DialogFragment {

    //  public onCompleteListener mCompleteListener;
    String params = null;


    String response;
    List<mInstallment> installmentData;

    TextView tvSilverPrice1, tvSilverPrice2, tvSilverVerifiedNumber1, tvSilverVerifiedNumber2, tvSilverMessageCount1, tvSilverMessageCount2;
    TextView tvGoldPrice1, tvGoldPrice2, tvGoldVerifiedNumber1, tvGoldVerifiedNumber2, tvGoldMessageCount1, tvGoldMessageCount2;

    AppCompatButton btPurchase;

    public static dialogInstallmentPlan newInstance(String response) {

        dialogInstallmentPlan frag = new dialogInstallmentPlan();
        Bundle args = new Bundle();


        args.putString("response", response);


        frag.setArguments(args);
        return frag;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle mArgs = getArguments();
        //    replyCheck = mArgs.getBoolean("replyCheck");
        //  name = mArgs.getString("name");
        response = mArgs.getString("response");


        JSONArray jsonarrayData = null;
        try {


            jsonarrayData = new JSONArray(response).getJSONArray(0);
            Gson gson;
            GsonBuilder gsonBuilder = new GsonBuilder();
            gson = gsonBuilder.create();
            Type member = new TypeToken<List<mInstallment>>() {
            }.getType();

            installmentData = new ArrayList<>();
            installmentData = (List<mInstallment>) gson.fromJson(jsonarrayData.toString(), member);

            Log.e("size", installmentData.get(0).getPkr_price() +" ===   "+installmentData.size()+"");

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.dialog_installment_plan, container, false);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);


        init(rootView);


        faTextView cancelButton = (faTextView) rootView.findViewById(R.id.faButtonContactDialogDismiss);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(final View v) {
                //Toast.makeText(getActivity().getApplicationContext(), "clcieck", Toast.LENGTH_SHORT).show();
                dialogInstallmentPlan.this.getDialog().cancel();
            }
        });


        return rootView;
    }


    private void init(View view) {

        btPurchase = (AppCompatButton) view.findViewById(R.id.ButtonContactDialogPurchase);


        tvSilverPrice1 = (TextView) view.findViewById(R.id.TextViewInstallmentSilverPrice1);
        tvSilverPrice2 = (TextView) view.findViewById(R.id.TextViewInstallmentSilverPrice2);

        tvSilverVerifiedNumber1 = (TextView) view.findViewById(R.id.TextViewInstallmentSilverVerifiedNumbers1);
        tvSilverVerifiedNumber2 = (TextView) view.findViewById(R.id.TextViewInstallmentSilverVerifiedNumbers2);


        tvSilverMessageCount1 = (TextView) view.findViewById(R.id.TextViewInstallmentSilver1MessageCount);
        tvSilverMessageCount2 = (TextView) view.findViewById(R.id.TextViewInstallmentSilver2MessageCount);

        tvGoldPrice1 = (TextView) view.findViewById(R.id.TextViewInstallmentGold1Price1);
        tvGoldPrice2 = (TextView) view.findViewById(R.id.TextViewInstallmentGold1Price2);
        tvGoldVerifiedNumber1 = (TextView) view.findViewById(R.id.TextViewInstallmentGold1VerifiedNumbers1);
        tvGoldVerifiedNumber2 = (TextView) view.findViewById(R.id.TextViewInstallmentGold1VerifiedNumbers2);
        tvGoldMessageCount1 = (TextView) view.findViewById(R.id.TextViewInstallmentGold1MessageCount);
        tvGoldMessageCount2 = (TextView) view.findViewById(R.id.TextViewInstallmentGold2MessageCount);


        //Silver Package   ==========

        tvSilverPrice1.setText("PKR "+installmentData.get(0).getPkr_price());
        tvSilverPrice2.setText("PKR "+installmentData.get(1).getPkr_price());

        tvSilverVerifiedNumber1.setText(installmentData.get(0).getPhone());
        tvSilverVerifiedNumber2.setText(installmentData.get(1).getPhone());

        tvSilverMessageCount1.setText(installmentData.get(0).getContact());
        tvSilverMessageCount2.setText(installmentData.get(1).getContact());


        //Gold Package   ==========

        tvGoldPrice1.setText("PKR "+ installmentData.get(2).getPkr_price());
        tvGoldPrice2.setText("PKR "+installmentData.get(3).getPkr_price());

        tvGoldVerifiedNumber1.setText(installmentData.get(2).getPhone());
        tvGoldVerifiedNumber2.setText(installmentData.get(3).getPhone());

        tvGoldMessageCount1.setText(installmentData.get(2).getContact());
        tvGoldMessageCount2.setText(installmentData.get(3).getContact());


        btPurchase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogInstallmentPlan.this.getDialog().cancel();
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }


}


