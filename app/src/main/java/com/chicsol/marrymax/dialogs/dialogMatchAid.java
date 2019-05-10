package com.chicsol.marrymax.dialogs;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.CardView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.chicsol.marrymax.R;
import com.chicsol.marrymax.activities.directive.MainDirectiveActivity;
import com.chicsol.marrymax.modal.WebArdType;
import com.chicsol.marrymax.other.MarryMax;
import com.chicsol.marrymax.preferences.SharedPreferenceManager;
import com.chicsol.marrymax.urls.Urls;
import com.chicsol.marrymax.utils.Constants;
import com.chicsol.marrymax.utils.MySingleton;
import com.chicsol.marrymax.utils.ViewGenerator;
import com.chicsol.marrymax.widgets.faTextView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * Created by zeedr on 24/10/2016.
 */

public class dialogMatchAid extends DialogFragment {


    String userpath, jsarray;
    int abtypeid = -1;
    Long member_status;
    private onCompleteListener mCompleteListener;
    Button mOkButton, mButtonSubscribe;


    private int feedback_due;
    private AppCompatButton btGiveFeedbackInterest;
    private CardView cvFeedbackPending;
    private TextView tvFeedbackPending;
    private AppCompatButton btGiveFeedback;


    public static dialogMatchAid newInstance(JSONArray jsArray, String userpath, long member_status, int feedback_due) {

        dialogMatchAid frag = new dialogMatchAid();
        Bundle args = new Bundle();
        args.putString("jsArray", jsArray.toString());
        args.putString("userpath", userpath);
        args.putLong("memstatus", member_status);
        args.putInt("feedback_due", feedback_due);

        frag.setArguments(args);
        return frag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle mArgs = getArguments();

        jsarray = mArgs.getString("jsArray");
        userpath = mArgs.getString("userpath");
        member_status = mArgs.getLong("memstatus");
        feedback_due = mArgs.getInt("feedback_due");


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.dialog_match_aid, container, false);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);


        tvFeedbackPending = (TextView) rootView.findViewById(R.id.TextViewDashMainFeedbackPending);
        cvFeedbackPending = (CardView) rootView.findViewById(R.id.CardViewDashMainFeedbackPending);
        btGiveFeedback = (AppCompatButton) rootView.findViewById(R.id.ButtonDashMainFeedbackPending);
        btGiveFeedback.setVisibility(View.GONE);
        btGiveFeedbackInterest = (AppCompatButton) rootView.findViewById(R.id.mButtonInterestGiveFeedback);


  /*      MarryMax max = new MarryMax(null);
        String desc = max.getFeedbackText(feedback_due, getContext());

        if (feedback_due == 1) {


            tvFeedbackPending.setText(Html.fromHtml(desc));
            btGiveFeedbackInterest.setVisibility(View.VISIBLE);
            cvFeedbackPending.setVisibility(View.VISIBLE);

        } else if (feedback_due == 2) {
            tvFeedbackPending.setBackgroundColor(Color.parseColor("#fff5d7"));
            tvFeedbackPending.setText(Html.fromHtml(desc));
            btGiveFeedbackInterest.setVisibility(View.VISIBLE);
            cvFeedbackPending.setVisibility(View.VISIBLE);

        } else {
            btGiveFeedbackInterest.setVisibility(View.GONE);
            cvFeedbackPending.setVisibility(View.GONE);
        }
*/

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






        mOkButton = (Button) rootView.findViewById(R.id.mButtonDialogMatchAid);
        mButtonSubscribe = (Button) rootView.findViewById(R.id.mButtonDialogMatchAidSubscribe);

        RadioGroup radioGroup = (RadioGroup) rootView.findViewById(R.id.RadioGroupDialogMatchAid);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                abtypeid = checkedId;

            }
        });


        List<WebArdType> dataList = new ArrayList<>();
        try {
            JSONArray jsonCountryStaeObj = new JSONArray(jsarray);
            Gson gsonc;
            GsonBuilder gsonBuilderc = new GsonBuilder();
            gsonc = gsonBuilderc.create();
            Type listType = new TypeToken<List<WebArdType>>() {
            }.getType();
            dataList = (List<WebArdType>) gsonc.fromJson(jsonCountryStaeObj.get(0).toString(), listType);
            ViewGenerator viewGenerator = new ViewGenerator(getContext());
            //Log.e("Member Status", "" + member_status);


            if (member_status < 4) {
                viewGenerator.generateDynamicRadiosForDialogsForMatchAid(dataList, radioGroup, true);
                mOkButton.setVisibility(View.GONE);
                mButtonSubscribe.setVisibility(View.VISIBLE);
            } else {
                mButtonSubscribe.setVisibility(View.GONE);
                mOkButton.setVisibility(View.VISIBLE);
                viewGenerator.generateDynamicRadiosForDialogsForMatchAid(dataList, radioGroup, false);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        mButtonSubscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MarryMax marryMax = new MarryMax(getActivity());
                marryMax.subscribe();
            }
        });

        mOkButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(final View v) {

                if (abtypeid != -1) {
                    JSONObject params = new JSONObject();
                    try {

                        params.put("id", abtypeid);
                        params.put("userpath", userpath);
                        params.put("path", SharedPreferenceManager.getUserObject(getContext()).getPath());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    query(params);
                } else {
                    Toast.makeText(getContext(), "Please select reason", Toast.LENGTH_SHORT).show();
                }

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


        faTextView cancelButton = (faTextView) rootView.findViewById(R.id.dismissBtnMatchAid);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(final View v) {

                dialogMatchAid.this.getDialog().cancel();
            }
        });


        return rootView;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {


            if (getTargetFragment() != null) {
                mCompleteListener = (onCompleteListener) getTargetFragment();
            } else {
                mCompleteListener = (onCompleteListener) context;
            }
        } catch (ClassCastException e) {
            throw new ClassCastException(e.toString() + " must implement OnCompleteListener");
        }
    }


    @Override
    public void onStart() {
        super.onStart();

        getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    private void query(JSONObject params) {

        final ProgressDialog pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Loading...");
        pDialog.show();
        //    Log.e("params", Urls.reportConcern + "  =======   " + params);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.PUT,
                Urls.addAssistance, params,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        //Log.e("response   ", response + "");
                        try {
                            int responseid = response.getInt("id");


                            if (responseid >= 1) {
                                Toast.makeText(getContext(), "Your request for Match Aid submitted", Toast.LENGTH_SHORT).show();
                                mCompleteListener.onComplete("Report Submitted");
                                dialogMatchAid.this.getDialog().cancel();
                            }


                        } catch (JSONException e) {
                            pDialog.dismiss();
                            e.printStackTrace();
                        }

                        pDialog.dismiss();


                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {


                VolleyLog.e("res err", "Error: " + error);
                // Toast.makeText(RegistrationActivity.this, "Incorrect Email or Password !", Toast.LENGTH_SHORT).show();

                pDialog.dismiss();
            }


        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return Constants.getHashMap();
            }
        };

// Adding request to request queue
        ///   rq.add(jsonObjReq);
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(getActivity()).addToRequestQueue(jsonObjReq);

    }

    public static interface onCompleteListener {
        public abstract void onComplete(String s);
    }

}


