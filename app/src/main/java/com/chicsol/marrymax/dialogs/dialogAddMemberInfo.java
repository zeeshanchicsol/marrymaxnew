package com.chicsol.marrymax.dialogs;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.AppCompatButton;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.chicsol.marrymax.modal.mMemDetail;
import com.chicsol.marrymax.preferences.SharedPreferenceManager;
import com.chicsol.marrymax.urls.Urls;
import com.chicsol.marrymax.utils.Constants;
import com.chicsol.marrymax.utils.MySingleton;
import com.chicsol.marrymax.widgets.faTextView;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;


/**
 * Created by zeedr on 24/10/2016.
 */

public class dialogAddMemberInfo extends DialogFragment {
    int member_notes_id = 0;
    // private String userpath;
    EditText etNotes;
    private ListView lv_mycontacts;
    private AppCompatButton btSave;
    //  private ImageView ivDeleteNotes;

    private EditText etMemInfoResidenceDetails, etMemInfoAboutParents, etMemInfoAboutSiblings, etMemInfoJobDetails, etMemInfoEducationDetail, etMemInfoSocialDetail;
    private onCompleteListener mCompleteListener;


    public static dialogAddMemberInfo newInstance(String userpath) {

        dialogAddMemberInfo frag = new dialogAddMemberInfo();
        Bundle args = new Bundle();

        //  args.putString("userpath", userpath);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle mArgs = getArguments();
        //    userpath = mArgs.getString("userpath");
        ///  Log.e("json data", myValue);


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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.dialog_add_member_info, container, false);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);


        etMemInfoResidenceDetails = (EditText) rootView.findViewById(R.id.EditTextMemInfoResidenceDetails);
        etMemInfoAboutParents = (EditText) rootView.findViewById(R.id.EditTextMemInfoAboutParents);
        etMemInfoAboutSiblings = (EditText) rootView.findViewById(R.id.EditTextMemInfoAboutSiblings);
        etMemInfoJobDetails = (EditText) rootView.findViewById(R.id.EditTextMemInfoJobDetails);
        etMemInfoEducationDetail = (EditText) rootView.findViewById(R.id.EditTextMemInfoEducationDetail);
        etMemInfoSocialDetail = (EditText) rootView.findViewById(R.id.EditTextMemInfoSocialDetail);


        //etNotes = (EditText) rootView.findViewById(R.id.EditTextMemberNotes);
        btSave = (AppCompatButton) rootView.findViewById(R.id.ButtonNotesSave);
        // ivDeleteNotes = (ImageView) rootView.findViewById(R.id.ImageViewDeleteNotes);
        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!TextUtils.isEmpty(etMemInfoResidenceDetails.getText().toString().trim()) || !TextUtils.isEmpty(etMemInfoAboutParents.getText().toString().trim()) || !TextUtils.isEmpty(etMemInfoAboutSiblings.getText().toString().trim())
                        || !TextUtils.isEmpty(etMemInfoJobDetails.getText().toString().trim()) || !TextUtils.isEmpty(etMemInfoEducationDetail.getText().toString().trim()) || !TextUtils.isEmpty(etMemInfoSocialDetail.getText().toString().trim())) {


                    mMemDetail memDetailObj = new mMemDetail();
                    memDetailObj.setResidence(etMemInfoResidenceDetails.getText().toString().trim());
                    memDetailObj.setParents(etMemInfoAboutParents.getText().toString().trim());
                    memDetailObj.setJobinfo(etMemInfoJobDetails.getText().toString().trim());
                    memDetailObj.setEducation(etMemInfoEducationDetail.getText().toString().trim());
                    memDetailObj.setSocial(etMemInfoSocialDetail.getText().toString().trim());


                    try {
                        Gson gson = new Gson();
                        String objparamsa = gson.toJson(memDetailObj);
                        JSONObject params = new JSONObject(objparamsa);
                        params.put("path", SharedPreferenceManager.getUserObject(getContext()).getPath());
                        saveMemberInfo(params);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                } else {
                    Toast.makeText(getContext(), "Please enter some information!", Toast.LENGTH_SHORT).show();
                }

            }
        });




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


        faTextView cancelButton = (faTextView) rootView.findViewById(R.id.dismissBtnGeoInfo);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(final View v) {
                //Toast.makeText(getActivity().getApplicationContext(), "clcieck", Toast.LENGTH_SHORT).show();
                dialogAddMemberInfo.this.getDialog().cancel();
            }
        });


        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();

        getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

/*    private void getNotes(String userpath) {

        final ProgressDialog pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Loading...");
        pDialog.show();

        JSONObject params = new JSONObject();
        try {

            params.put("userpath", userpath);
            params.put("path", SharedPreferenceManager.getUserObject(getContext()).getPath());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.e("params" + "  " + Urls.membersNotes, "" + params);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.PUT,
                Urls.membersNotes, params,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("re  update notes ", response + "");
                        try {
                            JSONObject responseObject = response.getJSONArray("data").getJSONArray(0).getJSONObject(0);
                            String notes = responseObject.getString("notes");
                            member_notes_id = responseObject.getInt("member_notes_id");
                            if (member_notes_id != 0) {
                                ivDeleteNotes.setVisibility(View.VISIBLE);
                                etNotes.setText(notes);
                            } else {
                                ivDeleteNotes.setVisibility(View.GONE);
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

    }*/

    private void saveMemberInfo(JSONObject params) {

        final ProgressDialog pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Loading...");
        pDialog.show();


        //Log.e("params" + "  " + Urls.memberInfo, "" + params);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.PUT,
                Urls.memberInfo, params,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        //Log.e("re  update notes ", response + "");
                        try {
                            int responseid = response.getInt("id");
                            if (responseid > 0) {
                                member_notes_id = responseid;
                                Toast.makeText(getContext(), "Saved", Toast.LENGTH_SHORT).show();
                                mCompleteListener.onComplete("");
                            }
                            dialogAddMemberInfo.this.getDialog().cancel();

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


