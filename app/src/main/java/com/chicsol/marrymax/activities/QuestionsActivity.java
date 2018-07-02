package com.chicsol.marrymax.activities;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
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
import com.chicsol.marrymax.adapters.ParentAdapter;
import com.chicsol.marrymax.modal.Members;
import com.chicsol.marrymax.modal.WebArd;
import com.chicsol.marrymax.modal.mChild;
import com.chicsol.marrymax.modal.mMemList;
import com.chicsol.marrymax.modal.mParentChild;
import com.chicsol.marrymax.preferences.SharedPreferenceManager;
import com.chicsol.marrymax.urls.Urls;
import com.chicsol.marrymax.utils.Constants;
import com.chicsol.marrymax.utils.MySingleton;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class QuestionsActivity extends AppCompatActivity {
    private RecyclerView recyclerViewParent;
    ParentAdapter parentAdapter;
    ArrayList<mParentChild> parentChildObj;
    private String Tag = "QuestionsActivity";
    AppCompatButton ButtonSendQuestions;
    private String userpath = "";
    private Members member;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);

        userpath = getIntent().getStringExtra("userpath");
        initialize();
        setListeners();
    }


    private void initialize() {

        member = new Members();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarPhotoUpload);
        toolbar.setTitle("Ice-Break Questions");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ButtonSendQuestions = (AppCompatButton) findViewById(R.id.ButtonSendQuestions);

        recyclerViewParent = (RecyclerView) findViewById(R.id.RecyclerViewQuestions);


        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewParent.setLayoutManager(manager);
        recyclerViewParent.setHasFixedSize(true);

        parentAdapter = new ParentAdapter(this, new ArrayList<mParentChild>());
        recyclerViewParent.setAdapter(parentAdapter);

        JSONObject paramsm = new JSONObject();
        try {

            paramsm.put("path", SharedPreferenceManager.getUserObject(getApplicationContext()).get_path());
            paramsm.put("userpath", userpath);
            getMemberData(paramsm);
            ///Log.e("Q",""+params.toString());
            //  sendQuestion(params);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        getRequest();
    }

    private void setListeners() {
        ButtonSendQuestions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                // Toast.makeText(QuestionsActivity.this, "zzz " + Constants.selectedQuestions.size(), Toast.LENGTH_SHORT).show();
                if (Constants.selectedQuestions.size() > 0) {

                    // Log.e("ids are", getQuestionsIds());
                    JSONObject params = new JSONObject();
                    try {

                        params.put("path", SharedPreferenceManager.getUserObject(getApplicationContext()).get_path());
                        params.put("userpath", userpath);
                        params.put("alias", SharedPreferenceManager.getUserObject(getApplicationContext()).getAlias());
                        params.put("questionids", getQuestionsIds());
///Log.e("Q",""+params.toString());
                        sendQuestion(params);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                } else {

                    Toast.makeText(getApplicationContext(), "You have selected 0 question(s). You have to select at least 1 question", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }


    private String getQuestionsIds() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < Constants.selectedQuestions.size(); i++) {

            if (i != Constants.selectedQuestions.size() - 1) {
                stringBuilder.append(Constants.selectedQuestions.keyAt(i) + ",");
            } else {
                stringBuilder.append(Constants.selectedQuestions.keyAt(i));
            }
        }
        return stringBuilder.toString();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private ArrayList<mParentChild> createData() {
        parentChildObj = new ArrayList<>();
        ArrayList<mChild> list1 = new ArrayList<>();
        ArrayList<mChild> list2 = new ArrayList<>();
        ArrayList<mChild> list3 = new ArrayList<>();
        ArrayList<mChild> list4 = new ArrayList<>();
        ArrayList<mChild> list5 = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            mChild c1 = new mChild();
            c1.setName("Child 1." + (i + 1));
            list1.add(c1);
        }

        for (int i = 0; i < 5; i++) {
            mChild c2 = new mChild();
            c2.setName("Child 2." + (i + 1));
            list2.add(c2);
        }


        for (int i = 0; i < 2; i++) {
            mChild c3 = new mChild();
            c3.setName("Child 3." + (i + 1));
            list3.add(c3);
        }


        for (int i = 0; i < 4; i++) {
            mChild c4 = new mChild();
            c4.setName("Child 4." + (i + 1));
            list4.add(c4);
        }

        for (int i = 0; i < 2; i++) {
            mChild c5 = new mChild();
            c5.setName("Child 5." + (i + 1));
            list5.add(c5);
        }


        mParentChild pc1 = new mParentChild();
        pc1.setChild(list1);
        pc1.setTitle("c1");
        parentChildObj.add(pc1);

        mParentChild pc2 = new mParentChild();
        pc2.setTitle("c2");
        pc2.setChild(list2);
        parentChildObj.add(pc2);


        mParentChild pc3 = new mParentChild();
        pc3.setChild(list3);
        pc3.setTitle("c3");
        parentChildObj.add(pc3);

        mParentChild pc4 = new mParentChild();
        pc4.setChild(list4);
        pc4.setTitle("c4");
        parentChildObj.add(pc4);

        mParentChild pc5 = new mParentChild();
        pc5.setChild(list5);
        pc5.setTitle("c5");
        parentChildObj.add(pc5);


        return parentChildObj;
    }

    private void getRequest() {


  /*      final ProgressDialog pDialog = new ProgressDialog(getApplicationContext());
        pDialog.setMessage("Loading...");
        pDialog.show();*/
        Log.e("api path", "" + Urls.getQuestionAnswers + SharedPreferenceManager.getUserObject(getApplicationContext()).get_path());

        JsonArrayRequest req = new JsonArrayRequest(Urls.getQuestionAnswers,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.e("Response", response.toString());
                        try {

                            Log.e("array length is", response.length() + "");
                            JSONArray jsonCountryStaeObj = response.getJSONArray(0);


                            Gson gsonc;
                            GsonBuilder gsonBuilderc = new GsonBuilder();
                            gsonc = gsonBuilderc.create();
                            Type listType = new TypeToken<List<mMemList>>() {
                            }.getType();

                            List<mMemList> questDataList = (List<mMemList>) gsonc.fromJson(jsonCountryStaeObj.toString(), listType);
                            Log.e("MyCountryStateDataList", "" + questDataList.size());


                            parentChildObj = new ArrayList<>();

                            for (int i = 0; i < questDataList.size(); i++) {
                                mMemList objMem = questDataList.get(i);

                                mParentChild pc1 = new mParentChild();
                                pc1.setTitle(objMem.getName());
                                pc1.setId(objMem.getId());


                                ArrayList<mChild> MQList = new ArrayList<>();
                                /*    for (int j = 1; i < response.length(); j++) {*/


                            /*    mParentChild pc1 = new mParentChild();
                                pc1.setChild(list1);
                                pc1.setTitle("c1");
                                parentChildObj.add(pc1);*/


                                JSONArray jsonArraySub = response.getJSONArray(i + 1);


                                Gson gsonq;
                                GsonBuilder gsonBuilderq = new GsonBuilder();
                                gsonq = gsonBuilderq.create();
                                Type listTypeq = new TypeToken<List<mChild>>() {
                                }.getType();

                                MQList = (ArrayList<mChild>) gsonq.fromJson(jsonArraySub.toString(), listTypeq);
                                pc1.setChild(MQList);
                                parentChildObj.add(pc1);
                                //  Log.e("MQList", "" + MQList.size());

                                //  }

                            }

                            parentAdapter.addAll(parentChildObj);

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
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(req, Tag);
    }

    private void sendQuestion(JSONObject params) {

        final ProgressDialog pDialog = new ProgressDialog(QuestionsActivity.this);
        pDialog.setMessage("Loading...");
        pDialog.show();
        //   RequestQueue rq = Volley.newRequestQueue(getActivity().getApplicationContext());


        Log.e("Params " + Urls.sendQuestion, "" + params);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.PUT,
                Urls.sendQuestion, params,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("re  update appearance", response + "");
                        try {
                            int responseid = response.getInt("id");
                            if (responseid >= 0) {
                                Toast.makeText(QuestionsActivity.this, "Questions Sent", Toast.LENGTH_SHORT).show();
                                finish();
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
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjReq);

    }


    private void getMemberData(JSONObject params) {

        final ProgressDialog pDialog = new ProgressDialog(QuestionsActivity.this);
        pDialog.setMessage("Loading...");
        pDialog.show();
        //   RequestQueue rq = Volley.newRequestQueue(getActivity().getApplicationContext());


        Log.e("Params Member Data" + Urls.memberData, "" + params);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.PUT,
                Urls.memberData, params,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("re  update appearance", response + "");
                        try {
                            //JSONArray jaData = response.getJSONArray("data");

                            JSONObject responseObject = response.getJSONArray("data").getJSONArray(0).getJSONObject(0);
                            Gson gsonc;
                            GsonBuilder gsonBuilderc = new GsonBuilder();
                            gsonc = gsonBuilderc.create();
                            Type listType = new TypeToken<Members>() {
                            }.getType();

                            member = (Members) gsonc.fromJson(responseObject.toString(), listType);

                            //  Log.e("getAlias", "" + mObj.getAlias());


                        } catch (JSONException e) {
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
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjReq);

    }
}
