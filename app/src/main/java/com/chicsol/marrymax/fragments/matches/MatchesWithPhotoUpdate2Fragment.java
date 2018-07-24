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
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.chicsol.marrymax.R;
import com.chicsol.marrymax.activities.DrawerActivity;
import com.chicsol.marrymax.adapters.RecyclerViewAdapterMyMatches;
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
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.chicsol.marrymax.utils.Constants.jsonArraySearch;

/**
 * Created by Android on 11/3/2016.
 */

public class MatchesWithPhotoUpdate2Fragment extends Fragment implements RecyclerViewAdapterMyMatches.OnLoadMoreListener, SwipeRefreshLayout.OnRefreshListener, dialogShowInterest.onCompleteListener, dialogRequestPhone.onCompleteListener, DashboardMatchesMainFragment.MatchesMainFragmentInterface, dialogRequest.onCompleteListener, dialogProfileCompletion.onCompleteListener, dialogRemoveFromSearch.onCompleteListener, UpdateMatchesCountCallback, MatchesRefreshCallBackInterface {
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
   TextView tvMatchesCount,tvMatchesCountCp;
    private long totalMatchesCount = 0;
    private String Tag = "MatchesWithPhotoUpdate2Fragment";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_dashboard_mymatches, container, false);
        Log.e("created", "created");

        initilize(rootView);
        setListenders();

        return rootView;
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
                memberSearchObj.set_path(SharedPreferenceManager.getUserObject(getContext()).get_path());
                memberSearchObj.set_member_status(SharedPreferenceManager.getUserObject(getContext()).get_member_status());
                memberSearchObj.set_phone_verified(SharedPreferenceManager.getUserObject(getContext()).get_phone_verified());
                memberSearchObj.set_email_verified(SharedPreferenceManager.getUserObject(getContext()).get_email_verified());
                //page and type
                memberSearchObj.set_page_no(1);
                memberSearchObj.set_type("");

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
        if (result != 0) {
            Toast.makeText(getContext(), "val: " + result, Toast.LENGTH_SHORT).show();
        }
        if (ConnectCheck.isConnected(getActivity().findViewById(android.R.id.content))) {
            Members memberSearchObj = DrawerActivity.rawSearchObj;
            if (memberSearchObj != null) {
                memberSearchObj.set_path(SharedPreferenceManager.getUserObject(getContext()).get_path());
                memberSearchObj.set_member_status(SharedPreferenceManager.getUserObject(getContext()).get_member_status());
                memberSearchObj.set_phone_verified(SharedPreferenceManager.getUserObject(getContext()).get_phone_verified());
                memberSearchObj.set_email_verified(SharedPreferenceManager.getUserObject(getContext()).get_email_verified());
                //page and type
                memberSearchObj.set_page_no(1);
                memberSearchObj.set_type("phu");

                Gson gson = new Gson();
                String params = gson.toJson(memberSearchObj);
                this.params = params;

                recyclerAdapter.setMemResultsObj(memberSearchObj);

                loadData(params, false);
            }
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

    @Override
    public void fragmentBecameVisible(Context context) {

    }

    private void initilize(View view) {
        fragment = MatchesWithPhotoUpdate2Fragment.this;
        membersDataList = new ArrayList<>();
        pDialog = (ProgressBar) view.findViewById(R.id.ProgressbarMyMatches);

        swipeRefresh = (SwipeRefreshLayout) view.findViewById(R.id.SwipeRefreshDashMainMM);
        LinearLayoutMMMatchesNotFound = (LinearLayout) view.findViewById(R.id.LinearLayoutMMMatchesNotFound);
        recyclerView = (RecyclerView) view.findViewById(R.id.RecyclerViewDashMainMyMatches);


        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);

        recyclerAdapter = new RecyclerViewAdapterMyMatches(getContext(), getFragmentManager(), this, fragment,this,this,Tag);
        recyclerAdapter.setLinearLayoutManager(mLayoutManager);

        recyclerAdapter.setRecyclerView(recyclerView);

        recyclerView.setAdapter(recyclerAdapter);
        swipeRefresh.setOnRefreshListener(this);

     //   tvMatchesCountCp = (TextView) view.findViewById(R.id.TextViewMMMatchesCountCompleteProfile);
        tvMatchesCount = (TextView) view.findViewById(R.id.TextViewMatchesTotalCount);

        ((AppCompatButton) view.findViewById(R.id.ButtonOnSearchClick)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                MarryMax max = new MarryMax(getActivity());
                max.onSearchClicked(getContext(), 0);
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
    private void getData() {
        //  String.Max
        pDialog.setVisibility(View.VISIBLE);
        //  Log.e("url", Urls.getSearchLists + SharedPreferenceManager.getUserObject(getApplicationContext()).get_path());
        JsonArrayRequest req = new JsonArrayRequest(Urls.getSearchLists + SharedPreferenceManager.getUserObject(getContext()).get_path(),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        jsonArraySearch = response;
                        pDialog.setVisibility(View.GONE);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Err", "Error: " + error.getMessage());
                pDialog.setVisibility(View.GONE);
            }
        });
        MySingleton.getInstance(getContext()).addToRequestQueue(req,Tag);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        //ImageLoader.getInstance().destroy();

        pDialog.setVisibility(View.GONE);

    }


    @Override
    public void onLoadMore() {

        if (lastPage != totalPages && lastPage < totalPages) {
            lastPage = lastPage + 1;

            Log.e("", "las p: " + lastPage + " Total Pages:" + totalPages);
       /*     Members memberSearchObj = DrawerActivity.rawSearchObj;

            memberSearchObj.set_path(SharedPreferenceManager.getUserObject(getContext()).get_path());
            memberSearchObj.set_member_status(SharedPreferenceManager.getUserObject(getContext()).get_member_status());
            memberSearchObj.set_phone_verified(SharedPreferenceManager.getUserObject(getContext()).get_phone_verified());
            memberSearchObj.set_email_verified(SharedPreferenceManager.getUserObject(getContext()).get_email_verified());
            //page and type
            memberSearchObj.set_page_no(lastPage);
            memberSearchObj.set_type("");

            Gson gson = new Gson();
            String params = gson.toJson(memberSearchObj);*/


            Gson gsont;
            GsonBuilder gsonBuildert = new GsonBuilder();
            gsont = gsonBuildert.create();
            Type membert = new TypeToken<Members>() {
            }.getType();
            Members memberObj = (Members) gsont.fromJson(params, membert);
            memberObj.set_page_no(lastPage);
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

                ListViewAdvSearchFragment.defaultSelectionsObj.set_path(SharedPreferenceManager.getUserObject(getContext()).get_path());
                ListViewAdvSearchFragment.defaultSelectionsObj.set_member_status(SharedPreferenceManager.getUserObject(getContext()).get_member_status());
                ListViewAdvSearchFragment.defaultSelectionsObj.set_phone_verified(SharedPreferenceManager.getUserObject(getContext()).get_phone_verified());
                ListViewAdvSearchFragment.defaultSelectionsObj.set_email_verified(SharedPreferenceManager.getUserObject(getContext()).get_email_verified());
                //page and type
                ListViewAdvSearchFragment.defaultSelectionsObj.set_page_no(1);
                ListViewAdvSearchFragment.defaultSelectionsObj.set_type("");


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
        if (ConnectCheck.isConnected(getActivity().findViewById(android.R.id.content))) {
            Members memberSearchObj = DrawerActivity.rawSearchObj;
            if (memberSearchObj != null) {
                memberSearchObj.set_path(SharedPreferenceManager.getUserObject(getContext()).get_path());
                memberSearchObj.set_member_status(SharedPreferenceManager.getUserObject(getContext()).get_member_status());
                memberSearchObj.set_phone_verified(SharedPreferenceManager.getUserObject(getContext()).get_phone_verified());
                memberSearchObj.set_email_verified(SharedPreferenceManager.getUserObject(getContext()).get_email_verified());
                //page and type
                memberSearchObj.set_page_no(1);
                memberSearchObj.set_type("phu");

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

        Log.e("Params search" + " " + Urls.searchProfiles, "" + params);

        Log.e("Params search" + " " + Urls.searchProfiles, "");
        final JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.PUT,
                Urls.searchProfiles, params,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("re  update appearance", response + "");
                        try {
                            JSONArray jsonArray = response.getJSONArray("data");

                            if (jsonArray.length() > 1) {

                                Log.e("Length", jsonArray.getJSONArray(0).length() + "");
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


                                    Log.e("Length=================", membersDataList.size() + "  ");


                                    Gson gsont;
                                    GsonBuilder gsonBuildert = new GsonBuilder();
                                    gsont = gsonBuildert.create();
                                    Type membert = new TypeToken<Members>() {
                                    }.getType();
                                    Members memberTotalPages = (Members) gson.fromJson(jsonarrayTotalPages.getJSONObject(0).toString(), membert);


                                    totalPages = memberTotalPages.get_total_pages();
                                    lastPage = 1;
                                    Log.e("total pages", "" + totalPages);
                                    swipeRefresh.setRefreshing(false);
                                    if (memberTotalPages.get_total_member_count() > 0) {
                                        if (getView() != null) {
                                          /*  getView().findViewById(R.id.TextViewMatchesTotalCount).setVisibility(View.VISIBLE);
                                            ((TextView) getView().findViewById(R.id.TextViewMatchesTotalCount)).setText("" + memberTotalPages.get_total_member_count() + " Matches Found");
*/

                                            tvMatchesCount.setVisibility(View.VISIBLE);
                                            totalMatchesCount = memberTotalPages.get_total_member_count();
                                            setMatchesCount();
                                        }
                                    }

                                } else {
                                    recyclerAdapter.clear();
                                    swipeRefresh.setRefreshing(false);
                                    LinearLayoutMMMatchesNotFound.setVisibility(View.VISIBLE);
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
                //if (!refresh) {
                pDialog.setVisibility(View.GONE);
                //  }
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
        MySingleton.getInstance(getContext()).addToRequestQueue(jsonObjReq,Tag);

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
                        Log.e("re  update appearance", response + "");
                        try {
                            JSONArray jsonArray = response.getJSONArray("data");

                            if (jsonArray.length() > 1) {

                                Log.e("Length", jsonArray.getJSONArray(0).length() + "");
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
        MySingleton.getInstance(getContext()).addToRequestQueue(jsonObjReq,Tag);

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
}
