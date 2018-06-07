package com.chicsol.marrymax.fragments.list;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.chicsol.marrymax.R;
import com.chicsol.marrymax.adapters.MySavedListAdapter;
import com.chicsol.marrymax.modal.mSavList;
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
 * Created by Android on 12/1/2016.
 */


public class MySavedListsFragment extends Fragment implements MySavedListAdapter.OnUpdateListener {
    private ListView lv_mySavedList;

    MySavedListAdapter myContactsListAdapter;
    List<mSavList> dataList;
    private ListView lv_mycontacts;
    private ProgressBar pDialog;
    LinearLayout LinearLayoutMMMatchesNotFound, llmainLayout;
    private TextView tvMySaveSearchesHeader;
    AppCompatButton btSearch;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_list_my_saved_lists, container, false);
        initialize(rootView);
        return rootView;
    }

    private void initialize(View v) {


        lv_mySavedList = (ListView) v.findViewById(R.id.ListViewMySavedList);


        LinearLayoutMMMatchesNotFound = (LinearLayout) v.findViewById(R.id.LinearLayoutMySavedSearchesEmpty);
        llmainLayout = (LinearLayout) v.findViewById(R.id.LinearLayoutMySavedSearchesMainLayout);
        tvMySaveSearchesHeader = (TextView) v.findViewById(R.id.TextViewMySaveSearchesHeader);
        pDialog = (ProgressBar) v.findViewById(R.id.ProgressbarProjectMain);

        dataList = new ArrayList<>();
        lv_mycontacts = (ListView) v.findViewById(R.id.ListViewMySavedList);
        myContactsListAdapter = new MySavedListAdapter(getActivity(), R.layout.item_list_my_saved_list, dataList, MySavedListsFragment.this, MySavedListsFragment.this);
        lv_mycontacts.setAdapter(myContactsListAdapter);
      /*  if (ConnectCheck.isConnected(getActivity().findViewById(android.R.id.content))) {

            getData();
        }*/


    }

    @Override
    public void onResume() {
        super.onResume();
        if (ConnectCheck.isConnected(getActivity().findViewById(android.R.id.content))) {

            getData();
        }
    }

    private void setListeners() {


    }

    private void getData() {


        pDialog.setVisibility(View.VISIBLE);
        Log.e("getSavedList", Urls.getSavedList + SharedPreferenceManager.getUserObject(getActivity().getApplicationContext()).get_path());
        JsonArrayRequest req = new JsonArrayRequest(Urls.getSavedList + SharedPreferenceManager.getUserObject(getActivity().getApplicationContext()).get_path(),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.e("Response getSavedList", response.toString() + "  ==   ");
                        if (response.length() > 0) {


                            Gson gsonc;
                            GsonBuilder gsonBuilderc = new GsonBuilder();
                            gsonc = gsonBuilderc.create();
                            Type listType = new TypeToken<List<mSavList>>() {
                            }.getType();
                            try {
                                dataList = (List<mSavList>) gsonc.fromJson(response.getJSONArray(0).toString(), listType);
                                // Log.e("List size in sav search", dataList.size() + "");
                                if (dataList.size() > 0) {
                                    LinearLayoutMMMatchesNotFound.setVisibility(View.GONE);
                                    llmainLayout.setVisibility(View.VISIBLE);
                                    //  tvMySaveSearchesHeader.setVisibility(View.VISIBLE);
                                    myContactsListAdapter.updateDataList(dataList);
                                } else {
                                    LinearLayoutMMMatchesNotFound.setVisibility(View.VISIBLE);
                                    llmainLayout.setVisibility(View.GONE);
                                    tvMySaveSearchesHeader.setVisibility(View.INVISIBLE);
                                    myContactsListAdapter.updateDataList(dataList);
                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            LinearLayoutMMMatchesNotFound.setVisibility(View.VISIBLE);
                            llmainLayout.setVisibility(View.GONE);
                            //   tvMySaveSearchesHeader.setVisibility(View.INVISIBLE);
                        }
                        pDialog.setVisibility(View.GONE);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Err", "Error: " + error.getMessage());
             /*   LinearLayoutMMMatchesNotFound.setVisibility(View.VISIBLE);
                llmainLayout.setVisibility(View.GONE);
                tvMySaveSearchesHeader.setVisibility(View.INVISIBLE);*/
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


    @Override
    public void onDestroy() {
        super.onDestroy();
    /*    if (pDialog != null && pDialog.isShowing()) {
            pDialog.cancel();
        }*/
    }

  /*  @Override
    public void onComplete(String s) {
        getData();
    }*/

    @Override
    public void onUpdate(String msg) {
        getData();
    }

}