package com.chicsol.marrymax.dialogs;


import android.app.ProgressDialog;
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
import com.chicsol.marrymax.fragments.inbox.DashboardMessagesDetailActivity;
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

public class dialogAddNotes extends DialogFragment {
    int member_notes_id = 0;
    private String userpath;
    EditText etNotes;
    private ListView lv_mycontacts;
    private AppCompatButton btSave;
    private ImageView ivDeleteNotes;

    public static dialogAddNotes newInstance(String userpath) {

        dialogAddNotes frag = new dialogAddNotes();
        Bundle args = new Bundle();
        args.putString("name", "Do's & Don't");
        args.putString("userpath", userpath);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle mArgs = getArguments();
        userpath = mArgs.getString("userpath");
        ///  Log.e("json data", myValue);


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.dialog_add_notes, container, false);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);


        etNotes = (EditText) rootView.findViewById(R.id.EditTextMemberNotes);
        btSave = (AppCompatButton) rootView.findViewById(R.id.ButtonNotesSave);
        ivDeleteNotes = (ImageView) rootView.findViewById(R.id.ImageViewDeleteNotes);
        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!TextUtils.isEmpty(etNotes.getText().toString().trim())) {
                    if (etNotes.getText().toString().trim().length() < 2) {
                        Toast.makeText(getContext(), "Min 5  characters are allowed", Toast.LENGTH_SHORT).show();
                    } else {
                        saveNotes();
                    }
                } else {
                    Toast.makeText(getContext(), "Please Enter Notes to Save", Toast.LENGTH_SHORT).show();
                }

            }
        });
        ivDeleteNotes.setImageResource(R.drawable.ic_delete_forever_black_24dp);
        ivDeleteNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                deleteNotes();
            }
        });
        getNotes(userpath);


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
                dialogAddNotes.this.getDialog().cancel();
            }
        });


        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();

        getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    private void getNotes(String userpath) {

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

        //Log.e("params" + "  " + Urls.membersNotes, "" + params);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.PUT,
                Urls.membersNotes, params,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        //Log.e("re  update notes ", response + "");
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

    }

    private void saveNotes() {

        final ProgressDialog pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Loading...");
        pDialog.show();

        JSONObject params = new JSONObject();
        try {

            params.put("message", etNotes.getText().toString());
            params.put("message_id", member_notes_id + "");
            params.put("userpath", userpath);
            params.put("path", SharedPreferenceManager.getUserObject(getContext()).getPath());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //Log.e("params" + "  " + Urls.sendNotes, "" + params);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.PUT,
                Urls.sendNotes, params,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        //Log.e("re  update notes ", response + "");
                        try {
                            int responseid = response.getInt("id");
                            if (responseid >= 1) {
                                member_notes_id = responseid;
                                Toast.makeText(getContext(), "Saved", Toast.LENGTH_SHORT).show();
                                dialogAddNotes.this.getDialog().cancel();
                            }
                            dialogAddNotes.this.getDialog().cancel();

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


    private void deleteNotes() {

        final ProgressDialog pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Loading...");
        pDialog.show();

        JSONObject params = new JSONObject();
        try {


            params.put("id", member_notes_id + "");
            params.put("path", SharedPreferenceManager.getUserObject(getContext()).getPath());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //Log.e("params" + "  " + Urls.deleteNotes, "" + params);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.PUT,
                Urls.deleteNotes, params,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        //Log.e("re  update notes ", response + "");
                        try {
                            int responseid = response.getInt("id");
                            if (responseid >= 1) {
                                member_notes_id = responseid;
                                Toast.makeText(getContext(), "Notes Deleted", Toast.LENGTH_SHORT).show();
                                dialogAddNotes.this.getDialog().cancel();
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
}


