package com.chicsol.marrymax.dialogs;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.chicsol.marrymax.R;
import com.chicsol.marrymax.modal.Members;
import com.chicsol.marrymax.modal.WebArd;
import com.chicsol.marrymax.preferences.SharedPreferenceManager;
import com.chicsol.marrymax.urls.Urls;
import com.chicsol.marrymax.utils.Constants;
import com.chicsol.marrymax.utils.MySingleton;
import com.chicsol.marrymax.utils.ViewGenerator;
import com.chicsol.marrymax.widgets.faTextView;
import com.chicsol.marrymax.widgets.mTextView;
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

public class dialogReportConcern extends DialogFragment {

    EditText etOtherReason;
    mTextView tvDesc;
    String userpath, notes, jsarray, alias;
    int abtypeid = -1;
    private onCompleteListener mCompleteListener;

    public static dialogReportConcern newInstance(JSONArray jsArray, String userpath, Members member) {

        dialogReportConcern frag = new dialogReportConcern();
        Bundle args = new Bundle();


        args.putString("jsArray", jsArray.toString());
        args.putString("userpath", userpath);


        args.putString("alias", String.valueOf(member.getAlias()));

        frag.setArguments(args);
        return frag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle mArgs = getArguments();


        jsarray = mArgs.getString("jsArray");


        userpath = mArgs.getString("userpath");
        alias = mArgs.getString("alias");


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.dialog_report_concern, container, false);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        tvDesc = (mTextView) rootView.findViewById(R.id.TextViewDialogReportConcernAlias);
        etOtherReason = (EditText) rootView.findViewById(R.id.EditTextReportConcernDialgOtherReason);
        tvDesc.setText(alias);

        final TextInputLayout textInputLayout = (TextInputLayout) rootView.findViewById(R.id.EditTextReportConcernDialgTextInputLayout);


        RadioGroup radioGroup = (RadioGroup) rootView.findViewById(R.id.RadioGroupDialogReportConcern);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                abtypeid = checkedId;
                if (checkedId == 47) {

                    textInputLayout.setVisibility(View.VISIBLE);
                } else {

                    textInputLayout.setVisibility(View.GONE);
                }
            }
        });


        List<WebArd> dataList = new ArrayList<>();
        try {
            JSONArray jsonCountryStaeObj = new JSONArray(jsarray);
            Gson gsonc;
            GsonBuilder gsonBuilderc = new GsonBuilder();
            gsonc = gsonBuilderc.create();
            Type listType = new TypeToken<List<WebArd>>() {
            }.getType();
            dataList = (List<WebArd>) gsonc.fromJson(jsonCountryStaeObj.toString(), listType);
            ViewGenerator viewGenerator = new ViewGenerator(getContext());
            viewGenerator.generateDynamicRadiosForDialogs(dataList, radioGroup);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        Button mOkButton = (Button) rootView.findViewById(R.id.mButtonDialogReportConcern);
        mOkButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(final View v) {
                boolean ccheck = false;
                if (abtypeid != -1) {
                    JSONObject params = new JSONObject();
                    try {
                        if (abtypeid == 47) {
                            if (!TextUtils.isEmpty(etOtherReason.getText().toString().trim())) {

                                if (etOtherReason.getText().toString().trim().length() < 15) {
                                    ccheck = false;
                                    etOtherReason.setError("Min 15 & max 200 characters");
                                    etOtherReason.requestFocus();
                                }else {

                                params.put("details", etOtherReason.getText().toString());
                                params.put("id", abtypeid);
                                ccheck = true;
                                }

                            } else {
                                Toast.makeText(getContext(), "Please Enter Reason", Toast.LENGTH_SHORT).show();
                                ccheck = false;
                            }
                        } else {
                            ccheck = true;
                            RadioButton radioSexButton = (RadioButton) rootView.findViewById(abtypeid);
                            params.put("id", abtypeid);
                            params.put("details", radioSexButton.getText().toString());
                        }
                        params.put("userpath", userpath);
                        params.put("path", SharedPreferenceManager.getUserObject(getContext()).getPath());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (ccheck) {
                        query(params);
                    }


                } else {
                    Toast.makeText(getContext(), "Please select reason", Toast.LENGTH_SHORT).show();
                }

            }
        });

        faTextView cancelButton = (faTextView) rootView.findViewById(R.id.dismissBtnReportConcern);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(final View v) {
                //Toast.makeText(getActivity().getApplicationContext(), "clcieck", Toast.LENGTH_SHORT).show();
                dialogReportConcern.this.getDialog().cancel();
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
        //Log.e("params", Urls.reportConcern + "  =======   " + params);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.PUT,
                Urls.reportConcern, params,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        //Log.e("response   ", response + "");
                        try {
                            int responseid = response.getInt("id");


                            if (responseid == 1) {
                                Toast.makeText(getContext(), "Report Submitted", Toast.LENGTH_SHORT).show();
                                mCompleteListener.onCompleteReportConcern("Report Submitted");
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
        public abstract void onCompleteReportConcern(String s);
    }

}


