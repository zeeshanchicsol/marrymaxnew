package com.chicsol.marrymax.fragments.inbox.requests;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.AppCompatButton;
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
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.chicsol.marrymax.R;
import com.chicsol.marrymax.activities.directive.MainDirectiveActivity;
import com.chicsol.marrymax.adapters.RecyclerViewAdapterMyInterestsRequests;
import com.chicsol.marrymax.dialogs.dialogDeclineInterestInbox;
import com.chicsol.marrymax.dialogs.dialogReplyOnAcceptInterestInbox;
import com.chicsol.marrymax.dialogs.dialogRequestPhone;
import com.chicsol.marrymax.dialogs.dialogShowInterest;
import com.chicsol.marrymax.dialogs.dialogWithdrawInterest;
import com.chicsol.marrymax.modal.Members;
import com.chicsol.marrymax.modal.mComCount;
import com.chicsol.marrymax.modal.mCommunication;
import com.chicsol.marrymax.other.MarryMax;
import com.chicsol.marrymax.preferences.SharedPreferenceManager;
import com.chicsol.marrymax.urls.Urls;
import com.chicsol.marrymax.utils.ConnectCheck;
import com.chicsol.marrymax.utils.Constants;
import com.chicsol.marrymax.utils.MySingleton;
import com.chicsol.marrymax.utils.ViewGenerator;
import com.chicsol.marrymax.utils.WrapContentLinearLayoutManager;
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

public class DashboardRequestsFragment extends Fragment implements RecyclerViewAdapterMyInterestsRequests.OnLoadMoreListener, SwipeRefreshLayout.OnRefreshListener, dialogShowInterest.onCompleteListener, dialogRequestPhone.onCompleteListener, dialogReplyOnAcceptInterestInbox.onCompleteListener, dialogDeclineInterestInbox.onCompleteListener, RecyclerViewAdapterMyInterestsRequests.OnUpdateListener {
    public static int result = 0;
    private static boolean m_iAmVisible;
    Context activity;
    StringBuilder htmlDescriptionText;
    ViewGenerator viewGenerator;
    // LinearLayout LinearLayoutMMMatchesNotFound;
    //private Button bt_loadmore;
    private
    RecyclerView recyclerView;
    private int lastPage = 1;
    private List<mCommunication> membersDataList;
    private int totalPages = 0;
    // private int currentPage=1;
    private Fragment fragment;
    private RecyclerViewAdapterMyInterestsRequests
            recyclerAdapter;
    private ProgressBar pDialog;
    private SwipeRefreshLayout swipeRefresh;
    private String params;
    private String type = "";
    private ImageView ivEmptyState;
    private LinearLayout llEmptyState;
    private TextView tvInterestRequestEmptyState;
    private boolean withdrawCheck;
    private Context context;
    private LinearLayout llEmptySubItems;
    private int getNew_requests_count = 0;
    private AppCompatButton btCompleteProfile, btOnSearch, btSubscribe;
    private String Tag = "DashboardRequestsFragment";

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
        View rootView = inflater.inflate(R.layout.fragment_dashboard_interests, container, false);

        initilize(rootView);


