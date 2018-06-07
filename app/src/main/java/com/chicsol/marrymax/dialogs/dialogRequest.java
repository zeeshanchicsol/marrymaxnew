package com.chicsol.marrymax.dialogs;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.Build;
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
import com.chicsol.marrymax.interfaces.RequestCallbackInterface;
import com.chicsol.marrymax.other.MarryMax;
import com.chicsol.marrymax.preferences.SharedPreferenceManager;
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

public class dialogRequest extends DialogFragment {

    public onCompleteListener mCompleteListener;
    mTextView tvDesc, tvTitle;
    boolean withdrawCheck;

    String title, desc, params = null, btnTitle;
    Button mOkButton, btSubscribe;
    RequestCallbackInterface requestInterface;

    public void setListener(RequestCallbackInterface listener) {
        requestInterface = listener;
    }

    public static dialogRequest newInstance(String params, String title, String desc, String btnTitle, boolean withdrawCheck) {
        /*       Members member, String userpath, boolean replyCheck, Members member2*/
        dialogRequest frag = new dialogRequest();
        Bundle args = new Bundle();


        args.putString("name", title);
        args.putString("desc", desc);
        args.putString("params", params);
        args.putString("btnTitle", btnTitle);
        args.putBoolean("withdrawCheck", withdrawCheck);

        frag.setArguments(args);
        return frag;
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle mArgs = getArguments();

        title = mArgs.getString("name");
        desc = mArgs.getString("desc");

        params = mArgs.getString("params");
        btnTitle = mArgs.getString("btnTitle");

        withdrawCheck = mArgs.getBoolean("withdrawCheck");

     /*   Log.e("name Fragment", name);
        Log.e("desc Fragment", desc);
        Log.e("param Fragment", param);
        Log.e("my_id Fragment", my_id);*/

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.dialog_request, container, false);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);


        mOkButton = (Button) rootView.findViewById(R.id.mButtonDialogRequest);
        btSubscribe = (Button) rootView.findViewById(R.id.mButtonDialogRequestSubscribe);


        tvTitle = (mTextView) rootView.findViewById(R.id.TextViewRequestDialogTitle);

        tvDesc = (mTextView) rootView.findViewById(R.id.TextViewRequestDialogDetails);


        tvTitle.setText(title);

        mOkButton.setText(btnTitle);


        if (Build.VERSION.SDK_INT >= 24) {
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
        });

        mOkButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(final View v) {

                try {
                    requestPermission(new JSONObject(params));
                } catch (JSONException e) {
                    e.printStackTrace();
                }


              /*  JSONObject params = new JSONObject();
                try {

                    //   params.put("selectdlist", selectdlist);

                    params.put("path", SharedPreferenceManager.getUserObject(getContext()).get_path());
                } catch (JSONException e) {
                    e.printStackTrace();
                }*/
                //   updateInterest(params);

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

        faTextView cancelButton = (faTextView) rootView.findViewById(R.id.faButtonRequestdismiss);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(final View v) {
                //Toast.makeText(getActivity().getApplicationContext(), "clcieck", Toast.LENGTH_SHORT).show();
                dialogRequest.this.getDialog().cancel();
            }
        });


        return rootView;
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
    public void onStart() {
        super.onStart();

        getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    private void requestPermission(JSONObject params) {
        String type = "";
        try {
            type = params.getString("type");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final ProgressDialog pDialog = new ProgressDialog(getContext());
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        pDialog.show();

        Log.e("params", params.toString());
        Log.e("profile path", Urls.submitRequest);
        final String finalType = type;
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.PUT,
                Urls.submitRequest, params,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("Res  interest ", response + "");

                        try {
                            int responseid = response.getInt("id");


                            if (responseid == 0) {
                                Toast.makeText(getContext(), "Request has not been sent successfully. ", Toast.LENGTH_SHORT).show();
                            } else if (responseid == 1) {

                                pDialog.dismiss();
                                dialogRequest.this.getDialog().cancel();
                                //  mCompleteListener.onComplete(responseid + "");
                                requestInterface.onRequestCallback(finalType + "", responseid + "");
                            } else if (responseid == -1) {
                                String desctxt = "";

                                if (SharedPreferenceManager.getUserObject(getContext()).get_member_status() == 3) {
                                    mOkButton.setVisibility(View.GONE);
                                    btSubscribe.setVisibility(View.VISIBLE);
                                    desctxt = "<ul><li>Your complimentary free member communication quota is exhausted.</li>\n" +
                                            "<br><li>You need to wait 72 hours before you can send new request.</li>\n" +
                                            "<br><li>To maximize your options and communicate immediately, please subscribe.</li>\n" +
                                            "</ul>";


                                } else if (SharedPreferenceManager.getUserObject(getContext()).get_member_status() == 4) {
                                    mOkButton.setVisibility(View.GONE);
                                    btSubscribe.setVisibility(View.GONE);

                                    desctxt = "<ul><li>Your complimentary paid member communication quota is exhausted.</li>\n" +
                                            "<br><li>You need to wait 24 hours before you can send new request.</li>\n" +
                                            "</ul>";


                                }

                                if (Build.VERSION.SDK_INT >= 24) {
                                    // for 24 api and more
                                    tvDesc.setText(Html.fromHtml(desctxt, Html.FROM_HTML_MODE_LEGACY));
                                } else {
                                    tvDesc.setText(Html.fromHtml(desctxt));
                                }


                            } else {
                                Toast.makeText(getContext(), "Request has been sent successfully", Toast.LENGTH_LONG).show();
                                pDialog.dismiss();
                                dialogRequest.this.getDialog().cancel();
                                //     mCompleteListener.onComplete(responseid + "");
                                requestInterface.onRequestCallback(finalType + "", responseid + "");
                            }


                        } catch (JSONException e) {
                            pDialog.dismiss();
                            dialogRequest.this.getDialog().cancel();
                            e.printStackTrace();
                        }


                        pDialog.dismiss();
                        // dialogRequest.this.getDialog().cancel();
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {


                VolleyLog.e("res err", "Error: " + error);
                pDialog.dismiss();
            }


        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return Constants.getHashMap();
            }
        };
        // Adding request to request queue
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(getContext()).addToRequestQueue(jsonObjReq);
    }


    public static interface onCompleteListener {
        public abstract void onComplete(String s);
    }


}


