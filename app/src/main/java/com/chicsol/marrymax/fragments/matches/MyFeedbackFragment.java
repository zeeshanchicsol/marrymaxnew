package com.chicsol.marrymax.fragments.matches;

import android.content.Context;
import android.content.Intent;
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
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.chicsol.marrymax.R;
import com.chicsol.marrymax.activities.search.SearchMainActivity;
import com.chicsol.marrymax.adapters.RecyclerViewAdapterMyFeedbacks;
import com.chicsol.marrymax.dialogs.dialogFeedback;
import com.chicsol.marrymax.dialogs.dialogFeedbackDetail;
import com.chicsol.marrymax.modal.mUsrFeedback;
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

public class MyFeedbackFragment extends Fragment implements RecyclerViewAdapterMyFeedbacks.OnUpdateListener, dialogFeedbackDetail.onCompleteListener, dialogFeedback.onCompleteListener {
    public static int result = 0;
    LinearLayout LinearLayoutMMMatchesNotFound;
    //private Button bt_loadmore;
    private
    RecyclerView recyclerView;
    private int lastPage = 1;
    private List<mUsrFeedback> dataList;
    private int totalPages = 0;
    // private int currentPage=1;
    private Fragment fragment;
    private RecyclerViewAdapterMyFeedbacks
            recyclerAdapter;
    private ProgressBar pDialog;
    // private SwipeRefreshLayout swipeRefresh;
    private String params;

    Context activity;

    AppCompatButton btSearch;

    private LinearLayout llEmptyState, llSubscribeNow;

    private TextView tvEmptyMessage;


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
        View rootView = inflater.inflate(R.layout.fragment_list_my_feedbacks, container, false);


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

