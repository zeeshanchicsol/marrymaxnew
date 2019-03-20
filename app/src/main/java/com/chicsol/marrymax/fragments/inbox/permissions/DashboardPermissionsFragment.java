package com.chicsol.marrymax.fragments.inbox.permissions;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.chicsol.marrymax.R;
import com.chicsol.marrymax.adapters.RecyclerViewAdapterPermissions;
import com.chicsol.marrymax.modal.mPermission;
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

/**
 * Created by Android on 11/3/2016.
 */

public class DashboardPermissionsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, RecyclerViewAdapterPermissions.OnUpdateListener {
    public static int result = 0;
    // LinearLayout LinearLayoutMMMatchesNotFound;
    //private Button bt_loadmore;
    private
    RecyclerView recyclerView;
    private int lastPage = 1;
    private List<mPermission> dataList;
    private int totalPages = 0;
    // private int currentPage=1;
    private Fragment fragment;
    private RecyclerViewAdapterPermissions
            recyclerAdapter;
    private ProgressBar pDialog;
    private SwipeRefreshLayout swipeRefresh;
    private String params;
    private static boolean m_iAmVisible;
    Context activity;
    boolean withdrawCheck;

    private ImageView ivEmptyState;
    private LinearLayout llEmptyState;
    private TextView tvInterestRequestEmptyState;

    private String type = "";
    private Context context;

    private String Tag = "DashboardPermissionsFragment";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    /*   @Override
       public void fragmentBecameVisible() {

           // You can do your animation here because we are visible! (make sure onViewCreated has been called too and the Layout has been laid. Source for another question but you get the idea.
       }*/
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_permissions, container, false);


        type = getArguments().getString("type");
        initilize(rootView);


        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        this.activity = context;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onResume() {
        super.onResume();

        if (ConnectCheck.isConnected(getActivity().findViewById(android.R.id.content))) {
            getData();
        }


    }


    private void initilize(View view) {


        fragment = DashboardPermissionsFragment.this;


        ivEmptyState = (ImageView) view.findViewById(R.id.ImageViewInterestRequestEmptyState);
        ivEmptyState.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_empty_state_new_interest_received));
        tvInterestRequestEmptyState = (TextView) view.findViewById(R.id.TextViewInterestRequestEmptyState);
        //  tvInterestRequestEmptyState.setText("You have 0 interests");

        llEmptyState = (LinearLayout) view.findViewById(R.id.LinearLayoutInterestsRequestsEmptyState);


        dataList = new ArrayList<>();
        pDialog = (ProgressBar) view.findViewById(R.id.ProgressbarMyInterests);
        //   pDialog.setMessage("Loading...");
        swipeRefresh = (SwipeRefreshLayout) view.findViewById(R.id.SwipeRefreshMyInterests);
        // LinearLayoutMMMatchesNotFound = (LinearLayout) view.findViewById(R.id.LinearLayoutMMMatchesNotFound);
        recyclerView = (RecyclerView) view.findViewById(R.id.RecyclerViewInboxListInterests);


        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);

        recyclerAdapter = new RecyclerViewAdapterPermissions(getContext(), getFragmentManager(), fragment, DashboardPermissionsFragment.this);


        recyclerAdapter.setLinearLayoutManager(mLayoutManager);

        recyclerAdapter.setRecyclerView(recyclerView);

        recyclerView.setAdapter(recyclerAdapter);
        swipeRefresh.setOnRefreshListener(this);


    }


    @Override
    public void onRefresh() {
        getData();
        if (ConnectCheck.isConnected(getActivity().findViewById(android.R.id.content))) {
            getData();
        }

    }


    private void getData() {

        recyclerAdapter.clearAll();


        pDialog.setVisibility(View.VISIBLE);
        Log.e("getSavedList", Urls.permission + SharedPreferenceManager.getUserObject(getActivity().getApplicationContext()).getPath() + "/" + type);
        JsonArrayRequest req = new JsonArrayRequest(Urls.permission + SharedPreferenceManager.getUserObject(getActivity().getApplicationContext()).getPath() + "/" + type,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.e("Response getSavedList", response.toString() + "  ==   ");
                        if (response.length() > 0) {


                            Gson gsonc;
                            GsonBuilder gsonBuilderc = new GsonBuilder();
                            gsonc = gsonBuilderc.create();
                            Type listType = new TypeToken<List<mPermission>>() {
                            }.getType();
                            try {
                                dataList = (List<mPermission>) gsonc.fromJson(response.getJSONArray(0).toString(), listType);
                                // Log.e("List size in sav search", dataList.size() + "");
                                if (dataList.size() > 0) {

                                    llEmptyState.setVisibility(View.GONE);

                                    recyclerAdapter.addAll(dataList);
                                } else {

                                    llEmptyState.setVisibility(View.VISIBLE);

                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        } else {

                            llEmptyState.setVisibility(View.VISIBLE);


                        }
                        pDialog.setVisibility(View.GONE);
                        swipeRefresh.setRefreshing(false);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Err", "Error: " + error.getMessage());


                llEmptyState.setVisibility(View.VISIBLE);

                pDialog.setVisibility(View.GONE);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return Constants.getHashMap();
            }
        };
        MySingleton.getInstance(getActivity().getApplicationContext()).addToRequestQueue(req, Tag);
    }


    @Override
    public void onUpdate(String msg) {
        if (ConnectCheck.isConnected(getActivity().findViewById(android.R.id.content))) {

     /*       JSONObject params = new JSONObject();
            try {
                params.put("path", SharedPreferenceManager.getUserObject(getContext()).getPath());
                params.put("page_no", 1);
                params.put("type", type);

                loadData(params.toString(), false);

            } catch (JSONException e) {
                e.printStackTrace();
            }*/
        }

    }


    @Override
    public void onStop() {
        super.onStop();
        MySingleton.getInstance(getContext()).cancelPendingRequests(Tag);

    }
}
