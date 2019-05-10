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
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.chicsol.marrymax.R;
import com.chicsol.marrymax.interfaces.WithdrawRequestCallBackInterface;
import com.chicsol.marrymax.urls.Urls;
import com.chicsol.marrymax.utils.Constants;
import com.chicsol.marrymax.utils.MySingleton;
import com.chicsol.marrymax.widgets.faTextView;
import com.chicsol.marrymax.widgets.mTextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

/**
 * Created by zeedr on 24/10/2016.
 */
public class dialogWithdrawInterest extends DialogFragment {

    private mTextView tvDesc, tvTitle;
    private JSONObject params;
    private String desc, title, type;
    // private onCompleteListener mCompleteListener;

    private WithdrawRequestCallBackInterface withdrawRequestCallBackInterface;

    public void setListener(WithdrawRequestCallBackInterface listener) {
        withdrawRequestCallBackInterface = listener;
    }

    // private ListView lv_mycontacts;
    public static dialogWithdrawInterest newInstance(JSONObject params, String title, String description, String type) {

        dialogWithdrawInterest frag = new dialogWithdrawInterest();
        Bundle args = new Bundle();
        args.putString("params", params.toString());
        args.putString("desc", description);
        args.putString("name", title);
        args.putString("type", type);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle mArgs = getArguments();
        try {
            params = new JSONObject(mArgs.getString("params"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        desc = mArgs.getString("desc");
        title = mArgs.getString("name");
        type = mArgs.getString("type");
    }

    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
       /* try {


            if (getTargetFragment() != null) {
                mCompleteListener = (onCompleteListener) getTargetFragment();
            } else {
                mCompleteListener = (onCompleteListener) activity;
            }
        } catch (ClassCastException e) {
            throw new ClassCastException(e.toString() + " must implement OnCompleteListener");
        }*/
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.dialog_withdraw_interest, container, false);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        tvDesc = (mTextView) rootView.findViewById(R.id.TextViewDialogWithdrawInterestDetails);
        tvTitle = (mTextView) rootView.findViewById(R.id.TextiewDialogWithdrawRequestTitle);
        tvTitle.setText(title);
        tvDesc.setText(Html.fromHtml(desc));


        Button mOkButton = (Button) rootView.findViewById(R.id.mButtonExpressWithDrawInterest);
        mOkButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(final View v) {

             /*   try {
                  //  params.put("checkedTextView", checkedTextView);
                } catch (JSONException e) {
                    e.printStackTrace();
                }*/

                withdrawInterest(params);
            }
        });

        faTextView cancelButton = (faTextView) rootView.findViewById(R.id.dismissBtnGeoInfo);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(final View v) {
                //Toast.makeText(getActivity().getApplicationContext(), "clcieck", Toast.LENGTH_SHORT).show();
                dialogWithdrawInterest.this.getDialog().cancel();
            }
        });


        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();

        getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    private void withdrawInterest(JSONObject params) {

        // Log.e("www params", params.toString() + "");

        final ProgressDialog pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        pDialog.show();

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.PUT,
                Urls.withdrawInterest, params,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        //Log.e("re  wwww interest", response + "");
                        try {
                            int responseid = response.getInt("id");


                            if (responseid == 1) {
                                Toast.makeText(getContext(), "Withdraw request has been completed successfully", Toast.LENGTH_LONG).show();
                                withdrawRequestCallBackInterface.onWithdrawRequestComplete(type + "");
                                //   mCompleteListener.onComplete("1");

                            } else {
                                //  mCompleteListener.onComplete(responseid+"");
                            }
                            dialogWithdrawInterest.this.getDialog().cancel();

                        } catch (JSONException e) {
                            pDialog.dismiss();
                            e.printStackTrace();
                            dialogWithdrawInterest.this.getDialog().cancel();

                        }

                        pDialog.dismiss();
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getContext(), "Error. Try Again", Toast.LENGTH_SHORT).show();

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


}


