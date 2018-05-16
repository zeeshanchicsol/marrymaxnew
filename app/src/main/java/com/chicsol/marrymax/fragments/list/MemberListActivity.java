package com.chicsol.marrymax.fragments.list;
/**
 * Created by macintoshhd on 12/4/17.
 */

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.chicsol.marrymax.R;
import com.chicsol.marrymax.activities.search.SearchMainActivity;
import com.chicsol.marrymax.adapters.RecyclerViewAdapterMemberList;
import com.chicsol.marrymax.modal.mMemListDetail;
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

public class MemberListActivity extends AppCompatActivity implements RecyclerViewAdapterMemberList.OnUpdateListener {
    public static int result = 0;
    LinearLayout LinearLayoutMMMatchesNotFound;
    //private Button bt_loadmore;
    private
    RecyclerView recyclerView;
    private int lastPage = 1;
    private List<mMemListDetail> membersDataList;
    private int totalPages = 0;
    // private int currentPage=1;
    private Fragment fragment;
    private RecyclerViewAdapterMemberList
            recyclerAdapter;
    private ProgressBar pDialog;
    // private SwipeRefreshLayout swipeRefresh;
    private String params;
    private static boolean m_iAmVisible;
    Context activity;
    String userpath, id, count;
    AppCompatButton btSearch;
    // private ImageView ivEmptyState;
    private LinearLayout llEmptyState;
    private TextView tvInterestRequestEmptyState;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar1);
        toolbar.setTitle(" Members List");

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


       // userpath = getIntent().getStringExtra("userpath");
        id = getIntent().getStringExtra("id");
        count = getIntent().getStringExtra("count");
        initilize();
    }

    /*   @Override
       public void fragmentBecameVisible() {

           // You can do your animation here because we are visible! (make sure onViewCreated has been called too and the Layout has been laid. Source for another question but you get the idea.
       }*/


    @Override
    public void onResume() {
        super.onResume();
        if (ConnectCheck.isConnected(findViewById(android.R.id.content))) {

            JSONObject params = new JSONObject();
            try {
                params.put("path", SharedPreferenceManager.getUserObject(getApplicationContext()).get_path());
                params.put("userpath", userpath);
                params.put("id", id);

                loadData(params.toString(), false);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }


    private void initilize() {


        // ivEmptyState = (ImageView) view.findViewById(R.id.ImageViewMyContactsEmptyState);
//        ivEmptyState.setImageDrawable(getApplicationContext().getResources().getDrawable(R.drawable.ic_empty_state_new_interest_received));
        //  tvInterestRequestEmptyState = (TextView) view.findViewById(R.id.TextViewMyContactsEmptyState);
        // tvInterestRequestEmptyState.setText("You have 0 interests");

        llEmptyState = (LinearLayout) findViewById(R.id.LinearLayoutMMMatchesNotFound);


        membersDataList = new ArrayList<>();
        pDialog = (ProgressBar) findViewById(R.id.ProgressbarMyContacts);
        //   pDialog.setMessage("Loading...");
        //   swipeRefresh = (SwipeRefreshLayout) view.findViewById(R.id.SwipeRefreshMyContacts);
        LinearLayoutMMMatchesNotFound = (LinearLayout) findViewById(R.id.LinearLayoutMMMatchesNotFound);
        recyclerView = (RecyclerView) findViewById(R.id.RecyclerViewMyContacts);


        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);

        recyclerAdapter = new RecyclerViewAdapterMemberList(getApplicationContext(), getSupportFragmentManager(), null, MemberListActivity.this, userpath, id, MemberListActivity.this);


        recyclerAdapter.setLinearLayoutManager(mLayoutManager);

        recyclerAdapter.setRecyclerView(recyclerView);

        recyclerView.setAdapter(recyclerAdapter);
        //  swipeRefresh.setOnRefreshListener(this);

        btSearch = (AppCompatButton) findViewById(R.id.ButtonMyContatsSearch);
        setListeners();
    }

    private void
    setListeners() {


        btSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MemberListActivity.this, SearchMainActivity.class);
                startActivity(intent);
            }
        });
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

        Log.e("Params membersList" + " " + Urls.membersList, "" + params);

        //  Log.e("Params search" + " " + Urls.searchProfiles, "");
        final JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.PUT,
                Urls.membersList, params,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("re  update contacts", response + "");
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
                                    Type member = new TypeToken<List<mMemListDetail>>() {
                                    }.getType();


                                    membersDataList = (List<mMemListDetail>) gson.fromJson(jsonarrayData.toString(), member);
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
                                LinearLayoutMMMatchesNotFound.setVisibility(View.VISIBLE);
                                //  swipeRefresh.setRefreshing(false);
                                //  LinearLayoutMMMatchesNotFound.setVisibility(View.VISIBLE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            recyclerAdapter.clear();
                        }
                        if (!refresh) {
                            // pDialog.hide();
                            pDialog.setVisibility(View.GONE);
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                recyclerAdapter.clear();

                VolleyLog.e("res err", "Error: " + error);
                if (!refresh) {
                    //pDialog.hide();
                    pDialog.setVisibility(View.GONE);
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
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjReq);

    }


    @Override
    public void onUpdate(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
        if (ConnectCheck.isConnected(findViewById(android.R.id.content))) {
            JSONObject params = new JSONObject();
            try {
                params.put("path", SharedPreferenceManager.getUserObject(getApplicationContext()).get_path());
                params.put("userpath", userpath);
                params.put("id", id);
                loadData(params.toString(), false);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}




