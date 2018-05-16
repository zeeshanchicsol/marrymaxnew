package com.chicsol.marrymax.fragments.list;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.chicsol.marrymax.R;
import com.chicsol.marrymax.adapters.RecyclerViewAdapterRecomendedMembers;
import com.chicsol.marrymax.modal.mRecommended;
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
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Android on 11/3/2016.
 */

public class RecommendedMatches extends Fragment implements RecyclerViewAdapterRecomendedMembers.OnUpdateListener {
    public static int result = 0;
    LinearLayout LinearLayoutMMMatchesNotFound;
    Context activity;
    AppCompatButton btSearch;
    private
    RecyclerView recyclerView;
    private List<mRecommended> membersDataList;
    private Fragment fragment;
    private RecyclerViewAdapterRecomendedMembers
            recyclerAdapter;
    private ProgressBar pDialog;
    private LinearLayout llEmptyState;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_list_my_recommended_members, container, false);

        initilize(rootView);


        return rootView;
    }

    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        this.activity = activity;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onResume() {
        super.onResume();
        if (ConnectCheck.isConnected(getActivity().findViewById(android.R.id.content))) {

            JSONObject params = new JSONObject();
            try {
                params.put("path", SharedPreferenceManager.getUserObject(getContext()).get_path());
                params.put("phone_verified", SharedPreferenceManager.getUserObject(getContext()).get_phone_verified());
                params.put("email_verified", SharedPreferenceManager.getUserObject(getContext()).get_email_verified());
                params.put("member_status", SharedPreferenceManager.getUserObject(getContext()).get_member_status());

                loadData(params.toString(), false);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    private void initilize(View view) {
        fragment = RecommendedMatches.this;


        // ivEmptyState = (ImageView) view.findViewById(R.id.ImageViewMyContactsEmptyState);
//        ivEmptyState.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_empty_state_new_interest_received));
        //  tvInterestRequestEmptyState = (TextView) view.findViewById(R.id.TextViewMyContactsEmptyState);
        // tvInterestRequestEmptyState.setText("You have 0 interests");

        llEmptyState = (LinearLayout) view.findViewById(R.id.LinearLayoutRecommendedMembersNotFound);


        membersDataList = new ArrayList<>();
        pDialog = (ProgressBar) view.findViewById(R.id.ProgressbarRecommendedMembers);
        //   pDialog.setMessage("Loading...");
        //   swipeRefresh = (SwipeRefreshLayout) view.findViewById(R.id.SwipeRefreshMyContacts);
        LinearLayoutMMMatchesNotFound = (LinearLayout) view.findViewById(R.id.LinearLayoutRecommendedMembersNotFound);
        recyclerView = (RecyclerView) view.findViewById(R.id.RecyclerViewRecomendedMembers);


        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);

        recyclerAdapter = new RecyclerViewAdapterRecomendedMembers(getContext(), getFragmentManager(), fragment, RecommendedMatches.this);


        recyclerAdapter.setLinearLayoutManager(mLayoutManager);

        recyclerAdapter.setRecyclerView(recyclerView);

        recyclerView.setAdapter(recyclerAdapter);
        //  swipeRefresh.setOnRefreshListener(this);


        setListeners();
    }

    private void
    setListeners() {


    }


    private void loadData(String paramsString, final boolean refresh) {


        pDialog.setVisibility(View.VISIBLE);
        //   RequestQueue rq = Volley.newRequestQueue(getActivity().getApplicationContext());

        JSONObject params = null;
        try {
            params = new JSONObject(paramsString);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.e("Params contacts" + " " + Urls.recommendList, "" + params);

        //  Log.e("Params search" + " " + Urls.searchProfiles, "");
        final JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.PUT,
                Urls.recommendList, params,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("recommendList  recommendList ", response + "");
                        try {
                            JSONArray jsonArray = response.getJSONArray("data");

                            if (jsonArray.length() > 0) {

                                Log.e("----------------Length", jsonArray.getJSONArray(0).length() + "");
                                JSONArray jsonarrayData = jsonArray.getJSONArray(0);


                                if (jsonarrayData.length() == 0) {

                                    recyclerAdapter.clear();
                                    // swipeRefresh.setRefreshing(false);
                                    recyclerView.setVisibility(View.GONE);
                                    llEmptyState.setVisibility(View.VISIBLE);

                                } else {


                                    Gson gson;
                                    GsonBuilder gsonBuilder = new GsonBuilder();
                                    gson = gsonBuilder.create();
                                    Type member = new TypeToken<List<mRecommended>>() {
                                    }.getType();


                                    membersDataList = (List<mRecommended>) gson.fromJson(jsonarrayData.toString(), member);
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
                                        llEmptyState.setVisibility(View.VISIBLE);
                                    }


                                }

                            } else {
                                recyclerAdapter.clear();
                                llEmptyState.setVisibility(View.VISIBLE);
                                //no data
                                //  swipeRefresh.setRefreshing(false);
                                //  LinearLayoutMMMatchesNotFound.setVisibility(View.VISIBLE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (!refresh) {
                            // pDialog.hide();

                            pDialog.setVisibility(View.GONE);
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {


                VolleyLog.e("res err", "Error: " + error);
                if (!refresh) {
                    //pDialog.hide();
                    pDialog.setVisibility(View.GONE);
                    llEmptyState.setVisibility(View.VISIBLE);
                }
                // LinearLayoutMMMatchesNotFound.setVisibility(View.VISIBLE);
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


    @Override
    public void onUpdate(String msg) {

        if (ConnectCheck.isConnected(getActivity().findViewById(android.R.id.content))) {
            JSONObject params = new JSONObject();
            try {
                params.put("path", SharedPreferenceManager.getUserObject(getContext()).get_path());
                params.put("phone_verified", SharedPreferenceManager.getUserObject(getContext()).get_phone_verified());
                params.put("email_verified", SharedPreferenceManager.getUserObject(getContext()).get_email_verified());
                params.put("member_status", SharedPreferenceManager.getUserObject(getContext()).get_member_status());


                loadData(params.toString(), false);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }


}
