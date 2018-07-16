package com.chicsol.marrymax.activities.search;

import android.content.Context;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.chicsol.marrymax.R;
import com.chicsol.marrymax.adapters.RecyclerViewAdapterMyMatchesSearch;
import com.chicsol.marrymax.dialogs.dialogProfileCompletion;
import com.chicsol.marrymax.dialogs.dialogRemoveFromSearch;
import com.chicsol.marrymax.dialogs.dialogRequest;
import com.chicsol.marrymax.dialogs.dialogRequestPhone;
import com.chicsol.marrymax.dialogs.dialogShowInterest;
import com.chicsol.marrymax.interfaces.MatchesRefreshCallBackInterface;
import com.chicsol.marrymax.interfaces.UpdateMatchesCountCallback;
import com.chicsol.marrymax.modal.Members;
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

import static com.chicsol.marrymax.utils.Constants.defaultSelectionsObj;

/**
 * Created by Android on 11/3/2016.
 */

public class SearchResultsActivity extends AppCompatActivity implements RecyclerViewAdapterMyMatchesSearch.OnLoadMoreListener, SwipeRefreshLayout.OnRefreshListener, dialogShowInterest.onCompleteListener, dialogRequestPhone.onCompleteListener, dialogRequest.onCompleteListener, dialogProfileCompletion.onCompleteListener, dialogRemoveFromSearch.onCompleteListener, UpdateMatchesCountCallback, MatchesRefreshCallBackInterface {
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
    private RecyclerViewAdapterMyMatchesSearch recyclerAdapter;
    private ProgressBar pDialog;
    private SwipeRefreshLayout swipeRefresh;
    private String params;
    private static boolean m_iAmVisible;
    Context activity;
    private Toolbar toolbar;
    private long totalMatchesCount = 0;
    private String TAG = "SearchResultsActivity ";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.fragment_dashboard_mymatches);


        initilize();
        setListenders();
    }

    /*   @Override
       public void fragmentBecameVisible() {

           // You can do your animation here because we are visible! (make sure onViewCreated has been called too and the Layout has been laid. Source for another question but you get the idea.
       }*/
/*    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_dashboard_mymatches, container, false);

        initilize(rootView);
        setListenders();

        return rootView;
    }*/

