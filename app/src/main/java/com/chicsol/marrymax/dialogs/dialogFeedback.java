package com.chicsol.marrymax.dialogs;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.AppCompatRatingBar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.chicsol.marrymax.adapters.RecyclerViewAdapterFeedbackQuestions;
import com.chicsol.marrymax.adapters.RecyclerViewAdapterQuestionsList;
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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class dialogFeedback extends DialogFragment {

    public onCompleteListener mCompleteListener;
    String userpath, jsarray;
    mLfm lfm;
    EditText etFeedback;
    AppCompatRatingBar mRbar;
    Context context;
    RecyclerView recyclerView;
    List<WebArd> questionsDataList;
    private RecyclerViewAdapterFeedbackQuestions recyclerAdapter;
    private String id = "";


    public static dialogFeedback newInstance(String userpath, String id) {

        Gson gson = new Gson();
        dialogFeedback frag = new dialogFeedback();
        Bundle args = new Bundle();
        args.putString("userpath", userpath);
        args.putString("id", id);
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
        Bundle mArgs = getArguments();
        userpath = mArgs.getString("userpath");
        id = mArgs.getString("id");

      /*  Gson gson = new Gson();


        jsarray = mArgs.getString("obj");
        lfm = gson.fromJson(jsarray, mLfm.class);*/
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.dialog_new_feedback, container, false);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);


        getFeedbackData();


        mRbar = (AppCompatRatingBar) rootView.findViewById(R.id.dialog_ratingbar);


        etFeedback = (EditText) rootView.findViewById(R.id.EditTextMatchAidFeedbackText);


        recyclerView = (RecyclerView) rootView.findViewById(R.id.RecyclerViewFeedbackQuestions);

        recyclerView.setHasFixedSize(true);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());

        // mLayoutManager.setStackFromEnd(true);
        // mLayoutManager.setReverseLayout(true);
        recyclerView.setLayoutManager(mLayoutManager);

        recyclerAdapter = new RecyclerViewAdapterFeedbackQuestions(context);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1));
        recyclerView.setAdapter(recyclerAdapter);
        //  recyclerAdapter.setOnItemClickListener(dialogFeedback.this);


        Button mOkButton = (Button) rootView.findViewById(R.id.mButtonDialogMatchAidUPViewProgress);
        mOkButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(final View v) {


                if (!checkFieldsSelection(v)) {

                    String fb = etFeedback.getText().toString();


                   /* path
                            userpath
                    text         (notes)
                    id2          (rating)
                    type        (question:answers,)   e.g  1:1,2:0,3:0,4:1
*/
                    StringBuilder anserString = new StringBuilder();
                    HashMap<String, String> ansList = recyclerAdapter.getmCheckedAnswersList();

                    Iterator it = ansList.entrySet().iterator();
                    while (it.hasNext()) {
                        Map.Entry pair = (Map.Entry) it.next();
                        // System.out.println(pair.getKey() + " = " + pair.getValue());

                        if (it.hasNext()) {
                            anserString.append(pair.getKey() + ":" + pair.getValue() + ",");
                        } else {
                            anserString.append(pair.getKey() + ":" + pair.getValue());
                        }


                        // it.remove(); // avoids a ConcurrentModificationException
                    }


                    JSONObject params = new JSONObject();
                    try {
                        params.put("path", SharedPreferenceManager.getUserObject(context).get_path());
                        params.put("userpath", userpath);
                        params.put("text", fb);
                        params.put("id2", mRbar.getNumStars());

                        params.put("type", anserString.toString());


                        Log.e("addFeedback", Urls.updFeedback + "   " + params);
                        addFeedback(params);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }


                //Log.e("stars", mRbar.getRating() + "" + etFeedback.getText().toString());


       /*         if (!checkSelections()) {
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

                }*/
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

    private boolean checkFieldsSelection(View v) {
        boolean ck = false;
        if (!TextUtils.isEmpty(etFeedback.getText().toString().trim())) {
            if (etFeedback.getText().length() > 200) {
                etFeedback.setError(" max 200 char");

                etFeedback.requestFocus();
                ck = true;

            }

        } else {
            Toast.makeText(context, "Please enter the reason", Toast.LENGTH_SHORT).show();

        }
        if (mRbar.getRating() >= 1) {

        } else {
            ck = true;
            Toast.makeText(context, "Please rate your feedback", Toast.LENGTH_SHORT).show();
        }

        if (recyclerAdapter.getmCheckedAnswersList().size() != questionsDataList.size()) {
            ck = true;
            Toast.makeText(context, "Please Select All Options", Toast.LENGTH_SHORT).show();
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

        JsonArrayRequest req = new JsonArrayRequest(Urls.getFeedbackData+"/"+id,
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

                            questionsDataList = (List<WebArd>) gsonc.fromJson(jsonCountryStaeObj.toString(), listType);
                            recyclerAdapter.addAll(questionsDataList);
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


        Log.e("updFeedback", Urls.updFeedback + "   " + params);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.PUT,
                Urls.updFeedback, params,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("re  addFeedback ", response + "");

                        try {
                            int responseid = response.getInt("id");

                            if (responseid > 0) {
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


