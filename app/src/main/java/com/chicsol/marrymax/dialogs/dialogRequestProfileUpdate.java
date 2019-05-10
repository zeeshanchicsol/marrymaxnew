package com.chicsol.marrymax.dialogs;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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
import com.chicsol.marrymax.adapters.MySpinnerAdapter;
import com.chicsol.marrymax.modal.WebArd;
import com.chicsol.marrymax.preferences.SharedPreferenceManager;
import com.chicsol.marrymax.urls.Urls;
import com.chicsol.marrymax.utils.Constants;
import com.chicsol.marrymax.utils.MySingleton;
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

public class dialogRequestProfileUpdate extends DialogFragment {

    public onCompleteListener mCompleteListener;


    private List<WebArd> MyCountryDataList, MyCountryStateDataList;
    String title, desc, params = null, btnTitle;

    private Spinner spAttribute, spValue;
    private MySpinnerAdapter adapter_attribute, adapter_value;
    private AdapterView.OnItemSelectedListener statesListener, attributeListener;
    private ProgressDialog pDialog;
    faTextView cancelButton;

    EditText etDesc;
    Button mOkButton;


 /*   private PhoneRequestCallBackInterface listener;

    public void setListener(PhoneRequestCallBackInterface listener) {
        this.listener = listener;
    }
*/

