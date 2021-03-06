package com.chicsol.marrymax.fragments.matches;

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
import com.chicsol.marrymax.activities.DrawerActivity;
import com.chicsol.marrymax.activities.directive.MainDirectiveActivity;
import com.chicsol.marrymax.adapters.RecyclerViewAdapterMyMatches;
import com.chicsol.marrymax.dialogs.dialogMatchingAttributeFragment;
import com.chicsol.marrymax.dialogs.dialogProfileCompletion;
import com.chicsol.marrymax.dialogs.dialogRemoveFromSearch;
import com.chicsol.marrymax.dialogs.dialogRequest;
import com.chicsol.marrymax.dialogs.dialogRequestPhone;
import com.chicsol.marrymax.dialogs.dialogShowInterest;
import com.chicsol.marrymax.fragments.DashboardMatchesMainFragment;
import com.chicsol.marrymax.interfaces.MatchesRefreshCallBackInterface;
import com.chicsol.marrymax.interfaces.UpdateMatchesCountCallback;
import com.chicsol.marrymax.modal.Members;
import com.chicsol.marrymax.other.MarryMax;
import com.chicsol.marrymax.preferences.SharedPreferenceManager;
import com.chicsol.marrymax.urls.Urls;
import com.chicsol.marrymax.utils.ConnectCheck;
import com.chicsol.marrymax.utils.Constants;
import com.chicsol.marrymax.utils.MySingleton;
import com.chicsol.marrymax.utils.WrapContentLinearLayoutManager;
import com.chicsol.marrymax.widgets.mTextView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by Android on 11/3/2016.
 */

public class AccpetedMembers extends  BaseMatchesFragment implements RecyclerViewAdapterMyMatches.OnLoadMoreListener, SwipeRefreshLayout.OnRefreshListener, dialogShowInterest.onCompleteListener, dialogRequestPhone.onCompleteListener, DashboardMatchesMainFragment.MatchesMainFragmentInterface, dialogRequest.onCompleteListener, dialogProfileCompletion.onCompleteListener, dialogRemoveFromSearch.onCompleteListener, UpdateMatchesCountCallback, MatchesRefreshCallBackInterface, dialogMatchingAttributeFragment.onMatchPreferenceCompleteListener {
    public static int result = 0;
    LinearLayout LinearLayoutMMMatchesNotFound;
    //private Button bt_loadmore;
    private
    RecyclerView recyclerView;
    private int lastPage = 1;
    private List<Members> membersDataList;
    private int totalPages = 0;
    // private int currentPage=1;
    private Fragment fragment;
    private RecyclerViewAdapterMyMatches recyclerAdapter;
    private ProgressBar pDialog;
    private SwipeRefreshLayout swipeRefresh;
    private String params;
    Context context;
    private TextView tvMatchesCount, tvMatchesCountCp, tvComplProfioleTitle, tvMatchesCountSubscribeNow, tvSubscribeNowTitle;
    LinearLayout llMMMatchesNotFoundCompleteProfile, llSubscribeNow;
    private long totalMatchesCount = 0;
    private String Tag = "AccpetedMembers";
    private mTextView tvNotFoundMain, tvMatchesNotFoundSubHeading;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

 /*   @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_dashboard_mymatches, container, false);
        Log.e("created", "created");

        initilize(rootView);
        setListenders();

        return rootView;
    }*/

