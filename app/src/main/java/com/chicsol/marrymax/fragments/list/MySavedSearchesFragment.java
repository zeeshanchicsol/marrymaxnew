package com.chicsol.marrymax.fragments.list;

import android.content.Intent;
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
import com.chicsol.marrymax.activities.search.SearchMainActivity;
import com.chicsol.marrymax.adapters.MySavedSearchesListAdapter;
import com.chicsol.marrymax.dialogs.dialogUpdateSavedSearch;
import com.chicsol.marrymax.modal.cModel;
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
 * Created by Android on 12/2/2016.
 */
public class MySavedSearchesFragment extends Fragment implements dialogUpdateSavedSearch.onCompleteListener, MySavedSearchesListAdapter.OnUpdateListener {
    public static int searchKey = -1;
    MySavedSearchesListAdapter myContactsListAdapter;
    List<cModel> dataList;
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
        View rootView = inflater.inflate(R.layout.activity_my_saved_searches, container, false);
        initialize(rootView);

        setListeners();
        return rootView;
    }

    private void initialize(View v) {


        btSearch = (AppCompatButton) v.findViewById(R.id.ButtonMySaveSearchSearch);
        LinearLayoutMMMatchesNotFound = (LinearLayout) v.findViewById(R.id.LinearLayoutMySavedSearchesEmpty);
        llmainLayout = (LinearLayout) v.findViewById(R.id.LinearLayoutMySavedSearchesMainLayout);
        tvMySaveSearchesHeader = (TextView) v.findViewById(R.id.TextViewMySaveSearchesHeader);
        pDialog = (ProgressBar) v.findViewById(R.id.ProgressbarProjectMain);

        dataList = new ArrayList<>();
        lv_mycontacts = (ListView) v.findViewById(R.id.ListViewMySavedSearches);
        myContactsListAdapter = new MySavedSearchesListAdapter(getActivity(), R.layout.item_list_my_saved_searches_list, dataList, MySavedSearchesFragment.this,MySavedSearchesFragment.this);
        lv_mycontacts.setAdapter(myContactsListAdapter);
        if (ConnectCheck.isConnected(getActivity().findViewById(android.R.id.content))) {

            getData();
        }


        setListeners();

    }

    private void setListeners() {


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
        //Log.e("getSaveSearchLists", Urls.getSaveSearchLists + SharedPreferenceManager.getUserObject(getActivity().getApplicationContext()).getPath());
        JsonArrayRequest req = new JsonArrayRequest(Urls.getSaveSearchLists + SharedPreferenceManager.getUserObject(getActivity().getApplicationContext()).getPath(),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        //Log.e("Response", response.toString() + "  ==   ");
                        if (response.length() > 0) {


                            Gson gsonc;
                            GsonBuilder gsonBuilderc = new GsonBuilder();
                            gsonc = gsonBuilderc.create();
                            Type listType = new TypeToken<List<cModel>>() {
                            }.getType();
                            try {
                                dataList = (List<cModel>) gsonc.fromJson(response.getJSONArray(0).toString(), listType);
                                // Log.e("List size in sav search", dataList.size() + "");
                                if (dataList.size() > 0) {
                                    LinearLayoutMMMatchesNotFound.setVisibility(View.GONE);
                                    llmainLayout.setVisibility(View.VISIBLE);
                                    tvMySaveSearchesHeader.setVisibility(View.VISIBLE);
                                    myContactsListAdapter.updateDataList(dataList);
                                } else {
                                    LinearLayoutMMMatchesNotFound.setVisibility(View.VISIBLE);
                                    llmainLayout.setVisibility(View.GONE);
                                    tvMySaveSearchesHeader.setVisibility(View.INVISIBLE);
                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            LinearLayoutMMMatchesNotFound.setVisibility(View.VISIBLE);
                            llmainLayout.setVisibility(View.GONE);
                            tvMySaveSearchesHeader.setVisibility(View.INVISIBLE);
                        }
                        pDialog.setVisibility(View.GONE);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Err", "Error: " + error.getMessage());
                LinearLayoutMMMatchesNotFound.setVisibility(View.VISIBLE);
                llmainLayout.setVisibility(View.GONE);
                tvMySaveSearchesHeader.setVisibility(View.INVISIBLE);
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

    @Override
    public void onComplete(String s) {
        getData();
    }

    @Override
    public void onUpdate(String msg) {
        getData();
    }
}