        return rootView;
    }

    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        this.activity = activity;
        this.context = activity;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onResume() {
        super.onResume();

        if (ConnectCheck.isConnected(getActivity().findViewById(android.R.id.content))) {

            getCommunicationCount();
            JSONObject params = new JSONObject();
            try {
                params.put("path", SharedPreferenceManager.getUserObject(getContext()).get_path());
                params.put("page_no", 1);
                params.put("type", type);

                loadData(params.toString(), false);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }


    private void initilize(View view) {
        htmlDescriptionText = new StringBuilder();
        if (getArguments() != null) {
            type = getArguments().getString("type");
            withdrawCheck = getArguments().getBoolean("withdrawcheck");
        }
        viewGenerator = new ViewGenerator(context);

        fragment = DashboardRequestsFragment.this;

        ivEmptyState = (ImageView) view.findViewById(R.id.ImageViewInterestRequestEmptyState);
        ivEmptyState.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_empty_state_request_received));
        tvInterestRequestEmptyState = (TextView) view.findViewById(R.id.TextViewInterestRequestEmptyState);

        btCompleteProfile = (AppCompatButton) view.findViewById(R.id.ButtonDInterestsCompleteProfile);
        // tvInterestRequestEmptyState.setText("You have 0 requests");
        btOnSearch = (AppCompatButton) view.findViewById(R.id.ButtonOnSearchClick);

        btSubscribe = (AppCompatButton) view.findViewById(R.id.ButtonDInterestsSubscribe);


        llEmptySubItems = (LinearLayout) view.findViewById(R.id.LinearLayoutEmptySubItems);
        llEmptyState = (LinearLayout) view.findViewById(R.id.LinearLayoutInterestsRequestsEmptyState);

        membersDataList = new ArrayList<>();
        pDialog = (ProgressBar) view.findViewById(R.id.ProgressbarMyInterests);
        //   pDialog.setMessage("Loading...");
        swipeRefresh = (SwipeRefreshLayout) view.findViewById(R.id.SwipeRefreshMyInterests);
        // LinearLayoutMMMatchesNotFound = (LinearLayout) view.findViewById(R.id.LinearLayoutMMMatchesNotFound);
        recyclerView = (RecyclerView) view.findViewById(R.id.RecyclerViewInboxListInterests);


        LinearLayoutManager mLayoutManager = new WrapContentLinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);

        recyclerAdapter = new RecyclerViewAdapterMyInterestsRequests(getContext(), getFragmentManager(), this, fragment, false, this, withdrawCheck);


        recyclerAdapter.setLinearLayoutManager(mLayoutManager);

        recyclerAdapter.setRecyclerView(recyclerView);

        recyclerView.setAdapter(recyclerAdapter);
        swipeRefresh.setOnRefreshListener(this);

        setListenders();
    }

    private void setListenders() {
        btOnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MarryMax(getActivity()).onSearchClicked(context, 0);
            }
        });
        btCompleteProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //new MarryMax(getActivity()).
                Intent in = new Intent(getActivity(), MainDirectiveActivity.class);
                in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                in.putExtra("type", 22);
                getActivity().startActivity(in);
            }
        });

        btSubscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MarryMax(getActivity()).subscribe();
            }
        });
    }

    @Override
    public void onLoadMore() {

        if (lastPage != totalPages && lastPage < totalPages) {
            lastPage = lastPage + 1;

            Log.e("", "las p: " + lastPage + " Total Pages:" + totalPages);


            JSONObject params = new JSONObject();
            try {
                params.put("path", SharedPreferenceManager.getUserObject(getContext()).get_path());
                params.put("page_no", lastPage);
                params.put("type", type);

                //loadData(params.toString(), false);
                loadMoreData(params.toString());

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }


    @Override
    public void onRefresh() {
        if (ConnectCheck.isConnected(getActivity().findViewById(android.R.id.content))) {

            JSONObject params = new JSONObject();
            try {
                params.put("path", SharedPreferenceManager.getUserObject(getContext()).get_path());
                params.put("page_no", 1);
                params.put("type", type);

                loadData(params.toString(), false);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    /*private void loadData() {
        itemList.clear();
        for (int i = 1; i <= 20; i++) {
            itemList.add(new Item("Item " + i));
        }
        mAdapter.addAll(itemList);
    }*/


    @Override
    public void onComplete(String s) {
        onRefresh();
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

        Log.e("Params search" + " " + Urls.interestRequestType, "" + params);

        //  Log.e("Params search" + " " + Urls.searchProfiles, "");
        final JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.PUT,
                Urls.interestRequestType, params,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("re  update appearance", response + "");
                        try {

                            llEmptySubItems.removeAllViews();

                            JSONArray jsonArray = response.getJSONArray("data");

                            //    if (jsonArray.length() > 1) {

                            Log.e("Length", jsonArray.getJSONArray(0).length() + "");
                            JSONArray jsonarrayData = jsonArray.getJSONArray(0);
                            JSONArray jsonarrayTotalPages = jsonArray.getJSONArray(1);

                            /*     if (jsonarrayData.length() == 0) {*/
                            htmlDescriptionText = new StringBuilder();
                            recyclerAdapter.clear();
                            swipeRefresh.setRefreshing(false);
                            recyclerView.setVisibility(View.GONE);
                            llEmptyState.setVisibility(View.VISIBLE);

                            JSONArray jsonarrayTotalPages1 = jsonArray.getJSONArray(1);
                            Gson gson;
                            GsonBuilder gsonBuilder = new GsonBuilder();
                            gson = gsonBuilder.create();


                            Type membert = new TypeToken<mCommunication>() {
                            }.getType();

                            mCommunication memberC = (mCommunication) gson.fromJson(jsonarrayTotalPages1.getJSONObject(0).toString(), membert);


                            Type memberlis = new TypeToken<List<mCommunication>>() {
                            }.getType();
                            membersDataList = (List<mCommunication>) gson.fromJson(jsonarrayData.toString(), memberlis);


                            if (type.equals("requestsent")) {
                                if (SharedPreferenceManager.getUserObject(context).get_member_status() < 3 || SharedPreferenceManager.getUserObject(context).get_member_status() > 4) {


                                    tvInterestRequestEmptyState.setText("You haven’t sent any request yet!  " +
                                            "\n Find your matches and start communicating.");
                                    btOnSearch.setVisibility(View.VISIBLE);

                                  /*  if (memberC.getRequesting_members_count() == 0) {
                                        tvInterestRequestEmptyState.setText("You haven’t send any request yet!  \n Find your matches and start communicating.");
                                    } else if (memberC.getRequesting_members_count() > 0) {
                                        tvInterestRequestEmptyState.setText("There are " + memberC.getRequesting_members_count() + " requests sent.Please complete & verify your profile to send requests.");
                                    }*/
                                } else {
                                    if (SharedPreferenceManager.getUserObject(context).get_member_status() == 3 || SharedPreferenceManager.getUserObject(context).get_member_status() == 4) {
                                        if (membersDataList.size() == 0) {

                                            htmlDescriptionText.append(" You haven’t sent any request yet!\n");
                                            if (SharedPreferenceManager.getUserObject(context).get_member_status() == 3) {
                                                htmlDescriptionText.append(" Subscribe now to enjoy following benefits.");

                                               /* htmlDescriptionText.append(" Priority Profile Listing.\n");
                                                htmlDescriptionText.append(" Maximum interaction & quick connect with other members.\n");
                                                htmlDescriptionText.append(" More Privacy options.\n");
                                                htmlDescriptionText.append(" Personalized service from MarryMax when need.\n");*/

                                                viewGenerator.generateTextViewWithIcon(llEmptySubItems, "Priority Profile Listing.");
                                                viewGenerator.generateTextViewWithIcon(llEmptySubItems, "Maximum interaction & quick connect with other members.");
                                                viewGenerator.generateTextViewWithIcon(llEmptySubItems, "More Privacy options.");
                                                viewGenerator.generateTextViewWithIcon(llEmptySubItems, "Personalized service from MarryMax when need.");

                                                btSubscribe.setVisibility(View.VISIBLE);
                                            }
                                            tvInterestRequestEmptyState.setText(htmlDescriptionText.toString());


                                        } else if (membersDataList.size() > 0) {
                                            //   records with load more
                                            recyclerView.setVisibility(View.VISIBLE);
                                            llEmptyState.setVisibility(View.GONE);

                                            gson = gsonBuilder.create();
                                            Type member = new TypeToken<List<mCommunication>>() {
                                            }.getType();


                                            membersDataList = (List<mCommunication>) gson.fromJson(jsonarrayData.toString(), member);
                                            if (membersDataList.size() > 0) {
                                                //    LinearLayoutMMMatchesNotFound.setVisibility(View.GONE);
                                                recyclerAdapter.addAll(membersDataList);


                                                //      Log.e("Length=================", membersDataList.size() + "  ");


                                                Gson gsont;
                                                GsonBuilder gsonBuildert = new GsonBuilder();
                                                gsont = gsonBuildert.create();
                                                Type memberta = new TypeToken<mCommunication>() {
                                                }.getType();
                                                mCommunication memberTotalPages = (mCommunication) gson.fromJson(jsonarrayTotalPages.getJSONObject(0).toString(), memberta);


                                                totalPages = (int) memberTotalPages.getRequesting_members_count();
                                                lastPage = 1;
                                                //   Log.e("total pages Req Sent", "" + totalPages);
                                                swipeRefresh.setRefreshing(false);
                                            } else {


                                                recyclerAdapter.clear();
                                                swipeRefresh.setRefreshing(false);
                                                //LinearLayoutMMMatchesNotFound.setVisibility(View.VISIBLE);
                                            }
                                        }
                                    }


                                }

                            } else {
                                //request receieved
                                if (SharedPreferenceManager.getUserObject(context).get_member_status() < 3 || SharedPreferenceManager.getUserObject(context).get_member_status() > 4) {
                                    if (memberC.getRequesting_members_count() == 0) {
                                        tvInterestRequestEmptyState.setText("There are " + getNew_requests_count + " requests. " +
                                                "\nPlease complete & verify your profile to view the requests," +
                                                "\n shown in you.");
                                        btCompleteProfile.setVisibility(View.VISIBLE);
                                        //new count
                                    } else if (memberC.getRequesting_members_count() > 0) {
                                        tvInterestRequestEmptyState.setText("There are " + getNew_requests_count + " requests, waiting for you to respond." +
                                                "\nPlease complete & verify your profile to view all requests.");
                                        btCompleteProfile.setVisibility(View.VISIBLE);

                                        //new count
                                    }

                                  /*  if (memberC.getInterested_members_count() == 0) {
                                        recyclerView.setVisibility(View.GONE);
                                        llEmptyState.setVisibility(View.VISIBLE);
                                        tvInterestRequestEmptyState.setText("There are " + getInterested_members_count + " interests."+
                                                "\nPlease complete & verify your profile to view   the interests, shown in you.");
                                    } else if (memberC.getInterested_members_count() > 0) {
                                        recyclerView.setVisibility(View.GONE);
                                        llEmptyState.setVisibility(View.VISIBLE);
                                        tvInterestRequestEmptyState.setText("There are " + getInterested_members_count + "  interests, waiting for you to respond." +
                                                "\nPlease complete & verify your profile to view the interests, shown in you.");

                                    }
                                    */


                                } else {

                                    if (SharedPreferenceManager.getUserObject(context).get_member_status() == 3 || SharedPreferenceManager.getUserObject(context).get_member_status() == 4) {
                                        if (membersDataList.size() == 0) {

                                            htmlDescriptionText.append(" You have 0 requests. \n");
                                            if (SharedPreferenceManager.getUserObject(context).get_member_status() == 3) {
                                                htmlDescriptionText.append("Subscribe now to enjoy following benefits. ");

                                          /*      htmlDescriptionText.append(" Priority Profile Listing. \n");
                                                htmlDescriptionText.append(" Maximum interaction & quick connect with other members. \n");
                                                htmlDescriptionText.append(" More Privacy options. \n");
                                                htmlDescriptionText.append(" Personalized service from MarryMax when need. \n");*/


                                                viewGenerator.generateTextViewWithIcon(llEmptySubItems, "Priority Profile Listing.");
                                                viewGenerator.generateTextViewWithIcon(llEmptySubItems, "Maximum interaction & quick connect with other members.");
                                                viewGenerator.generateTextViewWithIcon(llEmptySubItems, "More Privacy options.");
                                                viewGenerator.generateTextViewWithIcon(llEmptySubItems, "Personalized service from MarryMax when need.");

                                                btSubscribe.setVisibility(View.VISIBLE);
                                                // htmlDescriptionText.append("&#8226; \n");

                                            }
                                            tvInterestRequestEmptyState.setText(htmlDescriptionText.toString());


                                        } else if (membersDataList.size() > 0) {
                                            // records with load more

                                            gson = gsonBuilder.create();
                                            Type member = new TypeToken<List<mCommunication>>() {
                                            }.getType();


                                            membersDataList = (List<mCommunication>) gson.fromJson(jsonarrayData.toString(), member);
                                            if (membersDataList.size() > 0) {
                                                //    LinearLayoutMMMatchesNotFound.setVisibility(View.GONE);
                                                recyclerAdapter.addAll(membersDataList);
                                                recyclerView.setVisibility(View.VISIBLE);
                                                llEmptyState.setVisibility(View.GONE);

                                                //      Log.e("Length=================", membersDataList.size() + "  ");


                                                Gson gsont;
                                                GsonBuilder gsonBuildert = new GsonBuilder();
                                                gsont = gsonBuildert.create();
                                                Type memberth = new TypeToken<mCommunication>() {
                                                }.getType();
                                                mCommunication memberTotalPages = (mCommunication) gson.fromJson(jsonarrayTotalPages.getJSONObject(0).toString(), memberth);


                                                totalPages = (int) memberTotalPages.getRequesting_members_count();
                                                lastPage = 1;
                                                //   Log.e("Requests total pages", "" + totalPages);
                                                swipeRefresh.setRefreshing(false);
                                            } else {


                                                recyclerAdapter.clear();
                                                swipeRefresh.setRefreshing(false);
                                                //LinearLayoutMMMatchesNotFound.setVisibility(View.VISIBLE);
                                            }
                                        }
                                    }


                                }

                            }


                            /*    } else {





                                }*/


                          /*  } else {
                                recyclerAdapter.clear();
                                //no data
                                swipeRefresh.setRefreshing(false);
                                llEmptyState.setVisibility(View.VISIBLE);

                            }*/
                        } catch (JSONException e) {
                            // e.printStackTrace();
                            llEmptyState.setVisibility(View.VISIBLE);
                        }
                        if (!refresh) {
                            pDialog.setVisibility(View.GONE);
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
        MySingleton.getInstance(getContext()).addToRequestQueue(jsonObjReq, Tag);

    }

    private void loadMoreData(String paramsString) {
        recyclerAdapter.setProgressMore(true);
        JSONObject params = null;
        try {
            params = new JSONObject(paramsString);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //  Log.e("Params search" + " " + Urls.searchProfiles, "" + params);

        Log.e("Request Params" + " " + Urls.interestRequestType, "");
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.PUT,
                Urls.interestRequestType, params,
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
                                Type member = new TypeToken<List<mCommunication>>() {
                                }.getType();
                                recyclerAdapter.setProgressMore(false);
                                // membersDataList.clear();
                                membersDataList = (List<mCommunication>) gson.fromJson(jsonarrayData.toString(), member);

                                //   Log.e("Request Length 56", membersDataList.size() + "  ");
                                recyclerAdapter.addItemMore(membersDataList);
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
        MySingleton.getInstance(getContext()).addToRequestQueue(jsonObjReq, Tag);

    }


    @Override
    public void onUpdate(String msg) {
        if (ConnectCheck.isConnected(getActivity().findViewById(android.R.id.content))) {
            JSONObject params = new JSONObject();
            try {
                params.put("path", SharedPreferenceManager.getUserObject(getContext()).get_path());
                params.put("page_no", 1);
                params.put("type", type);

                loadData(params.toString(), false);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    private void getCommunicationCount() {
      /*  final ProgressDialog pDialog = new ProgressDialog(getContext());
        pDialog.setMessage("Loading...");
        pDialog.show();*/
        Log.e("url", Urls.getCommunicationCount + SharedPreferenceManager.getUserObject(context).get_path());
        JsonArrayRequest req = new JsonArrayRequest(Urls.getCommunicationCount + SharedPreferenceManager.getUserObject(context).get_path(),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.e("getSavedList ", response.toString() + "  ==   ");
                        Gson gsonc;
                        GsonBuilder gsonBuilderc = new GsonBuilder();
                        gsonc = gsonBuilderc.create();
                        Type listType = new TypeToken<mComCount>() {
                        }.getType();
                        try {


                            mComCount comCount = (mComCount) gsonc.fromJson(response.getJSONArray(0).getJSONObject(0).toString(), listType);

                            //     Log.e("ressssss", comCount.getNew_requests_count() + "");
                            getNew_requests_count = (int) comCount.getNew_requests_count();

                        } catch (JSONException e) {
                            e.printStackTrace();

                        }
                        //  pDialog.dismiss();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Err", "Error: " + error.getMessage());

                //   pDialog.dismiss();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return Constants.getHashMap();
            }
        };
        MySingleton.getInstance(context).addToRequestQueue(req, Tag);
    }

    @Override
    public void onStop() {
        super.onStop();
        MySingleton.getInstance(getContext()).cancelPendingRequests(Tag);

    }
}
