package com.chicsol.marrymax.fragments.list;

import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import com.chicsol.marrymax.adapters.RemovedFromSearchesListAdapter;
import com.chicsol.marrymax.modal.WebArdList;
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
import java.util.List;
import java.util.Map;

/**
 * Created by Android on 12/2/2016.
 */

public class RemovedFromSearchFragment extends Fragment implements RemovedFromSearchesListAdapter.OnUpdateListener {
    RemovedFromSearchesListAdapter myListAdapter;
    private ListView lv_mycontacts;
    private ProgressBar pDialog;
    LinearLayout LinearLayoutMMMatchesNotFound, llmainLayout;
    TextView tvHeadingRemoveFromSerch;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_list_removed_from_search, container, false);
        initialize(rootView);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
     /*   if (ConnectCheck.isConnected(getActivity().findViewById(android.R.id.content))) {
            getData();
        }*/
    }

    private void initialize(View v) {

        tvHeadingRemoveFromSerch = (TextView) v.findViewById(R.id.TextViewHeadingRemoveFromSerch);
        LinearLayoutMMMatchesNotFound = (LinearLayout) v.findViewById(R.id.LinearLayoutMySavedSearchesEmpty);
        llmainLayout = (LinearLayout) v.findViewById(R.id.LinearLayoutMySavedSearchesMainLayout);
        lv_mycontacts = (ListView) v.findViewById(R.id.ListViewRemovedFromSerch);


        pDialog = (ProgressBar) v.findViewById(R.id.ProgressbarProjectMain);
     if (ConnectCheck.isConnected(getActivity().findViewById(android.R.id.content))) {
      getData();
        }

    }


    private void getData() {
        pDialog.setVisibility(View.VISIBLE);
        Log.e("url", Urls.getRemovedList + SharedPreferenceManager.getUserObject(getActivity().getApplicationContext()).get_path());
        JsonArrayRequest req = new JsonArrayRequest(Urls.getRemovedList + SharedPreferenceManager.getUserObject(getContext()).get_path(),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.e("Response", response.toString() + "  ==   ");
                        if (response.length() > 0) {
                            Gson gsonc;
                            GsonBuilder gsonBuilderc = new GsonBuilder();
                            gsonc = gsonBuilderc.create();
                            Type listType = new TypeToken<List<WebArdList>>() {
                            }.getType();
                            List<WebArdList> dataList;

                            try {

                                Log.e("Response", response.getJSONArray(0).toString());
                                dataList = (List<WebArdList>) gsonc.fromJson(response.getJSONArray(0).toString(), listType);

                                if (dataList.size() > 0) {
                                    //  LinearLayoutMMMatchesNotFound.setVisibility(View.GONE);

                                    tvHeadingRemoveFromSerch.setVisibility(View.VISIBLE);
                                    llmainLayout.setVisibility(View.VISIBLE);
                             /*       myListAdapter.addAll(dataList);*/
                                    Log.e("dataList", "" + dataList.size());
                                    myListAdapter = new RemovedFromSearchesListAdapter(getActivity(), R.layout.item_list_removed_from_search, dataList, RemovedFromSearchFragment.this);
                                    lv_mycontacts.setAdapter(myListAdapter);


                                } else {
                                    tvHeadingRemoveFromSerch.setVisibility(View.INVISIBLE);
                                    LinearLayoutMMMatchesNotFound.setVisibility(View.VISIBLE);
                                    llmainLayout.setVisibility(View.GONE);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                               /* LinearLayoutMMMatchesNotFound.setVisibility(View.VISIBLE);
                                llmainLayout.setVisibility(View.GONE);
                                tvHeadingRemoveFromSerch.setVisibility(View.INVISIBLE);*/


                            }
                        } else {
                            LinearLayoutMMMatchesNotFound.setVisibility(View.VISIBLE);
                            llmainLayout.setVisibility(View.GONE);
                            tvHeadingRemoveFromSerch.setVisibility(View.INVISIBLE);

                        }

                        pDialog.setVisibility(View.GONE);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Err", "Error: " + error.getMessage());
                pDialog.setVisibility(View.GONE);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return Constants.getHashMap();
            }
        };
        MySingleton.getInstance(getContext()).addToRequestQueue(req);
    }

    @Override
    public void onUpdate(String msg) {
        getData();
    }
}

