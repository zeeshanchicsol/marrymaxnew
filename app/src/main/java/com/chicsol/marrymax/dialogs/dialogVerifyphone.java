package com.chicsol.marrymax.dialogs;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.chicsol.marrymax.R;
import com.chicsol.marrymax.modal.Members;
import com.chicsol.marrymax.preferences.SharedPreferenceManager;
import com.chicsol.marrymax.urls.Urls;
import com.chicsol.marrymax.utils.Constants;
import com.chicsol.marrymax.utils.MySingleton;
import com.chicsol.marrymax.widgets.faTextView;
import com.chicsol.marrymax.widgets.mTextView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by zeedr on 24/10/2016.
 */

public class dialogVerifyphone extends DialogFragment {
    String phone = null;
    EditText etCode;
    mTextView tvMobileNumber;
    LinearLayout llSendVerification, llVerifyCode, llError;
    private AppCompatButton btnSendVerificaitonCode, btnVerifyPhone;
    public onCompleteListener mCompleteListener;
    private boolean enterCode = false;

    public static dialogVerifyphone newInstance(String phone, boolean ec) {

        dialogVerifyphone frag = new dialogVerifyphone();
        Bundle args = new Bundle();

        args.putString("phone", phone);
        args.putBoolean("enterCode", ec);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle mArgs = getArguments();
        phone = mArgs.getString("phone");
        enterCode = mArgs.getBoolean("enterCode");

        ///  Log.e("json data", myValue);


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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.dialog_verify_phone, container, false);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);


        tvMobileNumber = (mTextView) rootView.findViewById(R.id.TextViewAccountSettingmcMobileNumber);
        etCode = (EditText) rootView.findViewById(R.id.EditTextAccountSettingMyContactVerifyCode);
        llSendVerification = (LinearLayout) rootView.findViewById(R.id.LinearlayoutAccountSettingMyContactSendVerificationCode);

        llVerifyCode = (LinearLayout) rootView.findViewById(R.id.LinearlayoutAccountSettingMyContactVerifyCode);

        llError = (LinearLayout) rootView.findViewById(R.id.LinearlayoutAccountSettingMyContactErrorVerficationCode);


        btnSendVerificaitonCode = (AppCompatButton) rootView.findViewById(R.id.ButtonAccountSettingmcSendVerificationCode);
        btnVerifyPhone = (AppCompatButton) rootView.findViewById(R.id.ButtonAccountSettingmcVerifyNow);

        tvMobileNumber.setText(phone);


        btnSendVerificaitonCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getmobileCode(SharedPreferenceManager.getUserObject(getContext()).get_path());

            }
        });
        btnVerifyPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (etCode.getText().toString().equals("")) {
                    Toast.makeText(getContext(), "Pleas Enter Code First", Toast.LENGTH_SHORT).show();

                } else if (!isCodeValid(etCode.getText().toString())) {
                    Toast.makeText(getContext(), "Enter Valid Code", Toast.LENGTH_SHORT).show();
                } else {
                    validateMobile(etCode.getText().toString());


                }
            }
        });

   /*     Bundle mArgs = getArguments();
        String myValue = mArgs.getString("jsondata");
        try {
            JSONArray jsonArray = new JSONArray(myValue);
            JSONArray dosDataList = jsonArray.getJSONArray(0);
            JSONArray dontsDataList = jsonArray.getJSONArray(1);
        } catch (JSONException e) {
            e.printStackTrace();
        }*/


   /*     Button mOkButton = (Button) rootView.findViewById(R.id.mButtonSubmitGeoInfo);
        mOkButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(final View v) {


                if (TextUtils.isEmpty(etCode.getText().toString())) {
                    etCode.setError("Please enter reason");


                    etCode.requestFocus();
                } else if (!(etCode.getText().toString().length() > 15) || !(etCode.getText().toString().length() <= 200)) {

                    etCode.setError("Min 15 char, max 200 char");
                } else {


                    settGeoReason(etCode.getText().toString());

                    dialogVerifyphone.this.getDialog().cancel();
                }


            }
        });*/

        faTextView cancelButton = (faTextView) rootView.findViewById(R.id.dismissBtnVerifyPhone);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(final View v) {
                dialogVerifyphone.this.getDialog().cancel();
            }
        });


        if (!enterCode) {
            llSendVerification.setVisibility(View.GONE);
          /*  btnVerifyPhone.setVisibility(View.GONE);
            llVerifyCode.setVisibility(View.GONE);
*/

        } else {
            llSendVerification.setVisibility(View.VISIBLE);
            llVerifyCode.setVisibility(View.GONE);
            //mobCode= response;
            // llError.setVisibility(View.GONE);

        }


        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();

        getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }


    private void getmobileCode(final String path) {
        final ProgressDialog pDialog = new ProgressDialog(getContext());
        pDialog.setMessage("Loading...");
        pDialog.show();
        Log.e("path", "" + Urls.getMobileCode + path);
        StringRequest req = new StringRequest(Urls.getMobileCode + path,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Response--", response.toString() + "==");
                        if (response != null) {

                            if (Long.parseLong(response) == 0) {
                                llSendVerification.setVisibility(View.GONE);
                                btnVerifyPhone.setVisibility(View.GONE);
                                llError.setVisibility(View.VISIBLE);


                            } else {
                                Toast.makeText(getContext(), "Your mobile verification code has been send successfully.", Toast.LENGTH_SHORT).show();

                                llSendVerification.setVisibility(View.GONE);
                                llVerifyCode.setVisibility(View.VISIBLE);
                                //mobCode= response;
                                llError.setVisibility(View.GONE);
                                // etCode.setText(response);
                            }
                        }
                        pDialog.dismiss();
                    }
                }, new Response.ErrorListener()

        {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Err", "Error: " + error.getMessage());
                pDialog.dismiss();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return Constants.getHashMap();
            }
        };

        // StringRequest stringRequest=new StringRequest()


        MySingleton.getInstance(

                getContext()).

                addToRequestQueue(req);

    }

    private void validateMobile(String postalcode) {

        final ProgressDialog pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        pDialog.show();

        JSONObject params = new JSONObject();
        try {

            params.put("postal_code", postalcode);
            params.put("path", SharedPreferenceManager.getUserObject(getContext()).get_path());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.e("validateMobile " + "  " + Urls.validateMobile, "" + params);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.PUT,
                Urls.validateMobile, params,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("Response verification", response + "");


                        Gson gson;
                        GsonBuilder gsonBuildert = new GsonBuilder();
                        gson = gsonBuildert.create();
                        Type membert = new TypeToken<Members>() {
                        }.getType();
                        Members mem = (Members) gson.fromJson(response.toString(), membert);

                        if (mem.get_phone_verified() == 1) {


                            //    mCompleteListener.onComplete("");
                            Members mem2 = SharedPreferenceManager.getUserObject(getContext());

                            if (mem.get_request_profile_view() != mem2.get_member_status()) {
                                mem2.set_member_status(mem.get_request_profile_view());
                                SharedPreferenceManager.setUserObject(getContext(), mem2);

                                dialogVerifyphone.this.getDialog().cancel();

                            }
                            dialogVerifyphone.this.getDialog().cancel();
                            mCompleteListener.onComplete("");

                        } else if (mem.get_phone_verified() == 0) {
                            Toast.makeText(getContext(), "Phone Not Verified", Toast.LENGTH_SHORT).show();
                            mCompleteListener.onComplete("");
                        }


                /*        try {
                            int responseid = response.getInt("id");


                            if (responseid == 1) {


                            }


                        } catch (JSONException e) {
                            pDialog.dismiss();
                            e.printStackTrace();
                        }*/

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


    private boolean isCodeValid(String input) {
        Pattern pattern;
        Matcher matcher;
        //  String EMAIL_PATTERN = "^\\d*[a-zA-Z][a-zA-Z_\\d]*$";
        String EMAIL_PATTERN = "^([0-9]){4,10}$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(input);
        return matcher.matches();

    }


}