    public static dialogRequestProfileUpdate newInstance(String params) {
        /*       Members member, String userpath, boolean replyCheck, Members member2*/
        dialogRequestProfileUpdate frag = new dialogRequestProfileUpdate();
        Bundle args = new Bundle();

        args.putString("params", params);


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


        params = mArgs.getString("params");


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.dialog_request_profile_update, container, false);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);


        initialize(rootView);
        setListeners();


        return rootView;
    }


    private void initialize(View rootView) {

        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Loading...");
        MyCountryDataList = new ArrayList<>();
        MyCountryStateDataList = new ArrayList<>();


        mOkButton = (Button) rootView.findViewById(R.id.mButtonDialogRequest);
        etDesc = (EditText) rootView.findViewById(R.id.EditTextReportConcernDialgOtherReason);

        spAttribute = (Spinner) rootView.findViewById(R.id.spinnerRequestProfileChangeAttribute);
        spValue = (Spinner) rootView.findViewById(R.id.spinnerRequestProfileChangeValue);

        cancelButton = (faTextView) rootView.findViewById(R.id.faButtonRequestdismiss);

        //MyCountry============================

        adapter_attribute = new MySpinnerAdapter(getContext(),
                android.R.layout.simple_spinner_item, MyCountryDataList);
        adapter_attribute.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spAttribute.setAdapter(adapter_attribute);

        //2=========
        adapter_value = new MySpinnerAdapter(getContext(),
                android.R.layout.simple_spinner_item, MyCountryStateDataList);
        adapter_value.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spValue.setAdapter(adapter_value);

        getRequestCategory();
    }

    private void setListeners() {

        attributeListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                WebArd ard = (WebArd) spAttribute.getSelectedItem();
            /*    if (position != -1 && position != 0) {
                   // cModel c = MyCountryDataList2.get(position - 1);
                  //  Log.e("country data code", "" + c.getCode());

                  //  getCurrentIP(c.getCode());

                }*/
                if (Integer.parseInt(ard.getId()) != 0) {
                    MyCountryStateDataList.clear();

                    adapter_value.clear();


                    getRequestOptions(ard.getId());


                } else {

                    //   Toast.makeText(RegisterGeographicActivity.this, "Please Select Country First", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };


        statesListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
      /*          WebArd ardState = (WebArd) spValue.getSelectedItem();
                WebArd ardCountry = (WebArd) spAttribute.getSelectedItem();

                Log.e("city state ", Integer.parseInt(ardState.getId()) + "  ----- " + Integer.parseInt(ardCountry.getId()) + "  " + updateData);

                //&& updateData != true
                if (Integer.parseInt(ardState.getId()) != 0 && Integer.parseInt(ardCountry.getId()) != 0) {
                    getRequestOptions(ardCountry.getId(), ardState.getId());

                } else {
                    //   Toast.makeText(RegisterGeographicActivity.this, "Please Select Country & State First", Toast.LENGTH_SHORT).show();
                }*/
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };


   /*     btSubscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MarryMax marryMax = new MarryMax(getActivity());
                marryMax.subscribe();
            }
        });*/

        mOkButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(final View v) {


                WebArd cat = (WebArd) spAttribute.getSelectedItem();
                WebArd sub = (WebArd) spValue.getSelectedItem();


                String desc = etDesc.getText().toString();
                if (sub != null) {
                    if (!sub.getId().equals("-1") && !TextUtils.isEmpty(desc)) {

                        JSONObject params = new JSONObject();
                        try {

                            params.put("path", SharedPreferenceManager.getUserObject(getContext()).getPath());

                            params.put("ID", cat.getId());
                            params.put("ID2", sub.getId());
                            params.put("Name", etDesc.getText().toString());

                            requestPermission(params);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {

                        Toast.makeText(getContext(), "Some Fields are missisng", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Some Fields are missisng", Toast.LENGTH_SHORT).show();
                }


            }
        });


        cancelButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(final View v) {
                //Toast.makeText(getActivity().getApplicationContext(), "clcieck", Toast.LENGTH_SHORT).show();
                dialogRequestProfileUpdate.this.getDialog().cancel();
            }
        });


        spAttribute.setOnItemSelectedListener(attributeListener);
        //  spValue.setOnItemSelectedListener(statesListener);

    }


    @Override
    public void onStart() {
        super.onStart();

        getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    private void requestPermission(JSONObject params) {
        /*        RequestUpdate             path,ID,ID2,Name                    Response 1 success,0 fail*/

        final ProgressDialog pDialog = new ProgressDialog(getContext());
        pDialog.setMessage("Loading...");
        pDialog.show();
        //Log.e("requestUpdate ", params.toString());
        //Log.e("requestUpdate path", Urls.requestUpdate);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.PUT,
                Urls.requestUpdate, params,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        //Log.e("Res  interest ", response + "");

                        try {
                            int responseid = response.getInt("id");


                            if (responseid == 0) {
                                pDialog.dismiss();
                                Toast.makeText(getContext(), "Request has not been sent successfully. ", Toast.LENGTH_SHORT).show();
                            } else if (responseid == 1) {
                                mCompleteListener.onComplete("");
                                pDialog.dismiss();
                                Toast.makeText(getContext(), "Request has been sent successfully", Toast.LENGTH_LONG).show();


                                dialogRequestProfileUpdate.this.getDialog().cancel();

                            } else {
                                mCompleteListener.onComplete("");
                                pDialog.dismiss();
                                dialogRequestProfileUpdate.this.getDialog().cancel();

                            }


                        } catch (JSONException e) {
                            pDialog.dismiss();
                            dialogRequestProfileUpdate.this.getDialog().cancel();
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


    private void getRequestOptions(final String country_id) {


        pDialog.show();

        JsonArrayRequest req = new JsonArrayRequest(Urls.getRequestOptions + country_id,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        //Log.e("Response states", response.toString());

                        try {


                            if (response.length() != 0) {
                                JSONArray jsonCountryStaeObj = response.getJSONArray(0);
                                MyCountryStateDataList.clear();

                                Gson gsonc;
                                GsonBuilder gsonBuilderc = new GsonBuilder();
                                gsonc = gsonBuilderc.create();
                                Type listType = new TypeToken<List<WebArd>>() {
                                }.getType();


                                MyCountryStateDataList = (List<WebArd>) gsonc.fromJson(jsonCountryStaeObj.toString(), listType);


                                MyCountryStateDataList.add(0, new WebArd("-1", "Please Select"));
                                adapter_value.updateDataList(MyCountryStateDataList);
                                spValue.setSelection(0);
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                            pDialog.dismiss();
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
        MySingleton.getInstance(getContext()).addToRequestQueue(req);
    }

    private void getRequestCategory() {


        pDialog.show();

        JsonArrayRequest req = new JsonArrayRequest(Urls.getRequestCategory,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        //Log.e("Response states", response.toString());

                        try {


                            if (response.length() != 0) {
                                JSONArray jsonCountryStaeObj = response.getJSONArray(0);
                                MyCountryStateDataList.clear();

                                Gson gsonc;
                                GsonBuilder gsonBuilderc = new GsonBuilder();
                                gsonc = gsonBuilderc.create();
                                Type listType = new TypeToken<List<WebArd>>() {
                                }.getType();


                                MyCountryDataList = (List<WebArd>) gsonc.fromJson(jsonCountryStaeObj.toString(), listType);


                                // MyCountryDataList.add(0, new WebArd("-1", "Please Select"));
                                adapter_attribute.updateDataList(MyCountryDataList);
                                spAttribute.setSelection(0);
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                            pDialog.dismiss();
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
        MySingleton.getInstance(getContext()).addToRequestQueue(req);
    }


    public static interface onCompleteListener {
        public abstract void onComplete(String s);
    }


}


