package com.chicsol.marrymax.dialogs;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.AppCompatRatingBar;
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
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.chicsol.marrymax.R;
import com.chicsol.marrymax.activities.ActivityLogin;
import com.chicsol.marrymax.modal.WebArd;
import com.chicsol.marrymax.modal.mCountryCode;
import com.chicsol.marrymax.modal.mLfm;
import com.chicsol.marrymax.other.MarryMax;
import com.chicsol.marrymax.preferences.SharedPreferenceManager;
import com.chicsol.marrymax.urls.Urls;
import com.chicsol.marrymax.utils.Constants;
import com.chicsol.marrymax.utils.MySingleton;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;


public class dialogFeedback extends DialogFragment {

    public onCompleteListener mCompleteListener;
    String userpath, jsarray;
    mLfm lfm;
    EditText etFeedback;
    Context context;

    public static dialogFeedback newInstance(String a) {

        Gson gson = new Gson();
        dialogFeedback frag = new dialogFeedback();
        Bundle args = new Bundle();
        args.putString("obj", "asd");

        frag.setArguments(args);
        return frag;
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

      /*  Gson gson = new Gson();
        Bundle mArgs = getArguments();

        jsarray = mArgs.getString("obj");
        lfm = gson.fromJson(jsarray, mLfm.class);*/
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.dialog_new_feedback, container, false);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);


        getFeedbackData();


        final AppCompatRatingBar mRbar = (AppCompatRatingBar) rootView.findViewById(R.id.dialog_ratingbar);


        etFeedback = (EditText) rootView.findViewById(R.id.EditTextMatchAidFeedbackText);

        Button mOkButton = (Button) rootView.findViewById(R.id.mButtonDialogMatchAidUPViewProgress);
        mOkButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(final View v) {

                //Log.e("stars", mRbar.getRating() + "" + etFeedback.getText().toString());


                if (!checkSelections()) {
                    if (mRbar.getRating() >= 1) {

                        String fb = etFeedback.getText().toString();

                        JSONObject params = new JSONObject();
                        try {


                            if (!TextUtils.isEmpty(fb.trim())) {

                                params.put("id", lfm.getId());
                                params.put("id2", mRbar.getNumStars());
                                params.put("text", fb);
                                Log.e("addFeedback", Urls.addFeedback + "   " + params);
                                addFeedback(params);
                            } else {

                                Toast.makeText(getContext(), "Enter Feedback Text ", Toast.LENGTH_SHORT).show();
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        // mCompleteListener.onComplete("asdsa");
                    } else {

                        Toast.makeText(getContext(), "Please rate your feedback", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });


        Button cancelButton = (Button) rootView.findViewById(R.id.mButtonDialogMatchAidUPCancel);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(final View v) {

                dialogFeedback.this.getDialog().cancel();
            }
        });


        return rootView;
    }

    private boolean checkSelections() {

        boolean ck = false;
        if (!TextUtils.isEmpty(etFeedback.getText().toString().trim())) {
            if (etFeedback.getText().length() > 200) {
                etFeedback.setError(" max 200 char");

                etFeedback.requestFocus();
                ck = true;

            }
        }
        return ck;
    }

    @Override
    public void onStart() {
        super.onStart();

        getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    public static interface onCompleteListener {
        public abstract void onComplete(String s);
    }

    private void getFeedbackData() {


        //Log.e("api path", "" + Urls.getFeedbackData );

        JsonArrayRequest req = new JsonArrayRequest(Urls.getFeedbackData,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        //   Log.d("Response", response.toString());
                        try {


                            JSONArray jsonCountryStaeObj = response.getJSONArray(0);


                            Gson gsonc;
                            GsonBuilder gsonBuilderc = new GsonBuilder();
                            gsonc = gsonBuilderc.create();
                            Type listType = new TypeToken<List<WebArd>>() {
                            }.getType();

                            List<WebArd> questionsDataList = (List<WebArd>) gsonc.fromJson(jsonCountryStaeObj.toString(), listType);

                            Log.e("getFeedbackData", questionsDataList.size() + "");


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        //  pDialog.dismiss();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Err", "Error: " + error.getMessage());
                //  pDialog.dismiss();
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
        MySingleton.getInstance(context).addToRequestQueue(req);
    }


    private void addFeedback(JSONObject params) {

        final ProgressDialog pDialog = new ProgressDialog(getContext());
        pDialog.setMessage("Loading...");
        pDialog.show();
        //   RequestQueue rq = Volley.newRequestQueue(getActivity().getApplicationContext());


        Log.e("addFeedback", Urls.addFeedback + "   " + params);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.PUT,
                Urls.addFeedback, params,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("re  addFeedback ", response + "");

                        try {
                            int responseid = response.getInt("id");

                            if (responseid >= 1) {
                                Toast.makeText(getContext(), "Feedback Submitted", Toast.LENGTH_SHORT).show();
                                mCompleteListener.onComplete("");
                                dialogFeedback.this.getDialog().cancel();
                            }
                            dialogFeedback.this.getDialog().cancel();

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


