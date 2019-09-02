package com.chicsol.marrymax.dialogs;


import android.app.ProgressDialog;
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
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.chicsol.marrymax.R;
import com.chicsol.marrymax.preferences.SharedPreferenceManager;
import com.chicsol.marrymax.urls.Urls;
import com.chicsol.marrymax.utils.Constants;
import com.chicsol.marrymax.utils.MySingleton;
import com.chicsol.marrymax.widgets.faTextView;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import static com.chicsol.marrymax.utils.Constants.defaultSelectionsObj;


/**
 * Created by zeedr on 24/10/2016.
 */

public class dialogSaveSearch extends DialogFragment {


    public static dialogSaveSearch newInstance() {
        dialogSaveSearch frag = new dialogSaveSearch();
        Bundle args = new Bundle();
        // args.putString("title", "Do's & Don't");
        //  args.putString("jsondata", data);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /// Bundle mArgs = getArguments();
        //   String myValue = mArgs.getString("jsondata");
        ///  Log.e("json data", myValue);


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.dialog_save_search, container, false);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        final EditText etSaveSearch = (EditText) rootView.findViewById(R.id.EditTextDialogSaveSearch);


        Button mOkButton = (Button) rootView.findViewById(R.id.ButtonDialogSaveSearch);
        mOkButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(final View v) {


                if (!TextUtils.isEmpty(etSaveSearch.getText().toString().trim())) {

                    //setChoice_caste_ids
                    if (defaultSelectionsObj.getChoice_caste_ids().length() > 300) {
                        Toast.makeText(getContext(), "You have selected too much castes", Toast.LENGTH_SHORT).show();
                    } else {


                        defaultSelectionsObj.setPath(SharedPreferenceManager.getUserObject(getContext()).getPath());
                        defaultSelectionsObj.setName(etSaveSearch.getText().toString());
                        defaultSelectionsObj.setNotes("");

                        Gson gson = new Gson();
                        String params = gson.toJson(defaultSelectionsObj);


                        saveSearch(params);
                    }
                } else {
                    Toast.makeText(getContext(), "Please Enter Search Name", Toast.LENGTH_SHORT).show();

                }
            }
        });

        faTextView cancelButton = (faTextView) rootView.findViewById(R.id.dismissBtnSaveSearch);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(final View v) {
                dialogSaveSearch.this.getDialog().cancel();
            }
        });


        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();

        getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }


    private void saveSearch(String paramsString) {
        JSONObject params = null;
        try {
            params = new JSONObject(paramsString);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final ProgressDialog pDialog = new ProgressDialog(getContext());
        pDialog.setMessage("Loading...");
        pDialog.show();
        pDialog.show();
        //   RequestQueue rq = Volley.newRequestQueue(getActivity().getApplicationContext());

       /* JSONObject params = new JSONObject();
        try {
            params.put("height_id", height_id);

        } catch (JSONException e) {
            e.printStackTrace();
        }*/
        //Log.e("Params", "" + params);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.PUT,
                Urls.saveSearch, params,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        //Log.e("re  save search", response + "");
                        try {
                            int responseid = response.getInt("id");

                            if (responseid == 1) {
                                Toast.makeText(getContext(), "Search Saved", Toast.LENGTH_SHORT).show();
                                dialogSaveSearch.this.getDialog().cancel();
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

}


