package com.chicsol.marrymax.activities.searchyourbestmatch;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.chicsol.marrymax.R;
import com.chicsol.marrymax.activities.search.SearchMainActivity;
import com.chicsol.marrymax.adapters.MySpinnerAdapter;
import com.chicsol.marrymax.adapters.RecyclerViewAdapterMyMatchesSearch;
import com.chicsol.marrymax.dialogs.dialogLoginToContinue;
import com.chicsol.marrymax.dialogs.dialogRequest;
import com.chicsol.marrymax.dialogs.dialogRequestPhone;
import com.chicsol.marrymax.dialogs.dialogShowInterest;
import com.chicsol.marrymax.interfaces.MatchesRefreshCallBackInterface;
import com.chicsol.marrymax.interfaces.UpdateMatchesCountCallback;
import com.chicsol.marrymax.modal.Members;
import com.chicsol.marrymax.modal.WebArd;
import com.chicsol.marrymax.other.MarryMax;
import com.chicsol.marrymax.urls.Urls;
import com.chicsol.marrymax.utils.Constants;
import com.chicsol.marrymax.utils.MySingleton;
import com.chicsol.marrymax.utils.WrapContentLinearLayoutManager;
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

import static com.chicsol.marrymax.utils.Constants.defaultSelectionsObj;


/**
 * Created by Android on 11/3/2016.
 */

public class SearchYourBestMatchResultsActivity extends AppCompatActivity implements RecyclerViewAdapterMyMatchesSearch.OnLoadMoreListener, SwipeRefreshLayout.OnRefreshListener, dialogShowInterest.onCompleteListener, dialogRequestPhone.onCompleteListener, dialogRequest.onCompleteListener, dialogLoginToContinue.onCompleteLoginListener, UpdateMatchesCountCallback, MatchesRefreshCallBackInterface {
    public static int result = 0;

    LinearLayout LinearLayoutMMMatchesNotFound, ll_bestmatch_bride_groom;
    //private Button bt_loadmore;
    private
    RecyclerView recyclerView;
    private int lastPage = 1;
    private List<Members> membersDataList;
    private List<WebArd> brideGroomDataList;
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
    private Spinner spinner_bride_groom;
    private TextView tvMatchesCount;
    String TAG = "SearchYourBestMatchResultsActivity";
    private LinearLayout llMatchPreference;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.fragment_dashboard_mymatches);


        if (defaultSelectionsObj == null) {

            Constants.defaultSelectionsObj = new Members();
        }


        if (defaultSelectionsObj.getChoice_age_from() == 0) {
            defaultSelectionsObj.setChoice_age_from(18);
        }
        if (defaultSelectionsObj.getChoice_age_upto() == 0) {
            defaultSelectionsObj.setChoice_age_upto(70);
        }

        Gson gson = new Gson();
        String params = gson.toJson(defaultSelectionsObj);
        //Log.e("params  ", params + "");


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
        //if (searchKey != -1) {

        //  Toast.makeText(getApplicationContext(), "Searchkey not  Null :"+searchKey, Toast.LENGTH_SHORT).show();

        Members memberSearchObj = defaultSelectionsObj;

      /*  if (memberSearchObj != null) {
            ///  memberSearchObj.setPath(SharedPreferenceManager.getUserObject(getApplicationContext()).getPath());

            // memberSearchObj.setMember_status(SharedPreferenceManager.getUserObject(getApplicationContext()).getMember_status());
            //  memberSearchObj.setPhone_verified(SharedPreferenceManager.getUserObject(getApplicationContext()).getPhone_verified());
            // memberSearchObj.setEmail_verified(SharedPreferenceManager.getUserObject(getApplicationContext()).getEmail_verified());
            //page and type
            memberSearchObj.setPage_no(1);
            memberSearchObj.setType("");

            Gson gson = new Gson();
            params = gson.toJson(memberSearchObj);

            loadData(params, false);


        }*/
