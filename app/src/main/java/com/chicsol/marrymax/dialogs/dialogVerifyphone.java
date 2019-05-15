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
import android.widget.TextView;
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
import com.chicsol.marrymax.widgets.PinEntryEditText;
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
  //  EditText etCode;
    PinEntryEditText etPinCode;
    mTextView tvMobileNumber, tvVerifyFoneMobileNumber;
    LinearLayout llSendVerification, llVerifyCode, llError;
    private AppCompatButton btnSendVerificaitonCode, btnVerifyPhone;
    public onCompleteListener mCompleteListener;
    private boolean enterCode = false;
    private TextView tvContactSupport;

    private Context context;
    private String country_id;

    TextView TextViewClickHereToEnterCode;

    public static dialogVerifyphone newInstance(String phone, String country_id, boolean ec) {

        dialogVerifyphone frag = new dialogVerifyphone();
        Bundle args = new Bundle();

        args.putString("country_id", country_id);
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
        country_id = mArgs.getString("country_id");


        ///  Log.e("json data", myValue);


    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        this.context = context;
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


        tvContactSupport = (TextView) rootView.findViewById(R.id.TextViewInformSupport);
        TextViewClickHereToEnterCode = (TextView) rootView.findViewById(R.id.TextViewClickHereToEnterCode);

        tvMobileNumber = (mTextView) rootView.findViewById(R.id.TextViewAccountSettingmcMobileNumber);

        tvVerifyFoneMobileNumber = (mTextView) rootView.findViewById(R.id.TextViewVerifyPhoneMobileNumber);


       // etCode = (EditText) rootView.findViewById(R.id.EditTextAccountSettingMyContactVerifyCode);
        etPinCode = (PinEntryEditText) rootView.findViewById(R.id.EditTextAccountSettingMyContactVerifyCodePinEntry);

        llSendVerification = (LinearLayout) rootView.findViewById(R.id.LinearlayoutAccountSettingMyContactSendVerificationCode);

        llVerifyCode = (LinearLayout) rootView.findViewById(R.id.LinearlayoutAccountSettingMyContactVerifyCode);

        llError = (LinearLayout) rootView.findViewById(R.id.LinearlayoutAccountSettingMyContactErrorVerficationCode);


        btnSendVerificaitonCode = (AppCompatButton) rootView.findViewById(R.id.ButtonAccountSettingmcSendVerificationCode);
        btnVerifyPhone = (AppCompatButton) rootView.findViewById(R.id.ButtonAccountSettingmcVerifyNow);

        tvMobileNumber.setText(phone);
        tvVerifyFoneMobileNumber.setText(phone);


        btnSendVerificaitonCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getmobileCode(SharedPreferenceManager.getUserObject(context).getPath());

            }
        });
        btnVerifyPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (etPinCode.getText().toString().trim().equals("")) {
                    Toast.makeText(context, "Pleas Enter Code First", Toast.LENGTH_SHORT).show();


                } else if ( etPinCode.getText().toString().length() ==6) {
                    Toast.makeText(context, "Enter Valid Code", Toast.LENGTH_SHORT).show();
                }


                else if (!isCodeValid(etPinCode.getText().toString())) {
                    Toast.makeText(context, "Enter Valid Code", Toast.LENGTH_SHORT).show();
                } else {
                    validateMobile(etPinCode.getText().toString());


                }
            }
        });
        TextViewClickHereToEnterCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llSendVerification.setVisibility(View.GONE);
                llVerifyCode.setVisibility(View.VISIBLE);
                //mobCode= response;
                llError.setVisibility(View.GONE);
            }
        });


        tvContactSupport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                /*  Toast.makeText(RegistrationActivity.this, "Correct", Toast.LENGTH_SHORT).show();
                 */
                JSONObject params = new JSONObject();
                try {


                    String[] separated = phone.split("-");
                    String phoneWithoutCountryCode = separated[1]; // this will contain "Fruit"

                    params.put("contact_phone", phoneWithoutCountryCode);
                    params.put("contact_ip", "");
                    params.put("emailaddress", "");
                    params.put("contact_category_id", "5");
                    params.put("contact_name", SharedPreferenceManager.getUserObject(context).getPersonal_name());

                    params.put("contact_message", "I am unable to verify phone number. - Sent from Account Setting");
                    params.put("contact_country_id", country_id);

                    params.put("path", SharedPreferenceManager.getUserObject(context).getPath());


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //Log.e("params contact", params.toString());
                contactUs(params);


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
        final ProgressDialog pDialog = new ProgressDialog(context);
        pDialog.setMessage("Loading...");
        pDialog.show();
        //Log.e("path", "" + Urls.getMobileCode + path);
        StringRequest req = new StringRequest(Urls.getMobileCode + path,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Log.d("Response--", response.toString() + "==");
                        if (response != null) {

                            if (Long.parseLong(response) == 0) {
                                llSendVerification.setVisibility(View.GONE);
                                btnVerifyPhone.setVisibility(View.GONE);
                                llError.setVisibility(View.VISIBLE);

                            } else {
                                Toast.makeText(context, "Your mobile verification code has been send successfully.", Toast.LENGTH_SHORT).show();

                                llSendVerification.setVisibility(View.GONE);
                                llVerifyCode.setVisibility(View.VISIBLE);
                                //mobCode= response;
                                llError.setVisibility(View.GONE);
                                // etCode.setText(response);
                            }
                        }
                        pDialog.dismiss();
                    }
                }, new Response.ErrorListener() {
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


        req.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(getActivity()).addToRequestQueue(req);

    }

    private void validateMobile(String postalcode) {

        final ProgressDialog pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        pDialog.show();

        JSONObject params = new JSONObject();
        try {

            params.put("postal_code", postalcode);
            params.put("path", SharedPreferenceManager.getUserObject(context).getPath());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //Log.e("validateMobile " + "  " + Urls.validateMobile, "" + params);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.PUT,
                Urls.validateMobile, params,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        //Log.e("Response verification", response + "");


                        Gson gson;
                        GsonBuilder gsonBuildert = new GsonBuilder();
                        gson = gsonBuildert.create();
                        Type membert = new TypeToken<Members>() {
                        }.getType();
                        Members mem = (Members) gson.fromJson(response.toString(), membert);

                        if (mem.getPhone_verified() == 1) {


                            //    mCompleteListener.onComplete("");
                            Members mem2 = SharedPreferenceManager.getUserObject(context);

                            if (mem.getRequest_profile_view() != mem2.getMember_status()) {
                                mem2.setMember_status(mem.getRequest_profile_view());
                                SharedPreferenceManager.setUserObject(context, mem2);

                                dialogVerifyphone.this.getDialog().cancel();

                            }
                            dialogVerifyphone.this.getDialog().cancel();
                            mCompleteListener.onComplete("");

                        } else if (mem.getPhone_verified() == 0) {
                            Toast.makeText(context, "Phone Not Verified", Toast.LENGTH_SHORT).show();
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


    private void contactUs(JSONObject params) {

        //Log.e("params", "" + params);
        final ProgressDialog pDialog = new ProgressDialog(context);
        pDialog.setMessage("Loading...");
        pDialog.show();


        //   RequestQueue rq = Volley.newRequestQueue(getActivity().getApplicationContext());
        //Log.e("params url", Urls.contactUs + "  ==  " + params);


        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.PUT,
                Urls.contactUs, params,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        //Log.e("res", response + "");
                        pDialog.dismiss();

                        try {
                            int responseid = response.getInt("id");

                            if (responseid == 1) {
                                Toast.makeText(context, "Thank you for contacting us. MarryMax support will contact you to verify your phone number.\n", Toast.LENGTH_SHORT).show();
                                dialogVerifyphone.this.getDialog().cancel();

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
        MySingleton.getInstance(context).addToRequestQueue(jsonObjReq);

    }
}