    @Override
    public View provideYourFragmentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_dashboard_mymatches, container, false);

        initilize(rootView);
        setListenders();


        return rootView;
    }
    @Override
    public Fragment getChildFragment() {
        return AccpetedMembers.this;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onResume() {
        super.onResume();
  /*      if (searchKey != -1) {

            //  Toast.makeText(getContext(), "Searchkey not  Null :"+searchKey, Toast.LENGTH_SHORT).show();


            Members memberSearchObj = ListViewAdvSearchFragment.defaultSelectionsObj;
            if (memberSearchObj != null) {
                memberSearchObj.setPath(SharedPreferenceManager.getUserObject(getContext()).getPath());
                memberSearchObj.setMember_status(SharedPreferenceManager.getUserObject(getContext()).getMember_status());
                memberSearchObj.setPhone_verified(SharedPreferenceManager.getUserObject(getContext()).getPhone_verified());
                memberSearchObj.setEmail_verified(SharedPreferenceManager.getUserObject(getContext()).getEmail_verified());
                //page and type
                memberSearchObj.setPage_no(1);
                memberSearchObj.setType("");

                Gson gson = new Gson();
                params = gson.toJson(memberSearchObj);

                loadData(params, false);

                searchKey = -1;
            }
        }*/
   /*     if (searchKey != null) {
            //  getSelectedSearchObject(SearchMainActivity.searchKey);
            Toast.makeText(getContext(), "Searchkey not  Null", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), " Null", Toast.LENGTH_SHORT).show();
            ///  getRawData();
            //   defaultSelectionsObj = DrawerActivity.rawSearchObj;
            //    listener.onItemSelected(dataList.get(0));
            //ListViewAdvSearchFragment.defaultSelectionsObj
        }*/

        lastPage = 1;
        recyclerAdapter.setMoreLoading(false);
        if (ConnectCheck.isConnected(getActivity().findViewById(android.R.id.content))) {
            Members memberSearchObj = DrawerActivity.rawSearchObj;
            if (memberSearchObj != null) {

                memberSearchObj.setPath(SharedPreferenceManager.getUserObject(getContext()).getPath());
                memberSearchObj.setMember_status(SharedPreferenceManager.getUserObject(getContext()).getMember_status());
                memberSearchObj.setPhone_verified(SharedPreferenceManager.getUserObject(getContext()).getPhone_verified());
                memberSearchObj.setEmail_verified(SharedPreferenceManager.getUserObject(getContext()).getEmail_verified());
                //page and type
                memberSearchObj.setPage_no(1);
                memberSearchObj.setType("ACT");

                Gson gson = new Gson();
                String params = gson.toJson(memberSearchObj);
                this.params = params;

                recyclerAdapter.setMemResultsObj(memberSearchObj);

                loadData(params, false);
            }


        }


        if (result != 0) {
            Toast.makeText(getContext(), "val: " + result, Toast.LENGTH_SHORT).show();
        }


        DrawerActivity.rawSearchObj = new Members();

    }


    @Override
    public void setMenuVisibility(final boolean visible) {
        super.setMenuVisibility(visible);
        if (visible) {
            // Toast.makeText(getContext(), "visible: ", Toast.LENGTH_SHORT).show();


        }
    }

    private void initilize(View view) {



        tvNotFoundMain = (mTextView) view.findViewById(R.id.mTextViewMatchesNotFoundMain);
        tvMatchesNotFoundSubHeading = (mTextView) view.findViewById(R.id.mTextViewMatchesNotFoundSubHeading);
        tvNotFoundMain.setText("Not Connected With Your Matches Yet ?");
        tvMatchesNotFoundSubHeading.setText("Don't sit back, talk to us and we will help you in getting connected with your matches.");


        fragment = AccpetedMembers.this;
        membersDataList = new ArrayList<>();
        pDialog = (ProgressBar) view.findViewById(R.id.ProgressbarMyMatches);
        tvMatchesCount = (TextView) view.findViewById(R.id.TextViewMatchesTotalCount);
        tvMatchesCountCp = (TextView) view.findViewById(R.id.TextViewMMMatchesCountCompleteProfile);
        tvComplProfioleTitle = (TextView) view.findViewById(R.id.TextViewMMMatchesTitleCompleteProfile);
        llMMMatchesNotFoundCompleteProfile = (LinearLayout) view.findViewById(R.id.LinearLayoutMMMatchesNotFoundCompleteProfile);
        tvSubscribeNowTitle = (TextView) view.findViewById(R.id.TextViewMMMatchesTitleSubscribeNow);
        tvMatchesCountSubscribeNow = (TextView) view.findViewById(R.id.TextViewMMMatchesCountSubscribeNow);
        llSubscribeNow = (LinearLayout) view.findViewById(R.id.LinearLayoutMMMatchesNotFoundSubscribeNow);


        swipeRefresh = (SwipeRefreshLayout) view.findViewById(R.id.SwipeRefreshDashMainMM);
        LinearLayoutMMMatchesNotFound = (LinearLayout) view.findViewById(R.id.LinearLayoutMMMatchesNotFound);
        recyclerView = (RecyclerView) view.findViewById(R.id.RecyclerViewDashMainMyMatches);


        LinearLayoutManager mLayoutManager = new WrapContentLinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);

        recyclerAdapter = new RecyclerViewAdapterMyMatches(getContext(), getFragmentManager(), this, fragment, this, this, Tag);
        recyclerAdapter.setLinearLayoutManager(mLayoutManager);

        recyclerAdapter.setRecyclerView(recyclerView);

        recyclerView.setAdapter(recyclerAdapter);
        swipeRefresh.setOnRefreshListener(this);


        ((AppCompatButton) view.findViewById(R.id.ButtonOnSearchClick)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                MarryMax max = new MarryMax(getActivity());
                max.onSearchClicked(getContext(), 0);
            }
        });

        ((AppCompatButton) view.findViewById(R.id.ButtonMMonCompleteProfile)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

              /*  MarryMax marryMax = new MarryMax(getActivity());
                marryMax.getProfileProgress(getContext(), SharedPreferenceManager.getUserObject(getContext()), getActivity());*/

                Intent in = new Intent(getActivity(), MainDirectiveActivity.class);
                in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                in.putExtra("type", 22);
                getActivity().startActivity(in);

            }
        });

    }

    private void setListenders() {


    }