/*    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        this.activity = activity;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }*/

    @Override
    public void onResume() {
        super.onResume();

   /*     if (ConnectCheck.isConnected(LinearLayoutMMMatchesNotFound)) {

            if (defaultSelectionsObj != null) {
                defaultSelectionsObj.set_path(SharedPreferenceManager.getUserObject(getApplicationContext()).get_path());
                defaultSelectionsObj.set_member_status(SharedPreferenceManager.getUserObject(getApplicationContext()).get_member_status());
                defaultSelectionsObj.set_phone_verified(SharedPreferenceManager.getUserObject(getApplicationContext()).get_phone_verified());
                defaultSelectionsObj.set_email_verified(SharedPreferenceManager.getUserObject(getApplicationContext()).get_email_verified());
                //page and type
                defaultSelectionsObj.set_page_no(1);
                defaultSelectionsObj.set_type("");

                Gson gson = new Gson();
                params = gson.toJson(defaultSelectionsObj);

                loadData(params, false);


            }

        }
*/


        if (ConnectCheck.isConnected(LinearLayoutMMMatchesNotFound)) {
            Members memberSearchObj = defaultSelectionsObj;
            if (memberSearchObj != null) {
                memberSearchObj.set_path(SharedPreferenceManager.getUserObject(getApplicationContext()).get_path());
                memberSearchObj.set_member_status(SharedPreferenceManager.getUserObject(getApplicationContext()).get_member_status());
                memberSearchObj.set_phone_verified(SharedPreferenceManager.getUserObject(getApplicationContext()).get_phone_verified());
                memberSearchObj.set_email_verified(SharedPreferenceManager.getUserObject(getApplicationContext()).get_email_verified());
                //page and type
                memberSearchObj.set_page_no(1);
                memberSearchObj.set_type("");

                Gson gson = new Gson();
                params = gson.toJson(memberSearchObj);

                loadData(params, false);


            }

        }


/*

            else {



       Members memberSearchObjDefault = DrawerActivity.rawSearchObj;
        if (memberSearchObjDefault != null) {
            memberSearchObjDefault.set_path(SharedPreferenceManager.getUserObject(getApplicationContext().getApplicationContext()).get_path());
            memberSearchObjDefault.set_member_status(SharedPreferenceManager.getUserObject(getApplicationContext()).get_member_status());
            memberSearchObjDefault.set_phone_verified(SharedPreferenceManager.getUserObject(getApplicationContext()).get_phone_verified());
            memberSearchObjDefault.set_email_verified(SharedPreferenceManager.getUserObject(getApplicationContext()).get_email_verified());
            //page and type
            memberSearchObjDefault.set_page_no(1);
            memberSearchObjDefault.set_type("");

            Gson gson = new Gson();
            String params = gson.toJson(memberSearchObj);
            this.params = params;
            loadData(params, false);
        }

            }
*/


        //   }
   /*     if (searchKey != null) {
            //  getSelectedSearchObject(AdvanceSearchMainActivity.searchKey);
            Toast.makeText(getApplicationContext(), "Searchkey not  Null", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), " Null", Toast.LENGTH_SHORT).show();
            ///  getRawData();
            //   defaultSelectionsObj = DrawerActivity.rawSearchObj;
            //    listener.onItemSelected(dataList.get(0));
            //ListViewAdvSearchFragment.defaultSelectionsObj
        }*/
        if (result != 0) {
            Toast.makeText(getApplicationContext(), "val: " + result, Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void initilize() {

        toolbar = (Toolbar) findViewById(R.id.toolbar1);
        toolbar.setVisibility(View.VISIBLE);
        toolbar.setTitle("Search");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // fragment = SearchResultsActivity.this;
        membersDataList = new ArrayList<>();
        pDialog = (ProgressBar) findViewById(R.id.ProgressbarMyMatches);
        //   pDialog.setMessage("Loading...");
        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.SwipeRefreshDashMainMM);
        LinearLayoutMMMatchesNotFound = (LinearLayout) findViewById(R.id.LinearLayoutMMMatchesNotFound);
        recyclerView = (RecyclerView) findViewById(R.id.RecyclerViewDashMainMyMatches);


        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);

        recyclerAdapter = new RecyclerViewAdapterMyMatchesSearch(SearchResultsActivity.this, getSupportFragmentManager(), this, this, this);
        recyclerAdapter.setLinearLayoutManager(mLayoutManager);

        recyclerAdapter.setRecyclerView(recyclerView);

        recyclerView.setAdapter(recyclerAdapter);
        swipeRefresh.setOnRefreshListener(this);


        //Log.e("Searchkey not  Null ","Searchkey not  Null ");

    }

    private void setListenders() {

    }


    //for getting default search data
/*    private void getData() {
        //  String.Max
        pDialog.setVisibility(View.VISIBLE);
        //  Log.e("url", Urls.getSearchLists + SharedPreferenceManager.getUserObject(getApplicationContext()).get_path());
        JsonArrayRequest req = new JsonArrayRequest(Urls.getSearchLists + SharedPreferenceManager.getUserObject(getApplicationContext()).get_path(),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        jsonArraySearch = response;
                        pDialog.setVisibility(View.INVISIBLE);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Err", "Error: " + error.getMessage());

                pDialog.setVisibility(View.INVISIBLE);
            }
        });
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(req);
    }*/


    @Override
    public void onLoadMore() {

        if (lastPage != totalPages && lastPage < totalPages) {
            lastPage = lastPage + 1;

            Log.e("", "las p: " + lastPage + " Total Pages:" + totalPages);
       /*     Members memberSearchObj = DrawerActivity.rawSearchObj;

            memberSearchObj.set_path(SharedPreferenceManager.getUserObject(getApplicationContext()).get_path());
            memberSearchObj.set_member_status(SharedPreferenceManager.getUserObject(getApplicationContext()).get_member_status());
            memberSearchObj.set_phone_verified(SharedPreferenceManager.getUserObject(getApplicationContext()).get_phone_verified());
            memberSearchObj.set_email_verified(SharedPreferenceManager.getUserObject(getApplicationContext()).get_email_verified());
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
            //   Members memberObj = (Members) gsont.fromJson(params, membert);
            //   memberObj.set_page_no(lastPage);
            defaultSelectionsObj.set_page_no(lastPage);
            //   gsont.toString();
            // Log.e("params json", gsont.toJson(memberObj));
            loadMoreData(gsont.toJson(defaultSelectionsObj));

        }

    }

/*
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 2) {

            if (resultCode == RESULT_OK) {
                //   String message = data.getStringExtra("MESSAGE");
                Log.e("===========", "==================");
                //  Toast.makeText(getApplicationContext(), "" + message, Toast.LENGTH_SHORT).show();
                //   textView1.setText(message);

                ListViewAdvSearchFragment.defaultSelectionsObj.set_path(SharedPreferenceManager.getUserObject(getApplicationContext()).get_path());
                ListViewAdvSearchFragment.defaultSelectionsObj.set_member_status(SharedPreferenceManager.getUserObject(getApplicationContext()).get_member_status());
                ListViewAdvSearchFragment.defaultSelectionsObj.set_phone_verified(SharedPreferenceManager.getUserObject(getApplicationContext()).get_phone_verified());
                ListViewAdvSearchFragment.defaultSelectionsObj.set_email_verified(SharedPreferenceManager.getUserObject(getApplicationContext()).get_email_verified());
                //page and type
                ListViewAdvSearchFragment.defaultSelectionsObj.set_page_no(1);
                ListViewAdvSearchFragment.defaultSelectionsObj.set_type("");


                Gson gson = new Gson();
                String memString = gson.toJson(ListViewAdvSearchFragment.defaultSelectionsObj);
                params = memString;
                loadData(memString, false);
                //     loadSearchProfilesData(memString, true);


            }
        }

    }*/

    @Override
    public void onRefresh() {


  /*      if (ConnectCheck.isConnected(LinearLayoutMMMatchesNotFound)) {

            defaultSelectionsObj.set_path(SharedPreferenceManager.getUserObject(getApplicationContext()).get_path());
            defaultSelectionsObj.set_member_status(SharedPreferenceManager.getUserObject(getApplicationContext()).get_member_status());
            defaultSelectionsObj.set_phone_verified(SharedPreferenceManager.getUserObject(getApplicationContext()).get_phone_verified());
            defaultSelectionsObj.set_email_verified(SharedPreferenceManager.getUserObject(getApplicationContext()).get_email_verified());
            //page and type
            defaultSelectionsObj.set_page_no(1);
            defaultSelectionsObj.set_type("");

            Gson gson = new Gson();
            params = gson.toJson(defaultSelectionsObj);
            loadData(params, false);

        }*/


        if (ConnectCheck.isConnected(LinearLayoutMMMatchesNotFound)) {
            Members memberSearchObj = defaultSelectionsObj;
            memberSearchObj.set_path(SharedPreferenceManager.getUserObject(getApplicationContext()).get_path());
            memberSearchObj.set_member_status(SharedPreferenceManager.getUserObject(getApplicationContext()).get_member_status());
            memberSearchObj.set_phone_verified(SharedPreferenceManager.getUserObject(getApplicationContext()).get_phone_verified());
            memberSearchObj.set_email_verified(SharedPreferenceManager.getUserObject(getApplicationContext()).get_email_verified());
            //page and type
            memberSearchObj.set_page_no(1);
            memberSearchObj.set_type("");

            Gson gson = new Gson();
            params = gson.toJson(memberSearchObj);


            recyclerAdapter.setMemResultsObj(memberSearchObj);
            loadData(params, false);

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


        //  pDialog.setVisibility(View.VISIBLE);
        setLoader(true);
        //   RequestQueue rq = Volley.newRequestQueue(getActivity().getApplicationContext());

        JSONObject params = null;
        try {
            params = new JSONObject(paramsString);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.e(TAG + "Params search" + " " + Urls.searchProfiles, "" + params);

        //Log.e("Params search" + " " + Urls.searchProfiles, "");
        final JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.PUT,
                Urls.searchProfiles, params,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("re  serrr appearance", response + "");
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


                                    //      Log.e("Length=================", membersDataList.size() + "  ");


                                    Gson gsont;
                                    GsonBuilder gsonBuildert = new GsonBuilder();
                                    gsont = gsonBuildert.create();
                                    Type membert = new TypeToken<Members>() {
                                    }.getType();
                                    Members memberTotalPages = (Members) gson.fromJson(jsonarrayTotalPages.getJSONObject(0).toString(), membert);

                                    toolbar.setTitle(memberTotalPages.get_total_member_count() + " - Matches Found");
                                    totalMatchesCount = memberTotalPages.get_total_member_count();
                                    totalPages = memberTotalPages.get_total_pages();
                                    lastPage = 1;

                                    //      Log.e("total pages", "" + totalPages);
                                    swipeRefresh.setRefreshing(false);
                                } else {
                                    recyclerAdapter.clear();
                                    swipeRefresh.setRefreshing(false);
                                    LinearLayoutMMMatchesNotFound.setVisibility(View.VISIBLE);
                                    findViewById(R.id.ButtonOnSearchClick).setVisibility(View.GONE);

                                }

                            } else {
                                recyclerAdapter.clear();
                                //no data
                                swipeRefresh.setRefreshing(false);
                                LinearLayoutMMMatchesNotFound.setVisibility(View.VISIBLE);
                                findViewById(R.id.ButtonOnSearchClick).setVisibility(View.GONE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (!refresh) {
                            // pDialog.dismiss();
                            pDialog.setVisibility(View.INVISIBLE);
                            setLoader(false);
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {


                VolleyLog.e("res err", "Error: " + error);
                if (!refresh) {
                    //pDialog.dismiss();
                    //   pDialog.setVisibility(View.INVISIBLE);
                    setLoader(false);
                }
                LinearLayoutMMMatchesNotFound.setVisibility(View.VISIBLE);
                findViewById(R.id.ButtonOnSearchClick).setVisibility(View.GONE);
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
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjReq);

    }

    @Override
    public void onComplete(int s) {

    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.action_filter_results);
        if (Constants.searchFromSavedListings == true) {
            Constants.searchFromSavedListings = false;
            item.setVisible(true);
        } else {
            item.setVisible(false);
        }

        super.onPrepareOptionsMenu(menu);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.registration_searchyourbestmatch_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.action_filter_results:
                Intent intent = new Intent(SearchResultsActivity.this, SearchMainActivity.class);
                intent.putExtra("fromResultsCheck", true);
                startActivity(intent);
                finish();
                return true;


            default:
                return super.onOptionsItemSelected(item);

        }
    }

    void setLoader(boolean set) {
      /*  llScreenMain.setVisibility(set ? View.GONE : View.VISIBLE);
        llScreenWait.setVisibility(set ? View.VISIBLE : View.GONE);
*/
        if (set) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        } else {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }
        pDialog.setVisibility(set ? View.VISIBLE : View.GONE);
    }


    @Override
    public void onUpdateMatchCount(boolean count) {
        totalMatchesCount--;
        toolbar.setTitle(totalMatchesCount + " - Matches Found");

    }

    @Override
    public void onRefreshMatch() {
        onRefresh();
    }
}