/*

            else {



       Members memberSearchObjDefault = DrawerActivity.rawSearchObj;
        if (memberSearchObjDefault != null) {
            memberSearchObjDefault.setPath(SharedPreferenceManager.getUserObject(getApplicationContext().getApplicationContext()).getPath());
            memberSearchObjDefault.setMember_status(SharedPreferenceManager.getUserObject(getApplicationContext()).getMember_status());
            memberSearchObjDefault.setPhone_verified(SharedPreferenceManager.getUserObject(getApplicationContext()).getPhone_verified());
            memberSearchObjDefault.setEmail_verified(SharedPreferenceManager.getUserObject(getApplicationContext()).getEmail_verified());
            //page and type
            memberSearchObjDefault.setPage_no(1);
            memberSearchObjDefault.setType("");

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
            //   Toast.makeText(getApplicationContext(), "val: " + result, Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void initilize() {
        TextView tvSubEmpty = (TextView) findViewById(R.id.mTextViewMatchesNotFoundSubHeading);
        tvSubEmpty.setVisibility(View.GONE);

        AppCompatButton ButtonOnSearchClick = (AppCompatButton) findViewById(R.id.ButtonOnSearchClick);
        ButtonOnSearchClick.setVisibility(View.GONE);

        TextView tvMainEmpty = (TextView) findViewById(R.id.mTextViewMatchesNotFoundMain);


        String desc = "<div style='text-align:center' ><b> <font color=#9a0909>0  Matches Found</font></b> <br> Not Connected With Your Matches Yet?</div>";
        tvMainEmpty.setText(Html.fromHtml(desc));


        llMatchPreference = (LinearLayout) findViewById(R.id.LinearLayoutMatchesMatchPreference);
        llMatchPreference.setVisibility(View.GONE);

        toolbar = (Toolbar) findViewById(R.id.toolbar1);
        toolbar.setVisibility(View.VISIBLE);


        toolbar.setTitle("Search");
        if (SearchMainActivity.filterCount > 0) {

            toolbar.setTitle("Filter (" + SearchMainActivity.filterCount + ")");
        }

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tvMatchesCount = (TextView) findViewById(R.id.TextViewMatchesTotalCount);


        // fragment = SearchResultsActivity.this;
        spinner_bride_groom = (Spinner) findViewById(R.id.sp_bestmatch_results_bride_groom);

        brideGroomDataList = new ArrayList<>();
        brideGroomDataList.add(new WebArd("0", "Bride"));
        brideGroomDataList.add(new WebArd("1", "Groom"));


        MySpinnerAdapter spAdapterBrideGroom = new MySpinnerAdapter(this,
                android.R.layout.simple_spinner_item, brideGroomDataList);
        spAdapterBrideGroom.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_bride_groom.setAdapter(spAdapterBrideGroom);


        if (defaultSelectionsObj.getGender() != null) {

            if (defaultSelectionsObj.getGender().equals("M")) {
                spinner_bride_groom.setSelection(1);
            } else {

                spinner_bride_groom.setSelection(0);
            }

        } else {
            defaultSelectionsObj.setGender("M");

        }


        membersDataList = new ArrayList<>();
        pDialog = (ProgressBar) findViewById(R.id.ProgressbarMyMatches);
        //   pDialog.setMessage("Loading...");
        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.SwipeRefreshDashMainMM);
        LinearLayoutMMMatchesNotFound = (LinearLayout) findViewById(R.id.LinearLayoutMMMatchesNotFound);
        ll_bestmatch_bride_groom = (LinearLayout) findViewById(R.id.LinearLayout_bestmatch_bride_groom);
        ll_bestmatch_bride_groom.setVisibility(View.VISIBLE);

        if (getIntent().getBundleExtra("bundle") != null) {
            Bundle b = getIntent().getBundleExtra("bundle");

            boolean d = b.getBoolean("aliascheck");
            if (d) {
                ll_bestmatch_bride_groom.setVisibility(View.GONE);
            }
            //    Toast.makeText(this, "" + d, Toast.LENGTH_SHORT).show();
        }


        recyclerView = (RecyclerView) findViewById(R.id.RecyclerViewDashMainMyMatches);


        LinearLayoutManager mLayoutManager = new WrapContentLinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);

        recyclerAdapter = new RecyclerViewAdapterMyMatchesSearch(SearchYourBestMatchResultsActivity.this, getSupportFragmentManager(), this, this, this, TAG);
        recyclerAdapter.setLinearLayoutManager(mLayoutManager);

        recyclerAdapter.setRecyclerView(recyclerView);

        recyclerView.setAdapter(recyclerAdapter);
        swipeRefresh.setOnRefreshListener(this);


        //Log.e("Searchkey not  Null ","Searchkey not  Null ");

    }

    private void setListenders() {


        spinner_bride_groom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Toast.makeText(activity, ""+brideGroomDataList.get(position).getName(), Toast.LENGTH_SHORT).show();

                //  Toast.makeText(getApplicationContext(), "" + position, Toast.LENGTH_SHORT).show();
                tvMatchesCount.setVisibility(View.GONE);
                if (position == 0) {
                    defaultSelectionsObj.setGender("F");
                } else {
                    defaultSelectionsObj.setGender("M");
                }
                Members memberSearchObj = defaultSelectionsObj;

                if (memberSearchObj != null) {
                    ///  memberSearchObj.setPath(SharedPreferenceManager.getUserObject(getApplicationContext()).getPath());

                    // memberSearchObj.setMember_status(SharedPreferenceManager.getUserObject(getApplicationContext()).getMember_status());
                    //  memberSearchObj.setPhone_verified(SharedPreferenceManager.getUserObject(getApplicationContext()).getPhone_verified());
                    // memberSearchObj.setEmail_verified(SharedPreferenceManager.getUserObject(getApplicationContext()).getEmail_verified());
                    //page and type
                    memberSearchObj.setPage_no(1);
                    memberSearchObj.setType("");

                    Gson gson = new Gson();
                    params = gson.toJson(memberSearchObj);


                    LinearLayoutManager mLayoutManager = new WrapContentLinearLayoutManager(getApplicationContext());
                    recyclerView.setLayoutManager(mLayoutManager);

                    recyclerAdapter = new RecyclerViewAdapterMyMatchesSearch(SearchYourBestMatchResultsActivity.this, getSupportFragmentManager(), SearchYourBestMatchResultsActivity.this, SearchYourBestMatchResultsActivity.this, SearchYourBestMatchResultsActivity.this, TAG);
                    recyclerAdapter.setLinearLayoutManager(mLayoutManager);

                    recyclerAdapter.setRecyclerView(recyclerView);

                    recyclerView.setAdapter(recyclerAdapter);


                    loadData(params, false);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


/*

    //for getting default search data
    private void getData() {
        //  String.Max
        pDialog.setVisibility(View.VISIBLE);
        //  Log.e("url", Urls.getSearchLists + SharedPreferenceManager.getUserObject(getApplicationContext()).getPath());
        JsonArrayRequest req = new JsonArrayRequest(Urls.getSearchLists + SharedPreferenceManager.getUserObject(getApplicationContext()).getPath(),
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
    }
*/


    @Override
    public void onLoadMore() {

        if (lastPage != totalPages && lastPage < totalPages) {
            lastPage = lastPage + 1;

            //Log.e("", "las p: " + lastPage + " Total Pages:" + totalPages);
       /*     Members memberSearchObj = DrawerActivity.rawSearchObj;

            memberSearchObj.setPath(SharedPreferenceManager.getUserObject(getApplicationContext()).getPath());
            memberSearchObj.setMember_status(SharedPreferenceManager.getUserObject(getApplicationContext()).getMember_status());
            memberSearchObj.setPhone_verified(SharedPreferenceManager.getUserObject(getApplicationContext()).getPhone_verified());
            memberSearchObj.setEmail_verified(SharedPreferenceManager.getUserObject(getApplicationContext()).getEmail_verified());
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

                ListViewAdvSearchFragment.defaultSelectionsObj.setPath(SharedPreferenceManager.getUserObject(getApplicationContext()).getPath());
                ListViewAdvSearchFragment.defaultSelectionsObj.setMember_status(SharedPreferenceManager.getUserObject(getApplicationContext()).getMember_status());
                ListViewAdvSearchFragment.defaultSelectionsObj.setPhone_verified(SharedPreferenceManager.getUserObject(getApplicationContext()).getPhone_verified());
                ListViewAdvSearchFragment.defaultSelectionsObj.setEmail_verified(SharedPreferenceManager.getUserObject(getApplicationContext()).getEmail_verified());
                //page and type
                ListViewAdvSearchFragment.defaultSelectionsObj.setPage_no(1);
                ListViewAdvSearchFragment.defaultSelectionsObj.setType("");


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
        Members memberSearchObj = defaultSelectionsObj;

        //  memberSearchObj.setPath(SharedPreferenceManager.getUserObject(getApplicationContext()).getPath());
        //  memberSearchObj.setMember_status(SharedPreferenceManager.getUserObject(getApplicationContext()).getMember_status());
        //  memberSearchObj.setPhone_verified(SharedPreferenceManager.getUserObject(getApplicationContext()).getPhone_verified());
        //  memberSearchObj.setEmail_verified(SharedPreferenceManager.getUserObject(getApplicationContext()).getEmail_verified());
        //page and type
        memberSearchObj.setPage_no(1);
        memberSearchObj.setType("");

        Gson gson = new Gson();
        params = gson.toJson(memberSearchObj);
        loadData(params, false);
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

        tvMatchesCount.setVisibility(View.GONE);
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
                        //Log.e("re  serrr appearance", response + "");
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


                                    //      Log.e("Length=================", membersDataList.size() + "  ");


                                    Gson gsont;
                                    GsonBuilder gsonBuildert = new GsonBuilder();
                                    gsont = gsonBuildert.create();
                                    Type membert = new TypeToken<Members>() {
                                    }.getType();
                                    Members memberTotalPages = (Members) gson.fromJson(jsonarrayTotalPages.getJSONObject(0).toString(), membert);

                                    //     toolbar.setTitle();
                                    tvMatchesCount.setVisibility(View.VISIBLE);
                                    tvMatchesCount.setText(NumberFormat.getNumberInstance(Locale.getDefault()).format(memberTotalPages.getTotal_member_count()) + " - Matches Found");
                                    totalPages = memberTotalPages.getTotal_pages();
                                    lastPage = 1;

                                    //      Log.e("total pages", "" + totalPages);
                                    swipeRefresh.setRefreshing(false);
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
                            // pDialog.dismiss();
                            pDialog.setVisibility(View.INVISIBLE);
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {


                VolleyLog.e("res err", "Error: " + error);
                if (!refresh) {
                    //pDialog.dismiss();
                    pDialog.setVisibility(View.INVISIBLE);
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
    public void onCompleteLogin(String s) {
        // Toast.makeText(getApplicationContext(), "Clicked", Toast.LENGTH_SHORT).show();

    }

 /*   @Override
    public void onArticleSelected(int position) {
        Toast.makeText(getApplicationContext(), "Clicked", Toast.LENGTH_SHORT).show();
    }*/


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.registration_searchyourbestmatch_menu, menu);


        for (int i = 0; i < menu.size(); i++) {
            MenuItem item = menu.getItem(i);
            if (item.getItemId() == R.id.action_filter_results) {
                View itemChooser = item.getActionView();
                if (itemChooser != null) {
                    itemChooser.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            MarryMax max = new MarryMax(SearchYourBestMatchResultsActivity.this);
                            max.onSearchClicked(getApplicationContext(), 1);

                        }
                    });
                }
            }
        }

        return true;
    }

/*    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.action_filter_results:


                MarryMax max = new MarryMax(SearchYourBestMatchResultsActivity.this);
                max.onSearchClicked(getApplicationContext(), 1);
                return true;


            default:
                return super.onOptionsItemSelected(item);

        }
    }*/


    @Override
    public void onUpdateMatchCount(boolean count) {

    }

    @Override
    public void onRefreshMatch() {
        onRefresh();
    }
}
