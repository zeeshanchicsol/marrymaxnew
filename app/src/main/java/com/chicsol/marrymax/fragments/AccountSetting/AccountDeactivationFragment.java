package com.chicsol.marrymax.fragments.AccountSetting;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatSpinner;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.chicsol.marrymax.R;
import com.chicsol.marrymax.adapters.MySpinnerAdapterType;
import com.chicsol.marrymax.modal.WebArdType;
import com.chicsol.marrymax.other.UserSessionManager;
import com.chicsol.marrymax.preferences.SharedPreferenceManager;
import com.chicsol.marrymax.urls.Urls;
import com.chicsol.marrymax.utils.ConnectCheck;
import com.chicsol.marrymax.utils.Constants;
import com.chicsol.marrymax.utils.MySingleton;
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
 * Created by Android on 11/3/2016.
 */

public class AccountDeactivationFragment extends Fragment {
    private ProgressBar pDialog;
    private AppCompatButton btDeactiveAccount;

    private AppCompatSpinner spinnerASaccountdeactivationReason;

    private MySpinnerAdapterType adapter_deactive_reasons;
    private List<WebArdType> deactiveReasonsDataList;

    private AppCompatEditText etPassword, etClose, etOtherReason;

    Context context;


    private String Tag = "DashMembersFragment";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_as_account_deactivation, container, false);
        initilize(rootView);
        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }


    private void initilize(View view) {
        pDialog = (ProgressBar) view.findViewById(R.id.ProgressbarProjectMain);
        btDeactiveAccount = (AppCompatButton) view.findViewById(R.id.ButtonASDeactiveAccount);

        spinnerASaccountdeactivationReason = (AppCompatSpinner) view.findViewById(R.id.spinnerASaccountdeactivationReason);
        deactiveReasonsDataList = new ArrayList<>();

        adapter_deactive_reasons = new MySpinnerAdapterType(getContext(),
                android.R.layout.simple_spinner_item, deactiveReasonsDataList);
        adapter_deactive_reasons.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerASaccountdeactivationReason.setAdapter(adapter_deactive_reasons);


        etPassword = (AppCompatEditText) view.findViewById(R.id.EditTextASDeactivePassword);
        etClose = (AppCompatEditText) view.findViewById(R.id.EditTextASTypeClose);

        etOtherReason = (AppCompatEditText) view.findViewById(R.id.EditTextOtherReason);

        getRequest();


        etClose.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_NEXT)) {
                    boolean hasLowercase = !etClose.getText().toString().equals(etClose.getText().toString().toUpperCase());

                    if (hasLowercase) {
                        etClose.setError("Type Close in Block Letters..");
                        Toast.makeText(getContext(), "Type Close in Block Letters..", Toast.LENGTH_SHORT).show();
                    } else {
                        etPassword.setFocusable(true);
                    }

                }
                return false;
            }
        });


        spinnerASaccountdeactivationReason.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                WebArdType obj = (WebArdType) spinnerASaccountdeactivationReason.getSelectedItem();
                if (obj.getId().equals("6")) {
                    etOtherReason.setVisibility(View.VISIBLE);

                } else {
                    etOtherReason.setText("");
                    etOtherReason.setVisibility(View.GONE);
                }


                // Log.e("Log", " " + obj.getId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        btDeactiveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ConnectCheck.isConnected(getActivity().findViewById(android.R.id.content))) {
                    WebArdType obj = (WebArdType) spinnerASaccountdeactivationReason.getSelectedItem();


                    boolean cancel = false;

                    View focusView = null;
                    String pas = etPassword.getText().toString();
                    String close = etClose.getText().toString();
                    String reason = etOtherReason.getText().toString();
                    etClose.setError(null);


                    if (spinnerASaccountdeactivationReason.getSelectedItemId() == 0) {


                        TextView errorText = (TextView) spinnerASaccountdeactivationReason.getSelectedView();
                        errorText.setError("");
                        errorText.setTextColor(getResources().getColor(R.color.colorTextRed));//just to highlight that this is an error
                        errorText.setText("Please select reason for deactivation");

                    } else if (obj.getId().equals("6") && TextUtils.isEmpty(reason.trim())) {
                        etOtherReason.setError("Please Enter Reason");
                        focusView = etOtherReason;
                        focusView.requestFocus();
                    }

                    /*else if (TextUtils.isEmpty(reason)) {
                    etOtherReason.setError("Please Enter Reason");
                    focusView = etOtherReason;
                    focusView.requestFocus();
                }*/
                    else if (TextUtils.isEmpty(close)) {
                        etClose.setError("Please Enter Close");
                        focusView = etClose;
                        focusView.requestFocus();
                    } else if (!close.equals("CLOSE")) {
                        etClose.setError("Please Enter CLOSE in block letters...");
                        focusView = etClose;
                        focusView.requestFocus();
                    } else if (TextUtils.isEmpty(pas)) {
                        etPassword.setError("Please Enter Password");
                        focusView = etPassword;
                        focusView.requestFocus();
                    } else {
                        Log.e("innnnnnnnnnb1", " " + obj.getId());

                        JSONObject params = new JSONObject();
                        try {
                            params.put("path", SharedPreferenceManager.getUserObject(getContext()).get_path());
                            params.put("deactivate_reason_id", obj.getId());
                            if (!obj.getId().equals("-1")) {
                                params.put("deactivate_reason", reason);
                            }
                            params.put("password", pas);

                            if (ConnectCheck.isConnected(getActivity().findViewById(android.R.id.content))) {


                                //  Log.e(""+ Urls.accountDeactivate, "" + params);
                                deactivateRequest(params);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }
              /*  if(obj.getId().equals("6")){
                  //  etOtherReason

                }*/


            }
        });

    }

    private void deactivateRequest(JSONObject params) {
        btDeactiveAccount.setEnabled(false);

        pDialog.setVisibility(View.VISIBLE);
        //   RequestQueue rq = Volley.newRequestQueue(getActivity().getApplicationContext());


        //   Log.e("Params", "" + params);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.PUT,
                Urls.accountDeactivate, params,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("re  update appearance", response + "");
                        try {
                            int responseid = response.getInt("id");

                            if (responseid == 1) {

                                UserSessionManager sessionManager = new UserSessionManager(context);
                                sessionManager.logoutUser();

                                Toast.makeText(context, "Account Deactivated", Toast.LENGTH_SHORT).show();

                            } else {
                                Toast.makeText(context, "Account did not deactivate. Check your password. Please try again.", Toast.LENGTH_SHORT).show();
                            }


                        } catch (JSONException e) {
                            pDialog.setVisibility(View.GONE);
                            e.printStackTrace();

                        }

                        pDialog.setVisibility(View.GONE);
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {


                VolleyLog.e("res err", "Error: " + error);
                // Toast.makeText(RegistrationActivity.this, "Incorrect Email or Password !", Toast.LENGTH_SHORT).show();

                pDialog.setVisibility(View.GONE);
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
        MySingleton.getInstance(getContext()).addToRequestQueue(jsonObjReq, Tag);

    }


    private void getRequest() {


        pDialog.setVisibility(View.VISIBLE);
        Log.e("api path", "" + Urls.getDeactivateReasons + SharedPreferenceManager.getUserObject(getContext()).get_path());

        JsonArrayRequest req = new JsonArrayRequest(Urls.getDeactivateReasons + SharedPreferenceManager.getUserObject(getContext()).get_path(),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("Response", response.toString());
                        try {

                            JSONArray jsonCountryStaeObj = response.getJSONArray(0);

                            Gson gsonc;
                            GsonBuilder gsonBuilderc = new GsonBuilder();
                            gsonc = gsonBuilderc.create();
                            Type listType = new TypeToken<List<WebArdType>>() {
                            }.getType();

                            deactiveReasonsDataList = (List<WebArdType>) gsonc.fromJson(jsonCountryStaeObj.toString(), listType);

                            deactiveReasonsDataList.add(0, new WebArdType("-1", "Please select a reason for deactivating your account"));
                            adapter_deactive_reasons.updateDataList(deactiveReasonsDataList);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        pDialog.setVisibility(View.GONE);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Err", "Error: " + error.getMessage());
                pDialog.setVisibility(View.GONE);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return Constants.getHashMap();
            }
        };
        MySingleton.getInstance(getContext()).addToRequestQueue(req, Tag);
    }

    @Override
    public void onStop() {
        super.onStop();
        MySingleton.getInstance(getContext()).cancelPendingRequests(Tag);

    }

}
