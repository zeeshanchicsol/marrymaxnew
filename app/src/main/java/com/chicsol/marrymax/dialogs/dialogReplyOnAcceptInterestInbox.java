package com.chicsol.marrymax.dialogs;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.chicsol.marrymax.R;
import com.chicsol.marrymax.modal.Members;
import com.chicsol.marrymax.modal.mCommunication;
import com.chicsol.marrymax.preferences.SharedPreferenceManager;
import com.chicsol.marrymax.urls.Urls;
import com.chicsol.marrymax.utils.Constants;
import com.chicsol.marrymax.utils.MySingleton;
import com.chicsol.marrymax.widgets.faTextView;
import com.chicsol.marrymax.widgets.mCheckBox;
import com.chicsol.marrymax.widgets.mTextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;


/**
 * Created by zeedr on 24/10/2016.
 */

public class dialogReplyOnAcceptInterestInbox extends DialogFragment {
    String request_id, image_view, phone_view, my_id;
    mCheckBox cbAllowPhone, cbAllowPics;
    mTextView tvDesc;
    String userpath, alias;
    boolean replyCheck;
    private onCompleteListener mCompleteListener;

    // private ListView lv_mycontacts;
    public static dialogReplyOnAcceptInterestInbox newInstance(mCommunication member, String userpath, boolean replyCheck, Members member2) {

        dialogReplyOnAcceptInterestInbox frag = new dialogReplyOnAcceptInterestInbox();
        Bundle args = new Bundle();


        args.putBoolean("replyCheck", replyCheck);
        args.putString("request_id", String.valueOf(member.getRequest_id()));
        args.putString("desc", String.valueOf(member2.get_image_view()));
        args.putString("param", String.valueOf(member2.get_phone_view()));

        args.putString("alias", member.getAlias());
        args.putString("userpath", userpath);


        frag.setArguments(args);
        return frag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle mArgs = getArguments();
        replyCheck = mArgs.getBoolean("replyCheck");
        request_id = mArgs.getString("request_id");
        image_view = mArgs.getString("desc");
        phone_view = mArgs.getString("param");
        my_id = mArgs.getString("my_id");
        userpath = mArgs.getString("userpath");
        alias = mArgs.getString("alias");

        alias = mArgs.getString("alias");

    }
/*    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(inflater.inflate(R.layout.dialog_profilefor, null))


          *//*      // Add action buttons
                .setPositiveButton("Submut", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // sign in the user ...
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialogDosDonts.this.getDialog().cancel();
                    }
                })*//*;


        return builder.create();

    }*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.dialog_accept_interest, container, false);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        cbAllowPhone = (mCheckBox) rootView.findViewById(R.id.CheckBoxAcceptInterestAllowPhone);
        cbAllowPics = (mCheckBox) rootView.findViewById(R.id.CheckBoxAcceptInterestAllowPics);
        tvDesc = (mTextView) rootView.findViewById(R.id.TextViewDialogAcceptInterestDetails);


        String txt = "<font color='#9a0606'>" + alias + "</font>";
        tvDesc.setText(Html.fromHtml("Great! You are interested in " + "<b>" + txt.toUpperCase() + "</b>"));

        if (image_view.equals("2") || phone_view.equals("5")) {


            if (image_view.equals("2")) {

                if (!replyCheck) {
                    cbAllowPics.setVisibility(View.VISIBLE);
                } else {

                    cbAllowPics.setVisibility(View.VISIBLE);
                    cbAllowPics.setText("Would you like to give permission to view your image?");
                }
            }
            if (phone_view.equals("5")) {
                if (!replyCheck) {
                    cbAllowPhone.setVisibility(View.VISIBLE);
                } else {
                    cbAllowPhone.setVisibility(View.VISIBLE);
                    cbAllowPhone.setText("Would you like to give permission to view your phone?");
                }

            }
        }


        Button mOkButton = (Button) rootView.findViewById(R.id.mButtonDialogAcceptInterest);
        mOkButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(final View v) {
                JSONObject params = new JSONObject();
                try {

                    params.put("request_response_id", "3");

                    if (cbAllowPhone.isChecked()) {

                        params.put("param", "1");
                    } else {
                        params.put("param", "0");
                    }
                    if (cbAllowPics.isChecked()) {

                        params.put("desc", "1");
                    } else {
                        params.put("desc", "0");
                    }

                    params.put("blocked_member", "0");
                    params.put("request_id", request_id);
                    params.put("type", "4");
                    params.put("userpath", userpath);
                    params.put("path", SharedPreferenceManager.getUserObject(getContext()).get_path());
                    params.put("alias", SharedPreferenceManager.getUserObject(getContext()).getAlias());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.e("request params", "" + params);


                query(params);

              /*  if (TextUtils.isEmpty(etCode.getText().toString())) {
                    etCode.setError("Please enter reason");


                    etCode.requestFocus();
                }
                else if (!(etCode.getText().toString().length()>15) || !(etCode.getText().toString().length()<=200)  ){

                    etCode.setError("Min 15 char, max 200 char");
                }
                else {


                 //   settGeoReason(etCode.getText().toString());

                    dialogAddtoList.this.getDialog().cancel();
                }
*/


            }
        });

        faTextView cancelButton = (faTextView) rootView.findViewById(R.id.dismissBtnAcceptInterest);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(final View v) {
                //Toast.makeText(getActivity().getApplicationContext(), "clcieck", Toast.LENGTH_SHORT).show();
                dialogReplyOnAcceptInterestInbox.this.getDialog().cancel();
            }
        });


        return rootView;
    }

/*
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            this.mCompleteListener = (onCompleteListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(e.toString() + " must implement OnCompleteListener");
        }
    }
*/


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
    public void onStart() {
        super.onStart();

        getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }


    private void query(JSONObject params) {

        final ProgressDialog pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        pDialog.show();


        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.PUT,
                Urls.responseInterest, params,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        Log.e("re  update interest", response + "");
                        dialogReplyOnAcceptInterestInbox.this.getDialog().cancel();
                        try {
                            int responseid = response.getInt("id");


                            if (responseid == 1) {


                            }


                        } catch (JSONException e) {
                            pDialog.hide();
                            // dialogReplyOnAcceptInterestInbox.this.getDialog().cancel();
                            e.printStackTrace();
                        }
                        // dialogReplyOnAcceptInterestInbox.this.getDialog().cancel();
                        mCompleteListener.onComplete("");
                        pDialog.hide();
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {


                VolleyLog.e("res err", "Error: " + error);
                // Toast.makeText(RegistrationActivity.this, "Incorrect Email or Password !", Toast.LENGTH_SHORT).show();

                pDialog.hide();
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


