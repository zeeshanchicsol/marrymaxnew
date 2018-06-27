package com.chicsol.marrymax.activities;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.SparseBooleanArray;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.chicsol.marrymax.R;
import com.chicsol.marrymax.adapters.ParentAdapter;
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
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class QuestionsActivity extends AppCompatActivity {
    private RecyclerView recyclerViewParent;
    ParentAdapter parentAdapter;
    ArrayList<mParentChild> parentChildObj;
    private String Tag = "QuestionsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);
        initialize();
    }


    private void initialize() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarPhotoUpload);
        toolbar.setTitle("Ice-Break Questions");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        recyclerViewParent = (RecyclerView) findViewById(R.id.RecyclerViewQuestions);


        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewParent.setLayoutManager(manager);
        recyclerViewParent.setHasFixedSize(true);

        parentAdapter = new ParentAdapter(this, new ArrayList<mParentChild>());
        recyclerViewParent.setAdapter(parentAdapter);

        getRequest();
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



                            parentChildObj=new ArrayList<>();

                            for (int i = 0; i < questDataList.size(); i++) {
                                mMemList objMem = questDataList.get(i);

                                mParentChild pc1 = new mParentChild();
                                pc1.setTitle(objMem.getName());


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
}