/*    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


 *//*       int id = item_slider.getItemId();

        if (id == R.id.action_search) {
            drawer.openDrawer(GravityCompat.END);

            return true;
        }*//*
        int id = item.getItemId();
        if (id == R.id.action_search) {

            if (jsonArraySearch == null) {
                getData();
            } else {

                Intent intent = new Intent(getActivity(), SearchMainActivity.class);
                startActivityForResult(intent, 2);
                // overridePendingTransition(R.anim.enter, R.anim.right_to_left);
            }
            return true;
        }


        return super.onOptionsItemSelected(item);
    }*/


    //for getting default search data


    @Override
    public void onDestroy() {
        super.onDestroy();
        //ImageLoader.getInstance().destroy();

        //   pDialog.setVisibility(View.GONE);

    }


    @Override
    public void onLoadMore() {
        lastPage = 1;
        recyclerAdapter.setMoreLoading(false);

        if (lastPage != totalPages && lastPage < totalPages) {
            lastPage = lastPage + 1;

            //Log.e("", "las p: " + lastPage + " Total Pages:" + totalPages);
       /*     Members memberSearchObj = DrawerActivity.rawSearchObj;

            memberSearchObj.setPath(SharedPreferenceManager.getUserObject(getContext()).getPath());
            memberSearchObj.setMember_status(SharedPreferenceManager.getUserObject(getContext()).getMember_status());
            memberSearchObj.setPhone_verified(SharedPreferenceManager.getUserObject(getContext()).getPhone_verified());
            memberSearchObj.setEmail_verified(SharedPreferenceManager.getUserObject(getContext()).getEmail_verified());
            //page and type
            memberSearchObj.setPage_no(lastPage);
            memberSearchObj.setType("");

            Gson gson = new Gson();
            String params = gson.toJson(memberSearchObj);*/


            Gson gsont;
            GsonBuilder gsonBuildert = new GsonBuilder();
            gsont = gsonBuildert.create();
            Type membert = new TypeToken<Members>() {
            }.getType();
            Members memberObj = (Members) gsont.fromJson(params, membert);
            memberObj.setPage_no(lastPage);
            gsont.toString();
            // Log.e("params json", gsont.toJson(memberObj));
            loadMoreData(gsont.toJson(memberObj));

        }

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

       /* if (requestCode == 2) {

            if (resultCode == RESULT_OK) {
                //   String message = data.getStringExtra("MESSAGE");
                Log.e("===========", "==================");
                //  Toast.makeText(getContext(), "" + message, Toast.LENGTH_SHORT).show();
                //   textView1.setText(message);

                ListViewAdvSearchFragment.defaultSelectionsObj.setPath(SharedPreferenceManager.getUserObject(getContext()).getPath());
                ListViewAdvSearchFragment.defaultSelectionsObj.setMember_status(SharedPreferenceManager.getUserObject(getContext()).getMember_status());
                ListViewAdvSearchFragment.defaultSelectionsObj.setPhone_verified(SharedPreferenceManager.getUserObject(getContext()).getPhone_verified());
                ListViewAdvSearchFragment.defaultSelectionsObj.setEmail_verified(SharedPreferenceManager.getUserObject(getContext()).getEmail_verified());
                //page and type
                ListViewAdvSearchFragment.defaultSelectionsObj.setPage_no(1);
                ListViewAdvSearchFragment.defaultSelectionsObj.setType("");


                Gson gson = new Gson();
                String memString = gson.toJson(ListViewAdvSearchFragment.defaultSelectionsObj);
                params = memString;
                loadData(memString, false);
                //     loadSearchProfilesData(memString, true);


            }
        }*/

    }

    @Override
    public void onRefresh() {
        Members memberSearchObj = DrawerActivity.rawSearchObj;
        if (ConnectCheck.isConnected(getActivity().findViewById(android.R.id.content))) {
            if (memberSearchObj != null) {


                memberSearchObj.setPath(SharedPreferenceManager.getUserObject(getContext()).getPath());
                memberSearchObj.setMember_status(SharedPreferenceManager.getUserObject(getContext()).getMember_status());
                memberSearchObj.setPhone_verified(SharedPreferenceManager.getUserObject(getContext()).getPhone_verified());
                memberSearchObj.setEmail_verified(SharedPreferenceManager.getUserObject(getContext()).getEmail_verified());
                //page and type
                memberSearchObj.setPage_no(1);
                memberSearchObj.setType("ACT");

                Gson gson = new Gson();
                params = gson.toJson(memberSearchObj);

                recyclerAdapter.setMemResultsObj(memberSearchObj);

                loadData(params, false);
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

        //Log.e("Params search" + " " + Urls.searchProfiles, "" + params);

        //Log.e("Params search" + " " + Urls.searchProfiles, "");
        final JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.PUT,
                Urls.searchProfiles, params,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        //Log.e("re  update appearance", response + "");
                        try {
                            JSONArray jsonArray = response.getJSONArray("data");

                            if (jsonArray.length() > 1) {

                                //Log.e("Length", jsonArray.getJSONArray(0).length() + "");
                                JSONArray jsonarrayData = jsonArray.getJSONArray(0);
                                JSONArray jsonarrayTotalPages = jsonArray.getJSONArray(1);

                                Gson gson;
                                GsonBuilder gsonBuilder = new GsonBuilder();
                                gson = gsonBuilder.create();
                                Type member = new TypeToken<List<Members>>() {
                                }.getType();


                                membersDataList = (List<Members>) gson.fromJson(jsonarrayData.toString(), member);
                                if (membersDataList.size() > 0) {
                                    LinearLayoutMMMatchesNotFound.setVisibility(View.GONE);
                                    recyclerAdapter.addAll(membersDataList);


                                    //Log.e("Length=================", membersDataList.size() + "  ");


                                    Gson gsont;
                                    GsonBuilder gsonBuildert = new GsonBuilder();
                                    gsont = gsonBuildert.create();
                                    Type membert = new TypeToken<Members>() {
                                    }.getType();
                                    Members memberTotalPages = (Members) gson.fromJson(jsonarrayTotalPages.getJSONObject(0).toString(), membert);


                                    totalPages = memberTotalPages.getTotal_pages();
                                    lastPage = 1;
                                    //Log.e("total pages", "" + totalPages);
                                    swipeRefresh.setRefreshing(false);
                                    if (memberTotalPages.getTotal_member_count() > 0) {

                                        if (getView() != null) {

                                            //        getView().findViewById(R.id.TextViewMatchesTotalCount).setVisibility(View.VISIBLE);
                                            //  ((TextView) getView().findViewById(R.id.TextViewMatchesTotalCount)).setText("" + memberTotalPages.getTotal_member_count() + " Matches Found");
                                            tvMatchesCount.setVisibility(View.VISIBLE);
                                            totalMatchesCount = memberTotalPages.getTotal_member_count();
                                            setMatchesCount();
                                        }

                                    }


                                } else {
                                    recyclerAdapter.clear();
                                    swipeRefresh.setRefreshing(false);
                                    if (SharedPreferenceManager.getUserObject(context).getMember_status() >= 0 && SharedPreferenceManager.getUserObject(context).getMember_status() <= 2) {

                                        Gson gsont;
                                        GsonBuilder gsonBuildert = new GsonBuilder();
                                        gsont = gsonBuildert.create();
                                        Type membert = new TypeToken<Members>() {
                                        }.getType();
                                        Members memberTotalPages = (Members) gson.fromJson(jsonarrayTotalPages.getJSONObject(0).toString(), membert);


                                        llMMMatchesNotFoundCompleteProfile.setVisibility(View.VISIBLE);
                                        tvMatchesCountCp.setText(NumberFormat.getNumberInstance(Locale.getDefault()).format(memberTotalPages.getTotal_member_count()) + "");
                                        tvComplProfioleTitle.setText("Members, With Accepted Interest");

                                    } else {
                                        LinearLayoutMMMatchesNotFound.setVisibility(View.VISIBLE);
                                    }
                                }

                            } else {
                                recyclerAdapter.clear();
                                //no data
                                swipeRefresh.setRefreshing(false);
                                LinearLayoutMMMatchesNotFound.setVisibility(View.VISIBLE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (!refresh) {
                            pDialog.setVisibility(View.GONE);
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {


                VolleyLog.e("res err", "Error: " + error);
                if (!refresh) {
                    pDialog.setVisibility(View.GONE);
                }
                LinearLayoutMMMatchesNotFound.setVisibility(View.VISIBLE);
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
        MySingleton.getInstance(context).addToRequestQueue(jsonObjReq, Tag);

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

        //  Log.e("Params search" + " " + Urls.searchProfiles, "");
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.PUT,
                Urls.searchProfiles, params,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        //Log.e("re  update appearance", response + "");
                        try {
                            JSONArray jsonArray = response.getJSONArray("data");

                            if (jsonArray.length() > 1) {

                                //Log.e("Length", jsonArray.getJSONArray(0).length() + "");
                                JSONArray jsonarrayData = jsonArray.getJSONArray(0);
                                JSONArray jsonarrayTotalPages = jsonArray.getJSONArray(1);

                                Gson gson;
                                GsonBuilder gsonBuilder = new GsonBuilder();
                                gson = gsonBuilder.create();
                                Type member = new TypeToken<List<Members>>() {
                                }.getType();
                                recyclerAdapter.setProgressMore(false);
                                // membersDataList.clear();
                                membersDataList = (List<Members>) gson.fromJson(jsonarrayData.toString(), member);

                                // Log.e("Length 56", membersDataList.size() + "  ");
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
        MySingleton.getInstance(context).addToRequestQueue(jsonObjReq, Tag);

    }

    @Override
    public void fragmentBecameVisible(Context context) {
        this.context = context;
    }

    @Override
    public void onComplete(int s) {

    }

    @Override
    public void onUpdateMatchCount(boolean count) {
        totalMatchesCount--;
        setMatchesCount();
    }

    private void setMatchesCount() {

        tvMatchesCount.setText(totalMatchesCount + " Matches Found");

    }

    @Override
    public void onRefreshMatch() {
        onRefresh();
    }


    @Override
    public void onStop() {
        super.onStop();
        MySingleton.getInstance(getContext()).cancelPendingRequests(Tag);

    }

    @Override
    public void onPreferenceComplete(String s) {
        loadData(params, false);
    }
}
