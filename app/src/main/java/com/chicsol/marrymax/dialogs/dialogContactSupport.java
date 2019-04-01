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

public class dialogContactSupport extends DialogFragment {
    private Context context;
    EditText etOtherReason;
    String userpath, notes, jsarray;
    int abtypeid = -1;
    private onCompleteListener mCompleteListener;

    public static dialogContactSupport newInstance(JSONArray jsArray) {

        dialogContactSupport frag = new dialogContactSupport();
        Bundle args = new Bundle();


        args.putString("jsArray", jsArray.toString());
        //    args.putString("userpath", userpath);


        frag.setArguments(args);
        return frag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle mArgs = getArguments();
        jsarray = mArgs.getString("jsArray");
        //      notes = mArgs.getString("selectdlist");
        //     userpath = mArgs.getString("userpath");

    }


    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        context = activity;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.dialog_contact_support, container, false);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        final TextInputLayout textInputLayout = (TextInputLayout) rootView.findViewById(R.id.EditTextBlockDialgTextInputLayout);

        etOtherReason = (EditText) rootView.findViewById(R.id.EditTextBlockDialgOtherReason);

        final RadioGroup radioGroup = (RadioGroup) rootView.findViewById(R.id.RadioGroupBlockReasonDialog);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                //  Log.e("generateDynamicRadi",""+checkedId);
                abtypeid = checkedId;
                if (checkedId == 116) {

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


        Button mOkButton = (Button) rootView.findViewById(R.id.mButtonDialogBlock);
        mOkButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(final View v) {

                if (abtypeid != -1) {
                    boolean ccehck = false;
                    JSONObject params = new JSONObject();
                    try {
                        if (abtypeid == 116) {

                            if (!TextUtils.isEmpty(etOtherReason.getText().toString().trim())) {


                                if (etOtherReason.getText().toString().trim().length() < 15) {
                                    ccehck = false;
                                    etOtherReason.setError("Min 15 & max 200 characters");

                                    etOtherReason.requestFocus();
                                } else {

                                    params.put("contact_message", etOtherReason.getText().toString());
                                    params.put("contact_category_id", abtypeid + "");
                                    ccehck = true;
                                }
                            } else {
                                ccehck = false;
                            }

                        } else {
                            ccehck = true;
                            RadioButton radioSexButton = (RadioButton) rootView.findViewById(abtypeid);
                            params.put("contact_message", radioSexButton.getText().toString());
                            params.put("contact_category_id", abtypeid + "");
                        }
                        params.put("flag", "usrmsg");
                        //    params.put("userpath", userpath);
                        //    params.put("path", SharedPreferenceManager.getUserObject(getContext()).getPath());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    //
                    //
                    if (ccehck) {
                        query(params);
                    } else {
                        Toast.makeText(getContext(), "Please enter other reason", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Please select reason", Toast.LENGTH_SHORT).show();

                }

            }
        });

        faTextView cancelButton = (faTextView) rootView.findViewById(R.id.mButtonDismissDialogBlock);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(final View v) {
                //Toast.makeText(getActivity().getApplicationContext(), "clcieck", Toast.LENGTH_SHORT).show();
                dialogContactSupport.this.getDialog().cancel();
            }
        });


        return rootView;
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

        //   Log.e("blockReason" + "  " + Urls.contactUs, "" + params);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.PUT,
                Urls.contactUs, params,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("re  update interest", response + "");
                        try {
                            int responseid = response.getInt("id");
                            if (responseid == 1) {
                                Toast.makeText(context, "Message has been sent successfully", Toast.LENGTH_SHORT).show();

                                mCompleteListener.onComplete("");
                            }
                            dialogContactSupport.this.getDialog().cancel();
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


