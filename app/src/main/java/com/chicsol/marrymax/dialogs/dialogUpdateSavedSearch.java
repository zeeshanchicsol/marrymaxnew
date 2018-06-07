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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.chicsol.marrymax.R;
import com.chicsol.marrymax.modal.cModel;
import com.chicsol.marrymax.preferences.SharedPreferenceManager;
import com.chicsol.marrymax.urls.Urls;
import com.chicsol.marrymax.utils.Constants;
import com.chicsol.marrymax.utils.MySingleton;
import com.chicsol.marrymax.widgets.faTextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;


/**
 * Created by zeedr on 24/10/2016.
 */

public class dialogUpdateSavedSearch extends DialogFragment {
    EditText etMemberNotes;
    String id, name;
    private ListView lv_mycontacts;
    public onCompleteListener mCompleteListener;

    public static dialogUpdateSavedSearch newInstance(cModel item) {

        dialogUpdateSavedSearch frag = new dialogUpdateSavedSearch();
        Bundle args = new Bundle();
        args.putString("id", item.getId());
        args.putString("name", item.getName());
        frag.setArguments(args);
        return frag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle mArgs = getArguments();
        id = mArgs.getString("id");
        name = mArgs.getString("name");


    }


    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        try {

            Log.e("getfragment manager", "======" + getTargetFragment());
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
        final View rootView = inflater.inflate(R.layout.dialog_update_saved_search, container, false);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        etMemberNotes = (EditText) rootView.findViewById(R.id.EditTextMemberNotes);
        etMemberNotes.setText(name);
      /*  lv_mycontacts=(ListView)rootView.findViewById(R.id.ListViewMySavedList);
        List<String> dataList=new ArrayList<>();
        dataList.add("asdsadsa");
        dataList.add("1212321");
        MyContactsListAdapter myContactsListAdapter=new MyContactsListAdapter(getActivity(),R.layout.item_list_my_saved_list,dataList);
        lv_mycontacts.setAdapter(myContactsListAdapter);*/

   /*     Bundle mArgs = getArguments();
        String myValue = mArgs.getString("jsondata");
        try {
            JSONArray jsonArray = new JSONArray(myValue);
            JSONArray dosDataList = jsonArray.getJSONArray(0);
            JSONArray dontsDataList = jsonArray.getJSONArray(1);
        } catch (JSONException e) {
            e.printStackTrace();
        }*/





     /*   RadioGroup radioGroup = (RadioGroup) rootView.findViewById(R.id.RadioGroupProfileFor);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        //  radioGroup.ad
        for (int i = 0; i < DataList.size(); i++) {
            mRadioButton mradiobutton = new mRadioButton(getContext());
            mradiobutton.setText(DataList.get(i));
            mradiobutton.setLayoutParams(layoutParams);

            radioGroup.addView(mradiobutton);
        }*/

        /*//do's
        for (int i=0;i<jsonArray1.length();i++){
            JSONObject jsonObject= jsonArray1.getJSONObject(i);
            Log.e("value",""+jsonObject.get("name").toString());
        }

        //dont's
        for (int i=0;i<jsonArray2.length();i++){
            JSONObject jsonObject= jsonArray2.getJSONObject(i);
            Log.e("value 2",""+jsonObject.get("name").toString());
        }*/


        Button mOkButton = (Button) rootView.findViewById(R.id.mButtonSubmitGeoInfo);
        mOkButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(final View v) {

                if (!TextUtils.isEmpty(etMemberNotes.getText().toString().trim())) {

                    JSONObject params = new JSONObject();
                    try {
                        params.put("id", id);
                        params.put("name", etMemberNotes.getText().toString());
                        params.put("path", SharedPreferenceManager.getUserObject(getContext()).get_path());
                        updateSearch(params);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {
                    Toast.makeText(getContext(), "Please enter search name first", Toast.LENGTH_SHORT).show();
                }


            }
        });

        faTextView cancelButton = (faTextView) rootView.findViewById(R.id.dismissBtnGeoInfo);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(final View v) {
                dialogUpdateSavedSearch.this.getDialog().cancel();
            }
        });


        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();

        getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    private void updateSearch(JSONObject params) {
        final ProgressDialog pDialog = new ProgressDialog(getContext());
        pDialog.setMessage("Loading...");
        pDialog.show();

        Log.e("params" + "  " + Urls.updateSaveSearch, "" + params);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.PUT,
                Urls.updateSaveSearch, params,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("re  update interest", response + "");
                        try {
                            int responseid = response.getInt("id");
                            if (responseid == 1) {
                                Toast.makeText(getContext(), "Search name updated", Toast.LENGTH_SHORT).show();
                                dialogUpdateSavedSearch.this.getDialog().cancel();
                                mCompleteListener.onComplete("");
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
        MySingleton.getInstance(getContext()).addToRequestQueue(jsonObjReq);

    }


    public static interface onCompleteListener {
        public abstract void onComplete(String s);
    }
}


