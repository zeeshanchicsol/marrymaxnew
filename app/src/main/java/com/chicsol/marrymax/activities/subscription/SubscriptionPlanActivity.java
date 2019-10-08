package com.chicsol.marrymax.activities.subscription;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.chicsol.marrymax.R;
import com.chicsol.marrymax.activities.UserProfileActivityFragment;
import com.chicsol.marrymax.adapters.RecyclerViewAdapterAmenities;
import com.chicsol.marrymax.adapters.RecyclerViewAdapterRecomendedMembers;
import com.chicsol.marrymax.adapters.RecyclerViewAdapterSubscriptionPlan;
import com.chicsol.marrymax.dialogs.dialogInstallmentPlan;
import com.chicsol.marrymax.dialogs.dialogMatchAid;
import com.chicsol.marrymax.modal.Subscription;
import com.chicsol.marrymax.modal.mInstallment;
import com.chicsol.marrymax.other.MarryMax;
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

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SubscriptionPlanActivity extends AppCompatActivity implements RecyclerViewAdapterRecomendedMembers.OnUpdateListener {
    public static int result = 0;
    LinearLayout LinearLayoutMMMatchesNotFound;

    private
    RecyclerView recyclerView;

    private List<Subscription> membersDataList;


    private Fragment fragment;
    private RecyclerViewAdapterSubscriptionPlan
            recyclerAdapter;
    private ProgressBar pDialog;
    TextView tvPackageDetails;
    Context activity;
    private RecyclerView rvAmenities;
    private RecyclerViewAdapterAmenities adapterAmenities;
    // AppCompatButton btSearch;
    private AppCompatButton btInstallmentPlan;
    private LinearLayout llEmptyState;
    // private AppCompatButton packageDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscription_plan);
        initilize();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (ConnectCheck.isConnected(findViewById(android.R.id.content))) {

            loadData();



    /*        JSONObject params = new JSONObject();
            try {
                params.put("path", SharedPreferenceManager.getUserObject(getApplicationContext()).getPath());
                params.put("phone_verified", SharedPreferenceManager.getUserObject(getApplicationContext()).getPhone_verified());
                params.put("member_status", SharedPreferenceManager.getUserObject(getApplicationContext()).getMember_status());

                loadData(params.toString(), false);

            } catch (JSONException e) {
                e.printStackTrace();
            }*/
        }

    }

    private void initilize() {


        btInstallmentPlan = (AppCompatButton) findViewById(R.id.ButtonInstallmentPlan);

        //  packageDetails = (AppCompatButton) findViewById(R.id.SubscriptionPlanPackageDetails);
        tvPackageDetails = (TextView) findViewById(R.id.TextViewSubscriptionPlanPackages);

        getSupportActionBar().setTitle("Subscription Packages");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        llEmptyState = (LinearLayout) findViewById(R.id.LinearLayoutSubscriptionPlanNotFound);


        membersDataList = new ArrayList<>();
        pDialog = (ProgressBar) findViewById(R.id.ProgressbarSubscriptionPlan);
        //   pDialog.setMessage("Loading...");
        //   swipeRefresh = (SwipeRefreshLayout) view.findViewById(R.id.SwipeRefreshMyContacts);
        LinearLayoutMMMatchesNotFound = (LinearLayout) findViewById(R.id.LinearLayoutSubscriptionPlanNotFound);
        recyclerView = (RecyclerView) findViewById(R.id.RecyclerViewSubscriptionPlan);


        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);

        recyclerAdapter = new RecyclerViewAdapterSubscriptionPlan(getApplicationContext(), null, null, null);


        recyclerAdapter.setLinearLayoutManager(mLayoutManager);

        recyclerAdapter.setRecyclerView(recyclerView);

        recyclerView.setAdapter(recyclerAdapter);


        rvAmenities = (RecyclerView) findViewById(R.id.RecyclerViewOptions);

        rvAmenities.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));

        List<String> featuresList = new ArrayList<String>();

        featuresList.add("Personalized Assistance");
        featuresList.add("View Verfied Phone Numbers");
        featuresList.add("Send Direct Messages");
        featuresList.add("Maxmum Privacy Options");
        featuresList.add("Priority Profile Listing");
        featuresList.add("And many more");

        adapterAmenities = new RecyclerViewAdapterAmenities(featuresList, getApplicationContext());
        // adapterAmenities.setOnItemClickListener(this);
        rvAmenities.setAdapter(adapterAmenities);


        //  swipeRefresh.setOnRefreshListener(this);

        //  btSearch = (AppCompatButton) findViewById(R.id.ButtonMyContatsSearch);
        setListeners();


    }

    private void
    setListeners() {


        btInstallmentPlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadInstallmentData();
            }
        });






    /*    packageDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               *//* Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(Urls.subscriptionUrl));
                startActivity(browserIntent);
*//*
                MarryMax marryMax = new MarryMax(SubscriptionPlanActivity.this);
                marryMax.benefits();
            }
        });*/

        tvPackageDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MarryMax marryMax = new MarryMax(SubscriptionPlanActivity.this);
                marryMax.benefits();
            }
        });

     /*   btSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SubscriptionPlanActivity.this, SearchMainActivity.class);
                startActivity(intent);
            }
        });*/
    }


    private void loadInstallmentData() {
        pDialog.setVisibility(View.VISIBLE);


        Log.e("getUsrInstPym", "" + Urls.getUsrInstPym + SharedPreferenceManager.getUserObject(getApplicationContext()).getPath());

        JsonArrayRequest req = new JsonArrayRequest(Urls.getUsrInstPym + SharedPreferenceManager.getUserObject(getApplicationContext()).getPath(),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.e("Response", response.toString());
                        try {
                            JSONArray jsonarrayData = response.getJSONArray(0);

                            //Log.e("json length", jsonarrayData.length() + "");

                            if (jsonarrayData.length() > 0) {


                                Gson gson;
                                GsonBuilder gsonBuilder = new GsonBuilder();
                                gson = gsonBuilder.create();
                                Type member = new TypeToken<List<mInstallment>>() {
                                }.getType();


                                List<mInstallment> installment = (List<mInstallment>) gson.fromJson(jsonarrayData.toString(), member);
                                if (installment.size() > 0) {

                                    dialogInstallmentPlan newFragment = dialogInstallmentPlan.newInstance(response.toString());
                                 //   newFragment.setTargetFragment(SubscriptionPlanActivity.this, 0);
                                    newFragment.show(getSupportFragmentManager(), "dialog");


                                }


                            }
                        } catch (JSONException e) {
                            e.printStackTrace();


                        }
                       /* if (!refresh) {
                            // pDialog.dismiss();
                            pDialog.setVisibility(View.GONE);
                        }*/
                        pDialog.setVisibility(View.GONE);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Err", "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(), "Contact MarryMax Support For Installments", Toast.LENGTH_LONG).show();
                pDialog.setVisibility(View.GONE);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return Constants.getHashMap();
            }
        };
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(req);
    }


    private void loadData() {
        pDialog.setVisibility(View.VISIBLE);


        //Log.e("getSubscriptions", "" + Urls.getSubscriptions + SharedPreferenceManager.getUserObject(getApplicationContext()).getPath());

        JsonArrayRequest req = new JsonArrayRequest(Urls.getSubscriptions + SharedPreferenceManager.getUserObject(getApplicationContext()).getPath(),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        //Log.d("Response", response.toString());
                        try {
                            JSONArray jsonarrayData = response.getJSONArray(0);

                            //Log.e("json length", jsonarrayData.length() + "");

                            if (jsonarrayData.length() > 0) {


                                if (jsonarrayData.length() == 0) {

                                    recyclerAdapter.clear();
                                    // swipeRefresh.setRefreshing(false);
                                    recyclerView.setVisibility(View.GONE);
                                    llEmptyState.setVisibility(View.VISIBLE);

                                } else {


                                    Gson gson;
                                    GsonBuilder gsonBuilder = new GsonBuilder();
                                    gson = gsonBuilder.create();
                                    Type member = new TypeToken<List<Subscription>>() {
                                    }.getType();


                                    membersDataList = (List<Subscription>) gson.fromJson(jsonarrayData.toString(), member);
                                    if (membersDataList.size() > 0) {
                                        LinearLayoutMMMatchesNotFound.setVisibility(View.GONE);
                                        recyclerAdapter.addAll(membersDataList);


                                        //      Log.e("Length=================", membersDataList.size() + "  ");

/*
                                        Gson gsont;
                                        GsonBuilder gsonBuildert = new GsonBuilder();
                                        gsont = gsonBuildert.create();
                                        Type membert = new TypeToken<mCommunication>() {
                                        }.getType();*/
                                        //                                      mContacts memberTotalPages = (mContacts) gson.fromJson(jsonarrayTotalPages.getJSONObject(0).toString(), membert);

/*
                                        totalPages = (int) memberTotalPages.getInterested_members_count();
                                        lastPage = 1;
                                        //      Log.e("total pages", "" + totalPages);
                                        swipeRefresh.setRefreshing(false);*/
                                    } else {
                                        recyclerAdapter.clear();
                                        // swipeRefresh.setRefreshing(false);
                                        LinearLayoutMMMatchesNotFound.setVisibility(View.VISIBLE);
                                    }


                                }

                            } else {
                                recyclerAdapter.clear();
                                //no data
                                //  swipeRefresh.setRefreshing(false);
                                //  LinearLayoutMMMatchesNotFound.setVisibility(View.VISIBLE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                       /* if (!refresh) {
                            // pDialog.dismiss();
                            pDialog.setVisibility(View.GONE);
                        }*/
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
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(req);
    }


    @Override
    public void onUpdate(String msg) {

        if (ConnectCheck.isConnected(SubscriptionPlanActivity.this.findViewById(android.R.id.content))) {

        }

    }


}