            getData();
        }

    }


    private void initilize(View view) {
        fragment = MyFeedbackFragment.this;

        llEmptyState = (LinearLayout) view.findViewById(R.id.LinearLayoutMMMatchesNotFound);

        llSubscribeNow = (LinearLayout) view.findViewById(R.id.LinearLayoutMMMatchesNotFoundSubscribeNow);

        tvEmptyMessage = (TextView) view.findViewById(R.id.TextViewMyContactsEmptyMessage);

        dataList = new ArrayList<>();
        pDialog = (ProgressBar) view.findViewById(R.id.ProgressbarMyContacts);
        //   pDialog.setMessage("Loading...");
        //   swipeRefresh = (SwipeRefreshLayout) view.findViewById(R.id.SwipeRefreshMyContacts);
        LinearLayoutMMMatchesNotFound = (LinearLayout) view.findViewById(R.id.LinearLayoutMMMatchesNotFound);
        recyclerView = (RecyclerView) view.findViewById(R.id.RecyclerViewMyContacts);


        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);


        tvEmptyMessage.setText("There are no  Feedbacks.");


        recyclerAdapter = new RecyclerViewAdapterMyFeedbacks(getContext(), getFragmentManager(), fragment, MyFeedbackFragment.this);


        recyclerAdapter.setLinearLayoutManager(mLayoutManager);

        recyclerAdapter.setRecyclerView(recyclerView);

        recyclerView.setAdapter(recyclerAdapter);
        //  swipeRefresh.setOnRefreshListener(this);

        btSearch = (AppCompatButton) view.findViewById(R.id.ButtonMyContatsSearch);
        setListeners();
    }

    private void
    setListeners() {


        btSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SearchMainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void getData() {


        pDialog.setVisibility(View.VISIBLE);
        Log.e("myContacts ", Urls.getUsrFeedbacks + "/" + SharedPreferenceManager.getUserObject(getActivity().getApplicationContext()).getPath());
        JsonArrayRequest req = new JsonArrayRequest(Urls.getUsrFeedbacks + "/" + SharedPreferenceManager.getUserObject(getActivity().getApplicationContext()).getPath(),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.e("Response getSavedList", response.toString() + "  ==   ");
                        if (response.length() > 0) {


                            Gson gsonc;
                            GsonBuilder gsonBuilderc = new GsonBuilder();
                            gsonc = gsonBuilderc.create();
                            Type listType = new TypeToken<List<mUsrFeedback>>() {
                            }.getType();
                            try {
                                dataList = (List<mUsrFeedback>) gsonc.fromJson(response.getJSONArray(0).toString(), listType);
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
        MySingleton.getInstance(getActivity().getApplicationContext()).addToRequestQueue(req);
    }










   /* private void loadData(String paramsString, final boolean refresh) {


        pDialog.setVisibility(View.VISIBLE);
        //   RequestQueue rq = Volley.newRequestQueue(getActivity().getApplicationContext());

        JSONObject params = null;
        try {
            params = new JSONObject(paramsString);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.e("Params contacts" + " " + Urls.myContacts, "" + params);

        //  Log.e("Params search" + " " + Urls.searchProfiles, "");
        final JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.PUT,
                Urls.myContacts, params,
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
                                    Type member = new TypeToken<List<mContacts>>() {
                                    }.getType();


                                    dataList = (List<mContacts>) gson.fromJson(jsonarrayData.toString(), member);
                                    if (dataList.size() > 0) {
                                        LinearLayoutMMMatchesNotFound.setVisibility(View.GONE);
                                        recyclerAdapter.addAll(dataList);


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
                        }
                        if (!refresh) {
                            // pDialog.dismiss();
                            pDialog.setVisibility(View.GONE);
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {


                VolleyLog.e("res err", "Error: " + error);
                if (!refresh) {
                    //pDialog.dismiss();
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
        MySingleton.getInstance(getContext()).addToRequestQueue(jsonObjReq);

    }*/


    @Override
    public void onUpdate(String msg) {
        Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
        if (ConnectCheck.isConnected(getActivity().findViewById(android.R.id.content))) {
            getData();
        }

    }

    @Override
    public void onComplete(String s) {
        if (ConnectCheck.isConnected(getActivity().findViewById(android.R.id.content))) {
            getData();
        }
    }

/*    private void loadMoreData(String paramsString) {
        recyclerAdapter.setProgressMore(true);
        JSONObject params = null;
        try {
            params = new JSONObject(paramsString);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //  Log.e("Params search" + " " + Urls.searchProfiles, "" + params);

        //  Log.e("Params search" + " " + Urls.searchProfiles, "");
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.PUT,
                Urls.myContacts, params,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        //  Log.e("re  update appearance", response + "");
                        try {
                            JSONArray jsonArray = response.getJSONArray("data");

                            if (jsonArray.length() > 1) {

                                Log.e("Length", jsonArray.getJSONArray(0).length() + "");
                                JSONArray jsonarrayData = jsonArray.getJSONArray(0);
                                JSONArray jsonarrayTotalPages = jsonArray.getJSONArray(1);

                                Gson gson;
                                GsonBuilder gsonBuilder = new GsonBuilder();
                                gson = gsonBuilder.create();
                                Type member = new TypeToken<List<mContacts>>() {
                                }.getType();
                                recyclerAdapter.setProgressMore(false);
                                // dataList.clear();
                                dataList = (List<mContacts>) gson.fromJson(jsonarrayData.toString(), member);

                                // Log.e("Length 56", dataList.size() + "  ");
                                recyclerAdapter.addItemMore(dataList);
                                recyclerAdapter.setMoreLoading(false);


                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        recyclerAdapter.setMoreLoading(false);

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {


                VolleyLog.e("res err", "Error: " + error);
                // Toast.makeText(RegistrationActivity.this, "Incorrect Email or Password !", Toast.LENGTH_SHORT).show();

                recyclerAdapter.setMoreLoading(false);

            }


        });

        // Adding request to request queue
        ///   rq.add(jsonObjReq);
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(getContext()).addToRequestQueue(jsonObjReq);

    }*/


}
